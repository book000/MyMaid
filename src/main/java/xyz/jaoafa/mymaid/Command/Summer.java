package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.EventHandler.OnSummer2017;

public class Summer implements CommandExecutor {
	JavaPlugin plugin;
	public Summer(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 1){
			// /summer online
			if(args[0].equalsIgnoreCase("online")){
				if (!(sender instanceof Player)) {
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}
				Player player = (Player) sender;
				Long counter = OnSummer2017.getSummerCounter(player);
				if(counter != 0){
					int time = (int) (counter / 1000);

					Method.SendMessage(sender, cmd, "ワールド「Summer2017」でのオンライン時間は " + (time/3600) + "時間" + ((time % 3600) / 60) + "分" + (time % 60) + "秒 (" + time + "秒)です。");
					return true;
				}else{
					Method.SendMessage(sender, cmd, "ワールド「Summer2017」でのオンライン時間は 0秒です。");
					return true;
				}
			}
		}else if(args.length == 2){
			// /summer online Player
			if(args[0].equalsIgnoreCase("online")){
				Player player = Bukkit.getPlayerExact(args[1]);
				if(player == null){
					Method.SendMessage(sender, cmd, "プレイヤー「" + args[1] + "」が見つかりません。");
					return true;
				}
				Long counter = OnSummer2017.getSummerCounter(player);
				if(counter != 0){
					int time = (int) (counter / 1000);
					Method.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」のワールド「Summer2017」でのオンライン時間は " + (time/3600) + "時間" + ((time % 3600) / 60) + "分" + (time % 60) + "秒 (" + time + "秒)です。");
					return true;
				}else{
					Method.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」のワールド「Summer2017」でのオンライン時間は 0秒です。");
					return true;
				}
			}
		}
		Method.SendMessage(sender, cmd, "----- Summer -----");
		Method.SendMessage(sender, cmd, "/summer online [Player]: ワールド「Summer2017」でのオンライン時間を表示します。");
		return true;
	}
}
