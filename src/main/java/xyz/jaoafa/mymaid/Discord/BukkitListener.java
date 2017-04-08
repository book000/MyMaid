package xyz.jaoafa.mymaid.Discord;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class BukkitListener implements Listener {
	JavaPlugin plugin;
	public BukkitListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void OnPlayerJoin(PlayerJoinEvent event){
		String JoinMessage = event.getJoinMessage();

		JoinMessage = ChatColor.stripColor(JoinMessage);

		Discord.send(JoinMessage);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnDeath(PlayerDeathEvent event){
		String DeathMessage = event.getDeathMessage();

		DeathMessage = ChatColor.stripColor(DeathMessage);

		Discord.send(DeathMessage);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void OnQuit(PlayerQuitEvent event){
		String QuitMessage = event.getQuitMessage();

		QuitMessage = ChatColor.stripColor(QuitMessage);

		Discord.send(QuitMessage);
	}
}
