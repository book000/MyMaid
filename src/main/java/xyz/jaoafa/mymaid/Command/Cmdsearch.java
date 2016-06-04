package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Cmdsearch implements CommandExecutor {
	JavaPlugin plugin;
	public Cmdsearch(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String,String> start = new HashMap<String,String>();
	public static Map<String,String> end = new HashMap<String,String>();
	public static Map<String,String> in = new HashMap<String,String>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 2){
			String text = "";
			int c = 1;
			while(args.length > c){
				text += args[c];
				if(args.length != (c+1)){
					text+=" ";
				}
				c++;
			}
			if(args[0].equalsIgnoreCase("start")){
				start.put(sender.getName(), text);
				sender.sendMessage("[CmdSearch] " + ChatColor.GREEN + "コマンドブロック検索を開始しました。(Start:" + text + ")");
				return true;
			}else if(args[0].equalsIgnoreCase("end")){
				end.put(sender.getName(), text);
				sender.sendMessage("[CmdSearch] " + ChatColor.GREEN + "コマンドブロック検索を開始しました。(End:" + text + ")");
				return true;
			}else if(args[0].equalsIgnoreCase("in")){
				in.put(sender.getName(), text);
				sender.sendMessage("[CmdSearch] " + ChatColor.GREEN + "コマンドブロック検索を開始しました。(In:" + text + ")");
				return true;
			}
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("stop")){
				if(start.containsKey(sender.getName())){
					start.remove(sender.getName());
				}
				if(end.containsKey(sender.getName())){
					end.remove(sender.getName());
				}
				if(in.containsKey(sender.getName())){
					in.remove(sender.getName());
				}
				sender.sendMessage("[CmdSearch] " + ChatColor.GREEN + "コマンドブロック検索を停止しました。");
				return true;
			}
		}else if(args.length == 0){
			if(start.containsKey(sender.getName())){
				sender.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Start:" + start.get(sender.getName()));
			}else{
				sender.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Start:なし");
			}
			if(end.containsKey(sender.getName())){
				sender.sendMessage("[CmdSearch] " + ChatColor.GREEN + "End:" + end.get(sender.getName()));
			}else{
				sender.sendMessage("[CmdSearch] " + ChatColor.GREEN + "End:なし");
			}
			if(in.containsKey(sender.getName())){
				sender.sendMessage("[CmdSearch] " + ChatColor.GREEN + "In:" + in.get(sender.getName()));
			}else{
				sender.sendMessage("[CmdSearch] " + ChatColor.GREEN + "In:なし");
			}
			return true;
		}else{
			sender.sendMessage("[CmdSearch] " + ChatColor.GREEN + "引数なんとかしろァ");
			return true;
		}
		return true;
	}
}
