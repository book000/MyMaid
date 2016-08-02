package xyz.jaoafa.mymaid.EventHandler;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Command.AFK;

public class OnJoin implements Listener {
	JavaPlugin plugin;
	public OnJoin(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer(); // Joinしたプレイヤー
		InetAddress ip = player.getAddress().getAddress();
		if(player.hasPermission("mymaid.pex.limited")){
			Method.url_jaoplugin("sinki", "p="+player.getName()+"&pex=Limited");
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(p.hasPermission("pin_code_auth.joinmsg")) {
					p.sendMessage("[PCA] " + ChatColor.GREEN + "新規ちゃん(通過無)だよ！やったね☆");
				}
			}
			/*
		}else if(player.hasPermission("mymaid.pex.provisional")){
			Method.url_jaoplugin("sinki", "p="+player.getName()+"&pex=Provisional");
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(p.hasPermission("pin_code_auth.joinmsg")) {
					p.sendMessage("[PCA] " + ChatColor.GREEN + "新規ちゃん(通過有)だよ！やったね☆");
				}
			}
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " group set D");
			*/
		}else{
			return;
		}

		String data = Method.url_access("http://nubesco.jaoafa.xyz/plugin/access.php?i="+ip);
		if(data.equalsIgnoreCase("NO")){
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(p.hasPermission("pin_code_auth.joinmsg")) {
					p.sendMessage("[PCA] " + ChatColor.GREEN + "このユーザーがアクセスしたページ:なし");
				}
			}

			Bukkit.getLogger().info("このユーザーがアクセスしたページ:なし");
		}else if(data.indexOf(",") == -1){
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(p.hasPermission("pin_code_auth.joinmsg")) {
					p.sendMessage("[PCA] " + ChatColor.GREEN + "このユーザーがアクセスしたページ:"+data+"");
				}
			}
			Bukkit.getLogger().info("このユーザーがアクセスしたページ:"+data+"");
		}else{
			String[] access = data.split(",", 0);
			String accesstext = "";
			for (String one: access){
				accesstext += "「"+one+"」";
			}
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(p.hasPermission("pin_code_auth.joinmsg")) {
					p.sendMessage("[PCA] " + ChatColor.GREEN + "このユーザーがアクセスしたページ:"+accesstext+"など");
				}
			}
			Bukkit.getLogger().info("このユーザーがアクセスしたページ:"+accesstext+"など");
		}
		Date Date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		player.sendMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GRAY + "■" + ChatColor.GOLD + "jaotan" +  ": " + "こんにちは！" + player.getName() + "さん！jao Minecraft Serverにようこそ！");
		player.sendMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GRAY + "■" + ChatColor.GOLD + "jaotan" +  ": " + "ルールはご覧になりましたか？もしご覧になられていない場合は以下リンクからご覧ください。");
		player.sendMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GRAY + "■" + ChatColor.GOLD + "jaotan" +  ": " + "https://jaoafa.xyz/rule");
		if(OnAsyncPlayerPreLoginEvent.LD.containsKey(player.getName())){
			if(OnAsyncPlayerPreLoginEvent.LD.get(player.getName())){
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(PermissionsEx.getUser(p).inGroup("Limited")){
						return;
					}
					if(PermissionsEx.getUser(p).inGroup("Provisional")){
						return;
					}
					if(PermissionsEx.getUser(p).inGroup("Default")){
						return;
					}
					if(PermissionsEx.getUser(p).inGroup("Ban")){
						return;
					}
					if(AFK.tnt.containsKey(p.getName())){
						return;
					}
				}
				Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
				for(String group : groups){
					if(PermissionsEx.getUser(player).inGroup(group)){
						PermissionsEx.getUser(player).removeGroup(group);
					}
				}
				PermissionsEx.getUser(player).addGroup("Default");
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(!AFK.tnt.containsKey(p.getName())){
						MyMaid.TitleSender.setTime_second(p, 2, 5, 2);
						MyMaid.TitleSender.sendTitle(p, "", ChatColor.GOLD + "jaotan" + ChatColor.WHITE + " によって " + ChatColor.BLUE + player.getName() + " がDefault権限に引き上げられました！");
					}
				}

			}
		}
	}
}
