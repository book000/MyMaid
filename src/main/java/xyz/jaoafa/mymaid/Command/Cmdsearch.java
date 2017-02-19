package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

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
				Method.SendMessage(sender, cmd, "コマンドブロック検索を開始しました。(Start:" + text + ")");
				return true;
			}else if(args[0].equalsIgnoreCase("end")){
				end.put(sender.getName(), text);
				Method.SendMessage(sender, cmd, "コマンドブロック検索を開始しました。(End:" + text + ")");
				return true;
			}else if(args[0].equalsIgnoreCase("in")){
				in.put(sender.getName(), text);
				Method.SendMessage(sender, cmd, "コマンドブロック検索を開始しました。(In:" + text + ")");
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
				Method.SendMessage(sender, cmd, "コマンドブロック検索を停止しました。");
				return true;
			}
		}else if(args.length == 0){
			if(start.containsKey(sender.getName())){
				Method.SendMessage(sender, cmd, "Start:" + start.get(sender.getName()));
			}else{
				Method.SendMessage(sender, cmd, "Start:なし");
			}
			if(end.containsKey(sender.getName())){
				Method.SendMessage(sender, cmd, "End:" + end.get(sender.getName()));
			}else{
				Method.SendMessage(sender, cmd, "End:なし");
			}
			if(in.containsKey(sender.getName())){
				Method.SendMessage(sender, cmd, "In:" + in.get(sender.getName()));
			}else{
				Method.SendMessage(sender, cmd, "In:なし");
			}
			return true;
		}else{
			Method.SendMessage(sender, cmd, "引数なんとかしろァ");
			return true;
		}
		return true;
	}
}
