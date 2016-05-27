package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Head implements CommandExecutor {
	JavaPlugin plugin;
	public Head(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			if (!(sender instanceof Player)) {
				sender.sendMessage("[PCA] " + ChatColor.GREEN + "このコマンドはゲーム内から実行してください。");
				Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
				return true;
			}
			Player player = (Player) sender; //コマンド実行者を代入
			String name = player.getName();
			Bukkit.dispatchCommand(sender, "give " + name + " skull 1 3 {SkullOwner:" + name + "}");
		}else if(args.length == 1){
			if (!(sender instanceof Player)) {
				sender.sendMessage("[PCA] " + ChatColor.GREEN + "このコマンドはゲーム内から実行してください。");
				Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
				return true;
			}
			Player player = (Player) sender; //コマンド実行者を代入
			String name = player.getName();
			Bukkit.dispatchCommand(sender, "give " + name + " skull 1 3 {SkullOwner:" + args[0] + "}");
		}else if(args.length == 2){
			Bukkit.dispatchCommand(sender, "give " + args[1] + " skull 1 3 {SkullOwner:" + args[0] + "}");
		}else{
			sender.sendMessage("[PLUGIN] " + ChatColor.GREEN + "このコマンドには1つ以上3つ以下の引数が必要です。");
		}
		return true;
	}
}
