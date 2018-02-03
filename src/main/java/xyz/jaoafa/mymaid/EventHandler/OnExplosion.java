package xyz.jaoafa.mymaid.EventHandler;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.jaoafa.mymaid.PermissionsManager;
import xyz.jaoafa.mymaid.Command.Explode;
import xyz.jaoafa.mymaid.Discord.Discord;

public class OnExplosion implements Listener {
	JavaPlugin plugin;
	public OnExplosion(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static Boolean tntexplode = true; // 通知をするかしないか

	@EventHandler(ignoreCancelled = true)
	public void onEntityExplodeEvent(EntityExplodeEvent event){
		Location location;
		try{
			BlockState states = null;
			for(Block block : event.blockList()){
				states = block.getState();
				break;
			}
			if(states == null){
				return;
			}
			location = states.getLocation();
		}catch(java.lang.NullPointerException e) {
			tntexplode = false;
			return;
		}
		try{ // 存在しないのかなんなのかぬるぽが発生するのでtrycatch
			int x = location.getBlockX();
			int y = location.getBlockY();
			int z = location.getBlockZ();
			for(Map.Entry<Location, Integer> explode : Explode.explode.entrySet()) {
				if(!location.getWorld().getName().equalsIgnoreCase(explode.getKey().getWorld().getName())){
					// ワールドが違ったらめんどくさいのでreturn
					return;
				}
				double distance = location.distance(explode.getKey());

				if(distance < explode.getValue()){ // TNT無効化範囲より中だったらイベントキャンセル(破壊オフ)してreturn
					event.setCancelled(true);
					return;
				}
			}

			double min = Double.MAX_VALUE; // 一番遠い範囲を設定
			Player min_player = null; // 一番近いプレイヤーを代入するための変数
			for(Player player: Bukkit.getServer().getOnlinePlayers()){
				Location location_p = player.getLocation(); // プレイヤーの現在地
				if(location.getWorld().getName().equals(location_p.getWorld().getName())){ // ワールド同じかどうか
					double distance = location.distance(location_p);
					if(distance < min){ // 近ければ代入、これを繰り返すことで一番近い人を探す
						min = distance;
						min_player = player;
					}
				}
			}
			if(min_player == null){
				return;
			}
			String group = PermissionsManager.getPermissionMainGroup(min_player);
			if(group.equalsIgnoreCase("QPPE")){
				event.setCancelled(true);
				return;
			}
			if(location.getWorld().getName().startsWith("kassi-hp-tk")){
				return;
			}
			if(tntexplode){ // 通知オンなら通知確認処理後通知
				if(min < 20 && (group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator"))){
					// 20ブロック以内でAdminとModeratorがいたら無視
				}else{
					tntexplode = false;
					for(Player p: Bukkit.getServer().getOnlinePlayers()) {
						if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
							p.sendMessage("[" + ChatColor.RED + "TNT" + ChatColor.WHITE + "] " + ChatColor.GREEN + min_player.getName() + "の近く(" + min + "block)の" + x + " " + y + " " + z + "地点["+location.getWorld().getName()+"]にてTNTが爆発し、ブロックが破壊されました。確認して下さい。");
						}
					}
					Discord.send("223582668132974594", "<:tnt:246922178165997568>***TNTを検知しました。***\nPlayer: " + min_player.getName() + "(の近く)\nXYZ: " + x + " " + y + " " + z + " [" + location.getWorld().getName() + " - " + event.getEntityType().name() + "]");
					new TNT_Explode_Reset(plugin).runTaskLater(plugin, 1200L);
					Bukkit.getLogger().info("TNT Exploded notice off");
				}
			}
		}catch(java.lang.NullPointerException e) {
			tntexplode = false;
			new TNT_Explode_Reset(plugin).runTaskLater(plugin, 1200L);
		}
		return;
	}
	public static class TNT_Explode_Reset extends BukkitRunnable {
		JavaPlugin plugin;
		public TNT_Explode_Reset(JavaPlugin plugin) {
			this.plugin = plugin;
		}
		@Override
		public void run() {
			tntexplode = true;
			plugin.getLogger().info("TNT Exploded notice on");
		}
	}
}
