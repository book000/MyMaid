package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcbans.firestar.mcbans.events.PlayerBannedEvent;

import xyz.jaoafa.mymaid.Method;

public class OnBannedEvent implements Listener {
	JavaPlugin plugin;
	public OnBannedEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onPlayerBanned(PlayerBannedEvent event){
		String player = event.getPlayerName();
		String sender = event.getSenderName();
		String reason = event.getReason();

		if(event.isGlobalBan()){
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "プレイヤー「" + player + "」がプレイヤー「" + sender +"」によってGBanされました。");
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "理由「" + reason + "」");
			Method.url_jaoplugin("ban", "p="+player+"&b="+sender+"&t=gban&r="+reason);
		}else if(event.isLocalBan()){
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "プレイヤー「" + player + "」がプレイヤー「" + sender +"」によってLBanされました。");
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "理由「" + reason + "」");
			Method.url_jaoplugin("ban", "p="+player+"&b="+sender+"&t=lban&r="+reason);
		}else if(event.isTempBan()){
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "プレイヤー「" + player + "」がプレイヤー「" + sender +"」によってTBanされました。");
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "理由「" + reason + "」");
			Method.url_jaoplugin("ban", "p="+player+"&b="+sender+"&t=tban&r="+reason);
		}
	}
}
