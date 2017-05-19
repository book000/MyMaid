package xyz.jaoafa.mymaid.Discord;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapWebChatEvent;

public class OnDynmapWebChat implements Listener {
	JavaPlugin plugin;
	public OnDynmapWebChat(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onDynmapWebChat(DynmapWebChatEvent event) {
		String player = event.getName();
		String message = event.getMessage();
		message = Discord.format(message);

		String SendMessage = "**" + player + "**: " + message;

		Discord.send(SendMessage);
	}
}
