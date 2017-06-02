package xyz.jaoafa.mymaid.EventHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import xyz.jaoafa.mymaid.MySQL;
import xyz.jaoafa.mymaid.Pointjao;
import xyz.jaoafa.mymaid.SKKColors.SKKColors;

public class OnPlayerJoinEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerJoinEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if(Bukkit.getServer().getOnlinePlayers().size() >= 1 && !MyMaid.nextbakrender){
			MyMaid.nextbakrender = true;
		}

		Player player = event.getPlayer();

		if(OnAsyncPlayerPreLoginEvent.FBAN.containsKey(event.getPlayer().getName())){
			String id = OnAsyncPlayerPreLoginEvent.FBAN.get(event.getPlayer().getName());
			event.getPlayer().sendMessage(ChatColor.RED + "あなたによると思われる破壊行為が一部確認されました。\n"
					+ "一定期間中に連絡がない場合は処罰される可能性があります。\n"
					+ "詳しくは以下ページをご覧ください\n"
					+ "https://jaoafa.com/proof/fban/?id=" + id);
			Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
			for(String group : groups){
				if(PermissionsEx.getUser(event.getPlayer()).inGroup(group)){
					PermissionsEx.getUser(event.getPlayer()).removeGroup(group);
				}
			}
			PermissionsEx.getUser(event.getPlayer()).addGroup("Limited");
			event.getPlayer().setOp(false);
		}

		UUID uuid = event.getPlayer().getUniqueId();

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

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM vote WHERE uuid = '" + uuid.toString() +"'");
			Calendar cal = Calendar.getInstance();
			cal.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
			cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
			long today9 = cal.getTimeInMillis() / 1000L;

			cal.add(Calendar.DAY_OF_MONTH, -1);
			long yesterday9 = cal.getTimeInMillis() / 1000L;

			long now = System.currentTimeMillis() / 1000L;

			boolean checktype; // true: 今日の9時 / false: 昨日の9時
			if(today9 <= now){
				checktype = true;
			}else{
				checktype = false;
			}


			if(res.next()){
				long lasttime = Long.parseLong(res.getString("lasttime"));
				if(checktype){
					if(lasttime < today9){
						player.sendMessage("[Vote] " + ChatColor.GREEN + "まだこのサーバに投票していないみたいです！");
						player.sendMessage("[Vote] " + ChatColor.GREEN + "よろしければ投票をお願いします！ https://jaoafa.com/vote");
					}
				}else{
					if(lasttime < yesterday9){
						player.sendMessage("[Vote] " + ChatColor.GREEN + "まだこのサーバに投票していないみたいです！");
						player.sendMessage("[Vote] " + ChatColor.GREEN + "よろしければ投票をお願いします！ https://jaoafa.com/vote");
					}
				}
			}else{
				player.sendMessage("[Vote] " + ChatColor.GREEN + "まだこのサーバに投票していないみたいです！");
				player.sendMessage("[Vote] " + ChatColor.GREEN + "よろしければ投票をお願いします！ https://jaoafa.com/vote");
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			BugReport.report(e);
			return;
		}


		SKKColors.UpdatePlayerSKKColor(player);

		String JoinMessage = SKKColors.getPlayerSKKJoinMessage(player);
		if(JoinMessage != null){
			event.setJoinMessage(JoinMessage);
		}

		String point = Method.url_jaoplugin("point", "u=" + event.getPlayer().getUniqueId());
		if(point.equalsIgnoreCase("YES")){
			SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
			Date Date = new Date();
			Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + event.getPlayer().getName() + "は本日初ログインです。");
			Pointjao.addjao(event.getPlayer(), 10, date.format(Date) + "のログインボーナス");

			/*
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			Date Date = new Date();
			if(date.format(Date).equalsIgnoreCase("2017-01-01")){
				event.getPlayer().sendMessage("[MyMaid] " + ChatColor.GREEN + "サーバからのプレゼントです！あけましておめでとうございます！");
				Random rnd = new Random();
				int random = rnd.nextInt(20)*10;
				random += 300;

				Pointjao.addjao(event.getPlayer(), random);
			}else if(date.format(Date).equalsIgnoreCase("2017-01-02")){
				event.getPlayer().sendMessage("[MyMaid] " + ChatColor.GREEN + "サーバからのプレゼントです！年賀状は届きましたか？");
				Random rnd = new Random();
				int random = rnd.nextInt(20)*10;
				random += 300;

				Pointjao.addjao(event.getPlayer(), random);
			}else if(date.format(Date).equalsIgnoreCase("2017-01-03")){
				event.getPlayer().sendMessage("[MyMaid] " + ChatColor.GREEN + "サーバからのプレゼントです！新ワールド「ReJao_Afa」がオープンしています！今回のお年玉イベントのポイントはそこで…！");
				Random rnd = new Random();
				int random = rnd.nextInt(20)*10;
				random += 300;

				Pointjao.addjao(event.getPlayer(), random);
			}
			 */

		}
		Date Date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GOLD + "■" + ChatColor.WHITE + "jaotan: 現在『" + Bukkit.getServer().getOnlinePlayers().size() + "人』がログインしています。");
		if((MyMaid.maxplayer+1) == Bukkit.getServer().getOnlinePlayers().size()){
			Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GOLD + "■" + ChatColor.WHITE + "jaotan: 最高ログイン人数を突破しました！おめでとうございます！前回のログイン人数突破は「" + MyMaid.maxplayertime + "」でした！");
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				Pointjao.addjao(p, 30, timeFormat.format(Date) + "の最高ログイン人数「" + Bukkit.getServer().getOnlinePlayers().size() + "人」を突破したため");
			}
			SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			MyMaid.maxplayer = Bukkit.getServer().getOnlinePlayers().size();
			MyMaid.maxplayertime = date.format(Date);
			plugin.getConfig().set("maxplayer", MyMaid.maxplayer);
			plugin.getConfig().set("maxplayertime", MyMaid.maxplayertime);
			Method.url_jaoplugin("max", "c=" + MyMaid.maxplayer);
		}else if(MyMaid.maxplayer == Bukkit.getServer().getOnlinePlayers().size()){
			Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GOLD + "■" + ChatColor.WHITE + "jaotan: 最高ログイン人数突破まであと1人です。");
			Method.url_jaoplugin("max", "c=1&w");
		}else if(MyMaid.maxplayer == (Bukkit.getServer().getOnlinePlayers().size()+1)){
			Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GOLD + "■" + ChatColor.WHITE + "jaotan: 最高ログイン人数突破まであと2人です。");
			Method.url_jaoplugin("max", "c=2&w");
		}

		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			new TabListSKKReloader(plugin, p).runTaskLater(plugin, 20L);
		}
	}
	private class TabListSKKReloader extends BukkitRunnable{
		Player player;
		public TabListSKKReloader(JavaPlugin plugin, Player player) {
			this.player = player;
		}
		@Override
		public void run() {
			SKKColors.setPlayerSKKTabList(player);
		}
	}
}
