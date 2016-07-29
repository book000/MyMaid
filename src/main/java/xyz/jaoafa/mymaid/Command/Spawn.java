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

public class Spawn implements CommandExecutor {
	JavaPlugin plugin;
	public Spawn(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		// コマンド実行者がプレイヤーかどうか
		if(!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		World World = player.getWorld();
		Location spawnloc = new Location(World, 0, 100, 0);
		int y = getGroundPos(spawnloc);
		Location spawn = new Location(World, 0, y, 0);
		player.teleport(spawn);
		Method.SendMessage(sender, cmd, "初期スポーン地点にテレポートしました。");
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
