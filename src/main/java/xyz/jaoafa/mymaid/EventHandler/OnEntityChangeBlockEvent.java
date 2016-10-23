package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OnEntityChangeBlockEvent implements Listener {
	JavaPlugin plugin;
	public OnEntityChangeBlockEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent e){
		EntityType type = e.getEntityType();
		if(type == EntityType.PLAYER){
			return;
		}else if(type == EntityType.VILLAGER){
			return;
		}
		e.setCancelled(true);
	}
	@EventHandler
	public void onBlockIgniteEvent(BlockIgniteEvent e){
		Entity entity = e.getIgnitingEntity();
		if ((entity instanceof Villager)) {
			return;
		}
		if ((entity instanceof Player)) {
			return;
		}
		e.setCancelled(true);
	}
}
