package xyz.jaoafa.mymaid.EventHandler;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class OnAsyncPlayerPreLoginEvent implements Listener {
	JavaPlugin plugin;
	public OnAsyncPlayerPreLoginEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onJoin(AsyncPlayerPreLoginEvent e){
		String name = e.getName();
		InetAddress ip = e.getAddress();
		UUID uuid = e.getUniqueId();
		String host = e.getAddress().getHostName();
		String data = Method.url_access("http://nubesco.jaoafa.xyz/plugin/login.php?p="+name+"&u="+uuid+"&i="+ip+"&h="+host);
		String[] arr = data.split("###", 0);
		if(!(arr[1].equalsIgnoreCase("NOSUB"))){
			if(!(arr[0].equalsIgnoreCase("SUCCESS"))){
				e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "Detect SubAccount.");
				Bukkit.getLogger().info(e.getName()+":Connection to server failed!(Detect SubAccount.)");
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(p.hasPermission("pin_code_auth.joinmsg")) {
						p.sendMessage("[MyMaid] " + ChatColor.GREEN + e.getName()+"->>複垢(" + arr[0] + ")");
					}
				}
				return;
			}
		}
		if(arr[1].equalsIgnoreCase("BANNED")){
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "[PBan] " + arr[2]);
			Bukkit.getLogger().info(e.getName()+":Connection to server failed!([PBan] " + arr[2] + ")");
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(p.hasPermission("pin_code_auth.joinmsg")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + e.getName()+"->>[PBan] " + arr[2]);
				}
			}
			return;
		}
		URLCodec codec = new URLCodec();
		String country = null;
		if(!arr[3].equalsIgnoreCase("OK")){
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "Connection to server failed");
			try {
				country = codec.decode(arr[3], StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException e1) {
				// TODO 自動生成された catch ブロック
				Bukkit.getLogger().info("err");
			} catch (DecoderException e1) {
				Bukkit.getLogger().info("err");
			}
			Bukkit.getLogger().info(e.getName()+":Connection to server failed!(Login Country OUT "+country+")");
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(p.hasPermission("pin_code_auth.joinmsg")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + e.getName()+"->>許可されていない地域からのアクセス("+country+")");
				}
			}
			return;
		}


		Bukkit.getLogger().info("------------------------------------------");
		Bukkit.getLogger().info("Player:"+name+" Log in.");
		Bukkit.getLogger().info("PlayerUUID:"+uuid);
		Bukkit.getLogger().info("PlayerIP:"+e.getAddress());
		Bukkit.getLogger().info("PlayerHost:"+host);
		Bukkit.getLogger().info("------------------------------------------");
	}

}
