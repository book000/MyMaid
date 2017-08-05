package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OnHangingBreakByEntityEvent {
	JavaPlugin plugin;
	public OnHangingBreakByEntityEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onHangingBreakByEntityEvent(HangingBreakByEntityEvent event){
		event.getRemover();
	}
}
