package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RuleLoad implements CommandExecutor {
	JavaPlugin plugin;
	public RuleLoad(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof RemoteConsoleCommandSender)) {
            sender.sendMessage("[IZIME-MAN] " + ChatColor.GREEN + "You do not have permission to perform this command. :)");
            return true;
        }
        if(args.length != 1){
            sender.sendMessage("false");
            return true;
        }
        for(final Player p: Bukkit.getServer().getOnlinePlayers()) {
           if(p.getName().equalsIgnoreCase(args[0])) {
               if(p.hasPermission("mymaid.pex.limited")){
                   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + p.getName() + " group set P");
                   for(Player pl: Bukkit.getServer().getOnlinePlayers()) {
                       if(pl.hasPermission("pin_code_auth.joinmsg")) {
                           pl.sendMessage("[PCA] " + ChatColor.GREEN + p.getName() + "は、ルールを読んだので、権限が変更されました。");
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
           if(pl.hasPermission("pin_code_auth.joinmsg")) {
               pl.sendMessage("[PCA] " + ChatColor.GREEN + args[0] + "は、ルールを読んだので、権限が変更されました。");
           }
       }
        return true;
    }
}
