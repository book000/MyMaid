package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class OnPlayerKickEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerKickEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKickEvent(PlayerKickEvent e){
		if(e.getReason().equals("disconnect.spam")){
			e.setCancelled(true);
		}else if(e.getReason().equals("Illegal characters in chat")){
			e.setCancelled(true);
		}else{
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")){
					p.sendMessage("[KickReason] " + ChatColor.GREEN + e.getPlayer().getName() + " Kick Reason: 「" + e.getReason() + "」");
				}
			}
		}
	}
}
