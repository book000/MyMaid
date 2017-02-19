package com.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Method;

public class Home implements CommandExecutor {
	JavaPlugin plugin;
	public Home(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String, Location> home = new HashMap<String, Location>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof org.bukkit.entity.Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
		if(args.length == 1 && args[0].equalsIgnoreCase("set")){
			home.put(player.getName(), player.getLocation());
			Method.SendMessage(sender, cmd, "いまあなたが立っている位置にホームをセットしました。");
			return true;
		}
		if(!home.containsKey(player.getName())){
			Method.SendMessage(sender, cmd, "ホームが設定されていません。設定するには/home setコマンドを使用します。");
			return true;
		}
		Method.SendMessage(sender, cmd, "ホームにテレポートします。");
		player.teleport(home.get(player.getName()));
		return true;
	}
}
