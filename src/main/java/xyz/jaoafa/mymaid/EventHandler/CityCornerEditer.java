package xyz.jaoafa.mymaid.EventHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.dynmap.markers.AreaMarker;

public class CityCornerEditer implements Listener {
	JavaPlugin plugin;
	public CityCornerEditer(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String, AreaMarker> AreaMarkers = new HashMap<String, AreaMarker>();
	public static Map<String, Integer> SelectCorner = new HashMap<String, Integer>();

	public static class _OpenGUI extends BukkitRunnable{
		Player player;
		AreaMarker area;
		public _OpenGUI(Player player, AreaMarker area) {
			this.player = player;
			this.area = area;
		}
		@Override
		public void run() {
			OpenGUI(player, area);
		}
	}

	static void OpenGUI(Player player, AreaMarker area){
		if(area == null){
			return;
		}

		AreaMarkers.put(player.getName(), area);

		int count = area.getCornerCount();
		int height = count / 9; // 9で割る
		if(count % 9 != 0){
			height++; // 9で割り切れなかったら1つ足す
		}

		height = height + 2; // さらにプラス2
		Inventory inv = Bukkit.getServer().createInventory(player, height * 9, "CityCorners");

		// 旗はダメージ値0～15

		for(int i = 0; i < area.getCornerCount(); i++){
			double x = area.getCornerX(i);
			double z = area.getCornerZ(i);

			int bannercolor = i % 16;
			ItemStack item = new ItemStack(Material.BANNER, 1, (short) bannercolor);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("#" + (i + 1));
			List<String> list = new ArrayList<String>();
			list.add("X: " + x);
			list.add("Z: " + z);
			list.add("");
			list.add(ChatColor.AQUA + "右クリックでコーナーモードに入れます。");
			meta.setLore(list);
			item.setItemMeta(meta);
			inv.setItem(i, item);
		}
		ItemStack item_red = new ItemStack(Material.WOOL);
		item_red.setDurability((short) 14); // 赤
		ItemMeta itemmeta_red = item_red.getItemMeta();
		itemmeta_red.setDisplayName("キャンセル");
		item_red.setItemMeta(itemmeta_red);
		inv.setItem(inv.getSize() - 2, item_red);

		ItemStack item_green = new ItemStack(Material.WOOL);
		item_green.setDurability((short) 5); // 黄緑
		ItemMeta itemmeta_green = item_green.getItemMeta();
		itemmeta_green.setDisplayName("保存");
		item_green.setItemMeta(itemmeta_green);
		inv.setItem(inv.getSize() - 1, item_green);

		player.openInventory(inv);
	}

	@EventHandler(ignoreCancelled = true)
	public void onCityCornersRightClick_Banner(InventoryClickEvent event) {
		if(event.getWhoClicked().getType() != EntityType.PLAYER) return;
		Player player = (Player) event.getWhoClicked();
		if(event.getClickedInventory() == null) return;
		if(event.getClickedInventory().getName().equals("CityCorners")){
			// コーナーエディター画面
			if(event.getCurrentItem().getType() == Material.BANNER){
				// 旗を右クリック→コーナーモード
				if(!event.isRightClick()) return;
				if(!AreaMarkers.containsKey(player.getName())) return;
				Inventory inv = event.getClickedInventory();
				int i = event.getSlot();
				AreaMarker area = AreaMarkers.get(player.getName());
				SaveCorners(player, inv, area, false); // #28
				new openCornerModeInv(player, area, i).runTaskLater(plugin, 1);
			}else if(event.getCurrentItem().getType() == Material.WOOL){
				// 羊毛→保存かキャンセルのどっちか
				ItemStack is = event.getCurrentItem();
				ItemMeta itemmeta = is.getItemMeta();
				if(itemmeta.getDisplayName().equalsIgnoreCase("保存")){
					// 保存
					AreaMarker area = AreaMarkers.get(player.getName());
					Inventory inv = event.getClickedInventory();
					SaveCorners(player, inv, area, true);
				}else if(itemmeta.getDisplayName().equalsIgnoreCase("キャンセル")){
					// キャンセル
					AreaMarkers.remove(player.getName());
					player.closeInventory();
				}
			}
		}else if(event.getClickedInventory().getName().equals("CityCorners_CornerMode")){
			// コーナーモード画面
			if(event.getCurrentItem().getType() == Material.WOOL){
				if(!AreaMarkers.containsKey(player.getName())) return;
				ItemStack is = event.getCurrentItem();
				ItemMeta itemmeta = is.getItemMeta();
				if(itemmeta.getDisplayName().equalsIgnoreCase("コーナーへテレポートする")){
					int i = SelectCorner.get(player.getName());
					AreaMarker area = AreaMarkers.get(player.getName());
					CornerTeleport(player, area, i);
				}else if(itemmeta.getDisplayName().equalsIgnoreCase("このコーナーの座標を変える")){
					int i = SelectCorner.get(player.getName());
					AreaMarker area = AreaMarkers.get(player.getName());
					CornerLocationEdit(player, area, i);
				}else if(itemmeta.getDisplayName().equalsIgnoreCase("このコーナーを削除する")){
					int i = SelectCorner.get(player.getName());
					AreaMarker area = AreaMarkers.get(player.getName());
					CornerDelete(player, area, i);
				}else if(itemmeta.getDisplayName().equalsIgnoreCase("コーナーモードをやめる")){
					player.closeInventory();
					AreaMarker area = AreaMarkers.get(player.getName());
					new _OpenGUI(player, area).runTaskLater(plugin, 1);
				}
			}
		}
	}

	void CornerTeleport(Player player, AreaMarker area, int i){
		// テレポート動作
		SelectCorner.remove(player.getName());
		player.closeInventory();
		String str_world = area.getWorld();
		World world = Bukkit.getWorld(str_world);
		double x = area.getCornerX(i);
		double z = area.getCornerZ(i);
		int y = getGroundPos(new Location(world, x, 0, z));
		Location loc = new Location(world, x, y, z);
		player.teleport(loc);
		player.sendMessage("[CornerEditer] " + ChatColor.GREEN + "#" + (i + 1) + "へテレポートしました。");
	}

	void CornerLocationEdit(Player player, AreaMarker area, int i){
		SelectCorner.remove(player.getName());
		player.closeInventory();
		Location loc = player.getLocation();
		double x = loc.getBlockX() + 0.5;
		double z = loc.getBlockZ() + 0.5;
		player.sendMessage("[CornerEditer] " + ChatColor.GREEN + "#" + (i + 1) + "の座標をX: " + x + ", Z: " + z + "に変更しました。");
		player.closeInventory();
	}

	void CornerDelete(Player player, AreaMarker area, int i){
		// 削除動作
		if(area.getCornerCount() <= 2){
			player.sendMessage("[CornerEditer] " + ChatColor.GREEN + "コーナー数が足りなくなるため、これ以上削除できません。");
			player.sendMessage("[CornerEditer] " + ChatColor.GREEN + "「/city addcorner " + area.getLabel() + "」を使用して追加してください。");
			SelectCorner.remove(player.getName());
			player.closeInventory();
			return;
		}
		area.deleteCorner(i);
		player.sendMessage("[CornerEditer] " + ChatColor.GREEN + "コーナー(#" + (i + 1) + ")を削除しました。");
		SelectCorner.remove(player.getName());
		player.closeInventory();
	}

	void SaveCorners(Player player, Inventory inv, AreaMarker area, boolean closeinv){
		// 保存動作
		Map<Integer, Location> corners = new HashMap<Integer, Location>();
		for(int i = 0; i < area.getCornerCount(); i++){
			double x = area.getCornerX(i);
			double z = area.getCornerZ(i);
			corners.put(i, new Location(Bukkit.getWorld(area.getWorld()), x, 0, z));
		}

		List<Double> Xs = new LinkedList<Double>();
		List<Double> Zs = new LinkedList<Double>();
		for(int i = 0; i < inv.getSize(); i++){
			ItemStack is = inv.getItem(i);
			if(is == null) continue;
			if(is.getType() != Material.BANNER) continue;
			ItemMeta itemmeta = is.getItemMeta();
			String name = itemmeta.getDisplayName();
			name = name.replaceFirst("#", "");
			int o;
			try{
				o = Integer.parseInt(name);
			}catch(NumberFormatException e){
				continue;
			}
			if(!corners.containsKey(o)){
				continue;
			}
			Location loc = corners.get(o - 1);
			double x = loc.getX();
			double z = loc.getZ();
			Xs.add(x);
			Zs.add(z);
		}
		if(Xs.size() < 2){
			player.sendMessage("[CornerEditer] " + ChatColor.GREEN + "コーナー数が足りなくなるため、この動作は行えません。");
			player.sendMessage("[CornerEditer] " + ChatColor.GREEN + "エリア「" + area.getLabel() + "」のコーナーデータの保存に失敗しました。");
			player.closeInventory();
			return;
		}
		double[] ArrXs = Xs.stream().mapToDouble(Double::doubleValue).toArray();
		double[] ArrZs = Zs.stream().mapToDouble(Double::doubleValue).toArray();
		area.setCornerLocations(ArrXs, ArrZs);
		AreaMarkers.remove(player.getName());
		player.sendMessage("[CornerEditer] " + ChatColor.GREEN + "エリア「" + area.getLabel() + "」のコーナーデータを保存しました。");
		if(closeinv) player.closeInventory();
	}

	private class openCornerModeInv extends BukkitRunnable{
		Player player;
		AreaMarker area;
		int i;
		public openCornerModeInv(Player player, AreaMarker area, int i) {
			this.player = player;
			this.area = area;
			this.i = i;
		}
		@Override
		public void run() {
			CornerMode(player, area, i);
		}
	}

	void CornerMode(Player player, AreaMarker area, int i){
		if(area == null){
			return;
		}
		SelectCorner.put(player.getName(), i);

		double x = area.getCornerX(i);
		double z = area.getCornerZ(i);

		Inventory inv = Bukkit.getServer().createInventory(player, 4 * 9, "CityCorners_CornerMode");

		// 旗はダメージ値0～15
		int bannercolor = i % 16;
		ItemStack item = new ItemStack(Material.BANNER, 1, (short) bannercolor);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("#" + (i + 1));
		List<String> list = new ArrayList<String>();
		list.add("X: " + x);
		list.add("Z: " + z);
		meta.setLore(list);
		item.setItemMeta(meta);

		inv.setItem(4, item);

		ItemStack item_green = new ItemStack(Material.WOOL);
		item_green.setDurability((short) 5); // 黄緑
		ItemMeta itemmeta_green = item_green.getItemMeta();
		itemmeta_green.setDisplayName("コーナーへテレポートする");
		item_green.setItemMeta(itemmeta_green);
		inv.setItem(20, item_green);

		ItemStack item_yellow = new ItemStack(Material.WOOL);
		item_yellow.setDurability((short) 4); // 黄
		ItemMeta itemmeta_yellow = item_yellow.getItemMeta();
		itemmeta_yellow.setDisplayName("このコーナーの座標を変える");
		item_yellow.setItemMeta(itemmeta_yellow);
		inv.setItem(22, item_yellow);

		ItemStack item_red = new ItemStack(Material.WOOL);
		item_red.setDurability((short) 14); // 赤
		ItemMeta itemmeta_red = item_red.getItemMeta();
		itemmeta_red.setDisplayName("このコーナーを削除する");
		item_red.setItemMeta(itemmeta_red);
		inv.setItem(24, item_red);

		ItemStack item_white = new ItemStack(Material.WOOL);
		ItemMeta itemmeta_white = item_white.getItemMeta();
		itemmeta_white.setDisplayName("コーナーモードをやめる");
		item_white.setItemMeta(itemmeta_white);
		inv.setItem(35, item_white);

		player.openInventory(inv);
	}
	/**
	 * 指定した地点の地面の高さを返す
	 *
	 * @param loc
	 *            地面を探したい場所の座標
	 * @return 地面の高さ（Y座標）
	 *
	 * http://www.jias.jp/blog/?57
	 */
	private int getGroundPos(Location loc) {

		// 最も高い位置にある非空気ブロックを取得
		loc = loc.getWorld().getHighestBlockAt(loc).getLocation();

		// 最後に見つかった地上の高さ
		int ground = loc.getBlockY();

		// 下に向かって探索
		for (int y = loc.getBlockY(); y != 0; y--) {
			// 座標をセット
			loc.setY(y);

			// そこは太陽光が一定以上届く場所で、非固体ブロックで、ひとつ上も非固体ブロックか
			if (loc.getBlock().getLightFromSky() >= 8
					&& !loc.getBlock().getType().isSolid()
					&& !loc.clone().add(0, 1, 0).getBlock().getType().isSolid()) {
				// 地上の高さとして記憶しておく
				ground = y;
			}
		}

		// 地上の高さを返す
		return ground;
	}
}
