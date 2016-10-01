package xyz.jaoafa.mymaid.EventHandler;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
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
import org.bukkit.scheduler.BukkitRunnable;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Method;

public class OnAsyncPlayerPreLoginEvent implements Listener {
	JavaPlugin plugin;
	public OnAsyncPlayerPreLoginEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String,Boolean> LD = new HashMap<String,Boolean>();
	@EventHandler
	public void onJoin(AsyncPlayerPreLoginEvent e){
		String name = e.getName();
		InetAddress ip = e.getAddress();
		UUID uuid = e.getUniqueId();
		String host = e.getAddress().getHostName();
		String data = Method.url_access("http://nubesco.jaoafa.xyz/plugin/login.php?p="+name+"&u="+uuid+"&i="+ip+"&h="+host);
		String[] arr = data.split("###", 0);
		if(arr[0].equalsIgnoreCase("subaccount")){
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "Detect SubAccount.");
			Bukkit.getLogger().info(e.getName()+":Connection to server failed!(Detect SubAccount.)");
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + e.getName()+"->>複垢(" + arr[1] + ")");
				}
			}
			return;
		}else if(arr[0].equalsIgnoreCase("pban")){
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "[PBan] " + arr[2]);
			Bukkit.getLogger().info(e.getName()+":Connection to server failed!([PBan] " + arr[2] + ")");
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + e.getName()+"->>複垢(" + arr[1] + ")");
				}
			}
			return;
		}else if(arr[0].equalsIgnoreCase("countoryout")){
			URLCodec codec = new URLCodec();
			String country = null;
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "Connection to server failed");
			try {
				country = codec.decode(arr[1], StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException e1) {
				// TODO 自動生成された catch ブロック
				Bukkit.getLogger().info("err");
			} catch (DecoderException e1) {
				Bukkit.getLogger().info("err");
			}
			Bukkit.getLogger().info(e.getName()+":Connection to server failed!(Login Country OUT "+country+")");
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + e.getName()+"->>許可されていない地域からのアクセス("+country+")");
				}
			}
			return;
		}else if(arr[0].equalsIgnoreCase("mcbans_reputation")){
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "Connection to server failed...");
			Bukkit.getLogger().info(e.getName()+":Connection to server failed!(mcbans_reputation)");
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + e.getName()+"->>鯖指定評価値下回り(" + arr[1] + ")");
				}
			}
			return;
		}

		new netaccess(plugin, name, uuid, ip, host).runTaskAsynchronously(plugin);

		Bukkit.getLogger().info("------------------------------------------");
		Bukkit.getLogger().info("Player:"+name+" Log in.");
		Bukkit.getLogger().info("PlayerUUID:"+uuid);
		Bukkit.getLogger().info("PlayerIP:"+e.getAddress());
		Bukkit.getLogger().info("PlayerHost:"+host);
		Bukkit.getLogger().info("------------------------------------------");
	}
	private class netaccess extends BukkitRunnable{
		String player;
		UUID uuid;
		InetAddress ip;
		String host;
    	public netaccess(JavaPlugin plugin, String player, UUID uuid, InetAddress ip, String host) {
    		this.player = player;
    		this.uuid = uuid;
    		this.ip = ip;
    		this.host = host;
    	}
		@Override
		public void run() {
			Method.url_jaoplugin("mccheck_anotherlogin", "p=" + player + "&u=" + uuid + "&i=" + ip + "&host=" + host);
		}
	}
}
