package xyz.jaoafa.mymaid.EventHandler;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(p.hasPermission("pin_code_auth.joinmsg")) {
					p.sendMessage("[PCA] " + ChatColor.GREEN + "新規ちゃんだよ！やったね☆");
				}
			}
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
		try{
			new netaccess(plugin, player).runTaskAsynchronously(plugin);
		}catch(java.lang.NoClassDefFoundError e){
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。通常は再起動で直りますが直らない場合は開発者に連絡を行ってください。");
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
				}
			}
		}
	}
	private class netaccess extends BukkitRunnable{
		Player player;
    	public netaccess(JavaPlugin plugin, Player player) {
    		this.player = player;
    	}
		@Override
		public void run() {
			Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
			String pex = "";
			for(String group : groups){
				if(PermissionsEx.getUser(player).inGroup(group)){
					if(PermissionsEx.getUser(player).inGroup("Default")){
						if(PermissionsEx.getUser(player).inGroup("Regular")){
							pex = "Regular";
						}
					}else{
						pex = group;
					}
				}
			}

			Method.url_jaoplugin("pex", "p="+player.getName()+"&u="+player.getUniqueId()+"&pex="+pex);
			String re = Method.url_jaoplugin("mcbanscheck", "p="+player.getName());
			if(re.equalsIgnoreCase("D")){
				Boolean check = true;
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(PermissionsEx.getUser(p).inGroup("Limited")){
						continue;
					}
					if(PermissionsEx.getUser(p).inGroup("Provisional")){
						continue;
					}
					if(PermissionsEx.getUser(p).inGroup("Default")){
						if(!PermissionsEx.getUser(p).inGroup("Regular")){
							continue;
						}
					}
					if(PermissionsEx.getUser(p).inGroup("Ban")){
						continue;
					}
					if(AFK.tnt.containsKey(p.getName())){
						continue;
					}
					check = false;
				}
				if(check){
					Method.url_jaoplugin("sinki", "p="+player.getName()+"&pex=Limited");
					return;
				}else{
					Method.url_jaoplugin("sinki", "p="+player.getName()+"&pex=Default");
				}
				groups = PermissionsEx.getPermissionManager().getGroupNames();
				for(String group : groups){
					if(PermissionsEx.getUser(player).inGroup(group)){
						PermissionsEx.getUser(player).removeGroup(group);
					}
				}
				PermissionsEx.getUser(player).addGroup("QPPE");
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(!AFK.tnt.containsKey(p.getName()) && p.getGameMode() != GameMode.SPECTATOR){
						MyMaid.TitleSender.setTime_second(p, 2, 5, 2);
						MyMaid.TitleSender.sendTitle(p, "", ChatColor.GOLD + "jaotan" + ChatColor.WHITE + " によって " + ChatColor.BLUE + player.getName() + ChatColor.WHITE + " がQPPE権限に引き上げられました！");
						return;
					}
				}
				MyMaid.TitleSender.sendTitle(player, "ルールをお読みください！", "サイトはこちらです。 https://jaoafa.xyz/");
			}else{
				Method.url_jaoplugin("sinki", "p="+player.getName()+"&pex=Limited");
				return;
			}
		}
	}
}
