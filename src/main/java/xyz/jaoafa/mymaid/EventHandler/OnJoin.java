package xyz.jaoafa.mymaid.EventHandler;

import java.net.InetAddress;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

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

		}else if(player.hasPermission("mymaid.pex.provisional")){
			Method.url_jaoplugin("sinki", "p="+player.getName()+"&pex=Provisional");
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(p.hasPermission("pin_code_auth.joinmsg")) {
					p.sendMessage("[PCA] " + ChatColor.GREEN + "新規ちゃん(通過有)だよ！やったね☆");
				}
			}
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " group set D");
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
		if(!event.getPlayer().hasPlayedBefore()) {
		    event.getPlayer().sendMessage("[PCA] " + ChatColor.GREEN + "こんにちは！Jao Minecraft Serverにようこそ。");
		    event.getPlayer().sendMessage("[PCA] " + ChatColor.GREEN + "ルールはご覧になりましたか？ご覧になられていない場合は下のURLをクリックしてご覧ください");
		    event.getPlayer().sendMessage("[PCA] " + ChatColor.GREEN + "https://jaoafa.xyz/");
		}
	}
}
