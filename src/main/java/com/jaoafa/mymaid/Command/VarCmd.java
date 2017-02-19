package com.jaoafa.mymaid.Command;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Method;

public class VarCmd implements CommandExecutor {
	JavaPlugin plugin;
	public VarCmd(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			Method.SendMessage(sender, cmd, "引数が適切ではありません。");
			return true;
		}
		String text = "";
		int c = 0;
		while(args.length > c){
			text += args[c];
			if(args.length != (c+1)){
				text += " ";
			}
			c++;
		}
		text = StringUtils.stripStart(text, "/");
		for(Map.Entry<String, String> e : Var.var.entrySet()) {
			//Bukkit.broadcastMessage("$" + e.getKey() + " => " + e.getValue());
			text = text.replaceAll("\\$" + e.getKey(), e.getValue());
		}
		Bukkit.dispatchCommand(sender, text);
		Method.SendMessage(sender, cmd, "コマンド「" + text + "」を実行しました。");
		return true;
	}
}
