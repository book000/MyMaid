package xyz.jaoafa.mymaid;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import xyz.jaoafa.mymaid.Command.AFK;
import xyz.jaoafa.mymaid.Command.Access;
import xyz.jaoafa.mymaid.Command.Book;
import xyz.jaoafa.mymaid.Command.Chat;
import xyz.jaoafa.mymaid.Command.Cmdb;
import xyz.jaoafa.mymaid.Command.Cmdsearch;
import xyz.jaoafa.mymaid.Command.Data;
import xyz.jaoafa.mymaid.Command.Dynmap_Teleporter;
import xyz.jaoafa.mymaid.Command.Gamemode_Change;
import xyz.jaoafa.mymaid.Command.Head;
import xyz.jaoafa.mymaid.Command.Ip_To_Host;
import xyz.jaoafa.mymaid.Command.Ja;
import xyz.jaoafa.mymaid.Command.JaoJao;
import xyz.jaoafa.mymaid.Command.Jf;
import xyz.jaoafa.mymaid.Command.Prison;
import xyz.jaoafa.mymaid.Command.SaveWorld;
import xyz.jaoafa.mymaid.Command.SignLock;
import xyz.jaoafa.mymaid.Command.TNTReload;
import xyz.jaoafa.mymaid.Command.Vote;
import xyz.jaoafa.mymaid.EventHandler.PlayerCommand;

public class MyMaid extends JavaPlugin implements Listener {
	Boolean nextbakrender = false;
	public static Boolean tntexplode = true;
	public static Map<String,String> chatcolor = new HashMap<String,String>();
	@Override
    public void onEnable() {
    	getLogger().info("(c) jao Minecraft Server MyMaid Project.");
    	getLogger().info("Product by tomachi.");
    	getServer().getPluginManager().registerEvents(this, this);
    	getServer().getPluginManager().registerEvents(new PlayerCommand(this), this);
		this.getServer().getScheduler().runTaskTimer(this, new World_saver(), 0L, 36000L);
		this.getServer().getScheduler().runTaskTimer(this, new Dynmap_Update_Render(), 0L, 36000L);

		getCommand("chat").setExecutor(new Chat(this));
		getCommand("jf").setExecutor(new Jf(this));
		getCommand("dt").setExecutor(new Dynmap_Teleporter(this));
		getCommand("dt").setTabCompleter(new Dynmap_Teleporter(this));
		getCommand("g").setExecutor(new Gamemode_Change(this));
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
		getCommand("book").setExecutor(new Book(this));
		getCommand("jail").setExecutor(new Prison(this));
		getCommand("jail").setTabCompleter(new Prison(this));
		getCommand("access").setExecutor(new Access(this));
		getCommand("ja").setExecutor(new Ja(this));
		getCommand("cmdsearch").setExecutor(new Cmdsearch(this));
    }

