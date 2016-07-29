package xyz.jaoafa.mymaid.Command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;

public class SSK implements CommandExecutor {
	JavaPlugin plugin;
	public SSK(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			Player player = (Player) sender;
			UUID uuid = player.getUniqueId();
	  		String data = Method.url_jaoplugin("joinvote", "u="+uuid);
	  		String[] arr = data.split("###", 0);
	  		MyMaid.chatcolor.put(player.getName(), arr[1]);
	  		Method.SendMessage(sender, cmd, player.getName()+"'s SKK -> "+arr[1]);
		}else if(args.length == 1){
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(p.getName().equalsIgnoreCase(args[0])){
					UUID uuid = p.getUniqueId();
			  		String data = Method.url_jaoplugin("joinvote", "u="+uuid);
			  		String[] arr = data.split("###", 0);
			  		MyMaid.chatcolor.put(p.getName(), arr[1]);
			  		Method.SendMessage(sender, cmd, p.getName()+"'s SKK -> "+arr[1]);
				}
    		}

		}

		return true;
	}
}
