package xyz.jaoafa.mymaid.Command;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Discord.Discord;
import xyz.jaoafa.mymaid.EventHandler.OnMyMaidJoinLeftChatCmdLogs;

public class Chat implements CommandExecutor {
	JavaPlugin plugin;
	public Chat(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/* onCommand chat
	 * 話者を偽装します。
	 * /chat [Player] [text...] */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Date Date;
		SimpleDateFormat H;
		SimpleDateFormat m;
		SimpleDateFormat s;
		String Hs;
		String ms;
		String ss;
		String date;
		if(args.length >= 2){
			for(Player pl: Bukkit.getServer().getOnlinePlayers()) {
				if(pl.getName().equalsIgnoreCase(args[0])) {
					Method.SendMessage(sender, cmd, "オンラインユーザーを話者に指定できません。");
					return true;
				}
			}
			ChatColor color;
			Boolean chatcolor = true;
			if(args[args.length-1].equalsIgnoreCase("color:" + "AQUA")){
				color = ChatColor.AQUA;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "BLACK")){
				color = ChatColor.BLACK;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "BLUE")){
				color = ChatColor.BLUE;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "DARK_AQUA")){
				color = ChatColor.DARK_AQUA;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "DARK_BLUE")){
				color = ChatColor.DARK_BLUE;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "DARK_GRAY")){
				color = ChatColor.DARK_GRAY;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "DARK_GREEN")){
				color = ChatColor.DARK_GREEN;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "DARK_PURPLE")){
				color = ChatColor.DARK_PURPLE;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "DARK_RED")){
				color = ChatColor.DARK_RED;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "GOLD")){
				color = ChatColor.GOLD;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "GREEN")){
				color = ChatColor.GREEN;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "LIGHT_PURPLE")){
				color = ChatColor.LIGHT_PURPLE;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "RED")){
				color = ChatColor.RED;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "WHITE")){
				color = ChatColor.WHITE;
			}else if(args[args.length-1].equalsIgnoreCase("color:" + "YELLOW")){
				color = ChatColor.YELLOW;
			}else{
				color = ChatColor.GRAY;
				chatcolor = false;
			}
			String text = "";
			int c = 1;
			while(args.length > c){
				if((args.length-1) == c && chatcolor){
					break;
				}
				text += args[c]+" ";
				c++;

			}
			if(args[0].equalsIgnoreCase("jaotan")){
				color = ChatColor.GOLD;
			}
			Date = new Date();
			H = new SimpleDateFormat("H");
			m = new SimpleDateFormat("m");
			s = new SimpleDateFormat("s");
			Hs = H.format(Date);
			ms = m.format(Date);
			ss = s.format(Date);
			date = String.format("%02d", Integer.parseInt(Hs)) + ":" + String.format("%02d", Integer.parseInt(ms)) + ":" + String.format("%02d", Integer.parseInt(ss));
			try{
				OnMyMaidJoinLeftChatCmdLogs.log(plugin, "chat", args[0], text);
			}catch(java.lang.NoClassDefFoundError e){
				Method.SendMessage(sender, cmd, BugReport.report(e));
			}
			Bukkit.broadcastMessage(ChatColor.GRAY + "["+ date + "]" + color + "■" + ChatColor.WHITE + args[0] +  ": " + text);
			text = Discord.format(text);
			Discord.send("**" + args[0] + "**: " + text);
		}else{
			Method.SendMessage(sender, cmd, "このコマンドには2つ以上の引数が必要です。");
		}
		return true;
	}
}
