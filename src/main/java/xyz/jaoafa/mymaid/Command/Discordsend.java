package xyz.jaoafa.mymaid.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Discord.Discord;

public class Discordsend implements CommandExecutor {
	JavaPlugin plugin;
	public Discordsend(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	String old = "";
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1){
			String name = sender.getName();
			String text = "";
			int c = 0;
			while(args.length > c){
				text += args[c];
				if(args.length != (c+1)){
					text+=" ";
				}
				c++;
			}
			/*
			if(old.equalsIgnoreCase(text)){
				Method.SendMessage(sender, cmd, "前回通報された内容と同じです。");
				return true;
			}
			*/
			//Method.url_jaoplugin("disreport", "p="+name+"&t="+text);
			text = Discord.format(text);
			String SendMessage = "**" + name + "**: " + text;
			Discord.send("250613942106193921", SendMessage);
			Method.SendMessage(sender, cmd, "送信しました。");
			old = text;
			return true;
		}
		return true;
	}
}
