package xyz.jaoafa.mymaid.Command;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Method;

public class Player implements CommandExecutor {
	JavaPlugin plugin;
	public Player(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			if (!(sender instanceof org.bukkit.entity.Player)) {
				Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
				return true;
			}
			org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
			Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
			for(String group : groups){
				if(PermissionsEx.getUser(player).inGroup(group)){
					if(PermissionsEx.getUser(player).inGroup("Default")){
						if(PermissionsEx.getUser(player).inGroup("Regular")){
							continue;
						}
					}
					Method.SendMessage(player, cmd, "You Permission group \"" + group +"\"");
				}
			}
		}else if(args.length == 1){
			String p = args[0];
			Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
			for(String group : groups){
				if(PermissionsEx.getUser(p).inGroup(group)){
					if(PermissionsEx.getUser(p).inGroup("Default")){
						if(PermissionsEx.getUser(p).inGroup("Regular")){
							continue;
						}
					}
					Method.SendMessage(sender, cmd, p + " Permission group \"" + group +"\"");
				}
			}
		}else{
			Method.SendMessage(sender, cmd, "引数が適していません。");
		}
		return true;
	}
}
