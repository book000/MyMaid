package xyz.jaoafa.mymaid.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Dynamic implements CommandExecutor {
	JavaPlugin plugin;
	public Dynamic(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		if(args.length != 1){
			Method.SendMessage(sender, cmd, "引数が適切ではありません。");
			return true;
		}
		if(args[0].equalsIgnoreCase("true")){
			Dynmap_Teleporter.dynamic.put(player.getName(), true);
			Method.SendMessage(sender, cmd, "trueに設定しました。");
			return true;
		}else{
			Dynmap_Teleporter.dynamic.remove(player.getName());
			Method.SendMessage(sender, cmd, "falseに設定しました。");
			return true;
		}
	}
}
