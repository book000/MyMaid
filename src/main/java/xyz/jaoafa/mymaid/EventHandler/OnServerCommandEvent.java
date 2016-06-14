package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OnServerCommandEvent implements Listener {
	JavaPlugin plugin;
	public OnServerCommandEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onServerCommandEvent(ServerCommandEvent e){
    	Bukkit.getLogger().info("[ConsoleCMD] " + e.getCommand());
    }
}
