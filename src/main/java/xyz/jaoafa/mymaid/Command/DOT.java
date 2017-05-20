package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Pointjao;

public class DOT implements CommandExecutor {
	JavaPlugin plugin;
	public DOT(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	//連投待機中か
	public static Map<String, Integer> runwait = new HashMap<String, Integer>();
	//かな変換
	public static Map<String, Boolean> kana = new HashMap<String, Boolean>();
	//連投実行中か
	public static Map<String, BukkitTask> run = new HashMap<String, BukkitTask>();
	//成功回数
	public static Map<String, Integer> success = new HashMap<String, Integer>();
	//失敗回数
	public static Map<String, Integer> unsuccess = new HashMap<String, Integer>();
	//DOTCOUNTERストップ
	public static Map<String, Boolean> dotcount_stop = new HashMap<String, Boolean>();

	public static Map<String,Integer> dotto = new HashMap<String,Integer>();
	public static Map<String,BukkitTask> dottotask = new HashMap<String,BukkitTask>();

	public static Map<String,Integer> doom = new HashMap<String,Integer>();
	public static Map<String,BukkitTask> doomtask = new HashMap<String,BukkitTask>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		if(player.isSleeping()){
			Method.SendMessage(sender, cmd, "ベットで寝ながらは違反だゾ！");
			return true;
		}
		if(run.containsKey(player.getName())){
			Method.SendMessage(sender, cmd, "ピリオド対決中だゾ！集中しやがれ！");
			return true;
		}
		if(runwait.containsKey(player.getName())){
			Method.SendMessage(sender, cmd, "ピリオド対決準備中だゾ！次に「.」を打った瞬間から開始だゾ！");
			return true;
		}
		/*
		int lag = (int) MyMaid.lag;
		if(lag > 1){
			Method.SendMessage(sender, cmd, "ピリオド対決を開始できません。ラグが激しすぎます。");
			return true;
		}
		*/
		if(args.length == 0){
			Method.SendMessage(sender, cmd, "引数無しでのピリオド連投はサポートを終了しました。「/. 60」をご利用ください。");
			return true;
		}
		int section = 60;
		Boolean sectionout = false;
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("10")){
				section = 10;
			}else if(args[0].equalsIgnoreCase("60")){
				section = 60;
			}else if(args[0].equalsIgnoreCase("300")){
				section = 300;
			}else{
				sectionout = true;
				try{
					section = Integer.parseInt(args[0]);
				}catch(NumberFormatException e){
					Method.SendMessage(sender, cmd, "数値を指定してください。");
					return true;
				}
			}
		}

		int use = 5;
		if(!Pointjao.hasjao(player, use)){
		 	 Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
		 	 return true;
		}
		Pointjao.usejao(player, use, "「/.」コマンド実行の為");

		Method.SendMessage(sender, cmd, "ピリオド対決を開始します。次に「.」を打った瞬間から" + section + "秒間計測します。");
		Method.SendMessage(sender, cmd, "正確にピリオド判定を行うため、かなローマ字変換をオフにして(/jp off)ご利用ください。");
		if(sectionout){
			Method.SendMessage(sender, cmd, "10秒、60秒、300秒以外であるため、ランキングには掲載されません。");
		}
		runwait.put(player.getName(), section);
		return true;
	}
}
