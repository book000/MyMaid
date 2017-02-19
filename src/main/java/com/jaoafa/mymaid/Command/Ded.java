package com.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Method;

public class Ded implements CommandExecutor {
	JavaPlugin plugin;
	public Ded(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String,Location> ded = new HashMap<String,Location>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		if(!ded.containsKey(player.getName())){
			Method.SendMessage(sender, cmd, "死亡した情報が存在しません。");
			return true;
		}
		Location loc = ded.get(player.getName());
		player.teleport(loc);
		Method.SendMessage(sender, cmd, loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "にテレポートしました。");
		return true;
	}
}
