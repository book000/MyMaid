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
					sender.sendMessage("[CHAT] " + ChatColor.GREEN + "オンラインユーザーを話者に指定できません。");
					return true;
				}
			}
			String text = "";
			int c = 1;
			while(args.length > c){
				text += args[c]+" ";
				c++;
			}
			Date = new Date();
			H = new SimpleDateFormat("H");
			m = new SimpleDateFormat("m");
			s = new SimpleDateFormat("s");
			Hs = H.format(Date);
			ms = m.format(Date);
			ss = s.format(Date);
			date = String.format("%02d", Integer.parseInt(Hs)) + ":" + String.format("%02d", Integer.parseInt(ms)) + ":" + String.format("%02d", Integer.parseInt(ss));
			Bukkit.broadcastMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GRAY + "■" + ChatColor.WHITE + args[0] +  ": " + text);
		}else{
			sender.sendMessage("[CHAT] " + ChatColor.GREEN + "このコマンドには2つ以上の引数が必要です。");
		}
		return true;
	}
}
