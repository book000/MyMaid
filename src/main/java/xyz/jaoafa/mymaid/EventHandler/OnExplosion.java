package xyz.jaoafa.mymaid.EventHandler;

import java.util.Map;
import java.util.UUID;

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

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Command.Explode;

public class OnExplosion implements Listener {
	JavaPlugin plugin;
	public OnExplosion(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static Boolean tntexplode = true;

	@EventHandler(ignoreCancelled = true)
	public void onEntityExplodeEvent(EntityExplodeEvent e){
		Location location;
		try{
			BlockState states = null;
			for(Block block : e.blockList()){
				states = block.getState();
				break;
			}
			if(states == null){
				return;
			}
			location = states.getLocation();
		}catch(java.lang.NullPointerException e1) {
			tntexplode = false;
			return;
		}
		try{
			int x = location.getBlockX();
			int y = location.getBlockY();
			int z = location.getBlockZ();
			for(Map.Entry<Location, Integer> explode : Explode.explode.entrySet()) {
				double distance;
				try {
					distance = location.distance(explode.getKey());
				}catch(Exception e1) {
					continue;
				}
				if(distance < explode.getValue()){
					e.setCancelled(true);
					return;
				}
			}

			double min = 1.79769313486231570E+308;
			Player min_player = null;
			for(Player player: Bukkit.getServer().getOnlinePlayers()){
				org.bukkit.Location location_p = player.getLocation();
				if(location.getWorld().getName().equals(location_p.getWorld().getName())){
					double distance = location.distance(location_p);
					if(distance < min){
						min = distance;
						min_player = player;
					}
				}
			}
			if(min_player == null){
				return;
			}
			if(min_player.hasPermission("mymaid.pex.default") || min_player.hasPermission("mymaid.pex.provisional")){
				e.setCancelled(true);
				return;
			}
			if(tntexplode){
				if(min < 20 && min_player.hasPermission("pin_code_auth.joinmsg")){
					// 無視
				}else{
					tntexplode = false;
					for(Player p: Bukkit.getServer().getOnlinePlayers()) {
						if(PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) {
							p.sendMessage("[" + ChatColor.RED + "TNT" + ChatColor.WHITE + "] " + ChatColor.GREEN + min_player.getName() + "の近く(" + min + "block)の" + x + " " + y + " " + z + "地点["+location.getWorld().getName()+"]にてTNTが爆発し、ブロックが破壊されました。確認して下さい。");
						}
					}
					Bukkit.getLogger().info(min_player.getName() + " near(" + min + "block) [" + x + " " + y + " " + z + " "+location.getWorld().getName()+"] TNTExploded.");
					String name = min_player.getName();
					UUID uuid = min_player.getUniqueId();
					Method.url_jaoplugin("tnt", "p="+name+"&u="+uuid+"&x="+x+"&y="+y+"&z="+z+"&w="+location.getWorld().getName());
					new TNT_Explode_Reset(plugin).runTaskLater(plugin, 1200L);
					Bukkit.getLogger().info("TNT Exploded notice off");
				}
			}
		}catch(java.lang.NullPointerException e1) {
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
