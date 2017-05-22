package xyz.jaoafa.mymaid.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Pointjao;
import xyz.jaoafa.mymaid.Command.Color;
import xyz.jaoafa.mymaid.Jail.Jail;

public class Menu implements Listener {
	JavaPlugin plugin;
	public Menu(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String,Map<Integer,String>> menu = new HashMap<String,Map<Integer,String>>();

	@EventHandler
	public void onClickBySULPHUR(PlayerInteractEvent event) {
	    if(event.getAction() != Action.RIGHT_CLICK_AIR) return;
	    if(event.getPlayer().getItemInHand().getType() != Material.SULPHUR) return;
	    Player player = event.getPlayer();
	    Inventory inv = Bukkit.getServer().createInventory(player, 4 * 9, "MyMenu");

	    ItemStack player_head = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta_player_head = (SkullMeta) player_head.getItemMeta();
		player_head.setDurability((short) 3);
		skullMeta_player_head.setOwner(player.getName());
		skullMeta_player_head.setDisplayName(player.getName() + "のデータ");

		String color = "";
		if(player.hasPermission("mymaid.pex.limited")){
			color = "BLACK";
		}else if(Jail.isJail(player)){
			color = "DARK_GRAY";
		}else if(Color.color.containsKey(player.getName())){
			ChatColor colorcolor = Color.color.get(player.getName());
			if(colorcolor == ChatColor.WHITE){
				color = "WHITE";
			}else if(colorcolor == ChatColor.DARK_BLUE){
				color = "DARK_BLUE";
			}else if(colorcolor == ChatColor.BLUE){
				color = "BLUE";
			}else if(colorcolor == ChatColor.AQUA){
				color = "AQUA";
			}else if(colorcolor == ChatColor.DARK_AQUA){
				color = "DARK_AQUA";
			}else if(colorcolor == ChatColor.DARK_GREEN){
				color = "DARK_GREEN";
			}else if(colorcolor == ChatColor.GREEN){
				color = "GREEN";
			}else if(colorcolor == ChatColor.YELLOW){
				color = "YELLOW";
			}else if(colorcolor == ChatColor.GOLD){
				color = "GOLD";
			}else if(colorcolor == ChatColor.RED){
				color = "RED";
			}else if(colorcolor == ChatColor.DARK_RED){
				color = "DARK_RED";
			}else if(colorcolor == ChatColor.DARK_PURPLE){
				color = "DARK_PURPLE";
			}else if(colorcolor == ChatColor.LIGHT_PURPLE){
				color = "LIGHT_PURPLE";
			}
		}else if(MyMaid.chatcolor.containsKey(player.getName())){
  			int i = Integer.parseInt(MyMaid.chatcolor.get(player.getName()));
			if(i >= 0 && i <= 5){
				color = "WHITE";
			}else if(i >= 6 && i <= 19){
				color = "DARK_BLUE";
			}else if(i >= 20 && i <= 33){
				color = "BLUE";
			}else if(i >= 34 && i <= 47){
				color = "AQUA";
			}else if(i >= 48 && i <= 61){
				color = "DARK_AQUA";
			}else if(i >= 62 && i <= 76){
				color = "DARK_GREEN";
			}else if(i >= 77 && i <= 89){
				color = "GREEN";
			}else if(i >= 90 && i <= 103){
				color = "YELLOW";
			}else if(i >= 104 && i <= 117){
				color = "GOLD";
			}else if(i >= 118 && i <= 131){
				color = "RED";
			}else if(i >= 132 && i <= 145){
				color = "DARK_RED";
			}else if(i >= 146 && i <= 159){
				color = "DARK_PURPLE";
			}else if(i >= 160){
				color = "LIGHT_PURPLE";
			}
		}else{
			color = "GRAY";
		}
		String nowGroup = "";
		Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
		for(String group : groups){
			if(PermissionsEx.getUser(player).inGroup(group)){
				nowGroup = group;
			}
		}

		List<String> lore = new ArrayList<String>();
		lore.add("UUID: " + player.getUniqueId());
		lore.add("権限: " + nowGroup);
		lore.add("jaoポイント: " + Pointjao.getjao(player) + "jao");
		lore.add("投票回数: " + MyMaid.chatcolor.get(player.getName()) + "回");
		lore.add("色: " + color);
		skullMeta_player_head.setLore(lore);
		player_head.setItemMeta(skullMeta_player_head);
	    inv.setItem(0, player_head);

	    createDisplay(Material.LEATHER_HELMET, inv, 9, "jao afaと挨拶(ログイン時)します。", "挨拶はぜひしようね！(/jfでも同様のことが行えます。)", "jf");
	    createDisplay(Material.DIAMOND_HELMET, inv, 10, "jaojaoと挨拶(ログアウト時)します。", "挨拶はぜひしようね！(/j2でも同様のことが行えます。)", "j2");
	    createDisplay(Material.ICE, inv, 11, "AFK(Away From Keyboard)になります。", "放置状態になります。(/afkでも同様のことが行えます。)", "afk");
	    createDisplay(Material.TNT, inv, 12, "いま居る位置から100ブロックの範囲のTNT爆破を一時的に無効化します。", "DOOM!DOOM!(/eでも同様のことが行えます。)", "e");
	    createDisplay(Material.SKULL_ITEM, inv, 13, "自分の頭を取り出します。", "(/headでも同様のことが行えます。)", "head");
	    createDisplayHead(inv, "MHF_ArrowDown", 14, "現在のインベントリを保存します。", "(/invsaveでも同様のことが行えます。)", "invsave " + player.getName());
	    createDisplayHead(inv, "MHF_ArrowUp", 15, "保存されたインベントリを読み込みします。", "(/invloadでも同様のことが行えます。)", "invload " + player.getName());
	    createDisplay(Material.WOOD_AXE, inv, 16, "ピリオド連投10秒部門に出場します。", "(/. 10でも同様のことが行えます。)", ". 10");
	    createDisplay(Material.IRON_AXE, inv, 17, "ピリオド連投60秒部門に出場します。", "(/. 60でも同様のことが行えます。)", ". 60");
	    createDisplay(Material.DIAMOND_AXE, inv, 18, "ピリオド連投300秒部門に出場します。", "(/. 300でも同様のことが行えます。)", ". 300");
	    createDisplay(Material.WATCH, inv, 19, "ここはどこ？私はだれ？", "いまいる場所の土地名と所有者を登録されていたら表示します。(/whereでも同様のことが行えます。)", "where");
	    createDisplay(Material.COMPASS, inv, 20, "ホームに戻ります。", "(/homeでも同様のことが行えます。)", "home");
	    createDisplay(Material.BED, inv, 21, "ホームを設定します。", "(/home setでも同様のことが行えます。)", "home set");

	    createDisplay(Material.BARRIER, inv, 27, "WorldEditで指定している範囲をすべてAIRにします。", "(//set 0でも同様のことが行えます。)", "/set 0");
	    createDisplayHead(inv, "MHF_ArrowLeft", 28, "WorldEditで行った動作を一回分戻します。", "(//undoでも同様のことが行えます。)", "/undo");
	    createDisplay(Material.PAPER, inv, 29, "WorldEditで指定している範囲を解除します。", "(//selでも同様のことが行えます。)", "/sel");
	    createDisplayDye(inv, 8, 30, "ASyncWorldEditの機能を有効化します。", "(/awe toggle onでも同様のことが行えます。)", "/awe toggle on");
	    createDisplayDye(inv, 10, 31, "ASyncWorldEditの機能を無効化します。", "(/awe toggle offでも同様のことが行えます。)", "/awe toggle off");

	    player.openInventory(inv);
	}
	public static void createDisplay(Material mat, Inventory inv, int Slot, String name, String lore, String Command) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		inv.setItem(Slot, item);
		if(inv.getHolder() != null && inv.getHolder() instanceof Player) {
			Player player = (Player) inv.getHolder();
		    if(menu.containsKey(player.getName())){
		    	Map<Integer, String> menudata = menu.get(player.getName());
		    	menudata.put(Slot, Command);
		    	menu.put(player.getName(), menudata);
		    }else{
		    	Map<Integer, String> menudata = new HashMap<Integer,String>();
		    	menudata.put(Slot, Command);
		    	menu.put(player.getName(), menudata);
		    }
		}
	}

