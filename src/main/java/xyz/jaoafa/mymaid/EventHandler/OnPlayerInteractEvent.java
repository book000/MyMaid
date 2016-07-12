package xyz.jaoafa.mymaid.EventHandler;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Command.Explode;
import xyz.jaoafa.mymaid.EventHandler.OnExplosion.TNT_Explode_Reset;

public class OnPlayerInteractEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerInteractEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteractEvent(PlayerInteractEvent e){
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
	    Block clickedBlock = e.getClickedBlock();
	    Material material = clickedBlock.getType();
	    if (material == Material.BED) {
	        Player player = e.getPlayer();
	        Location location = clickedBlock.getLocation();
	        if(location.getWorld().getEnvironment() == Environment.NORMAL){
	        	return;
	        }
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

			if(player.hasPermission("mymaid.pex.default") || player.hasPermission("mymaid.pex.provisional")){
				e.setCancelled(true);
				return;
			}
			if(OnExplosion.tntexplode){
				if(player.hasPermission("pin_code_auth.joinmsg")){
					// 無視
				}else{
					OnExplosion.tntexplode = false;
					for(Player p: Bukkit.getServer().getOnlinePlayers()) {
						if(p.hasPermission("pin_code_auth.joinmsg")) {
							p.sendMessage("[" + ChatColor.RED + "TNT(BED)" + ChatColor.WHITE + "] " + ChatColor.GREEN + player.getName() + "の近くの" + x + " " + y + " " + z + "地点["+location.getWorld().getName()+"]にてTNT(BED)が爆発し、ブロックが破壊されました。確認して下さい。");
							Bukkit.getLogger().info(player.getName() + " near [" + x + " " + y + " " + z + " "+location.getWorld().getName()+"] TNT(BED)Exploded.");
						}
					}
					String name = player.getName();
					UUID uuid = player.getUniqueId();
					Method.url_jaoplugin("tnt", "p="+name+"&u="+uuid+"&x="+x+"&y="+y+"&z="+z+"&w="+location.getWorld().getName());
					new TNT_Explode_Reset(plugin).runTaskLater(plugin, 1200L);
					Bukkit.getLogger().info("TNT Exploded notice off");
				}
			}
			return;
	    }
	}
}
