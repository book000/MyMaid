package xyz.jaoafa.mymaid.Command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.EventHandler.OnExplosion;

public class TNTReload implements CommandExecutor {

	JavaPlugin plugin;
	public TNTReload(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("true")){
				OnExplosion.tntexplode = true;
				sender.sendMessage("[TNT] " + ChatColor.GREEN + "TNT Explode notice true");
			}else if(args[0].equalsIgnoreCase("false")){
				OnExplosion.tntexplode = false;
				sender.sendMessage("[TNT] " + ChatColor.GREEN + "TNT Explode notice false");
			}else if(args[0].equalsIgnoreCase("now")){
				if(OnExplosion.tntexplode){
					sender.sendMessage("[TNT] " + ChatColor.GREEN + "TNT Explode notice true now");
				}else{
					sender.sendMessage("[TNT] " + ChatColor.GREEN + "TNT Explode notice false now");
				}

			}else{
				sender.sendMessage("[TNT] " + ChatColor.GREEN + "true or false plz");
			}
			return true;
		}else{
			sender.sendMessage("[TNT] " + ChatColor.GREEN + "このコマンドは1つの引数が必要です。");
			return true;
		}
	}
}
