package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class WorldTeleport implements CommandExecutor {
	JavaPlugin plugin;
	public WorldTeleport(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof org.bukkit.entity.Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("1")){
				// Jao_Afa
				World world = Bukkit.getServer().getWorld("Jao_Afa");
				if(world == null){
					Method.SendMessage(sender, cmd, "「Jao_Afa」ワールドの取得に失敗しました。");
					return true;
				}
				Location loc = new Location(world, 0, 0, 0, 0, 0);
				int y = getGroundPos(loc);
				loc = new Location(world, 0, y, 0, 0, 0);
				player.teleport(loc);
				Method.SendMessage(sender, cmd, "「Jao_Afa」ワールドにテレポートしました。");
				return true;
			}else if(args[0].equalsIgnoreCase("2")){
				// ReJao_Afa
				World world = Bukkit.getServer().getWorld("ReJao_Afa");
				if(world == null){
					Method.SendMessage(sender, cmd, "「ReJao_Afa」ワールドの取得に失敗しました。");
					return true;
				}
				Location loc = new Location(world, 0, 0, 0, 0, 0);
				int y = getGroundPos(loc);
				loc = new Location(world, 0, y, 0, 0, 0);
				player.teleport(loc);
				Method.SendMessage(sender, cmd, "「ReJao_Afa」ワールドにテレポートしました。");
				return true;
			}else{
				World world = Bukkit.getServer().getWorld(args[0]);
				if(world == null){
					Method.SendMessage(sender, cmd, "指定されたワールドは存在しません。");
					return true;
				}else{
					Location loc = new Location(world, 0, 0, 0, 0, 0);
					int y = getGroundPos(loc);
					loc = new Location(world, 0, y, 0, 0, 0);
					player.teleport(loc);
					Method.SendMessage(sender, cmd, "「" + world.getName() + "」ワールドにテレポートしました。");
					return true;
				}
			}
		}else if(args.length == 2){
			Player play = null;
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(p.getName().equalsIgnoreCase(args[0])){
					play = p;
				}
			}
			if(play == null){
				Method.SendMessage(sender, cmd, "ユーザーが見つかりませんでした。");
				return true;
			}
			if(args[1].equalsIgnoreCase("1")){
				// Jao_Afa
				World world = Bukkit.getServer().getWorld("Jao_Afa");
				if(world == null){
					Method.SendMessage(sender, cmd, "「Jao_Afa」ワールドの取得に失敗しました。");
					return true;
				}
				Location loc = new Location(world, 0, 0, 0, 0, 0);
				int y = getGroundPos(loc);
				loc = new Location(world, 0, y, 0, 0, 0);
				play.teleport(loc);
				Method.SendMessage(sender, cmd, play.getName() + "が「Jao_Afa」ワールドにテレポートしました。");
				Method.SendMessage(play, cmd, "「Jao_Afa」ワールドにテレポートしました。");
				return true;
			}else if(args[1].equalsIgnoreCase("2")){
				// ReJao_Afa
				World world = Bukkit.getServer().getWorld("ReJao_Afa");
				if(world == null){
					Method.SendMessage(sender, cmd, "「ReJao_Afa」ワールドの取得に失敗しました。");
					return true;
				}
				Location loc = new Location(world, 0, 0, 0, 0, 0);
				int y = getGroundPos(loc);
				loc = new Location(world, 0, y, 0, 0, 0);
				play.teleport(loc);
				Method.SendMessage(sender, cmd, play.getName() + "が「ReJao_Afa」ワールドにテレポートしました。");
				Method.SendMessage(play, cmd, "「ReJao_Afa」ワールドにテレポートしました。");
				return true;
			}else{
				World world = Bukkit.getServer().getWorld(args[1]);
				if(world == null){
					Method.SendMessage(sender, cmd, "指定されたワールドは存在しません。");
					return true;
				}else{
					Location loc = new Location(world, 0, 0, 0, 0, 0);
					int y = getGroundPos(loc);
					loc = new Location(world, 0, y, 0, 0, 0);
					play.teleport(loc);
					Method.SendMessage(sender, cmd, play.getName() + "が「" + world.getName() + "」ワールドにテレポートしました。");
					Method.SendMessage(play, cmd, "「" + world.getName() + "」ワールドにテレポートしました。");
					return true;
				}
			}

		}else{

		}

		return true;
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
