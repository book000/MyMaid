package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Pointjao;

public class Jao implements CommandExecutor {
	JavaPlugin plugin;
	public Jao(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		if(args.length == 1){
			for(Player player: Bukkit.getServer().getOnlinePlayers()) {
				if(player.getName().equalsIgnoreCase(args[0])){
					int now = Pointjao.getjao(player);
					Method.SendMessage(sender, cmd, "現在" + player.getName() + "が所持しているポイント数は" + now + "ポイントです。");
					return true;
				}
			}
			Method.SendMessage(sender, cmd, "プレイヤーが見つかりません。");
			return true;
		}
		Player player = (Player) sender;
		int now = Pointjao.getjao(player);
		Method.SendMessage(sender, cmd, "現在あなたが所持しているポイント数は" + now + "ポイントです。");
		return true;
	}
}
