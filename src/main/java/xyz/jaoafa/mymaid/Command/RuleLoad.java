package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.PermissionsManager;

public class RuleLoad implements CommandExecutor {
	JavaPlugin plugin;
	public RuleLoad(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof RemoteConsoleCommandSender)) {
			Method.SendMessage(sender, cmd, "You do not have permission to perform this command. :)");
			return true;
		}
		if(args.length != 1){
			sender.sendMessage("false");
			return true;
		}
		for(final Player p: Bukkit.getServer().getOnlinePlayers()) {
			if(p.getName().equalsIgnoreCase(args[0])) {
				String group = PermissionsManager.getPermissionMainGroup(p);
				if(group.equalsIgnoreCase("Limited")){
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + p.getName() + " group set P");
					for(Player pl: Bukkit.getServer().getOnlinePlayers()) {
						String pl_group = PermissionsManager.getPermissionMainGroup(pl);
						if(pl_group.equalsIgnoreCase("Admin") || pl_group.equalsIgnoreCase("Moderator")){
							Method.SendMessage(pl, cmd, p.getName() + "は、ルールを読んだので、権限が変更されました。");
						}
					}
					sender.sendMessage("true");
					return true;
				}else{
					sender.sendMessage("false");
					return true;
				}
			}
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + args[0] + " group set P");
		for(Player pl: Bukkit.getServer().getOnlinePlayers()) {
			String pl_group = PermissionsManager.getPermissionMainGroup(pl);
			if(pl_group.equalsIgnoreCase("Admin") || pl_group.equalsIgnoreCase("Moderator")){
				Method.SendMessage(pl, cmd, args[0] + "は、ルールを読んだので、権限が変更されました。");
			}
		}
		return true;
	}
}
