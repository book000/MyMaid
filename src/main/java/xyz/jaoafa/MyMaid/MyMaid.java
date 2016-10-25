package xyz.jaoafa.mymaid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.ucchyocean.lc.LunaChat;
import com.github.ucchyocean.lc.LunaChatAPI;

import xyz.jaoafa.mymaid.Command.AFK;
import xyz.jaoafa.mymaid.Command.AFK.afking;
import xyz.jaoafa.mymaid.Command.Access;
import xyz.jaoafa.mymaid.Command.ArrowShotter;
import xyz.jaoafa.mymaid.Command.Chat;
import xyz.jaoafa.mymaid.Command.Ck;
import xyz.jaoafa.mymaid.Command.Cmdb;
import xyz.jaoafa.mymaid.Command.Cmdsearch;
import xyz.jaoafa.mymaid.Command.Color;
import xyz.jaoafa.mymaid.Command.DOT;
import xyz.jaoafa.mymaid.Command.Data;
import xyz.jaoafa.mymaid.Command.Ded;
import xyz.jaoafa.mymaid.Command.DedMsg;
import xyz.jaoafa.mymaid.Command.Dynamic;
import xyz.jaoafa.mymaid.Command.Dynmap_Compass;
import xyz.jaoafa.mymaid.Command.Dynmap_Teleporter;
import xyz.jaoafa.mymaid.Command.E;
import xyz.jaoafa.mymaid.Command.Explode;
import xyz.jaoafa.mymaid.Command.Eye;
import xyz.jaoafa.mymaid.Command.Gamemode_Change;
import xyz.jaoafa.mymaid.Command.Gettissue;
import xyz.jaoafa.mymaid.Command.Guard;
import xyz.jaoafa.mymaid.Command.Head;
import xyz.jaoafa.mymaid.Command.Inv;
import xyz.jaoafa.mymaid.Command.InvEnder;
import xyz.jaoafa.mymaid.Command.InvLoad;
import xyz.jaoafa.mymaid.Command.InvSave;
import xyz.jaoafa.mymaid.Command.Ip_To_Host;
import xyz.jaoafa.mymaid.Command.Ja;
import xyz.jaoafa.mymaid.Command.Jao;
import xyz.jaoafa.mymaid.Command.JaoJao;
import xyz.jaoafa.mymaid.Command.Jf;
import xyz.jaoafa.mymaid.Command.Lag;
import xyz.jaoafa.mymaid.Command.MakeCmd;
import xyz.jaoafa.mymaid.Command.MyMaid_NetworkApi;
import xyz.jaoafa.mymaid.Command.Pexup;
import xyz.jaoafa.mymaid.Command.Pin;
import xyz.jaoafa.mymaid.Command.Prison;
import xyz.jaoafa.mymaid.Command.Quiz;
import xyz.jaoafa.mymaid.Command.Report;
import xyz.jaoafa.mymaid.Command.RuleLoad;
import xyz.jaoafa.mymaid.Command.SSK;
import xyz.jaoafa.mymaid.Command.SaveWorld;
import xyz.jaoafa.mymaid.Command.SignLock;
import xyz.jaoafa.mymaid.Command.Spawn;
import xyz.jaoafa.mymaid.Command.TNTReload;
import xyz.jaoafa.mymaid.Command.Unko;
import xyz.jaoafa.mymaid.Command.Var;
import xyz.jaoafa.mymaid.Command.VarCmd;
import xyz.jaoafa.mymaid.Command.Vote;
import xyz.jaoafa.mymaid.Command.Where;
import xyz.jaoafa.mymaid.EventHandler.DefaultCheck;
import xyz.jaoafa.mymaid.EventHandler.EyeMove;
import xyz.jaoafa.mymaid.EventHandler.MoveLocationName;
import xyz.jaoafa.mymaid.EventHandler.OnAsyncPlayerChatEvent;
import xyz.jaoafa.mymaid.EventHandler.OnAsyncPlayerPreLoginEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBannedEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBlockBreakEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBlockIgniteEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBlockPlaceEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBlockRedstoneEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBreak;
import xyz.jaoafa.mymaid.EventHandler.OnEEWReceiveEvent;
import xyz.jaoafa.mymaid.EventHandler.OnEntityChangeBlockEvent;
import xyz.jaoafa.mymaid.EventHandler.OnExplosion;
import xyz.jaoafa.mymaid.EventHandler.OnFrom;
import xyz.jaoafa.mymaid.EventHandler.OnHeadClick;
import xyz.jaoafa.mymaid.EventHandler.OnInventoryClickEvent;
import xyz.jaoafa.mymaid.EventHandler.OnJoin;
import xyz.jaoafa.mymaid.EventHandler.OnLunaChatChannelMemberChangedEvent;
import xyz.jaoafa.mymaid.EventHandler.OnMyMaidCommandblockLogs;
import xyz.jaoafa.mymaid.EventHandler.OnMyMaidJoinLeftChatCmdLogs;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerBedEnterEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerBedLeaveEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerBucketEmptyEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerCommand;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerCommandPreprocessEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerCommandSendAdmin;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerDeathEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerGameModeChangeEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerInteractEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerItemHeldEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerJoinEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerKickEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerMoveAFK;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerMoveEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerPickupItemEvent;
import xyz.jaoafa.mymaid.EventHandler.OnQuitGame;
import xyz.jaoafa.mymaid.EventHandler.OnServerCommandEvent;
import xyz.jaoafa.mymaid.EventHandler.OnSignClick;
import xyz.jaoafa.mymaid.EventHandler.OnVotifierEvent;

