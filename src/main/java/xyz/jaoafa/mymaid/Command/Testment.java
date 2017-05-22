package xyz.jaoafa.mymaid.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Jail.Jail;

public class Testment implements CommandExecutor {
	JavaPlugin plugin;
	public Testment(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 1){
			if(!(sender instanceof Player)){
				Method.SendMessage(sender, cmd, "サーバ内で実行してください。");
				return true;
			}
			Player player = (Player) sender;
			// 遺言
			String lasttext = "";
			int c = 0;
			while(args.length > c){
				lasttext += args[c];
				if(args.length != (c+1)){
					lasttext += " ";
				}
				c++;
			}
			Jail.JailLastText(cmd, player, lasttext);
			return true;
		}
		Method.SendMessage(sender, cmd, "---- Testment ----");
		Method.SendMessage(sender, cmd, "/testment <LastText>: 遺言を残します。");
		return true;
	}
}
