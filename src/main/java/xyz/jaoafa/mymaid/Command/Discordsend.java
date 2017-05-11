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
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1){
			if(Discord.isChannel(args[0])){
				String name = sender.getName();
				String text = "";
				int c = 1;
				while(args.length > c){
					text += args[c];
					if(args.length != (c+1)){
						text+=" ";
					}
					c++;
				}
				text = Discord.format(text);
				String SendMessage = "**" + name + "**: " + text;
				Discord.send(args[0], SendMessage);
				Method.SendMessage(sender, cmd, "送信しました。");
				return true;
			}
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
			text = Discord.format(text);
			String SendMessage = "**" + name + "**: " + text;
			Discord.send("223582668132974594", SendMessage);
			Method.SendMessage(sender, cmd, "送信しました。");
			return true;
		}
		return true;
	}
}
