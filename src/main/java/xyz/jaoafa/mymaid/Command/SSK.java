package xyz.jaoafa.mymaid.Command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.MyMaid;

public class SSK {
	JavaPlugin plugin;
	public SSK(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			Player player = (Player) sender;
			UUID uuid = player.getUniqueId();
	  		String data = MyMaid.url_access("http://toma.webcrow.jp/jao.php?file=joinvote.php&u="+uuid);
	  		String[] arr = data.split("###", 0);
	  		MyMaid.chatcolor.put(player.getName(), arr[1]);
	  		sender.sendMessage(player.getName()+"'s SSK -> "+arr[1]);
		}else if(args.length == 1){
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(p.getName().equalsIgnoreCase(args[0])){
					UUID uuid = p.getUniqueId();
			  		String data = MyMaid.url_access("http://toma.webcrow.jp/jao.php?file=joinvote.php&u="+uuid);
			  		String[] arr = data.split("###", 0);
			  		MyMaid.chatcolor.put(p.getName(), arr[1]);
			  		sender.sendMessage(p.getName()+"'s SSK -> "+arr[1]);
				}
    		}

		}

		return true;
	}
}
