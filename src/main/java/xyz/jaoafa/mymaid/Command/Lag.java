package xyz.jaoafa.mymaid.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Lag implements CommandExecutor {
	JavaPlugin plugin;
	public Lag(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/* onCommand jf
	 * jao afaします。
	 * /jf */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		String data = Method.url_jaoplugin("lag", "show");
		String[] arr = data.split("###", 0);
		String start = arr[0];
		String end = arr[1];
		String lag = arr[2];
		Method.SendMessage(sender, cmd, "最新のラグデータ: " + lag + "秒 (" + start + "～" + end + ")");
		return true;
	}
}
