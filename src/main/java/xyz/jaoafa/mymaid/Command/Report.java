package xyz.jaoafa.mymaid.Command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Report implements CommandExecutor {
	JavaPlugin plugin;
	public Report(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			sender.sendMessage("[Report] " + ChatColor.GREEN + "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		if(args.length >= 1){
			Player player = (Player) sender; //コマンド実行者を代入
			String name = player.getName();
			UUID uuid = player.getUniqueId();
			String text = "";
			int c = 0;
			while(args.length > c){
				text += args[c];
				if(args.length != (c+1)){
					text+=" ";
				}
				c++;
			}
			Method.url_jaoplugin("report", "p="+name+"&u="+uuid+"&t="+text);
			sender.sendMessage("[Report] " + ChatColor.GREEN + "送信しました。");
			return true;
		}
		return true;
	}
}