public class MyMaid extends JavaPlugin implements Listener {
	public static Boolean nextbakrender = false;

	public static Map<String,String> chatcolor = new HashMap<String,String>();
	public static TitleSender TitleSender;
	public static FileConfiguration conf;
	public static LunaChatAPI lunachatapi;
	public static LunaChat lunachat;
	public static int maxplayer;
	public static String maxplayertime;
	@Override
    public void onEnable() {
		getLogger().info("--------------------------------------------------");
    	getLogger().info("(c) jao Minecraft Server MyMaid Project.");
    	getLogger().info("Product by tomachi.");

    	Load_Plugin("EEWAlert");
    	Load_Plugin("PermissionsEx");
    	Load_Plugin("WorldEdit");
    	Load_Plugin("LunaChat");
    	Load_Plugin("CoreProtect");
    	Load_Plugin("Votifier");
    	Load_Plugin("MCBans");

		Import_Listener();
    	Import_Task();
    	Import_Command_Executor();
    	Load_Config();
    	getLogger().info("--------------------------------------------------");

		TitleSender = new TitleSender();

		lunachat = (LunaChat)getServer().getPluginManager().getPlugin("LunaChat");
	    lunachatapi = lunachat.getLunaChatAPI();


    }
    @Override
    public void onDisable() {
    	conf.set("prison",Prison.prison);
		conf.set("prison_block",Prison.prison_block);
		conf.set("prison_lasttext",Prison.jail_lasttext);
		conf.set("var",Var.var);
		conf.set("color",Color.color);
		conf.set("jao",Pointjao.jao);
		conf.set("maxplayer",maxplayer);
		conf.set("maxplayertime",maxplayertime);
    	saveConfig();
    }

