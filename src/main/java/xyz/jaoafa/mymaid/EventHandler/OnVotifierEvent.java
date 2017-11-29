package xyz.jaoafa.mymaid.EventHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.MySQL;
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

		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				BugReport.report(e1);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			BugReport.report(e);
			return;
		}

		statement = MySQL.check(statement);

		UUID uuid = null;
		try {
			ResultSet res = statement.executeQuery("SELECT * FROM log WHERE player = '" + name + "'");
			if(res.next()){
				uuid = UUID.fromString(res.getString("uuid"));
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			BugReport.report(e);
			return;
		}

		if(uuid == null){
			Discord.send("254166905852657675", ":outbox_tray:**投票受信エラー**: " + name + "のプレイヤーデータがデータベースから取得できなかったため、投票処理が正常に行われませんでした。");
			return;
		}

		/* ------------- 投票イベント関連開始 ------------- */

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
		/*
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date start = format.parse("2017/07/01 00:00:00");
			Date end = format.parse("2017/07/14 23:59:59");
			if(Method.isPeriod(start, end)){
				VOTECOUNT += 20;
				Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + player.getName() + "さんが投票し、七夕イベントボーナスを追加で20ポイントポイント追加しました。");
    			Discord.send(player.getName() + "さんが投票し、七夕イベントボーナスを追加で20ポイント追加しました。");
			}
		} catch (ParseException e) {
			BugReport.report(e);
		}
		*/

		/* ------------- 投票イベント関連終了 ------------- */

		if(Bukkit.getPlayer(uuid) != null){
			Player player = Bukkit.getPlayer(uuid);
			if(player == null){
				Discord.send("254166905852657675", ":outbox_tray:**投票受信エラー**: " + name + "のBukkit.getPlayerがnullを返却したため、投票処理が正常に行われませんでした。");
				return;
			}
			name = player.getName();

			SKKColors.UpdatePlayerSKKColor(player);
			oldVote = String.valueOf(SKKColors.getPlayerVoteCount(player)) + "回";

			i = Method.url_jaoplugin("vote", "p=" + name + "&u=" + uuid + plusargs);

			SKKColors.UpdatePlayerSKKColor(player);
			newVote = String.valueOf(SKKColors.getPlayerVoteCount(player)) + "回";

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date Date = new Date();
			oldjao = String.valueOf(Pointjao.getjao(player)) + "jao";
			Pointjao.addjao(player, VOTEPOINT, sdf.format(Date) + "の投票ボーナス");
			newjao = String.valueOf(Pointjao.getjao(player)) + "jao";
		}else if(Bukkit.getOfflinePlayer(uuid) != null){
			OfflinePlayer offplayer = Bukkit.getOfflinePlayer(uuid);
			if(offplayer == null){
				Discord.send("254166905852657675", ":outbox_tray:**投票受信エラー**: " + name + "のBukkit.getOfflinePlayerがnullを返却したため、投票処理が正常に行われませんでした。");
				return;
			}
			name = offplayer.getName();

			SKKColors.UpdatePlayerSKKColor(offplayer);
			oldVote = String.valueOf(SKKColors.getPlayerVoteCount(offplayer)) + "回";

			i = Method.url_jaoplugin("vote", "p=" + name + "&u=" + uuid + plusargs);

			SKKColors.UpdatePlayerSKKColor(offplayer);
			newVote = String.valueOf(SKKColors.getPlayerVoteCount(offplayer)) + "回";

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date Date = new Date();
			oldjao = String.valueOf(Pointjao.getjao(offplayer)) + "jao";
			Pointjao.addjao(offplayer, VOTEPOINT, sdf.format(Date) + "の投票ボーナス");
			newjao = String.valueOf(Pointjao.getjao(offplayer)) + "jao";
		}else{
			Discord.send("254166905852657675", ":outbox_tray:**投票受信エラー**: " + name + "のオフラインプレイヤーデータが取得できなかったため、投票処理が正常に行われませんでした。");
			return;
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + "プレイヤー「" + name + "」が投票をしました！(現在の投票数:" + i + "回)");
		Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + "投票をよろしくお願いします！ https://bitly.com/jfvote");
		Discord.send("プレイヤー「" + name + "」が投票をしました！(現在の投票数:" + i + "回)");
		Discord.send("投票をよろしくお願いします！ https://jaoafa.com/vote");
		Discord.send("254166905852657675", ":inbox_tray:**投票を受信しました。(" + format.format(new Date()) + ")**\n"
				+ "プレイヤー: `"  + name + "`\n"
				+ "投票前カウント: " + oldVote + "\n"
				+ "投票後カウント: " + newVote + "\n"
				+ "投票前jaoポイント: " + oldjao + "\n"
				+ "投票後jaoポイント: " + newjao);

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
	}
}
