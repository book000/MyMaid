package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OnPlayerTeleportEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerTeleportEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onPlayerTeleportEvent(PlayerTeleportEvent event){
		Location from = event.getFrom();
		Location to = event.getTo();
		Player player = event.getPlayer();
		if(!from.getWorld().getName().equalsIgnoreCase(to.getWorld().getName())){
			return;
		}
		if(from.distance(to) >= 10000){
			player.sendMessage("[TeleportCheck] " + ChatColor.GREEN + "テレポートに失敗しました。");
			event.setCancelled(true);
		}
	}
}
