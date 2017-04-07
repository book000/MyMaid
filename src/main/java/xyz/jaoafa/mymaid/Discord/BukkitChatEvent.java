package xyz.jaoafa.mymaid.Discord;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Command.DOT;

public class BukkitChatEvent implements Listener {
	JavaPlugin plugin;
	public BukkitChatEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
		String message = event.getMessage();
		Player player = event.getPlayer();

		if(DOT.run.containsKey(player.getName())){
			return;
		}

		message = Discord.format(message);

		String SendMessage = "**" + player.getName() + "**: " + message;

		Discord.send(SendMessage);
	}
}
