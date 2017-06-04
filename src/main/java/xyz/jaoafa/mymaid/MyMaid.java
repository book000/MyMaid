package xyz.jaoafa.mymaid;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.github.ucchyocean.lc.LunaChat;
import com.github.ucchyocean.lc.LunaChatAPI;
import com.github.ucchyocean.lc.bridge.DynmapBridge;
import com.github.ucchyocean.lc.channel.ChannelPlayer;
import com.ittekikun.plugin.eewalert.EEWAlert;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Command.AFK;
import xyz.jaoafa.mymaid.Command.Access;
import xyz.jaoafa.mymaid.Command.ArrowShotter;
import xyz.jaoafa.mymaid.Command.AutoHeal;
import xyz.jaoafa.mymaid.Command.BON;
import xyz.jaoafa.mymaid.Command.Card;
import xyz.jaoafa.mymaid.Command.Chat;
import xyz.jaoafa.mymaid.Command.Ck;
import xyz.jaoafa.mymaid.Command.Cmb;
import xyz.jaoafa.mymaid.Command.Cmd_Account;
import xyz.jaoafa.mymaid.Command.Cmd_Messenger;
import xyz.jaoafa.mymaid.Command.Cmdb;
import xyz.jaoafa.mymaid.Command.Cmdmymaid;
import xyz.jaoafa.mymaid.Command.Cmdsearch;
import xyz.jaoafa.mymaid.Command.Color;
import xyz.jaoafa.mymaid.Command.DOT;
import xyz.jaoafa.mymaid.Command.Data;
import xyz.jaoafa.mymaid.Command.Ded;
import xyz.jaoafa.mymaid.Command.DedMsg;
import xyz.jaoafa.mymaid.Command.DedRain;
import xyz.jaoafa.mymaid.Command.DiscordLink;
import xyz.jaoafa.mymaid.Command.Discordsend;
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
import xyz.jaoafa.mymaid.Command.Home;
import xyz.jaoafa.mymaid.Command.Inv;
import xyz.jaoafa.mymaid.Command.InvEdit;
import xyz.jaoafa.mymaid.Command.InvEnder;
import xyz.jaoafa.mymaid.Command.InvLoad;
import xyz.jaoafa.mymaid.Command.InvSave;
import xyz.jaoafa.mymaid.Command.Ip_To_Host;
import xyz.jaoafa.mymaid.Command.ItemEdit;
import xyz.jaoafa.mymaid.Command.Ja;
import xyz.jaoafa.mymaid.Command.Jao;
import xyz.jaoafa.mymaid.Command.JaoJao;
import xyz.jaoafa.mymaid.Command.Jf;
import xyz.jaoafa.mymaid.Command.Lag;
import xyz.jaoafa.mymaid.Command.Land;
import xyz.jaoafa.mymaid.Command.MakeCmd;
import xyz.jaoafa.mymaid.Command.MyBlock;
import xyz.jaoafa.mymaid.Command.MyMaid_NetworkApi;
import xyz.jaoafa.mymaid.Command.MyMob;
import xyz.jaoafa.mymaid.Command.Nuke;
import xyz.jaoafa.mymaid.Command.Pexup;
import xyz.jaoafa.mymaid.Command.Pin;
import xyz.jaoafa.mymaid.Command.Prison;
import xyz.jaoafa.mymaid.Command.Quiz;
import xyz.jaoafa.mymaid.Command.Report;
import xyz.jaoafa.mymaid.Command.RuleLoad;
import xyz.jaoafa.mymaid.Command.SSK;
import xyz.jaoafa.mymaid.Command.SaveWorld;
import xyz.jaoafa.mymaid.Command.SelectorChecker;
import xyz.jaoafa.mymaid.Command.SetHome;
import xyz.jaoafa.mymaid.Command.SignLock;
import xyz.jaoafa.mymaid.Command.Spawn;
import xyz.jaoafa.mymaid.Command.TNTReload;
import xyz.jaoafa.mymaid.Command.TS;
import xyz.jaoafa.mymaid.Command.Testment;
import xyz.jaoafa.mymaid.Command.Unko;
import xyz.jaoafa.mymaid.Command.UpGallery;
import xyz.jaoafa.mymaid.Command.Var;
import xyz.jaoafa.mymaid.Command.VarCmd;
import xyz.jaoafa.mymaid.Command.Vote;
import xyz.jaoafa.mymaid.Command.Vote_Add;
import xyz.jaoafa.mymaid.Command.WO;
import xyz.jaoafa.mymaid.Command.WTP;
import xyz.jaoafa.mymaid.Command.Where;
import xyz.jaoafa.mymaid.Command.WorldTeleport;
import xyz.jaoafa.mymaid.Discord.Discord;
import xyz.jaoafa.mymaid.EventHandler.AntiJaoium;
import xyz.jaoafa.mymaid.EventHandler.DefaultCheck;
import xyz.jaoafa.mymaid.EventHandler.EyeMove;
import xyz.jaoafa.mymaid.EventHandler.Menu;
import xyz.jaoafa.mymaid.EventHandler.MoveLocationName;
import xyz.jaoafa.mymaid.EventHandler.NoWither;
import xyz.jaoafa.mymaid.EventHandler.OnAsyncPlayerChatEvent;
import xyz.jaoafa.mymaid.EventHandler.OnAsyncPlayerPreLoginEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBannedEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBlockRedstoneEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBowClickEvent;
import xyz.jaoafa.mymaid.EventHandler.OnBreak;
import xyz.jaoafa.mymaid.EventHandler.OnEEWReceiveEvent;
import xyz.jaoafa.mymaid.EventHandler.OnEntityChangeBlockEvent;
import xyz.jaoafa.mymaid.EventHandler.OnExplosion;
import xyz.jaoafa.mymaid.EventHandler.OnFrom;
import xyz.jaoafa.mymaid.EventHandler.OnHeadClick;
import xyz.jaoafa.mymaid.EventHandler.OnJoin;
import xyz.jaoafa.mymaid.EventHandler.OnLunaChatChannelMemberChangedEvent;
import xyz.jaoafa.mymaid.EventHandler.OnMyMaidCommandblockLogs;
import xyz.jaoafa.mymaid.EventHandler.OnMyMaidJoinLeftChatCmdLogs;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerCommand;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerCommandSendAdmin;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerDeathEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerGameModeChangeEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerInteractEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerItemConsumeEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerJoinEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerKickEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerMoveAFK;
import xyz.jaoafa.mymaid.EventHandler.OnPlayerTeleportEvent;
import xyz.jaoafa.mymaid.EventHandler.OnPotionSplashEvent;
import xyz.jaoafa.mymaid.EventHandler.OnProjectileLaunchEvent;
import xyz.jaoafa.mymaid.EventHandler.OnQuitGame;
import xyz.jaoafa.mymaid.EventHandler.OnServerCommandEvent;
import xyz.jaoafa.mymaid.EventHandler.OnSignClick;
import xyz.jaoafa.mymaid.EventHandler.OnVehicleCreateEvent;
import xyz.jaoafa.mymaid.EventHandler.OnVotifierEvent;
import xyz.jaoafa.mymaid.EventHandler.OnWeatherChangeEvent;
import xyz.jaoafa.mymaid.EventHandler.SpawnEggRegulation;
import xyz.jaoafa.mymaid.Jail.Event;
import xyz.jaoafa.mymaid.Jail.Jail;
import xyz.jaoafa.mymaid.SKKColors.SKKColors;

