package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Rider implements CommandExecutor {
	JavaPlugin plugin;
	public Rider(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		if(args.length == 1){
			Player p = Bukkit.getPlayerExact(args[0]);
			if(p == null){
				Method.SendMessage(sender, cmd, "指定されたプレイヤーは見つかりませんでした。");
				return true;
			}
			if(player.getUniqueId().toString().equals(p.getUniqueId().toString())){
				Method.SendMessage(sender, cmd, "処理できません。");
				return true;
			}
			player.setPassenger(p);
			return true;
		}
		Method.SendMessage(sender, cmd, "----- Rider -----");
		Method.SendMessage(sender, cmd, "/rider <Player>: プレイヤーに乗ります");
		return true;
	}
}
