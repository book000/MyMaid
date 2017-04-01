package xyz.jaoafa.mymaid.EventHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ucchyocean.lc.japanize.JapanizeType;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.MyMaid;

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
			//if((PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator"))){
				p.sendMessage(ChatColor.GRAY + "(" + groupname +") " + player.getName() + ": " + ChatColor.YELLOW + command);
			}
		}
		if(command.contains(" ")){
			String[] commands = command.split(" ", 0);
			List<String> tells = new ArrayList<String>();
			tells.add("/tell");
			tells.add("/msg");
			tells.add("/message");
			tells.add("/m");
			tells.add("/t");
			tells.add("/w");

			/*
			if(!Bukkit.getServer().getOnlinePlayers().contains(commands[1])){
				return;
			}
			*/
			if(tells.contains(commands[0])){
				if(commands.length <= 2){
					return;
				}
				String text = "";
				int c = 2;
				while(commands.length > c){
					text += commands[c];
					if(commands.length != (c+1)){
						text += " ";
					}
					c++;
				}
				if(MyMaid.lunachatapi.isPlayerJapanize(player.getName())){
					String jp = MyMaid.lunachatapi.japanize(text, JapanizeType.GOOGLE_IME);
					for(Player p: Bukkit.getServer().getOnlinePlayers()) {
						if((PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) && (!player.getName().equals(p.getName()))){
						//if((PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator"))){
							p.sendMessage(ChatColor.GRAY + "(" + ChatColor.YELLOW + jp + ChatColor.GRAY + ")");
						}
					}
				}

			}else if(commands[0].equalsIgnoreCase("/r")){
				if(commands.length <= 1){
					return;
				}
				String text = "";
				int c = 1;
				while(commands.length > c){
					text += commands[c];
					if(commands.length != (c+1)){
						text += " ";
					}
					c++;
				}
				if(MyMaid.lunachatapi.isPlayerJapanize(player.getName())){
					String jp = MyMaid.lunachatapi.japanize(text, JapanizeType.GOOGLE_IME);
					for(Player p: Bukkit.getServer().getOnlinePlayers()) {
						if((PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) && (!player.getName().equals(p.getName()))){
						//if((PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator"))){
							p.sendMessage(ChatColor.GRAY + "(" + ChatColor.YELLOW + jp + ChatColor.GRAY + ")");
						}
					}
				}
			}
		}

	}
}
