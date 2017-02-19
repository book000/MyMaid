package com.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Method;

public class Cmdb implements CommandExecutor {
	JavaPlugin plugin;
	public Cmdb(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			if (!(sender instanceof Player)) {
				Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
				return true;
			}
			Player player = (Player) sender; //コマンド実行者を代入
			String name = player.getName();
			Bukkit.dispatchCommand(sender, "give " + name + " minecraft:command_block");
		}else if(args.length == 1){
			if (!(sender instanceof Player)) {
				Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
				return true;
			}
			Bukkit.dispatchCommand(sender, "give " + args[0] + " minecraft:command_block");
		}else{
			Method.SendMessage(sender, cmd, "このコマンドには1つ以上3つ以下の引数が必要です。");
		}
		return true;
	}
}
