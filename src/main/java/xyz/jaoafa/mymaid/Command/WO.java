package xyz.jaoafa.mymaid.Command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Pointjao;
import xyz.jaoafa.mymaid.SKKColors.SKKColors;

public class WO implements CommandExecutor {
	JavaPlugin plugin;
	ArrayList<String> text = new ArrayList<String>();
	public static boolean nowwo = false;
	public static boolean stopwo = false;
	public WO(JavaPlugin plugin) {
		this.plugin = plugin;

		text.add("(‘o’) ﾍｲ ﾕﾉｫﾝﾜｲ ﾜｨｱｨﾜｨｬ?");
		text.add("(‘o’) ｲﾖｯﾊ ｲﾔﾊ ｲﾔﾊ ｲﾔﾊ ｲﾔﾊ ｲﾔﾊ ｲﾔﾊ … ｫﾎﾎｯﾊｰﾎﾎｯﾊｰﾎﾎｯﾊｰﾊﾊﾊﾊﾊﾎﾎ…");
		text.add("(‘o’) ィ～ッニャッハッハッハッハッハッハッハッハッハッ");
		text.add("(‘o’) ィ～ニャッハッハッハッハッハッハッハッハッ");
		text.add("(‘o’) ﾝィ～ッニャッハッハッハッハッハッハッハッハッハッハッ");
		text.add("(‘o’) オーホホオーホホオーホホホホホホ");
		text.add("(‘o’) ｲﾖ ｲﾖ ｲﾖ ﾎﾎ ｲﾖ ｲﾖ ｲﾖ ﾎﾎ ｲﾖ ｲﾖ ｲﾖ ﾎﾎ ｵｰﾎﾎ ｵｯﾎﾎ");
		text.add("(‘o’) ｲﾖ ｲﾖ ｲﾖ ﾎﾎ ｲﾖ ｲﾖ ｲﾖ ﾎﾎ ｲﾖ ｲﾖ ｲﾖ…ｲﾖ…ｲﾖ…ｲﾖ…");
		text.add("(‘o’) ィ～ニャッハッハッハッハッハハハッハッハハハッハッハッハハハッ(ﾋﾟｩｰﾝ)");
		text.add("(‘o’) ィ～ニャッハッハッハッハハハッハッハハハハハハッハッハッ(ｳｫｰｱｰ?ﾀﾞｨｬ)");
		text.add("(‘o’) ィ～ニャッハッハッハッハッハハハッハッハハハッハッハッハハハッ(ﾋﾟｩｰﾝ)");
		text.add("(‘o’) ィ～ニャッハッハッハッハハハッハッハハハハハハッハッハッ(ﾆｮﾝ)ウォオオオオウ！！！！！！");
		text.add("(‘o’) ＜ を");

	}


	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		int use = 10;
		if(!Pointjao.hasjao(player, use)){
		 	 Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
		 	 return true;
		}
		Pointjao.usejao(player, use, "woコマンド実行の為");
		String Msg = SKKColors.getPlayerSKKChatColor(player) + "■" + ChatColor.WHITE + player.getName() + ": ";
		try{
			new Message(Msg, player).runTaskTimer(plugin, 0, 20);
		}catch(java.lang.NoClassDefFoundError e){
			BugReport.report(e);
		}
		return true;
	}
	private class Message extends BukkitRunnable{
		int i;
		String Msg;
		Player player;
		public Message(String Msg, Player player){
			i = 0;
			this.Msg = Msg;
			this.player = player;
		}
		@Override
		public void run() {
			if(i == 0){
				nowwo = true;
			}
			if(i >= text.size()){
				nowwo = false;
				player.sendMessage("[WO] " + ChatColor.GREEN + "woコマンドを途中で止められずに終えることができたため、ポイントを返却します！");
				Pointjao.addjao(player, 10, "woコマンドのポイント返却のため");
				cancel();
			}else{
				if(stopwo){
					Date Date = new Date();
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + Msg + text.get(text.size()-1));
					nowwo = false;
					stopwo = false;
					cancel();
				}
				if(nowwo){
					Date Date = new Date();
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + Msg + text.get(i));
					i++;
				}
			}
		}
	}
}
