package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcbans.firestar.mcbans.events.PlayerBannedEvent;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.PermissionsManager;
import xyz.jaoafa.mymaid.Jail.EBan;
import xyz.jaoafa.mymaid.Jail.Jail;

public class OnBannedEvent implements Listener {
	JavaPlugin plugin;
	public OnBannedEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerBanned(PlayerBannedEvent event){
		String player = event.getPlayerName();
		String sender = event.getSenderName();
		String reason = event.getReason();

		if(event.isGlobalBan()){
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "プレイヤー「" + player + "」がプレイヤー「" + sender +"」によってGBanされました。");
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "理由「" + reason + "」");
			Method.url_jaoplugin("ban", "p="+player+"&b="+sender+"&t=gban&r="+reason);
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				String group = PermissionsManager.getPermissionMainGroup(p);
				if(group.equalsIgnoreCase("Admin")) {
					p.sendMessage("[BANDATA] " + ChatColor.GREEN + "サーバの評価値を上げるため、MCBansに証拠画像を提供してください！ http://mcbans.com/server/jaoafa.com");
				}
			}

			OfflinePlayer offplayer = Bukkit.getOfflinePlayer(player);
			OfflinePlayer jaotan = Bukkit.getOfflinePlayer("jaotan");
			if(Jail.isJail(offplayer)){
				Jail.JailRemove(offplayer, jaotan);
			}
		}else if(event.isLocalBan()){
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "プレイヤー「" + player + "」がプレイヤー「" + sender +"」によってLBanされました。");
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "理由「" + reason + "」");
			Method.url_jaoplugin("ban", "p="+player+"&b="+sender+"&t=lban&r="+reason);

			OfflinePlayer offplayer = Bukkit.getOfflinePlayer(player);
			OfflinePlayer jaotan = Bukkit.getOfflinePlayer("jaotan");
			if(Jail.isJail(offplayer)){
				Jail.JailRemove(offplayer, jaotan);
			}
			if(EBan.isEBan(offplayer)){
				EBan.Remove(offplayer);
			}
		}else if(event.isTempBan()){
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "プレイヤー「" + player + "」がプレイヤー「" + sender +"」によってTBanされました。");
			Bukkit.broadcastMessage("[BANDATA] " + ChatColor.GREEN + "理由「" + reason + "」");
			Method.url_jaoplugin("ban", "p="+player+"&b="+sender+"&t=tban&r="+reason);
		}
	}
}