public class MyMaid extends JavaPlugin implements Listener {
	public static Boolean nextbakrender = false;


	public static TitleSender TitleSender;
	public static FileConfiguration conf;
	public static LunaChatAPI lunachatapi;
	public static LunaChat lunachat;
	public static int maxplayer;
	public static String maxplayertime;
	public static Connection c = null;
	public static String sqluser;
	public static String sqlpassword;
	private static JavaPlugin instance;
	private static MyMaid mymaid;
	public static Discord discord = null;
	public static DynmapBridge dynmapbridge;
	/**
	 * プラグインが起動したときに呼び出し
	 * @author mine_book000
	 * @since 2016/04/04
	 */
	@Override
	public void onEnable() {
		getLogger().info("--------------------------------------------------");
		// クレジット
		getLogger().info("(c) jao Minecraft Server MyMaid Project.");
		getLogger().info("Product by tomachi.");

		//連携プラグインの確認
		Load_Plugin("EEWAlert");
		Load_Plugin("PermissionsEx");
		Load_Plugin("WorldEdit");
		Load_Plugin("dynmap");
		Load_Plugin("LunaChat");
		Load_Plugin("CoreProtect");
		Load_Plugin("Votifier");
		Load_Plugin("MCBans");

		//EEWAlertの設定
		EEWAlert eew = (EEWAlert)getServer().getPluginManager().getPlugin("EEWAlert");
		String eewnoticeold;
		if(eew.eewAlertConfig.notificationMode){
			eewnoticeold = "true";
		}else{
			eewnoticeold = "false";
		}
		eew.eewAlertConfig.notificationMode = false;
		getLogger().info("EEWAlertの通知動作設定を" + eewnoticeold + "からfalseに変更しました。");

		//初期設定(TitleSender, Lunachat設定)
		FirstSetting();
		//リスナーを設定
		Import_Listener();
		//スケジュールタスクをスケジュ―リング
		Import_Task();
		//コマンドを設定
		Import_Command_Executor();
		//jaopoint読み込み
		Load_JaoPoint();
		//Jail情報読み込み
		Load_JailData();
		//コンフィグ読み込み
		Load_Config();
		//レシピ(クラフト)追加
		Add_Recipe();

		//Compromised Accountのキャッシュ処理
		Compromised_Account_Cacher();
		//BukkitRunnableの動作確認
		BukkitRunnableTester();

		getLogger().info("--------------------------------------------------");

		Discord.send("**Server Started.**");
	}
	/**
	 * 初期設定
	 * @author mine_book000
	 */
	private void FirstSetting(){
		TitleSender = new TitleSender();

		lunachat = (LunaChat)getServer().getPluginManager().getPlugin("LunaChat");
		lunachatapi = lunachat.getLunaChatAPI();
		Method.OnEnable_TPSSetting();

		instance = this;
		mymaid = this;

		BugReport.start();
		SKKColors.first(this);
		dynmapbridge = DynmapBridge.load(getJavaPlugin()); // nullが帰ってくるかも?
	}
	/**
	 * 連携プラグイン確認
	 * @author mine_book000
	 */
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
	/**
	 * コマンドの設定
	 * @author mine_book000
	 */
	private void Import_Command_Executor(){
		//Command Executor
		getCommand("access").setExecutor(new Access(this));
		getCommand("afk").setExecutor(new AFK(this));
		getCommand("as").setExecutor(new ArrowShotter(this));
		getCommand("autoheal").setExecutor(new AutoHeal(this));
		getCommand("bon").setExecutor(new BON(this));
		getCommand("card").setExecutor(new Card(this));
		getCommand("chat").setExecutor(new Chat(this));
		getCommand("ck").setExecutor(new Ck(this));
		getCommand("cmb").setExecutor(new Cmb(this));
		getCommand("account").setExecutor(new Cmd_Account(this));
		getCommand("messenger").setExecutor(new Cmd_Messenger(this));
		getCommand("cmdb").setExecutor(new Cmdb(this));
		getCommand("mymaid").setExecutor(new Cmdmymaid(this));
		getCommand("cmdsearch").setExecutor(new Cmdsearch(this));
		getCommand("color").setExecutor(new Color(this));
		getCommand("color").setTabCompleter(new Color(this));
		getCommand("data").setExecutor(new Data(this));
		getCommand("ded").setExecutor(new Ded(this));
		getCommand("dedmsg").setExecutor(new DedMsg(this));
		getCommand("dedrain").setExecutor(new DedRain(this));
		getCommand("discordlink").setExecutor(new DiscordLink(this));
		getCommand("discordsend").setExecutor(new Discordsend(this));
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
		getCommand("home").setExecutor(new Home(this));
		getCommand("home").setTabCompleter(new Home(this));
		getCommand("inv").setExecutor(new Inv(this));
		getCommand("invedit").setExecutor(new InvEdit(this));
		getCommand("invsave").setExecutor(new InvSave(this));
		getCommand("invload").setExecutor(new InvLoad(this));
		getCommand("invender").setExecutor(new InvEnder(this));
		getCommand("iphost").setExecutor(new Ip_To_Host(this));
		getCommand("itemedit").setExecutor(new ItemEdit(this));
		getCommand("ja").setExecutor(new Ja(this));
		getCommand("jao").setExecutor(new Jao(this));
		getCommand("j2").setExecutor(new JaoJao(this));
		getCommand("jf").setExecutor(new Jf(this));
		getCommand("lag").setExecutor(new Lag(this));
		getCommand("land").setExecutor(new Land(this));
		getCommand("makecmd").setExecutor(new MakeCmd(this));
		getCommand("myblock").setExecutor(new MyBlock(this));
		getCommand("mymaid_networkapi").setExecutor(new MyMaid_NetworkApi(this));
		getCommand("mymob").setExecutor(new MyMob(this));
		getCommand("nuke").setExecutor(new Nuke(this));
		getCommand("pexup").setExecutor(new Pexup(this));
		getCommand("pin").setExecutor(new Pin(this));
		getCommand("player").setExecutor(new xyz.jaoafa.mymaid.Command.Player(this));
		getCommand("jail").setExecutor(new Prison(this));
		getCommand("jail").setTabCompleter(new Prison(this));
		getCommand("quiz").setExecutor(new Quiz(this));
		getCommand("report").setExecutor(new Report(this));
		getCommand("ruleload").setExecutor(new RuleLoad(this));
		getCommand("save-world").setExecutor(new SaveWorld(this));
		getCommand("selector").setExecutor(new SelectorChecker(this));
		getCommand("sethome").setExecutor(new SetHome(this));
		getCommand("sign").setExecutor(new xyz.jaoafa.mymaid.Command.Sign(this));
		getCommand("signlock").setExecutor(new SignLock(this));
		getCommand("spawn").setExecutor(new Spawn(this));
		getCommand("skk").setExecutor(new SSK(this));
		getCommand("testment").setExecutor(new Testment(this));
		getCommand("tnt").setExecutor(new TNTReload(this));
		getCommand("ts").setExecutor(new TS(this));
		getCommand("unko").setExecutor(new Unko(this));
		getCommand("upgallery").setExecutor(new UpGallery(this));
		getCommand("var").setExecutor(new Var(this));
		getCommand("var").setTabCompleter(new Var(this));
		getCommand("varcmd").setExecutor(new VarCmd(this));
		getCommand("vote").setExecutor(new Vote(this));
		getCommand("voteadd").setExecutor(new Vote_Add(this));
		getCommand("where").setExecutor(new Where(this));
		getCommand("wo").setExecutor(new WO(this));
		getCommand("wt").setExecutor(new WorldTeleport(this));
		getCommand("wtp").setExecutor(new WTP(this));
	}
	/**
	 * スケジューリング
	 * @author mine_book000
	 */
	private void Import_Task(){
		new World_saver().runTaskTimer(this, 0L, 36000L);
		new Dynmap_Update_Render().runTaskTimer(this, 0L, 36000L);
		new AFKChecker(this).runTaskTimer(this, 0L, 1200L);
		new AutoMessage().runTaskTimer(this, 0L, 12000L);
		new TPSChange().runTaskTimer(this, 0L, 1200L);
	}
	/**
	 * リスナー設定
	 * @author mine_book000
	 */
	private void Import_Listener(){
		//Listener
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new AntiJaoium(this), this);
		getServer().getPluginManager().registerEvents(new DefaultCheck(this), this);
		getServer().getPluginManager().registerEvents(new EyeMove(this), this);
		getServer().getPluginManager().registerEvents(new Menu(this), this);
		getServer().getPluginManager().registerEvents(new MoveLocationName(this), this);
		getServer().getPluginManager().registerEvents(new NoWither(this), this);
		getServer().getPluginManager().registerEvents(new OnAsyncPlayerChatEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnAsyncPlayerPreLoginEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnBannedEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnBlockRedstoneEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnBowClickEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnBreak(this), this);
		getServer().getPluginManager().registerEvents(new OnEEWReceiveEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnEntityChangeBlockEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnExplosion(this), this);
		getServer().getPluginManager().registerEvents(new OnFrom(this), this);
		getServer().getPluginManager().registerEvents(new OnHeadClick(this), this);
		getServer().getPluginManager().registerEvents(new OnJoin(this), this);
		getServer().getPluginManager().registerEvents(new OnLunaChatChannelMemberChangedEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnMyMaidCommandblockLogs(this), this);
		getServer().getPluginManager().registerEvents(new OnMyMaidJoinLeftChatCmdLogs(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerCommand(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerCommandSendAdmin(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerDeathEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerGameModeChangeEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerInteractEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerItemConsumeEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerKickEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerMoveAFK(this), this);
		getServer().getPluginManager().registerEvents(new OnPlayerTeleportEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnPotionSplashEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnProjectileLaunchEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnQuitGame(this), this);
		getServer().getPluginManager().registerEvents(new OnServerCommandEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnSignClick(this), this);
		getServer().getPluginManager().registerEvents(new OnVehicleCreateEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnVotifierEvent(this), this);
		getServer().getPluginManager().registerEvents(new OnWeatherChangeEvent(this), this);
		getServer().getPluginManager().registerEvents(new Land(this), this);
		getServer().getPluginManager().registerEvents(new Cmb(this), this);
		getServer().getPluginManager().registerEvents(new SpawnEggRegulation(this), this);

		getServer().getPluginManager().registerEvents(new Event(this), this);
	}
	/**
	 * コンフィグ読み込み
	 * @author mine_book000
	 */
	private void Load_Config(){
		ConfigurationSerialization.registerClass(SerializableLocation.class);
		conf = getConfig();

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
		if(conf.contains("sqluser") && conf.contains("sqlpassword")){
			MyMaid.sqluser = conf.getString("sqluser");
			MyMaid.sqlpassword = conf.getString("sqlpassword");
			MySQL_Enable(conf.getString("sqluser"), conf.getString("sqlpassword"));
		}else{
			getLogger().info("MySQL Connect err. [conf NotFound]");
			getLogger().info("Disable MyMaid...");
			getServer().getPluginManager().disablePlugin(this);
		}
		if(conf.contains("dedrain")){
			DedRain.flag = conf.getBoolean("dedrain");
		}else{
			DedRain.flag = false;
		}
		if(conf.contains("discordtoken")){
			discord = new Discord(this, conf.getString("discordtoken"));
			discord.start();
		}else{
			getLogger().info("Discordへの接続に失敗しました。 [conf NotFound]");
			getLogger().info("Disable MyMaid...");
			getServer().getPluginManager().disablePlugin(this);
		}
	}
	/**
	 * jaoPoint読み込み
	 * @author mine_book000
	 */
	private void Load_JaoPoint(){
		//jaoポイントをロード
		try {
			Pointjao.Loadjao();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			getLogger().info("jaoPointのロードに失敗しました。");
		}
	}
	/**
	 * Jail情報読み込み
	 * @author mine_book000
	 */
	private void Load_JailData(){
		//jaoポイントをロード
		try {
			Jail.LoadJailData();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			getLogger().info("jail情報のロードに失敗しました。");
		}
	}
	/**
	 * MySQLの初期設定
	 * @author mine_book000
	 */
	private void MySQL_Enable(String user, String password){
		MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", user, password);

		try {
			c = MySQL.openConnection();
		} catch (ClassNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			getLogger().info("MySQL Connect err. [ClassNotFoundException]");
			getLogger().info("Disable MyMaid...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			getLogger().info("MySQL Connect err. [SQLException: " + e.getSQLState() + "]");
			getLogger().info("Disable MyMaid...");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		getLogger().info("MySQL Connect successful.");
	}
	/**
	 * レシピ追加設定
	 * @author mine_book000
	 */
	private void Add_Recipe(){
		/* Ekusas83を以下の工程で作るの図
		 *
		 * [腐った肉] [ソウルサンド] [腐った肉]
		 * [ガストの涙] [エンドポータル] [ガストの涙]
		 * [腐った肉] [ソウルサンド] [腐った肉]
		 */
		ItemStack Ekusas83 = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta_Ekusas83 = (SkullMeta) Ekusas83.getItemMeta();
		Ekusas83.setDurability((short) 3);
		skullMeta_Ekusas83.setOwner("Ekusas83");
		Ekusas83.setItemMeta(skullMeta_Ekusas83);
		ShapedRecipe Ekusas83_sr = new ShapedRecipe(Ekusas83);

		Ekusas83_sr.shape("*+*","$%$","*+*");

		Ekusas83_sr.setIngredient('*', Material.ROTTEN_FLESH);
		Ekusas83_sr.setIngredient('+', Material.SOUL_SAND);
		Ekusas83_sr.setIngredient('$', Material.GHAST_TEAR);
		Ekusas83_sr.setIngredient('%', Material.ENDER_PORTAL_FRAME);
		Ekusas83_sr.setIngredient('%', Material.ENDER_PORTAL_FRAME);

		getServer().addRecipe(Ekusas83_sr);

		// -------------------------------------------- //
		/* X4Zを以下の工程で作るの図
		 *
		 * [Ekusas83] [砂利] [Ekusas83]
		 * [砂利] [レッドストーンブロック] [砂利]
		 * [Ekusas83] [砂利] [Ekusas83]
		 */
		ItemStack X4Z = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta_X4Z = (SkullMeta) X4Z.getItemMeta();
		X4Z.setDurability((short) 3);
		skullMeta_X4Z.setOwner("X4Z");
		X4Z.setItemMeta(skullMeta_X4Z);
		ShapedRecipe X4Z_sr = new ShapedRecipe(X4Z);

		X4Z_sr.shape("*+*","+$+","*+*");

		X4Z_sr.setIngredient('*', Ekusas83.getData());
		X4Z_sr.setIngredient('+', Material.GRAVEL);
		X4Z_sr.setIngredient('$', Material.REDSTONE_BLOCK);

		getServer().addRecipe(X4Z_sr);
		// -------------------------------------------- //
		/* X5Zを以下の工程で作るの図
		 *
		 * [水バケツ] [X4Z] [火打ち石]
		 * [火薬] [ラピスラズリブロック] [火薬]
		 * [TNT] [Ekusas83] [溶岩バケツ]
		 */
		ItemStack X5Z = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta_X5Z = (SkullMeta) X5Z.getItemMeta();
		X5Z.setDurability((short) 3);
		skullMeta_X5Z.setOwner("X5Z");
		X5Z.setItemMeta(skullMeta_X5Z);
		ShapedRecipe X5Z_sr = new ShapedRecipe(X5Z);

		X5Z_sr.shape("WZF","SRS","TEL");

		X5Z_sr.setIngredient('W', Material.WATER_BUCKET);
		X5Z_sr.setIngredient('Z', X4Z.getData());
		X5Z_sr.setIngredient('F', Material.FLINT_AND_STEEL);
		X5Z_sr.setIngredient('S', Material.SULPHUR);
		X5Z_sr.setIngredient('R', Material.LAPIS_BLOCK);
		X5Z_sr.setIngredient('F', Material.FLINT_AND_STEEL);
		X5Z_sr.setIngredient('T', Material.TNT);
		X5Z_sr.setIngredient('E', Ekusas83.getData());
		X5Z_sr.setIngredient('L', Material.LAVA_BUCKET);

		getServer().addRecipe(X5Z_sr);
		// -------------------------------------------- //
		/* X6Zを以下の工程で作るの図
		 *
		 * [AIR] [AIR] [AIR]
		 * [X5Z] [スライムボール] [Ekusas83]
		 * [AIR] [AIR] [AIR]
		 */
		ItemStack X6Z = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta_X6Z = (SkullMeta) X6Z.getItemMeta();
		X6Z.setDurability((short) 3);
		skullMeta_X6Z.setOwner("X6Z");
		X6Z.setItemMeta(skullMeta_X6Z);
		ShapedRecipe X6Z_sr = new ShapedRecipe(X6Z);

		X6Z_sr.shape("+*$");

		X6Z_sr.setIngredient('+', X5Z.getData());
		X6Z_sr.setIngredient('*', Material.SLIME_BALL);
		X6Z_sr.setIngredient('$', Ekusas83.getData());

		getServer().addRecipe(X6Z_sr);
		// -------------------------------------------- //
		/* X8Zを以下の工程で作るの図
		 *
		 * [AIR] [AIR] [AIR]
		 * [X4Z] [スライムボール] [X4Z]
		 * [AIR] [AIR] [AIR]
		 */
		ItemStack X8Z = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta_X8Z = (SkullMeta) X8Z.getItemMeta();
		X8Z.setDurability((short) 3);
		skullMeta_X8Z.setOwner("X8Z");
		X8Z.setItemMeta(skullMeta_X8Z);
		ShapedRecipe X8Z_sr = new ShapedRecipe(X8Z);

		X8Z_sr.shape("+*+");

		X8Z_sr.setIngredient('+', X4Z.getData());
		X8Z_sr.setIngredient('*', Material.SLIME_BALL);

		getServer().addRecipe(X8Z_sr);
		// -------------------------------------------- //

		FurnaceRecipe test = new FurnaceRecipe(X4Z, X5Z.getData());
		getServer().addRecipe(test);
		FurnaceRecipe Jari_X4Z = new FurnaceRecipe(X4Z, Material.GRAVEL);
		getServer().addRecipe(Jari_X4Z);
		FurnaceRecipe Wheat_Bread = new FurnaceRecipe(new ItemStack(Material.BREAD), Material.WHEAT);
		getServer().addRecipe(Wheat_Bread);
		FurnaceRecipe Grass_Dirt = new FurnaceRecipe(new ItemStack(Material.GLASS), Material.DIRT);
		getServer().addRecipe(Grass_Dirt);

		ItemStack mine_book000 = new ItemStack(Material.SKULL_ITEM);
		SkullMeta skullMeta_mine_book000 = (SkullMeta) mine_book000.getItemMeta();
		mine_book000.setDurability((short) 3);
		skullMeta_mine_book000.setOwner("mine_book000");
		mine_book000.setItemMeta(skullMeta_mine_book000);

		getServer().addRecipe(X8Z_sr);

		FurnaceRecipe wooden_axe_mine_book000 = new FurnaceRecipe(mine_book000, Material.WOOD_AXE);
		getServer().addRecipe(wooden_axe_mine_book000);
	}

	public static Map<String, String> cac = new HashMap<String, String>();
	public static Map<String, Map<String, String>> mcjppvp_banned = new HashMap<String, Map<String, String>>();
	/**
	 * Compromised Accountをログインさせないようにする前提キャッシュ
	 * @author mine_book000
	 * @see https://github.com/unchama/BanAssist
	 */
	private void Compromised_Account_Cacher(){
		try {
			URL url = new URL("https://pvp.minecraft.jp/punishments/banned-players.json");
			URLConnection urlCon = url.openConnection();
			urlCon.setRequestProperty("User-Agent", "MyMaid - Compromised Account Checker");
			InputStream in = urlCon.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String jstr = reader.readLine();

			if(jstr == null){
				return;
			}

			// 不要な配列[]を除去、},{でsplitするために一時トークンを付与
			String[] banlist = jstr.replace("[", "").replace("]", "").replace("},{", "}},{{").split(Pattern.quote("},{"));
			// 各プレイヤーに対してチェック
			for (String p : banlist) {
				JSONObject jsonObject = (JSONObject) new JSONParser().parse(p);
				if (jsonObject.get("reason").equals("Compromised Account")) {
					cac.put((String) jsonObject.get("uuid"), (String) jsonObject.get("name"));
				}else{
					Map<String, String> mcjppvp_data = new HashMap<String, String>();
					mcjppvp_data.put("name", (String) jsonObject.get("name"));
					mcjppvp_data.put("created", (String) jsonObject.get("created"));
					mcjppvp_data.put("reason", (String) jsonObject.get("reason"));
					mcjppvp_banned.put((String) jsonObject.get("uuid"), mcjppvp_data);
				}
			}
		} catch (Exception e) {
			getLogger().info("Compromised Account Cache Error.");
			e.printStackTrace();
		}
	}

	/**
	 * BukkitRunnableが動かないときの対処(強制再起動)
	 * @author mine_book000
	 */
	private void BukkitRunnableTester(){
		try{
			new BukkitRunnabletest().runTaskLater(this, 200);
		}catch(java.lang.NoClassDefFoundError e){
			restart();
		}
	}


	@Override
	public void onDisable() {
		try {
			Pointjao.Savejao();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			getLogger().info("jaoPointのセーブに失敗しました。");
			BugReport.report(e);
		}
		try {
			Jail.SaveJailData();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			getLogger().info("jail情報のセーブに失敗しました。");
			BugReport.report(e);
		}

		conf.set("var",Var.var);
		Map<String,String> colorstr = new HashMap<String,String>();
		for(Entry<String, ChatColor> p: Color.color.entrySet()){
			ChatColor color = p.getValue();
			String chatcolor;
			if(color.equals(ChatColor.AQUA)){
				chatcolor = "AQUA";
			}else if(color.equals(ChatColor.BLACK)){
				chatcolor = "BLACK";
			}else if(color.equals(ChatColor.BLUE)){
				chatcolor = "BLUE";
			}else if(color.equals(ChatColor.DARK_AQUA)){
				chatcolor = "DARK_AQUA";
			}else if(color.equals(ChatColor.DARK_BLUE)){
				chatcolor = "DARK_BLUE";
			}else if(color.equals(ChatColor.DARK_GRAY)){
				chatcolor = "DARK_GRAY";
			}else if(color.equals(ChatColor.DARK_GREEN)){
				chatcolor = "DARK_GREEN";
			}else if(color.equals(ChatColor.DARK_PURPLE)){
				chatcolor = "DARK_PURPLE";
			}else if(color.equals(ChatColor.DARK_RED)){
				chatcolor = "DARK_RED";
			}else if(color.equals(ChatColor.GOLD)){
				chatcolor = "GOLD";
			}else if(color.equals(ChatColor.GREEN)){
				chatcolor = "GREEN";
			}else if(color.equals(ChatColor.LIGHT_PURPLE)){
				chatcolor = "LIGHT_PURPLE";
			}else if(color.equals(ChatColor.RED)){
				chatcolor = "RED";
			}else if(color.equals(ChatColor.WHITE)){
				chatcolor = "WHITE";
			}else if(color.equals(ChatColor.YELLOW)){
				chatcolor = "YELLOW";
			}else if(color.equals(ChatColor.GRAY)){
				chatcolor = "GRAY";
			}else{
				chatcolor = null;
			}
			colorstr.put(p.getKey(), chatcolor);
		}
		conf.set("color", colorstr);
		conf.set("maxplayer",maxplayer);
		conf.set("maxplayertime",maxplayertime);
		conf.set("dedrain", DedRain.flag);
		saveConfig();

		if(discord != null){
			discord.end();
		}
	}

	/* ----- タスク系 ----- */

	/**
	 * ワールド保存タスク(30分毎)
	 * @author mine_book000
	 */
	private class World_saver extends BukkitRunnable{
		@Override
		public void run() {
			if(nextbakrender){
				List<World> worlds = Bukkit.getServer().getWorlds();
				for (World w : worlds)
				{
					w.save();
				}
			}
		}
	}

	/**
	 * 定期メッセージ(10分毎)
	 * @author mine_book000
	 */
	private class AutoMessage extends BukkitRunnable{
		@Override
		public void run() {
			Messenger.RandomBroadcastMessage();

			//ついでにjaoポイント保存
			try {
				Pointjao.Savejao();
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				getLogger().info("jaoPointのセーブに失敗しました。");
				BugReport.report(e);
			}
			//さらにJail情報も保存
			try {
				Jail.SaveJailData();
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				getLogger().info("jail情報のセーブに失敗しました。");
				BugReport.report(e);
			}
			//どうせならSKK情報も
			SKKColors.Save();
		}
	}

	/**
	 * TPS更新(1分毎)
	 * @author mine_book000
	 */
	private class TPSChange extends BukkitRunnable{
		@Override
		public void run() {
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

			for(Player player: Bukkit.getServer().getOnlinePlayers()) {
				String header = Method.getPlayerListHeader(player);
				Method.setPlayerListHeaderFooterByJSON(player, header, tpsmsg);
			}

		}
	}

	/**
	 * Dynmapの自動レンダリング(30分毎)
	 * @author mine_book000
	 */
	private class Dynmap_Update_Render extends BukkitRunnable{
		@Override
		public void run() {
			if(nextbakrender){
				long i = 0;
				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					new Dynmap_Update_Render_User(player).runTaskLater(MyMaid.this, i);
					i += 100;
				}
			}
		}
	}
	/**
	 * Dynmapの自動レンダリング(時間差タスク)
	 * @author mine_book000
	 */
	private class Dynmap_Update_Render_User extends BukkitRunnable{
		Player player;
		public Dynmap_Update_Render_User(Player player){
			this.player = player;
		}
		@Override
		public void run() {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dynmap updaterender Jao_Afa " + player.getLocation().getBlockX() + " " + player.getLocation().getBlockZ() );
		}
	}

	public static Map<String,Long> afktime = new HashMap<String,Long>();
	/**
	 * AFKチェックタスク
	 * @author mine_book000
	 */
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
						Discord.send(player.getName() + " is afk!");
						MyMaid.TitleSender.setTime_tick(player, 0, 99999999, 0);
						MyMaid.TitleSender.sendTitle(player, ChatColor.RED + "AFK NOW!", ChatColor.BLUE + "" + ChatColor.BOLD + "When you are back, please enter the command '/afk'.");
						MyMaid.TitleSender.setTime_tick(player, 0, 99999999, 0);
						try{
							BukkitTask task = new AFK.afking(plugin, player).runTaskTimer(plugin, 0L, 5L);
							AFK.tnt.put(player.getName(), task);
						}catch(java.lang.NoClassDefFoundError e){
							BugReport.report(e);
							AFK.tnt.put(player.getName(), null);
						}
						String listname = player.getPlayerListName().replaceAll(player.getName(), ChatColor.DARK_GRAY + player.getName());
						player.setPlayerListName(listname);
					}
				}
			}
		}
	}

	private class BukkitRunnabletest extends BukkitRunnable{
		@Override
		public void run() {
		}
	}

	public void restart() {
		try {
			Class<?> c = Class.forName("java.lang.ApplicationShutdownHooks");
			Field f = c.getDeclaredField("hooks");
			f.setAccessible(true);
			List<Thread> list = new ArrayList<Thread>();
			Map<?, ?> hooks = (Map<?, ?>)f.get(null);
			for(Object thread : hooks.values()) {
				list.add((Thread)thread);
			}
			for(Thread thread : list) {
				Runtime.getRuntime().removeShutdownHook(thread);
			}
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						String command = "/var/minecraft/autorestart.sh";
						new ProcessBuilder(command).start();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			});
			Bukkit.shutdown();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static class dot extends BukkitRunnable{
		Player player;
		public dot(JavaPlugin plugin, Player player) {
			this.player = player;
		}
		@Override
		public void run() {
			if(DOT.dotto.containsKey(player.getName())){
				int dot = DOT.dotto.get(player.getName());
				if(dot != 1){
					DOT.dotto.remove(player.getName());
					DOT.dottotask.remove(player.getName());
					Bukkit.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + "'s DOTTO COUNTER: " + dot + "/1s");
				}else{
					DOT.dotto.remove(player.getName());
					DOT.dottotask.remove(player.getName());
				}
			}
		}

	}
	/*
	public static class doom extends BukkitRunnable{
		Player player;
		public doom(JavaPlugin plugin, Player player) {
			this.player = player;
		}
		@Override
		public void run() {
			if(DOT.doom.containsKey(player.getName())){
				int dot = DOT.doom.get(player.getName());
				if(dot != 1){
					DOT.doom.remove(player.getName());
					DOT.doomtask.remove(player.getName());
					if(dot >= 10){
						Collection<Channel> channels = MyMaid.lunachatapi.getChannels();
						boolean chan = true;
						for(Channel channel: channels){
							if(channel.getName().equals("_CHAT_JAIL_")){
								chan = false;
							}
						}
						if(chan){
							MyMaid.lunachatapi.createChannel("_CHAT_JAIL_");
						}
						if(MyMaid.lunachatapi.getChannel("_CHAT_JAIL_").isBroadcastChannel()){
							MyMaid.lunachatapi.getChannel("_CHAT_JAIL_").setBroadcast(false);
						}
						MyMaid.lunachatapi.getChannel("_CHAT_JAIL_").addMember(ChannelPlayer.getChannelPlayer(player.getName()));
						MyMaid.lunachatapi.setDefaultChannel(player.getName(), "_CHAT_JAIL_");
						DOT.dotcount_stop.put(player.getName(), true);
						Bukkit.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + "はチャットスパムとして判定されましたので隔離チャンネルに移動しました。(" + dot + "/1s)");
					}
				}else{
					DOT.doom.remove(player.getName());
					DOT.doomtask.remove(player.getName());
				}
			}
		}

	}
	*/
	public static class dot_section extends BukkitRunnable{
		Player player;
		LunaChatAPI lunachatapi;
		int section;
		public dot_section(JavaPlugin plugin, Player player, LunaChatAPI lunachatapi, int section) {
			this.player = player;
			this.lunachatapi = lunachatapi;
			this.section = section;
		}
		@Override
		public void run() {
			//Bukkit.broadcastMessage(DOT.success.toString() + DOT.unsuccess.toString());
			int success;
			if(DOT.success.containsKey(player.getName())){
				success = DOT.success.get(player.getName());
			}else{
				success = 0;
			}
			int unsuccess;
			if(DOT.unsuccess.containsKey(player.getName())){
				try{
					unsuccess = DOT.unsuccess.get(player.getName());
				}catch(NullPointerException e){
					unsuccess = 0;
				}
			}else{
				unsuccess = 0;
			}
			DOT.run.get(player.getName()).cancel();
			DOT.run.remove(player.getName());
			DOT.dotcount_stop.remove(player.getName());
			DOT.success.remove(player.getName());
			DOT.unsuccess.remove(player.getName());
			lunachatapi.getChannel("_DOT_").removeMember(ChannelPlayer.getChannelPlayer(player.getName()));
			if(section == 10){
				String jyuni = Method.url_jaoplugin("dot", "p=" + player.getName() + "&u=" + player.getUniqueId() + "&success=" + success + "&unsuccess=" + unsuccess + "&" + section + "s");
				Bukkit.broadcastMessage("[.] " + ChatColor.GREEN + player.getName() + "のピリオド対決(" + section + "秒部門)の結果: 成功回数" + success + " 失敗回数" + unsuccess + "(累計順位: " + jyuni + "位)");
			}else if(section == 60){
				String jyuni = Method.url_jaoplugin("dot", "p=" + player.getName() + "&u=" + player.getUniqueId() + "&success=" + success + "&unsuccess=" + unsuccess + "&" + section + "s");
				Bukkit.broadcastMessage("[.] " + ChatColor.GREEN + player.getName() + "のピリオド対決(" + section + "秒部門)の結果: 成功回数" + success + " 失敗回数" + unsuccess + "(累計順位: " + jyuni + "位)");
			}else if(section == 300){
				String jyuni = Method.url_jaoplugin("dot", "p=" + player.getName() + "&u=" + player.getUniqueId() + "&success=" + success + "&unsuccess=" + unsuccess + "&" + section + "s");
				Bukkit.broadcastMessage("[.] " + ChatColor.GREEN + player.getName() + "のピリオド対決(" + section + "秒部門)の結果: 成功回数" + success + " 失敗回数" + unsuccess + "(累計順位: " + jyuni + "位)");
			}else{
				Bukkit.broadcastMessage("[.] " + ChatColor.GREEN + player.getName() + "のピリオド対決(" + section + "秒例外部門)の結果: 成功回数" + success + " 失敗回数" + unsuccess + "(部門外のためrankingなし)");
			}

			MyMaid.lunachatapi.setPlayersJapanize(player.getName(), DOT.kana.get(player.getName()));
		}
	}
	/*
	public static class QueueDiscordSendData extends BukkitRunnable{
		JavaPlugin plugin;
		public QueueDiscordSendData(JavaPlugin plugin) {
			this.plugin = plugin;
		}
		@Override
		public void run() {
			String senddata = Discord.SendData.poll();
			if(senddata == null){
				Discord.task.cancel();
				return;
			}
			if(!Discord.retrysend(senddata)){
				Discord.Queuesend(senddata);
			}
		}

	}
	*/
	public static JavaPlugin getJavaPlugin(){
		return instance;
	}
	public static MyMaid getInstance(){
		return mymaid;
	}
	public static class usemodget extends BukkitRunnable{
		Player player;
		public usemodget(JavaPlugin plugin, Player player) {
			this.player = player;
		}
		@Override
		public void run() {
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
					e1.printStackTrace();
					for(Player p: Bukkit.getServer().getOnlinePlayers()) {
						if(PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) {
							p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。ClassNotFoundException / SQLException");
							p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
						}
					}
					return;
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) {
						p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。SQLException");
						p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
					}
				}
				return;
			}

			statement = MySQL.check(statement);


			Set<String> mods = player.getListeningPluginChannels();
			StringBuilder builder = new StringBuilder();
			for(String mod : mods){
				builder.append(mod).append(",");
			}
			String strmods;
			if(builder.length() == 0){
				strmods = "Vanilla";
			}else{
				strmods = builder.substring(0, builder.length() - 1);
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "Mod情報: " + strmods);
				}
			}

			try {
				statement.executeUpdate("INSERT INTO usemod (id, player, uuid, mods, date) VALUES (NULL, '" + player.getName() + "', '" + player.getUniqueId().toString() + "', '" + strmods + "', '" + sdf.format(new Date()) + "');");
			} catch (SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) {
						p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。SQLException");
						p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e1.getMessage());
					}
				}
			}
		}
	}
}