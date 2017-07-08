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
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onVotifierEvent(VotifierEvent event) {
		final int VOTEPOINT = 20;

        Vote vote = event.getVote();
        String name = vote.getUsername();
        String i;
        if (Bukkit.getPlayer(vote.getUsername()) == null) {
        	i = Method.url_jaoplugin("vote", "p="+name);
        	String uuid = Method.url_jaoplugin("point", "p="+name);
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date Date = new Date();
        	Pointjao.addjao("" + uuid, VOTEPOINT, sdf.format(Date) + "の投票ボーナス");
        } else {
        	Player player;
        	if (Bukkit.getPlayer(name) == null) {
        		player = Bukkit.getOfflinePlayer(name).getPlayer();
            } else {
            	player = Bukkit.getPlayer(name);
            }

        	UUID uuid = player.getUniqueId();
        	i = Method.url_jaoplugin("vote", "p="+name+"&u="+uuid);
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			Date Date = new Date();
        	Pointjao.addjao(player, VOTEPOINT, sdf.format(Date) + "の投票ボーナス");

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

        }
        Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + "プレイヤー「" + name + "」が投票をしました！(現在の投票数:" + i + "回)");
        Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + "投票をよろしくお願いします！ https://bitly.com/jfvote");
        Discord.send("プレイヤー「" + name + "」が投票をしました！(現在の投票数:" + i + "回)");
        Discord.send("投票をよろしくお願いします！ https://bitly.com/jfvote");
    }
}
