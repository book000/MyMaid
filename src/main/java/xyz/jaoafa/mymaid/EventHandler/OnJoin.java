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
import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Command.AFK;
import xyz.jaoafa.mymaid.Command.Cmd_Account;

public class OnJoin implements Listener {
	JavaPlugin plugin;
	public OnJoin(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer(); // Joinしたプレイヤー

		String tps1m = Method.getTPS1m();
		String tps5m = Method.getTPS5m();
		String tps15m = Method.getTPS15m();

		String tps1mcolor;
		try{
			double tps1m_double = Double.parseDouble(tps1m);
			tps1mcolor = Method.TPSColor(tps1m_double);
		}catch(NumberFormatException e){
			tps1mcolor = "green";
		}

		String tps5mcolor;
		try{
			double tps5m_double = Double.parseDouble(tps5m);
			tps5mcolor = Method.TPSColor(tps5m_double);
		}catch(NumberFormatException e){
			tps5mcolor = "green";
		}

		String tps15mcolor;
		try{
			double tps15m_double = Double.parseDouble(tps15m);
			tps15mcolor = Method.TPSColor(tps15m_double);
		}catch(NumberFormatException e){
			tps15mcolor = "green";
		}


		String tpsmsg = "[\"\",{\"text\":\"TPS\n\",\"color\":\"gold\"},{\"text\":\"1m: \",\"color\":\"red\"},{\"text\":\"" + tps1m + "\",\"color\":\"" + tps1mcolor + "\"},{\"text\":\"\n\",\"color\":\"\"},{\"text\":\"5m: \",\"color\":\"yellow\"},{\"text\":\"" + tps5m + "\",\"color\":\"" + tps5mcolor + "\"},{\"text\":\"\n\",\"color\":\"none\"},{\"text\":\"15m: \",\"color\":\"green\"},{\"text\":\"" + tps15m + "\",\"color\":\"" + tps15mcolor + "\"}]";

		Method.setPlayerListHeaderFooterByJSON(player, "[\"\",{\"text\":\"jao \",\"color\":\"gold\"},{\"text\":\"Minecraft \",\"color\":\"yellow\"},{\"text\":\"Server\",\"color\":\"aqua\"}]", tpsmsg);

		try{
			new MyMaid.usemodget(plugin, player).runTaskLater(plugin, 10L);
		}catch(java.lang.NoClassDefFoundError e){

		}
		InetAddress ip = player.getAddress().getAddress();
		if(PermissionsEx.getUser(player).inGroup("Limited")){
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "新規ちゃんだよ！やったね☆");
				}
			}
		}else{
			return;
		}
		if(!player.hasPlayedBefore()){
			// ログイン初
			Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + player.getName() + "さんは当サーバに初ログインです。はじめまして！");
		}

		String data = Method.url_jaoplugin("access", "i="+ip);
		if(data.equalsIgnoreCase("NO")){
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "このユーザーがアクセスしたページ:なし");
				}
			}

			Bukkit.getLogger().info("このユーザーがアクセスしたページ:なし");
		}else if(data.indexOf(",") == -1){
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "このユーザーがアクセスしたページ:"+data+"");
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
				if(PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "このユーザーがアクセスしたページ:"+accesstext+"など");
				}
			}
			Bukkit.getLogger().info("このユーザーがアクセスしたページ:"+accesstext+"など");
		}
		Date Date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		player.sendMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GRAY + "■" + ChatColor.GOLD + "jaotan" +  ": " + "こんにちは！" + player.getName() + "さん！jao Minecraft Serverにようこそ！");
		player.sendMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GRAY + "■" + ChatColor.GOLD + "jaotan" +  ": " + "ルールはご覧になりましたか？もしご覧になられていない場合は以下リンクからご覧ください。");
		player.sendMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GRAY + "■" + ChatColor.GOLD + "jaotan" +  ": " + "https://jaoafa.com/rule");
		if(!Cmd_Account.getStatus(Cmd_Account.getJson(player, "wp_getaccount"))){
			Cmd_Account.getJson(player, "wp_create");
		}

		try{
			new netaccess(plugin, player).runTaskAsynchronously(plugin);
		}catch(java.lang.NoClassDefFoundError e){
			new netaccess(plugin, player).run();
			BugReport.report(e);
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
				if(OnAsyncPlayerPreLoginEvent.FBAN.containsKey(player.getName())){
					check = false;
				}
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
					if(AFK.getAFKing(p)){
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
					if(!AFK.getAFKing(p) && p.getGameMode() != GameMode.SPECTATOR){
						MyMaid.TitleSender.setTime_second(p, 2, 5, 2);
						MyMaid.TitleSender.sendTitle(p, "", ChatColor.GOLD + "jaotan" + ChatColor.WHITE + " によって " + ChatColor.BLUE + player.getName() + ChatColor.WHITE + " がQPPE権限に引き上げられました！");
						return;
					}
				}
				MyMaid.TitleSender.sendTitle(player, "ルールをお読みください！", "サイトはこちらです。 https://jaoafa.com/");
			}else{
				Method.url_jaoplugin("sinki", "p="+player.getName()+"&pex=Limited");
				return;
			}
		}
	}
}
