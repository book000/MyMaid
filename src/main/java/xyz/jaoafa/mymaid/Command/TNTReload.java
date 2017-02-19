package xyz.jaoafa.mymaid.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
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
				Method.SendMessage(sender, cmd, "TNT Explode notice true");
			}else if(args[0].equalsIgnoreCase("false")){
				OnExplosion.tntexplode = false;
				Method.SendMessage(sender, cmd, "TNT Explode notice false");
			}else if(args[0].equalsIgnoreCase("now")){
				if(OnExplosion.tntexplode){
					Method.SendMessage(sender, cmd, "TNT Explode notice true now");
				}else{
					Method.SendMessage(sender, cmd, "TNT Explode notice false now");
				}

			}else{
				Method.SendMessage(sender, cmd, "true or false plz");
			}
			return true;
		}else{
			Method.SendMessage(sender, cmd, "このコマンドは1つの引数が必要です。");
			return true;
		}
	}
}
