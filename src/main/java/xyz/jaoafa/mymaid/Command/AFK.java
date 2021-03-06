package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Discord.Discord;

public class AFK implements CommandExecutor, Listener{
	static JavaPlugin plugin;
	public AFK(JavaPlugin plugin) {
		AFK.plugin = plugin;
	}
	private static Map<String, BukkitTask> afking = new HashMap<String, BukkitTask>();
	private static Map<String, ItemStack> head = new HashMap<String, ItemStack>();
	private static Map<String, Location> loc = new HashMap<String, Location>();

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;

		if(!getAFKing(player)){
			// NOT AFKなら NOT AFK→AFK
			setAFK_True(player);
		}else{
			// AFKなら AFK→NOT AFK
			setAFK_False(player);
		}
		return true;
	}

	/**
	 * プレイヤーをAFKにする
	 *
	 * @param player 設定するプレイヤー
	 * @author mine_book000
	 */
	public static void setAFK_True(Player player){
		if(getHeadICE(player)){
			ItemStack[] is = player.getInventory().getArmorContents();
			head.put(player.getName(), is[3]);
			ItemStack[] headice = {
					new ItemStack(is[0]),
					new ItemStack(is[1]),
					new ItemStack(is[2]),
					new ItemStack(Material.ICE)
				};
			player.getInventory().setArmorContents(headice);
			player.updateInventory();
		}

		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " is afk!");
		Discord.send(player.getName() + " is afk!");

		String listname = player.getPlayerListName().replaceAll(player.getName(), ChatColor.DARK_GRAY + player.getName());
		player.setPlayerListName(listname);

		MyMaid.TitleSender.setTime_tick(player, 0, 99999999, 0);
		MyMaid.TitleSender.sendTitle(player, ChatColor.RED + "AFK NOW!", ChatColor.BLUE + "" + ChatColor.BOLD + "When you are back, please enter the command '/afk'.");
		MyMaid.TitleSender.setTime_tick(player, 0, 99999999, 0);

		try{
			BukkitTask task = new AFK.afking(plugin, player).runTaskTimer(plugin, 0L, 5L);
			AFK.afking.put(player.getName(), task);
		}catch(java.lang.NoClassDefFoundError e){
			BugReport.report(e);
			AFK.afking.put(player.getName(), null);
		}
	}

	/**
	 * プレイヤーのAFKを解除する
	 *
	 * @param player 解除するプレイヤー
	 * @author mine_book000
	 */
	public static void setAFK_False(Player player){
		if(head.containsKey(player.getName())){
			if(head.get(player.getName()) != null){
				ItemStack[] is = player.getInventory().getArmorContents();
				ItemStack[] noheadice = {
						new ItemStack(is[0]),
						new ItemStack(is[1]),
						new ItemStack(is[2]),
						new ItemStack(head.get(player.getName()))
					};
				player.getInventory().setArmorContents(noheadice);
				player.updateInventory();
			}
		}

		if(afking.get(player.getName()) != null){
			afking.get(player.getName()).cancel();
		}
		afking.remove(player.getName());

		Discord.send(player.getName() + " is now online!");
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " is now online!");

		String listname = player.getPlayerListName().replaceAll(player.getName(), ChatColor.WHITE + player.getName());
		player.setPlayerListName(listname);

		MyMaid.TitleSender.resetTitle(player);

		if(loc.containsKey(player.getName())){
			player.teleport(loc.get(player.getName()));
			loc.remove(player.getName());
		}
	}

	/**
	 * プレイヤーがAFKかどうか調べる
	 *
	 * @param player 調べるプレイヤー
	 * @return AFKかどうか
	 * @author mine_book000
	 */
	public static boolean getAFKing(Player player){
		if(afking.containsKey(player.getName())){
			if(afking.get(player.getName()) != null){
				return true;
			}
		}
		return false;
	}

	/**
	 * ICEを頭にかぶせてもよいか調べる
	 *
	 * @param player 調べるプレイヤー
	 * @return かぶせてよいか
	 * @author mine_book000
	 */
	static boolean getHeadICE(Player player){
		if(player.getWorld().getName().equalsIgnoreCase("Summer2017")){
			return false;
		}
		return true;
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event){
		Player player = event.getPlayer();

		MyMaid.afktime.put(player.getName(), System.currentTimeMillis()); // 動いたら更新する

		if(!getAFKing(player)){
			return;
	   	}

		setAFK_False(player);
	}

	static public class afking extends BukkitRunnable{
		JavaPlugin plugin;
		Player player;
    	public afking(JavaPlugin plugin, Player player) {
    		this.plugin = plugin;
    		this.player = player;
    	}
		@Override
		public void run() {

			if(player.getWorld().getName().equalsIgnoreCase("Summer2017")){
				player.sendMessage("[Summer2017] " + ChatColor.GREEN + "ワールド「Summer2017」ではAFK状態になることができません。");
				player.sendMessage("[Summer2017] " + ChatColor.GREEN + "Jao_Afaワールドに移動します…");
				World world = Bukkit.getServer().getWorld("Jao_Afa");
				if(world == null){
					player.sendMessage("[Summer2017] " + ChatColor.GREEN + "「Jao_Afa」ワールドの取得に失敗しました。");
					return;
				}
				AFK.loc.put(player.getName(), player.getLocation());
				Location loc = new Location(world, 0, 0, 0, 0, 0);
				int y = getGroundPos(loc);
				loc = new Location(world, 0, y, 0, 0, 0);
				loc.add(0.5f,0f,0.5f);
				player.teleport(loc);
				return;
			}

			//player.getWorld().playSound(player.getLocation(),Sound.EXPLODE,1,1);
			player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
			String listname = player.getPlayerListName();
			if(!listname.contains(ChatColor.DARK_GRAY + player.getName())){
				listname = listname.replaceAll(player.getName(), ChatColor.DARK_GRAY + player.getName());
				player.setPlayerListName(listname);
			}
		}
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
	private static int getGroundPos(Location loc) {

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