    private void Load_Plugin(String PluginName){
    	if(getServer().getPluginManager().isPluginEnabled(PluginName)){
    		getLogger().info("MyMaid Success(LOADED: " + PluginName + ")");
    		getLogger().info("Using " + PluginName);
		}else{
			getLogger().warning("MyMaid ERR(NOTLOADED: " + PluginName + ")");
			getLogger().info("Disable MyMaid...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
    }
    private void Import_Command_Executor(){
    	//Command Executor
    	getCommand("access").setExecutor(new Access(this));
    	getCommand("afk").setExecutor(new AFK(this));
    	getCommand("as").setExecutor(new ArrowShotter(this));
    	getCommand("chat").setExecutor(new Chat(this));
    	getCommand("ck").setExecutor(new Ck(this));
    	getCommand("cmdb").setExecutor(new Cmdb(this));
    	getCommand("cmdsearch").setExecutor(new Cmdsearch(this));
    	getCommand("color").setExecutor(new Color(this));
    	getCommand("color").setTabCompleter(new Color(this));
    	getCommand("data").setExecutor(new Data(this));
    	getCommand("ded").setExecutor(new Ded(this));
    	getCommand("dedmsg").setExecutor(new DedMsg(this));
    	getCommand(".").setExecutor(new DOT(this));
    	getCommand("dynamic").setExecutor(new Dynamic(this));
    	getCommand("dc").setExecutor(new Dynmap_Compass(this));
    	getCommand("dc").setTabCompleter(new Dynmap_Compass(this));
    	getCommand("dt").setExecutor(new Dynmap_Teleporter(this));
    	getCommand("dt").setTabCompleter(new Dynmap_Teleporter(this));
    	getCommand("e").setExecutor(new E(this));
    	getCommand("explode").setExecutor(new Explode(this));
    	getCommand("eye").setExecutor(new Eye(this));
    	getCommand("g").setExecutor(new Gamemode_Change(this));
    	getCommand("gettissue").setExecutor(new Gettissue(this));
    	getCommand("guard").setExecutor(new Guard(this));
    	getCommand("head").setExecutor(new Head(this));
    	getCommand("inv").setExecutor(new Inv(this));
    	getCommand("invsave").setExecutor(new InvSave(this));
    	getCommand("invload").setExecutor(new InvLoad(this));
    	getCommand("invender").setExecutor(new InvEnder(this));
    	getCommand("iphost").setExecutor(new Ip_To_Host(this));
    	getCommand("ja").setExecutor(new Ja(this));
    	getCommand("jao").setExecutor(new Jao(this));
    	getCommand("j2").setExecutor(new JaoJao(this));
    	getCommand("jf").setExecutor(new Jf(this));
    	getCommand("lag").setExecutor(new Lag(this));
    	getCommand("makecmd").setExecutor(new MakeCmd(this));
    	getCommand("mymaid_networkapi").setExecutor(new MyMaid_NetworkApi(this));
    	getCommand("pexup").setExecutor(new Pexup(this));
    	getCommand("pin").setExecutor(new Pin(this));
    	getCommand("player").setExecutor(new xyz.jaoafa.mymaid.Command.Player(this));
    	getCommand("jail").setExecutor(new Prison(this));
    	getCommand("jail").setTabCompleter(new Prison(this));
    	getCommand("quiz").setExecutor(new Quiz(this));
    	getCommand("report").setExecutor(new Report(this));
    	getCommand("ruleload").setExecutor(new RuleLoad(this));
    	getCommand("save-world").setExecutor(new SaveWorld(this));
    	getCommand("sign").setExecutor(new xyz.jaoafa.mymaid.Command.Sign(this));
    	getCommand("signlock").setExecutor(new SignLock(this));
    	getCommand("spawn").setExecutor(new Spawn(this));
    	getCommand("skk").setExecutor(new SSK(this));
    	getCommand("tnt").setExecutor(new TNTReload(this));
    	getCommand("unko").setExecutor(new Unko(this));
    	getCommand("var").setExecutor(new Var(this));
    	getCommand("var").setTabCompleter(new Var(this));
    	getCommand("varcmd").setExecutor(new VarCmd(this));
    	getCommand("vote").setExecutor(new Vote(this));
    	getCommand("where").setExecutor(new Where(this));
    }
    private void Import_Task(){
    	//Task
    	new World_saver().runTaskTimer(this, 0L, 36000L);
    	new Dynmap_Update_Render().runTaskTimer(this, 0L, 36000L);
    	new Lag_Counter(this).runTaskTimer(this, 0L, 6000L);
    	new AFKChecker(this).runTaskTimer(this, 0L, 1200L);
    }
    private void Import_Listener(){
    	//Listener
    	getServer().getPluginManager().registerEvents(this, this);
    	getServer().getPluginManager().registerEvents(new DefaultCheck(this), this);
    	getServer().getPluginManager().registerEvents(new EyeMove(this), this);
    	getServer().getPluginManager().registerEvents(new MoveLocationName(this), this);
    	getServer().getPluginManager().registerEvents(new OnAsyncPlayerChatEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnAsyncPlayerPreLoginEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnBannedEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnBlockBreakEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnBlockIgniteEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnBlockPlaceEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnBlockRedstoneEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnBreak(this), this);
    	getServer().getPluginManager().registerEvents(new OnEEWReceiveEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnEntityChangeBlockEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnExplosion(this), this);
    	getServer().getPluginManager().registerEvents(new OnFrom(this), this);
    	getServer().getPluginManager().registerEvents(new OnHeadClick(this), this);
    	getServer().getPluginManager().registerEvents(new OnInventoryClickEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnJoin(this), this);
    	getServer().getPluginManager().registerEvents(new OnLunaChatChannelMemberChangedEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnMyMaidCommandblockLogs(this), this);
    	getServer().getPluginManager().registerEvents(new OnMyMaidJoinLeftChatCmdLogs(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerBedEnterEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerBedLeaveEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerBucketEmptyEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerCommand(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerCommandPreprocessEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerCommandSendAdmin(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerDeathEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerGameModeChangeEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerInteractEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerItemHeldEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerKickEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerMoveAFK(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerMoveEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnPlayerPickupItemEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnQuitGame(this), this);
    	getServer().getPluginManager().registerEvents(new OnServerCommandEvent(this), this);
    	getServer().getPluginManager().registerEvents(new OnSignClick(this), this);
    	getServer().getPluginManager().registerEvents(new OnVotifierEvent(this), this);
    }
    private void Load_Config(){
    	conf = getConfig();

		if(conf.contains("prison")){
			//Prison.prison = (Map<String,Boolean>) conf.getConfigurationSection("prison").getKeys(false);
			Map<String, Object> pl = conf.getConfigurationSection("prison").getValues(true);
			if(pl.size() != 0){
				for(Entry<String, Object> p: pl.entrySet()){
					Prison.prison.put(p.getKey(), (Boolean) p.getValue());
				}
			}
 		}else{
 			Prison.prison = new HashMap<String,Boolean>();
 			conf.set("prison",Prison.prison);
 		}
		if(conf.contains("prison_block")){
			//Prison.prison_block = (Map<String,Boolean>) conf.getConfigurationSection("prison_block").getKeys(false);
			Map<String, Object> pl = conf.getConfigurationSection("prison_block").getValues(true);
			if(pl.size() != 0){
				for(Entry<String, Object> p: pl.entrySet()){
					Prison.prison_block.put(p.getKey(), (Boolean) p.getValue());
				}
			}
 		}else{
 			Prison.prison_block = new HashMap<String,Boolean>();
 			conf.set("prison_block",Prison.prison_block);
 		}
		if(conf.contains("prison_lasttext")){
			Map<String, Object> pl = conf.getConfigurationSection("prison_lasttext").getValues(true);
			if(pl.size() != 0){
				for(Entry<String, Object> p: pl.entrySet()){
					Prison.jail_lasttext.put(p.getKey(), p.getValue().toString());
				}
			}
 		}else{
 			Prison.jail_lasttext = new HashMap<String,String>();
 			conf.set("prison_lasttext",Prison.jail_lasttext);
 		}
		if(conf.contains("var")){
			//Prison.prison_block = (Map<String,Boolean>) conf.getConfigurationSection("prison_block").getKeys(false);
			Map<String, Object> var = conf.getConfigurationSection("var").getValues(true);
			if(var.size() != 0){
				for(Entry<String, Object> p: var.entrySet()){
					Var.var.put(p.getKey(), (String) p.getValue());
				}
			}
 		}else{
 			Var.var = new HashMap<String, String>();
 			conf.set("var",Var.var);
 		}
		if(conf.contains("jao")){
			//Prison.prison_block = (Map<String,Boolean>) conf.getConfigurationSection("prison_block").getKeys(false);
			Map<String, Object> jao = conf.getConfigurationSection("jao").getValues(true);
			if(jao.size() != 0){
				for(Entry<String, Object> p: jao.entrySet()){
					Pointjao.jao.put(p.getKey(), (Integer) p.getValue());
				}
			}
 		}else{
 			Pointjao.jao = new HashMap<String, Integer>();
 			conf.set("jao",Pointjao.jao);
 		}
		if(conf.contains("color")){
			//Prison.prison_block = (Map<String,Boolean>) conf.getConfigurationSection("prison_block").getKeys(false);
			Map<String, Object> color = conf.getConfigurationSection("color").getValues(true);
			if(color.size() != 0){
				for(Entry<String, Object> p: color.entrySet()){
					String colorstring = (String) p.getValue();
					if(!colorstring.equalsIgnoreCase("null")){
						ChatColor chatcolor;
						if(colorstring.equalsIgnoreCase("AQUA")){
							chatcolor = ChatColor.AQUA;
						}else if(colorstring.equalsIgnoreCase("BLACK")){
							chatcolor = ChatColor.BLACK;
						}else if(colorstring.equalsIgnoreCase("BLUE")){
							chatcolor = ChatColor.BLUE;
						}else if(colorstring.equalsIgnoreCase("DARK_AQUA")){
							chatcolor = ChatColor.DARK_AQUA;
						}else if(colorstring.equalsIgnoreCase("DARK_BLUE")){
							chatcolor = ChatColor.DARK_BLUE;
						}else if(colorstring.equalsIgnoreCase("DARK_GRAY")){
							chatcolor = ChatColor.DARK_GRAY;
						}else if(colorstring.equalsIgnoreCase("DARK_GREEN")){
							chatcolor = ChatColor.DARK_GREEN;
						}else if(colorstring.equalsIgnoreCase("DARK_PURPLE")){
							chatcolor = ChatColor.DARK_PURPLE;
						}else if(colorstring.equalsIgnoreCase("DARK_RED")){
							chatcolor = ChatColor.DARK_RED;
						}else if(colorstring.equalsIgnoreCase("GOLD")){
							chatcolor = ChatColor.GOLD;
						}else if(colorstring.equalsIgnoreCase("GREEN")){
							chatcolor = ChatColor.GREEN;
						}else if(colorstring.equalsIgnoreCase("LIGHT_PURPLE")){
							chatcolor = ChatColor.LIGHT_PURPLE;
						}else if(colorstring.equalsIgnoreCase("RED")){
							chatcolor = ChatColor.RED;
						}else if(colorstring.equalsIgnoreCase("WHITE")){
							chatcolor = ChatColor.WHITE;
						}else if(colorstring.equalsIgnoreCase("YELLOW")){
							chatcolor = ChatColor.YELLOW;
						}else if(colorstring.equalsIgnoreCase("GRAY")){
							chatcolor = ChatColor.GRAY;
						}else{
							chatcolor = null;
						}
						Color.color.put(p.getKey(), chatcolor);
					}
				}
			}
 		}else{
 			Color.color = new HashMap<String, ChatColor>();
 			conf.set("color",Color.color);
 		}
		if(conf.contains("maxplayer")){
			maxplayer = conf.getInt("maxplayer");
 		}else{
 			maxplayer = 0;
 		}
		if(conf.contains("maxplayertime")){
			maxplayertime = conf.getString("maxplayertime");
 		}else{
 			maxplayertime = "無し";
 		}
    }

    private class World_saver extends BukkitRunnable{
		@Override
		public void run() {
			if(nextbakrender){
				List<World> worlds = Bukkit.getServer().getWorlds();
				for (World w : worlds)
				{
					w.save();
				}

				List<String> list = getConfig().getStringList("messages");
				Collections.shuffle(list);
				String message = list.get(0);
				String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
				for(Player play: Bukkit.getServer().getOnlinePlayers()) {
					String msg = message.replaceAll("%player%", play.getName());
					play.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "└( ・з・)┘" + ChatColor.WHITE +  ": " + msg);
				}
				//Bukkit.broadcastMessage("今流行りのピリオド連投対決はコマンド「/.」を使用することで楽しめます！詳しくは https://jaoafa.xyz/blog/tomachi/period_match で！");

			}
		}
	}
    private class Dynmap_Update_Render extends BukkitRunnable{
		@Override
		public void run() {
			if(nextbakrender){
				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dynmap updaterender Jao_Afa " + player.getLocation().getBlockX() + " " + player.getLocation().getBlockZ() );
				}
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
    public static double lag = new Double(0);
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

			lag = Double.parseDouble(interval);

			Method.url_jaoplugin("lag", "start=" + start_ymdhis + "&end=" + end_ymdhis + "&lag=" + String.format("%.5f", (Double.parseDouble(interval) - 10)));
		}
	}
    public static Map<String,Long> afktime = new HashMap<String,Long>();
    private class AFKChecker extends BukkitRunnable{
    	JavaPlugin plugin;
    	public AFKChecker(JavaPlugin plugin) {
    		this.plugin = plugin;
    	}
		@Override
		public void run() {
			if(nextbakrender){
				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					if(AFK.tnt.containsKey(player.getName())){
						continue;
					}
					if(!afktime.containsKey(player.getName())){
						continue;
					}
					if(player.getGameMode() == GameMode.SPECTATOR){
						continue;
					}
					long nowtime = System.currentTimeMillis();
					long lastmovetime = afktime.get(player.getName());
					long sa = nowtime - lastmovetime;
					if(sa >= 180000){
						ItemStack[] is = player.getInventory().getArmorContents();
						ItemStack[] after={
								new ItemStack(is[0]),
								new ItemStack(is[1]),
								new ItemStack(is[2]),
								new ItemStack(Material.ICE)};
						player.getInventory().setArmorContents(after);
						player.updateInventory();
						Bukkit.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " is afk!");
						MyMaid.TitleSender.setTime_tick(player, 0, 99999999, 0);
						MyMaid.TitleSender.sendTitle(player, ChatColor.RED + "AFK NOW!", ChatColor.BLUE + "" + ChatColor.BOLD + "When you are back, please enter the command '/afk'.");
						MyMaid.TitleSender.setTime_tick(player, 0, 99999999, 0);
						AFK.tnt.put(player.getName(), new afking(plugin, player).runTaskTimer(plugin, 0L, 5L));
					}
				}
			}
		}
	}
}