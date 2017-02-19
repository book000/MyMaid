package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Command.DOT;

public class OnPlayerBedLeaveEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerBedLeaveEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBedLeaveEvent(PlayerBedLeaveEvent event) {
		Player player = event.getPlayer();
		DOT.bed.remove(player.getName());
	}
}
