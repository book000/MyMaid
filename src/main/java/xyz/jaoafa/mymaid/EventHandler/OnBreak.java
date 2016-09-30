package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.Crops;
import org.bukkit.plugin.java.JavaPlugin;

public class OnBreak implements Listener {
	JavaPlugin plugin;
	public OnBreak(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		if(e.getBlock().getWorld().getName().equalsIgnoreCase("Summer2016")){
			return;
		}
		Player player = e.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
		if(e.getBlock().getType() == Material.CROPS){
			Crops block = (Crops)e.getBlock().getState().getData();
			if(block.getState() == CropState.RIPE){
				e.setCancelled(true);
			}
		}
	}
}
