package xyz.jaoafa.mymaid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.jaoafa.mymaid.Command.AFK;
import xyz.jaoafa.mymaid.Command.Access;
import xyz.jaoafa.mymaid.Command.ArrowShotter;
import xyz.jaoafa.mymaid.Command.Chat;
import xyz.jaoafa.mymaid.Command.Cmdb;
import xyz.jaoafa.mymaid.Command.Cmdsearch;
import xyz.jaoafa.mymaid.Command.Data;
import xyz.jaoafa.mymaid.Command.Dynmap_Teleporter;
import xyz.jaoafa.mymaid.Command.E;
import xyz.jaoafa.mymaid.Command.Explode;
import xyz.jaoafa.mymaid.Command.Gamemode_Change;
import xyz.jaoafa.mymaid.Command.Head;
import xyz.jaoafa.mymaid.Command.Ip_To_Host;
import xyz.jaoafa.mymaid.Command.Ja;
import xyz.jaoafa.mymaid.Command.JaoJao;
import xyz.jaoafa.mymaid.Command.Jf;
import xyz.jaoafa.mymaid.Command.Lag;
import xyz.jaoafa.mymaid.Command.MakeCmd;
import xyz.jaoafa.mymaid.Command.MyMaid_NetworkApi;
import xyz.jaoafa.mymaid.Command.Prison;
import xyz.jaoafa.mymaid.Command.Report;
import xyz.jaoafa.mymaid.Command.RuleLoad;
import xyz.jaoafa.mymaid.Command.SSK;
import xyz.jaoafa.mymaid.Command.SaveWorld;
import xyz.jaoafa.mymaid.Command.SignLock;
import xyz.jaoafa.mymaid.Command.TNTReload;
import xyz.jaoafa.mymaid.Command.Vote;
import xyz.jaoafa.mymaid.EventHandler.OnAsyncPlayerChatEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBlockBreakEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBlockIgniteEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBlockPlaceEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBlockRedstoneEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBreak;
import xyz.jaoafa.mymaid.EventHandler.OnEEWReceiveEvent;
import xyz.jaoafa.mymaid.EventHandler.OnExplosion;
import xyz.jaoafa.mymaid.EventHandler.OnFrom;
import xyz.jaoafa.mymaid.EventHandler.OnHeadClick;
import xyz.jaoafa.mymaid.EventHandler.OnJoin;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerBucketEmptyEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerCommand;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerCommandPreprocessEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerInteractEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerItemHeldEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerJoinEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerMoveEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerPickupItemEvent;
import xyz.jaoafa.mymaid.EventHandler.OnQuitGame;
import xyz.jaoafa.mymaid.EventHandler.OnServerCommandEvent;
import xyz.jaoafa.mymaid.EventHandler.OnSignClick;

public class MyMaid extends JavaPlugin implements Listener {
	public static Boolean nextbakrender = false;

