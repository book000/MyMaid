package xyz.jaoafa.mymaid.Command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Gamemode_Change implements CommandExecutor {
	JavaPlugin plugin;
	public Gamemode_Change(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	/* onCommand g
	 * ゲームモードを変えます
	 * /g [newgamemode] */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			sender.sendMessage("[G] " + ChatColor.GREEN + "このコマンドは1つまたは2つの引数が必要です。");
			return false;
		}
		String regex = "^[0-3]$"; //正規表現
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(args[0]);
		if (!m.find()){
			sender.sendMessage("[G] " + ChatColor.GREEN + "エラーが発生しました。1桁の半角数字を入力してください。");
			return true;
		}
		Bukkit.dispatchCommand(sender, "gamemode " + args[0]);
		return true;
	}
}
