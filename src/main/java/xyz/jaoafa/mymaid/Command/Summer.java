package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Pointjao;
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
		}else if(args.length == 3){
			// /summer exchange [jP/jSP] jao
			if(args[0].equalsIgnoreCase("exchange")){
				// jP: jao Point(クリエイティブワールドで使用できるポイント)
				// jSP: (Summer2017ワールドで使用できるポイント)
				if(args[1].equalsIgnoreCase("jSP")){
					// jP -< jSP

					if (!(sender instanceof Player)) {
						Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
						return true;
					}
					Player player = (Player) sender;
					int i;
					try {
						i = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						Method.SendMessage(sender, cmd, "「交換ポイント」には数値を入力してください。");
						return true;
					}
					if(!Pointjao.hasjao(player, i)){
						Method.SendMessage(sender, cmd, "指定された交換ポイントのjaoPointをあなたは持っていません。");
						return true;
					}
					Pointjao.usejao(player, i, "jao Survival Pointへのポイント交換");
					MyMaid.econ.depositPlayer(player, i);
					Method.SendMessage(sender, cmd, "あなたは現在jao Survival Pointを " + MyMaid.econ.format(MyMaid.econ.getBalance(player)) + "持っています。");
					return true;
					//Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "money give " + player.getName() + " " + i);
				}else if(args[1].equalsIgnoreCase("jP")){
					// jSP -> jP
					if (!(sender instanceof Player)) {
						Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
						return true;
					}
					Player player = (Player) sender;
					int i;
					try {
						i = Integer.parseInt(args[2]);
					} catch (NumberFormatException e) {
						Method.SendMessage(sender, cmd, "「交換ポイント」には数値を入力してください。");
						return true;
					}
					if(!MyMaid.econ.has(player, i)){
						Method.SendMessage(sender, cmd, "指定された交換ポイントのjaoSurvivalPointをあなたは持っていません。");
						return true;
					}
					Pointjao.addjao(player, i, "jao Survival Pointからのポイント交換");
					MyMaid.econ.withdrawPlayer(player, i);
					Method.SendMessage(sender, cmd, "あなたは現在jao Survival Pointを " + MyMaid.econ.format(MyMaid.econ.getBalance(player)) + "持っています。");
					return true;
				}
			}
		}
		Method.SendMessage(sender, cmd, "----- Summer -----");
		Method.SendMessage(sender, cmd, "/summer online [Player]: ワールド「Summer2017」でのオンライン時間を表示します。");
		Method.SendMessage(sender, cmd, "/summer exchange <jP|jSP> <ExchangePoint>: jaoPointとjaoSurvivalPoint(/money)の交換をします。");
		Method.SendMessage(sender, cmd, "jP: jaoSurvivalPointからjaoPointに交換します。");
		Method.SendMessage(sender, cmd, "jSP: jaoPointからjaoSurvivalPointに交換します。");
		return true;
	}
}
