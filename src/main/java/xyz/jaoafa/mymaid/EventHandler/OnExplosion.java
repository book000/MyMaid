package xyz.jaoafa.mymaid.EventHandler;

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

import xyz.jaoafa.mymaid.Method;

public class OnExplosion implements Listener {
	JavaPlugin plugin;
	public OnExplosion(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static Boolean tntexplode = true;

	@EventHandler(ignoreCancelled = true)
    public void onEntityExplodeEvent(EntityExplodeEvent e){
    	try {
    		BlockState states = null;
        	for(Block block : e.blockList()){
        		states = block.getState();
        		break;
        	}
        	Location location = states.getLocation();
        	int x = location.getBlockX();
        	int y = location.getBlockY();
        	int z = location.getBlockZ();

        	double min = 1.79769313486231570E+308;
        	Player min_player = null;
        	for(Player player: Bukkit.getServer().getOnlinePlayers()){
        		org.bukkit.Location location_p = player.getLocation();
            	double distance = location.distance(location_p);
            	if(distance < min){
            		min = distance;
            		min_player = player;
            	}
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
            			if(p.hasPermission("pin_code_auth.joinmsg")) {
            				p.sendMessage("[" + ChatColor.RED + "TNT" + ChatColor.WHITE + "] " + ChatColor.GREEN + min_player.getName() + "の近く(" + min + "block)の" + x + " " + y + " " + z + "地点にてTNTが爆発し、ブロックが破壊されました。確認して下さい。");
            				Bukkit.getLogger().info(min_player.getName() + " near(" + min + "block) [" + x + " " + y + " " + z + "] TNTExploded.");
            			}
            		}
            		String name = min_player.getName();
        			UUID uuid = min_player.getUniqueId();
        			Method.url_jaoplugin("tnt", "p="+name+"&u="+uuid+"&x="+x+"&y="+y+"&z="+z);
        			new TNT_Explode_Reset(plugin).runTaskLater(plugin, 1200L);
                    Bukkit.getLogger().info("TNT Exploded notice off");
            	}
        	}
    	}catch(Exception e1) {
    		tntexplode = false;
    	}
    	return;
    }
	private class TNT_Explode_Reset extends BukkitRunnable {
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
