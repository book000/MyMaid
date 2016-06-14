package xyz.jaoafa.mymaid.EventHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;

public class OnPlayerJoinEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerJoinEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
  		if(Bukkit.getServer().getOnlinePlayers().size() >= 1 && !MyMaid.nextbakrender){
  			MyMaid.nextbakrender = true;
  		}
  		UUID uuid = event.getPlayer().getUniqueId();
  		String data = Method.url_jaoplugin("joinvote", "u="+uuid);
  		String[] arr = data.split("###", 0);
  		String result = arr[0];
  		MyMaid.chatcolor.put(event.getPlayer().getName(), arr[1]);
  		if(!result.equalsIgnoreCase("null")){
  			event.setJoinMessage(ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.YELLOW + ", " + ChatColor.YELLOW +result+" joined the game.");
  		}
  		if(event.getPlayer().hasPermission("mymaid.pex.limited")){
  			event.setJoinMessage(ChatColor.RED + event.getPlayer().getName() + ChatColor.YELLOW + " joined the game.");
		}
  		Date Date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GRAY + "■" + ChatColor.WHITE + "jaotan: 現在『" + Bukkit.getServer().getOnlinePlayers().size() + "人』がログインしています");
  	}
}
