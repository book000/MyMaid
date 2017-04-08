package xyz.jaoafa.mymaid.Command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;

public class Vote_Add implements CommandExecutor {
	JavaPlugin plugin;
	public Vote_Add(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static FileConfiguration conf;
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		// 変数定義
		if (!(sender instanceof ConsoleCommandSender)) {

			Method.SendMessage(sender, cmd, "投票は以下リンクからお願いします！");
			Method.SendMessage(sender, cmd, "https://jaoafa.com/vote");
			return true;
		}
		if(args.length != 1){
			return true;
		}
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			if(p.getName().equalsIgnoreCase(args[0])) {
				String name = p.getName();
				UUID uuid = p.getUniqueId();
				String i = Method.url_jaoplugin("vote", "p="+name+"&u="+uuid);
				MyMaid.chatcolor.put(name, i);

			}
		}
		return true;
	}
}