    @Override
    public void onDisable() {

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
				tntexplode = true;
				//Bukkit.broadcastMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "└( ・з・)┘" + ChatColor.WHITE +  ": " + "自己紹介の変更はこちらより https://jaoafa.xyz/minecraftjp/login");
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

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent e){
    	Location to = e.getTo();
    	Player player = e.getPlayer();
  		if(!Prison.prison.containsKey(player.getName())){
  			return;
  		}
  		if(Prison.prison.get(player.getName())){
  			return;
  		}
  		World World = Bukkit.getServer().getWorld("Jao_Afa");
  		Location prison = new Location(World, 1767, 70, 1767);
  		if(prison.distance(to) >= 150){
  			player.sendMessage("[PRISON] " + ChatColor.GREEN + "あなたは南の楽園から出られません！");
  			e.setCancelled(true);
  			if(prison.distance(to) >= 200){
  				player.teleport(prison);
  			}
  		}
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e){
    	Player player = e.getPlayer();
  		if(!Prison.prison.containsKey(player.getName())){
  			return;
  		}
  		e.setCancelled(true);
  		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはコマンドを実行できません。");
  		getLogger().info("[JAIL] "+player.getName()+"==>あなたはコマンドを実行できません。");
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e){
    	Player player = e.getPlayer();
  		if(!Prison.prison.containsKey(player.getName())){
  			return;
  		}
  		if(Prison.prison_block.get(player.getName())){
  			return;
  		}
  		e.setCancelled(true);
  		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはブロックを置けません。");
  		getLogger().info("[JAIL] "+player.getName()+"==>あなたはブロックを置けません。");
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e){
    	Player player = e.getPlayer();
  		if(!Prison.prison.containsKey(player.getName())){
  			return;
  		}
  		if(Prison.prison_block.get(player.getName())){
  			return;
  		}
  		e.setCancelled(true);
  		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはブロックを壊せません。");
  		getLogger().info("[JAIL] "+player.getName()+"==>あなたはブロックを壊せません。");
    }
    @EventHandler
    public void onBlockIgniteEvent(BlockIgniteEvent e){
    	Player player = e.getPlayer();
  		if(!Prison.prison.containsKey(player.getName())){
  			return;
  		}
  		e.setCancelled(true);
  		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはブロックを着火できません。");
  		getLogger().info("[JAIL] "+player.getName()+"==>あなたはブロックを着火できません。");
    }
    @EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent e){
    	Player player = e.getPlayer();
  		if(!Prison.prison.containsKey(player.getName())){
  			return;
  		}
  		e.setCancelled(true);
  		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたは水や溶岩を撒けません。");
  		getLogger().info("[JAIL] "+player.getName()+"==>あなたは水や溶岩を撒けません。");
    }
    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent e){
    	Player player = e.getPlayer();
  		if(!Prison.prison.containsKey(player.getName())){
  			return;
  		}
  		e.setCancelled(true);
    }
    @EventHandler
    public void onBlockRedstoneEvent(BlockRedstoneEvent e){
    	if(e.getOldCurrent() == 0 && e.getNewCurrent() > 0){
    		if(e.getBlock().getType() == Material.COMMAND){
        		CommandBlock cmdb = (CommandBlock)e.getBlock().getState();
        		for(Player player: Bukkit.getServer().getOnlinePlayers()){
        			if(Cmdsearch.start.containsKey(player.getName())){
        				if(cmdb.getCommand().startsWith(Cmdsearch.start.get(player.getName()))){
        					double min = 1.79769313486231570E+308;
        		        	Player min_player = null;
        					for(Player p: Bukkit.getServer().getOnlinePlayers()){
        						Location location_p = p.getLocation();
        		            	double distance = cmdb.getLocation().distance(location_p);
        		            	if(distance < min){
        		            		min = distance;
        		            		min_player = p;
        		            	}
        		        	}
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Found Cmdb(start)");
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "XYZ: " + cmdb.getX() + " " + cmdb.getY() + " " + cmdb.getZ());
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Near Player: " + min_player.getName() + " (" + min + "Block)");
        				}
    				}
    				if(Cmdsearch.end.containsKey(player.getName())){
    					if(cmdb.getCommand().endsWith(Cmdsearch.start.get(player.getName()))){
        					double min = 1.79769313486231570E+308;
        		        	Player min_player = null;
        					for(Player p: Bukkit.getServer().getOnlinePlayers()){
        						Location location_p = p.getLocation();
        		            	double distance = cmdb.getLocation().distance(location_p);
        		            	if(distance < min){
        		            		min = distance;
        		            		min_player = p;
        		            	}
        		        	}
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Found Cmdb(end)");
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "XYZ: " + cmdb.getX() + " " + cmdb.getY() + " " + cmdb.getZ());
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Near Player: " + min_player.getName() + " (" + min + "Block)");
        				}
    				}
    				if(Cmdsearch.in.containsKey(player.getName())){
    					if(cmdb.getCommand().indexOf(Cmdsearch.start.get(player.getName())) != -1){
        					double min = 1.79769313486231570E+308;
        		        	Player min_player = null;
        					for(Player p: Bukkit.getServer().getOnlinePlayers()){
        						Location location_p = p.getLocation();
        		            	double distance = cmdb.getLocation().distance(location_p);
        		            	if(distance < min){
        		            		min = distance;
        		            		min_player = p;
        		            	}
        		        	}
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Found Cmdb(in)");
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "XYZ: " + cmdb.getX() + " " + cmdb.getY() + " " + cmdb.getZ());
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Near Player: " + min_player + " (" + min + "Block)");
        				}
    				}
            	}
        	}
    	}
    }
	BukkitTask task = null;
	@EventHandler(ignoreCancelled = true)
    public void onExplosion(EntityExplodeEvent e){
    	try {
    		BlockState states = null;
        	for(Block block : e.blockList()){
        		states = block.getState();
        		break;
        	}
        	Location location = states.getLocation();
        	int x = location.getBlockX();
        	int y = location.getBlockY();
        	int z = location.getBlockZ();

        	double min = 1.79769313486231570E+308;
        	Player min_player = null;
        	for(Player player: Bukkit.getServer().getOnlinePlayers()){
        		org.bukkit.Location location_p = player.getLocation();
            	double distance = location.distance(location_p);
            	if(distance < min){
            		min = distance;
            		min_player = player;
            	}
        	}
        	if(min_player.hasPermission("mymaid.pex.default") || min_player.hasPermission("mymaid.pex.provisional")){
        		e.setCancelled(true);
        	}
    		if(tntexplode){
            	if(min < 20 && min_player.hasPermission("pin_code_auth.joinmsg")){
            		// 無視
            	}else{
            		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
            			if(p.hasPermission("pin_code_auth.joinmsg")) {
            				p.sendMessage("[" + ChatColor.RED + "TNT" + ChatColor.WHITE + "] " + ChatColor.GREEN + min_player.getName() + "の近くの" + x + " " + y + " " + z + "地点にてTNTが爆発し、ブロックが破壊されました。確認して下さい。");
            				Bukkit.getLogger().info(min_player.getName() + " near [" + x + " " + y + " " + z + "] TNTExploded.");
            			}
            		}
            		String name = min_player.getName();
        			UUID uuid = min_player.getUniqueId();
        			url_access("http://toma.webcrow.jp/jaotnt.php?p="+name+"&u="+uuid+"&x="+x+"&y="+y+"&z="+z);

            		task = new BukkitRunnable() {
                        @Override
                        public void run() {
                        	tntexplode = true;
                        	Bukkit.getLogger().info("TNT Exploded notice on");
                            task.cancel();
                        }
                    }.runTaskLater(this, 1200);
                    tntexplode = false;
                    Bukkit.getLogger().info("TNT Exploded notice off");
            	}
        	}
    	}catch(Exception e1) {
    		tntexplode = false;
    	}

    	return;
    }
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		if(e.getBlock().getType() == Material.CROPS){
			Crops block = (Crops)e.getBlock().getState().getData();
			if(block.getState() == CropState.RIPE){
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onFrom(EntityChangeBlockEvent e){
		if(e.getBlock().getType() == Material.SOIL){
			e.setCancelled(true);
		}
		//Bukkit.broadcastMessage("EntityChangeBlockEvent! " + e.getBlock().getType());
	}

	@EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e){
    	String command = e.getMessage();
    	Player player = e.getPlayer();
    	String[] args = command.split(" ", 0);
    	if(args.length == 2){
    		if(args[0].equalsIgnoreCase("/kill")){
        		if(args[1].equalsIgnoreCase("@e")){
        			player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @eはサーバー内のすべてのエンティティが削除されてしまうので使用できません");
        			e.setCancelled(true);
        			return;
        		}
        		if(args[1].equalsIgnoreCase("@a")){
        			player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @aはサーバー内のすべてのプレイヤーが殺害されてしまうので使用できません");
        			e.setCancelled(true);
        			return;
        		}
        		if(args[1].startsWith("@e")){
        			if(player.hasPermission("mymaid.pex.provisional") || player.hasPermission("mymaid.pex.default")){
        				player.sendMessage("[COMMAND] " + ChatColor.GREEN + "└( ・з・)┘");
            			e.setCancelled(true);
            			return;
        			}
        		}
        	}
    		if(args[0].equalsIgnoreCase("/minecraft:kill")){
        		if(args[1].equalsIgnoreCase("@e")){
        			player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @eはサーバー内のすべてのエンティティが削除されてしまうので使用できません");
        			e.setCancelled(true);
        			return;
        		}
        		if(args[1].equalsIgnoreCase("@a")){
        			player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @aはサーバー内のすべてのプレイヤーが殺害されてしまうので使用できません");
        			e.setCancelled(true);
        			return;
        		}
        		if(args[1].startsWith("@e")){
        			if(player.hasPermission("mymaid.pex.provisional") || player.hasPermission("mymaid.pex.default")){
        				player.sendMessage("[COMMAND] " + ChatColor.GREEN + "└( ・з・)┘");
            			e.setCancelled(true);
            			return;
        			}
        		}
        	}
    	}
	}
	public static String url_access(String address){
		System.out.println("[MyMaid] URLConnect Start:"+address);
		try{
			URL url=new URL(address);
			// URL接続
			HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
			connect.setRequestMethod("GET");//プロトコルの設定
			InputStream in=connect.getInputStream();//ファイルを開く

			// ネットからデータの読み込み
			String data=readString(in);//1行読み取り
			// URL切断
			in.close();//InputStreamを閉じる
			connect.disconnect();//サイトの接続を切断
			System.out.println("[MyMaid] URLConnect End:"+address);
			System.out.println(data);
			return data;
		}catch(Exception e){
			//例外処理が発生したら、表示する
			System.out.println(e);
			System.out.println("[MyMaid] URLConnect Err:"+address);
			return "";
		}
	}
	//InputStreamより１行だけ読む（読めなければnullを返す）
	static String readString(InputStream in){
		try{
			int l;//呼んだ長さを記録
			int a;//読んだ一文字の記録に使う
			byte b[]=new byte[2048];//呼んだデータを格納
			a=in.read();//１文字読む
			if (a<0) return null;//ファイルを読みっていたら、nullを返す
			l=0;
			while(a>10){//行の終わりまで読む
				if (a>=' '){//何かの文字であれば、バイトに追加
					b[l]=(byte)a;
					l++;
				}
				a=in.read();//次を読む
			}
			return new String(b,0,l);//文字列に変換
		}catch(IOException e){
			//Errが出たら、表示してnull値を返す
			System.out.println("Err="+e);
			return null;
		}
	}


	@EventHandler //看板ブロックを右クリック
	public void onSignClick(PlayerInteractEvent event) {
	    if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
	    if(event.getPlayer().getItemInHand().getType() != Material.STICK) return;
	    Block clickedBlock = event.getClickedBlock();
	    Material material = clickedBlock.getType();
	    if (material == Material.SIGN_POST || material == Material.WALL_SIGN) {
	        Sign sign = (Sign) clickedBlock.getState();
	        xyz.jaoafa.mymaid.Command.Sign.signlist.put(event.getPlayer().getName(), sign);
	        int x = sign.getX();
	        int y = sign.getY();
	        int z = sign.getZ();
	        event.getPlayer().sendMessage("[Sign] " + ChatColor.GREEN + "看板を選択しました。[" + x + " " + y + " " + z + "]");
	    }
	}
	@EventHandler
	public void onHeadClick(PlayerInteractEvent event) {
	    if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
	    if(event.getPlayer().getItemInHand().getType() != Material.STICK) return;
	    Block clickedBlock = event.getClickedBlock();
	    Material material = clickedBlock.getType();
	    if (material == Material.SKULL) {
	        Skull skull = (Skull) clickedBlock.getState();
	        Player player = event.getPlayer();
	        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw "+player.getName()+" [\"\",{\"text\":\"このユーザー「"+skull.getOwner()+"」のユーザーページを開く\",\"color\":\"aqua\",\"underlined\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://jaoafa.xyz/user/"+skull.getOwner()+"\"}}]");
	    }
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e){
		String Msg = e.getPlayer().getName();
		Date Date = new Date();
		String message = e.getMessage();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

		if(e.getPlayer().hasPermission("mymaid.pex.limited")){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.BLACK + "■" + ChatColor.WHITE + e.getPlayer().getName());
				e.setFormat(Msg);
	  	}else if(Prison.prison.containsKey(e.getPlayer().getName())){
			Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.DARK_GRAY + "■" + ChatColor.WHITE + e.getPlayer().getName());
			e.setFormat(Msg);
  		}else if(chatcolor.containsKey(e.getPlayer().getName())){
	  		int i = Integer.parseInt(chatcolor.get(e.getPlayer().getName()));
			if(i >= 0 && i <= 5){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.WHITE + "■" + ChatColor.WHITE + e.getPlayer().getName());
			}else if(i >= 6 && i <= 19){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.DARK_BLUE + "■" + ChatColor.WHITE + e.getPlayer().getName());
			}else if(i >= 20 && i <= 33){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.BLUE + "■" + ChatColor.WHITE + e.getPlayer().getName());
			}else if(i >= 34 && i <= 47){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.AQUA + "■" + ChatColor.WHITE + e.getPlayer().getName());
			}else if(i >= 48 && i <= 61){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.DARK_AQUA + "■" + ChatColor.WHITE + e.getPlayer().getName());
			}else if(i >= 62 && i <= 76){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.DARK_GREEN + "■" + ChatColor.WHITE + e.getPlayer().getName());
			}else if(i >= 77 && i <= 89){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.GREEN + "■" + ChatColor.WHITE + e.getPlayer().getName());
			}else if(i >= 90 && i <= 103){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.YELLOW + "■" + ChatColor.WHITE + e.getPlayer().getName());
			}else if(i >= 104 && i <= 117){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.GOLD + "■" + ChatColor.WHITE + e.getPlayer().getName());
			}else if(i >= 118 && i <= 131){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.RED + "■" + ChatColor.WHITE + e.getPlayer().getName());
			}else if(i >= 132 && i <= 145){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.DARK_RED + "■" + ChatColor.WHITE + e.getPlayer().getName());
			}else if(i >= 146 && i <= 159){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.DARK_PURPLE + "■" + ChatColor.WHITE + e.getPlayer().getName());
			}else if(i >= 160){
				Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.LIGHT_PURPLE + "■" + ChatColor.WHITE + e.getPlayer().getName());
			}
			e.setFormat(Msg);
			//e.setCancelled(true);
			//Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + Msg + ": " + message);
		}else{
			Msg = e.getPlayer().getName().replaceFirst(e.getPlayer().getName(), ChatColor.GRAY + "■" + ChatColor.WHITE + e.getPlayer().getName());
			e.setFormat(Msg);
		}
	}

  	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
  		nextbakrender = true;
  		UUID uuid = event.getPlayer().getUniqueId();
  		String data = url_access("http://toma.webcrow.jp/jao.php?file=joinvote.php&u="+uuid);
  		String[] arr = data.split("###", 0);
  		String result = arr[0];
  		chatcolor.put(event.getPlayer().getName(), arr[1]);
  		if(!result.equalsIgnoreCase("null")){
  			event.setJoinMessage(ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.YELLOW + ", " + ChatColor.YELLOW +result+" joined the game.");
  		}
  		if(event.getPlayer().hasPermission("mymaid.pex.limited")){
  			event.setJoinMessage(ChatColor.RED + event.getPlayer().getName() + ChatColor.YELLOW + " joined the game.");
		}
  	}
  	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuitGame(PlayerQuitEvent event){
  		Player player = event.getPlayer();
  		nextbakrender = false;
  		try {
  			AFK.tnt.get(player.getName()).cancel();
  		}catch(NullPointerException e){

  		}
  		player.sendMessage("[AFK] " + ChatColor.GREEN + "AFK false");
  		//Bukkit.dispatchCommand(player.getPlayer(), "gamerule sendCommandFeedback false");
		//Bukkit.dispatchCommand(player.getPlayer(), "title " + player.getPlayer().getName() + " reset");
		//Bukkit.dispatchCommand(player.getPlayer(), "gamerule sendCommandFeedback true");
  		ItemStack[] is = player.getInventory().getArmorContents();
		if(is[3].getType() == Material.ICE){
			ItemStack[] after={
					new ItemStack(is[0]),
					new ItemStack(is[1]),
					new ItemStack(is[2]),
					new ItemStack(Material.AIR)};
			player.getInventory().setArmorContents(after);
			player.updateInventory();

		}
  	}
}