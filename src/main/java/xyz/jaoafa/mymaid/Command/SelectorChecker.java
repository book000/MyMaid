package xyz.jaoafa.mymaid.Command;

import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.ParseSelector;

public class SelectorChecker implements CommandExecutor {
	JavaPlugin plugin;
	public SelectorChecker(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length != 1){
			Method.SendMessage(sender, cmd, "---- Selector Checker ----");
			Method.SendMessage(sender, cmd, "/selector <Selector>: Selectorが適切かどうか調べます。");
			return true;
		}
		try{
			ParseSelector parser = new ParseSelector(args[0]);
			if(parser.isValidValues()){
				Method.SendMessage(sender, cmd, "指定されたセレクターは適切です。");
				Method.SendMessage(sender, cmd, "セレクター: " + parser.getSelector());
				Method.SendMessage(sender, cmd, "引数: ");
				for(Entry<String, String> one : parser.getArgs().entrySet()){
					String key = one.getKey();
					String value = one.getValue();
					Method.SendMessage(sender, cmd, key + " = " + value);
				}
				return true;
			}else{
				Method.SendMessage(sender, cmd, "指定されたセレクターは適切ではありません。");
				return true;
			}
		}catch(IllegalArgumentException e){
			Method.SendMessage(sender, cmd, "指定されたセレクターは適切でありません");
			Method.SendMessage(sender, cmd, "理由: " + e.getMessage());
			return true;
		}

	}
}
