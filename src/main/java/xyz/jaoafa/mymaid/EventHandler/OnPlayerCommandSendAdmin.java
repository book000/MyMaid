package xyz.jaoafa.mymaid.EventHandler;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class OnPlayerCommandSendAdmin implements Listener {
	JavaPlugin plugin;
	public OnPlayerCommandSendAdmin(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e){
		Player player = e.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
  		String command = e.getMessage();
		String groupname = "";
  		Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
		for(String group : groups){
			if(PermissionsEx.getUser(player).inGroup(group)){
				groupname = group;
			}
		}
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			if((PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) && (!player.getName().equals(p.getName()))){
				p.sendMessage(ChatColor.GRAY + "(" + groupname +") " + player.getName() + ": " + ChatColor.YELLOW + command);
			}
		}
	}
}