	public static Map<String,String> chatcolor = new HashMap<String,String>();
	public static TitleSender TitleSender;
	FileConfiguration conf;
	@Override
    public void onEnable() {
    	getLogger().info("(c) jao Minecraft Server MyMaid Project.");
    	getLogger().info("Product by tomachi.");

    	//Listener
    	getServer().getPluginManager().registerEvents(this, this);
    	getServer().getPluginManager().registerEvents(new OnAsyncPlayerChatEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnBlockBreakEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnBlockIgniteEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnBlockPlaceEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnBlockRedstoneEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnBreak(this), this);
    	getServer().getPluginManager().registerEvents(new OnEEWReceiveEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnExplosion(this), this);
    	getServer().getPluginManager().registerEvents(new OnFrom(this), this);
    	getServer().getPluginManager().registerEvents(new OnHeadClick(this), this);
    	getServer().getPluginManager().registerEvents(new OnJoin(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerBucketEmptyEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerCommand(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerCommandPreprocessEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerInteractEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerMoveEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerPickupItemEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnQuitGame(this), this);
    	getServer().getPluginManager().registerEvents(new OnServerCommandEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnSignClick(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerItemHeldEvent(this), this);

    	//Task
    	new World_saver().runTaskTimer(this, 0L, 36000L);
    	new Dynmap_Update_Render().runTaskTimer(this, 0L, 36000L);
    	new Lag_Counter(this).runTaskTimer(this, 0L, 6000L);

    	//Command Executor
		getCommand("chat").setExecutor(new Chat(this));
		getCommand("jf").setExecutor(new Jf(this));
		getCommand("dt").setExecutor(new Dynmap_Teleporter(this));
		getCommand("dt").setTabCompleter(new Dynmap_Teleporter(this));
		getCommand("explode").setExecutor(new Explode(this));
		getCommand("g").setExecutor(new Gamemode_Change(this));
		getCommand("e").setExecutor(new E(this));
		getCommand("iphost").setExecutor(new Ip_To_Host(this));
		getCommand("data").setExecutor(new Data(this));
		getCommand("tnt").setExecutor(new TNTReload(this));
		getCommand("afk").setExecutor(new AFK(this));
		getCommand("j2").setExecutor(new JaoJao(this));
		getCommand("vote").setExecutor(new Vote(this));
		getCommand("sign").setExecutor(new xyz.jaoafa.mymaid.Command.Sign(this));
		getCommand("signlock").setExecutor(new SignLock(this));
		getCommand("save-world").setExecutor(new SaveWorld(this));
		getCommand("head").setExecutor(new Head(this));
		getCommand("cmdb").setExecutor(new Cmdb(this));
		getCommand("jail").setExecutor(new Prison(this));
		getCommand("jail").setTabCompleter(new Prison(this));
		getCommand("report").setExecutor(new Report(this));
		getCommand("ruleload").setExecutor(new RuleLoad(this));
		getCommand("access").setExecutor(new Access(this));
		getCommand("ja").setExecutor(new Ja(this));
		getCommand("cmdsearch").setExecutor(new Cmdsearch(this));
		getCommand("skk").setExecutor(new SSK(this));
		getCommand("makecmd").setExecutor(new MakeCmd(this));
		getCommand("lag").setExecutor(new Lag(this));
		getCommand("mymaid_networkapi").setExecutor(new MyMaid_NetworkApi(this));
		getCommand("as").setExecutor(new ArrowShotter(this));

		conf = getConfig();

		if(conf.contains("prison")){
			//Prison.prison = (Map<String,Boolean>) conf.getConfigurationSection("prison").getKeys(false);
			Map<String, Object> pl = conf.getConfigurationSection("prison").getValues(true);
			for(Entry<String, Object> p: pl.entrySet()){
				Prison.prison.put(p.getKey(), (Boolean) p.getValue());
			}
 		}else{
 			Prison.prison = new HashMap<String,Boolean>();
 			conf.set("prison",Prison.prison);
 		}
		if(conf.contains("prison_block")){
			//Prison.prison_block = (Map<String,Boolean>) conf.getConfigurationSection("prison_block").getKeys(false);
			Map<String, Object> pl = conf.getConfigurationSection("prison_block").getValues(true);
			for(Entry<String, Object> p: pl.entrySet()){
				Prison.prison_block.put(p.getKey(), (Boolean) p.getValue());
			}
 		}else{
 			Prison.prison_block = new HashMap<String,Boolean>();
 			conf.set("prison_block",Prison.prison_block);
 		}
		if(conf.contains("prison_lasttext")){
			Map<String, Object> pl = conf.getConfigurationSection("prison_lasttext").getValues(true);
			for(Entry<String, Object> p: pl.entrySet()){
				Prison.prison_lasttext.put(p.getKey(), p.getValue().toString());
			}
			Prison.prison_lasttext = null;
 		}else{
 			Prison.prison_lasttext = new HashMap<String,String>();
 			conf.set("prison_lasttext",Prison.prison_lasttext);
 		}

		TitleSender = new TitleSender();
    }

    @Override
    public void onDisable() {
    	conf.set("prison",Prison.prison);
		conf.set("prison_block",Prison.prison_block);
		conf.set("prison_lasttext",Prison.prison_lasttext);
    	saveConfig();
    }


    private class World_saver extends BukkitRunnable{
		@Override
		public void run() {
			if(nextbakrender){
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
				Date Date = new Date();
				SimpleDateFormat H = new SimpleDateFormat("H");
				SimpleDateFormat m = new SimpleDateFormat("m");
				SimpleDateFormat s = new SimpleDateFormat("s");
				String Hs = H.format(Date);
				String ms = m.format(Date);
				String ss = s.format(Date);
				String date = String.format("%02d", Integer.parseInt(Hs)) + ":" + String.format("%02d", Integer.parseInt(ms)) + ":" + String.format("%02d", Integer.parseInt(ss));
				for(Player play: Bukkit.getServer().getOnlinePlayers()) {
					play.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "└( ・з・)┘" + ChatColor.WHITE +  ": " + "あなたのユーザーページはこちらです。https://jaoafa.xyz/user/"+play.getName()+"");
				}
			}
		}
	}
    private class Dynmap_Update_Render extends BukkitRunnable{
		@Override
		public void run() {
			if(nextbakrender){
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dynmap updaterender Jao_Afa 0 0");
			}
		}
	}
    private class Lag_Counter extends BukkitRunnable{
    	JavaPlugin plugin;
    	public Lag_Counter(JavaPlugin plugin) {
    		this.plugin = plugin;
    	}
		@Override
		public void run() {
			if(nextbakrender){
				long start = System.currentTimeMillis();
				new Lag_Counter_End(plugin, start).runTaskLater(plugin, 200L);
			}
		}
	}
    private class Lag_Counter_End extends BukkitRunnable{
    	long start;
    	public Lag_Counter_End(JavaPlugin plugin, long start) {
    		this.start = start;
    	}
		@Override
		public void run() {
			long end = System.currentTimeMillis();
			String interval = Method.format(start, end);

			Calendar start_calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
			Calendar end_calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
			start_calendar.setTimeInMillis(start);
			end_calendar.setTimeInMillis(end);
			start_calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
			end_calendar.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd_HH:mm:ss.SSSSS");
			String start_ymdhis = sdf.format(start);
			String end_ymdhis = sdf.format(end);
			Method.url_jaoplugin("lag", "start=" + start_ymdhis + "&end=" + end_ymdhis + "&lag=" + String.format("%.5f", (Double.parseDouble(interval) - 10)));
		}
	}
}