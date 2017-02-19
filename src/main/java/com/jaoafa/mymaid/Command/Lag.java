package com.jaoafa.mymaid.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Method;

public class Lag implements CommandExecutor {
	JavaPlugin plugin;
	public Lag(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/* onCommand jf
	 * jao afaします。
	 * /jf */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Method.SendMessage(sender, cmd, "最新のラグデータを確認するには/tpsコマンドを実行してください。");
		return true;
	}
}
