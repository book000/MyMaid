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

public class DOT implements CommandExecutor {
	JavaPlugin plugin;
	public DOT(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	//連投待機中か
	public static Map<String, Boolean> runwait = new HashMap<String, Boolean>();
	//連投実行中か
	public static Map<String, BukkitTask> run = new HashMap<String, BukkitTask>();
	//ベットで寝ているか
	public static Map<String, Boolean> bed = new HashMap<String, Boolean>();
	//成功回数
	public static Map<String, Integer> success = new HashMap<String, Integer>();
	//失敗回数
	public static Map<String, Integer> unsuccess = new HashMap<String, Integer>();
	//DOTCOUNTERストップ
	public static Map<String, Boolean> dotcount_stop = new HashMap<String, Boolean>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		if(bed.containsKey(player.getName())){
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
		Method.SendMessage(sender, cmd, "ピリオド対決を開始します。次に「.」を打った瞬間から60秒間計測します。");
		runwait.put(player.getName(), true);
		return true;
	}
}
