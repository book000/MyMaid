package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OnFrom implements Listener {
	JavaPlugin plugin;
	public OnFrom(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onFrom(EntityChangeBlockEvent e){
		if(e.getBlock().getType() == Material.SOIL){
			e.setCancelled(true);
		}
		//Bukkit.broadcastMessage("EntityChangeBlockEvent! " + e.getBlock().getType());
	}
}
