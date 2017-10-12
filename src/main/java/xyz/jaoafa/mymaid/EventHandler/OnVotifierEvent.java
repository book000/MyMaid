package xyz.jaoafa.mymaid.EventHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Pointjao;
import xyz.jaoafa.mymaid.Discord.Discord;
import xyz.jaoafa.mymaid.SKKColors.SKKColors;

public class OnVotifierEvent implements Listener {
	JavaPlugin plugin;
	public OnVotifierEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onVotifierEvent(VotifierEvent event) {
		final int VOTEPOINT = 20;

		String oldVote = "取得できませんでした";
		String newVote = "取得できませんでした";

		String oldjao = "取得できませんでした";
		String newjao = "取得できませんでした";

		Vote vote = event.getVote();
		String name = vote.getUsername();
		String i;
		if (Bukkit.getPlayer(name) == null) {
			String plusargs = "";

			// ハロウィンイベント Issue #18
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date start = format.parse("2017/10/15 09:00:00");
				Date end = format.parse("2017/11/01 08:59:59");
				if(Method.isPeriod(start, end)){
					plusargs = "&pluscount=2"; // 2倍
				}
			} catch (ParseException e) {
				BugReport.report(e);
			}

			i = Method.url_jaoplugin("vote", "p=" + name + plusargs);
			String uuid = Method.url_jaoplugin("point", "p=" + name);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date Date = new Date();
			oldjao = String.valueOf(Pointjao.getjao(uuid)) + "jao";
			Pointjao.addjao(uuid, VOTEPOINT, sdf.format(Date) + "の投票ボーナス");
			newjao = String.valueOf(Pointjao.getjao(uuid)) + "jao";
		} else {
			Player player = Bukkit.getPlayer(name);

			if(player == null){
				Discord.send("254166905852657675", ":outbox_tray:**エラー**: " + name + "のBukkit.getPlayerがnullを返却したため、投票処理が正常に行われませんでした。");
				return;
			}
			SKKColors.UpdatePlayerSKKColor(player);
			oldVote = String.valueOf(SKKColors.getPlayerVoteCount(player)) + "回";

			String plusargs = "";

			// ハロウィンイベント Issue #18
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date start = format.parse("2017/10/15 09:00:00");
				Date end = format.parse("2017/11/01 08:59:59");
				if(Method.isPeriod(start, end)){
					plusargs = "&pluscount=2"; // 2倍
				}
			} catch (ParseException e) {
				BugReport.report(e);
			}

			UUID uuid = player.getUniqueId();
			i = Method.url_jaoplugin("vote", "p=" + name + "&u=" + uuid + plusargs);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date Date = new Date();
			Pointjao.addjao(player, VOTEPOINT, sdf.format(Date) + "の投票ボーナス");

			/*
        	try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date start = format.parse("2017/07/01 00:00:00");
				Date end = format.parse("2017/07/14 23:59:59");
				if(Method.isPeriod(start, end)){
					Pointjao.addjao(player, 10, "七夕投票イベント");
					Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + player.getName() + "さんが投票し、七夕イベントボーナスを追加で20ポイントポイント追加しました。");
	    			Discord.send(player.getName() + "さんが投票し、七夕イベントボーナスを追加で20ポイント追加しました。");
				}
			} catch (ParseException e) {
				BugReport.report(e);
			}
			 */

			//SimpleDateFormat date = new SimpleDateFormat("yyyy-MM");
			/*
    		if(date.format(Date).equalsIgnoreCase("2017-02")){
    			Random rnd = new Random();
    			int random = rnd.nextInt(50)+1;

    			Pointjao.addjao(player, random, sdf.format(Date) + "の投票ボーナス(2月ポイント補填ボーナス)");
    			Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + player.getName() + "さんが投票し、2月ポイント補填ボーナスを" + random + "ポイント追加しました。");
    			Discord.send(player.getName() + "さんが投票し、2月ポイント補填ボーナスを" + random + "ポイント追加しました。");
    		}
			 */
			SKKColors.UpdatePlayerSKKColor(player);
			newVote = String.valueOf(SKKColors.getPlayerVoteCount(player)) + "回";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + "プレイヤー「" + name + "」が投票をしました！(現在の投票数:" + i + "回)");
		Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + "投票をよろしくお願いします！ https://bitly.com/jfvote");
		Discord.send("プレイヤー「" + name + "」が投票をしました！(現在の投票数:" + i + "回)");
		Discord.send("投票をよろしくお願いします！ https://bitly.com/jfvote");
		Discord.send("254166905852657675", ":inbox_tray:**投票を受信しました。(" + format.format(new Date()) + ")**\n"
					+ "プレイヤー: `"  + name + "`\n"
					+ "投票前カウント: " + oldVote + "\n"
					+ "投票後カウント: " + newVote + "\n"
					+ "投票前jaoポイント: " + oldjao + "\n"
					+ "投票後jaoポイント: " + newjao);
	}
}