	public static void createDisplayHead(Inventory inv, String owner, int Slot, String name, String lore, String Command) {
		ItemStack player_head = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta_player_head = (SkullMeta) player_head.getItemMeta();
		player_head.setDurability((short) 3);
		skullMeta_player_head.setOwner(owner);
		skullMeta_player_head.setDisplayName(name);
		skullMeta_player_head.setLore(Arrays.asList(lore));
		player_head.setItemMeta(skullMeta_player_head);
		inv.setItem(Slot, player_head);
		if(inv.getHolder() != null && inv.getHolder() instanceof Player) {
			Player player = (Player) inv.getHolder();
		    if(menu.containsKey(player.getName())){
		    	Map<Integer, String> menudata = menu.get(player.getName());
		    	menudata.put(Slot, Command);
		    	menu.put(player.getName(), menudata);
		    }else{
		    	Map<Integer, String> menudata = new HashMap<Integer,String>();
		    	menudata.put(Slot, Command);
		    	menu.put(player.getName(), menudata);
		    }
		}
	}
	public static void createDisplayDye(Inventory inv, int durability, int Slot, String name, String lore, String Command) {
		ItemStack item = new ItemStack(Material.INK_SACK, (byte) 1, (short) durability);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		inv.setItem(Slot, item);
		if(inv.getHolder() != null && inv.getHolder() instanceof Player) {
			Player player = (Player) inv.getHolder();
		    if(menu.containsKey(player.getName())){
		    	Map<Integer, String> menudata = menu.get(player.getName());
		    	menudata.put(Slot, Command);
		    	menu.put(player.getName(), menudata);
		    }else{
		    	Map<Integer, String> menudata = new HashMap<Integer,String>();
		    	menudata.put(Slot, Command);
		    	menu.put(player.getName(), menudata);
		    }
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onMenuClick(InventoryClickEvent event) {
		if(event.getWhoClicked().getType() != EntityType.PLAYER) return;
		if(event.getClickedInventory() == null) return;
		if(!event.getClickedInventory().getName().equals("MyMenu")) return;
		Player player = (Player) event.getWhoClicked();
		if(menu.containsKey(player.getName())){
			Map<Integer, String> menudata = menu.get(player.getName());
			if(menudata.containsKey(event.getSlot())){
				String command = menudata.get(event.getSlot());
				Bukkit.dispatchCommand(player, command);
				player.closeInventory();
				menu.remove(player.getName());
			}
		}
	}

}
