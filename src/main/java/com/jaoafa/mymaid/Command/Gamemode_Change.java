package com.jaoafa.mymaid.Command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Method;

public class Gamemode_Change implements CommandExecutor {
	JavaPlugin plugin;
	public Gamemode_Change(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	/* onCommand g
	 * ゲームモードを変えます
	 * /g [newgamemode] */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		if(args.length == 0){
			Method.SendMessage(sender, cmd, "このコマンドは1つまたは2つの引数が必要です。");
			return false;
		}
		String regex = "^[0-3]$"; //正規表現
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(args[0]);
		if (!m.find()){
			Method.SendMessage(sender, cmd, "エラーが発生しました。1桁の半角数字を入力してください。");
			return true;
		}
		Player player = (Player) sender;
		if(args[0].equalsIgnoreCase("0")){
			Method.SendMessage(sender, cmd, player.getGameMode() + " => SURVIVAL");
			player.setGameMode(GameMode.SURVIVAL);
		}else if(args[0].equalsIgnoreCase("1")){
			Method.SendMessage(sender, cmd, player.getGameMode() + " => CREATIVE");
			player.setGameMode(GameMode.CREATIVE);
		}else if(args[0].equalsIgnoreCase("2")){
			Method.SendMessage(sender, cmd, player.getGameMode() + " => ADVENTURE");
			player.setGameMode(GameMode.ADVENTURE);
		}else if(args[0].equalsIgnoreCase("3")){
			Method.SendMessage(sender, cmd, player.getGameMode() + " => SPECTATOR");
			player.setGameMode(GameMode.SPECTATOR);
		}

		return true;
	}
}
