package xyz.jaoafa.mymaid.Command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import eu.manuelgu.discordmc.DiscordMC;
import eu.manuelgu.discordmc.MessageAPI;
import sx.blah.discord.handle.obj.IChannel;
import xyz.jaoafa.mymaid.Method;

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
			if(old.equalsIgnoreCase(text)){
				Method.SendMessage(sender, cmd, "前回通報された内容と同じです。");
				return true;
			}
			//Method.url_jaoplugin("disreport", "p="+name+"&t="+text);
			List<IChannel> ch = DiscordMC.getClient().getGuilds().get(0).getChannelsByName("jaotan");
			MessageAPI.sendToDiscord(ch, name + ": " + text);
			Method.SendMessage(sender, cmd, "送信しました。");
			old = text;
			return true;
		}
		return true;
	}
}
