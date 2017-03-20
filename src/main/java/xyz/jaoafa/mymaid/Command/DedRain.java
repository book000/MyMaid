package xyz.jaoafa.mymaid.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class DedRain implements CommandExecutor {
	JavaPlugin plugin;
	public DedRain(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static boolean flag;
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 1){
			String old = Boolean.toString(flag);
			if(args[0].equalsIgnoreCase("true")){
				Method.SendMessage(sender, cmd, "降水禁止設定を" + old + "から" + "true" + "に変更しました。");
				flag = true;
				return true;
			}else if(args[0].equalsIgnoreCase("false")){
				Method.SendMessage(sender, cmd, "降水禁止設定を" + old + "から" + "false" + "に変更しました。");
				flag = false;
				return true;
			}
		}else if(args.length == 0){
			String now = Boolean.toString(flag);
			Method.SendMessage(sender, cmd, "現在の降水禁止設定は" + now + "です。");
			Method.SendMessage(sender, cmd, "/dedrain <true/false>で変更することができます。");
			return true;
		}
		Method.SendMessage(sender, cmd, "----- DedRain -----");
		Method.SendMessage(sender, cmd, "/dedrain: 現在の降水禁止設定を確認できます。");
		Method.SendMessage(sender, cmd, "/dedrain <true/false>: 降水禁止設定を変更できます。");
		return true;
	}
}
