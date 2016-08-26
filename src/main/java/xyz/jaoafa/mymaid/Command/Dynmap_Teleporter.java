package xyz.jaoafa.mymaid.Command;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;

public class Dynmap_Teleporter implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public Dynmap_Teleporter(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static Map<String,Boolean> dynamic = new HashMap<String,Boolean>();
	public static Map<String,BukkitTask> dynamic_teleporter = new HashMap<String,BukkitTask>();
	private BukkitTask task = null;
	private int taskcount = 0;
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
	    URLCodec codec = new URLCodec();
		if(args.length == 0){
			Method.SendMessage(sender, cmd, "このコマンドは1つまたは2つの引数が必要です。");
			return false;
		}else if(args.length == 1){
			// コマンド実行者がプレイヤーかどうか
			if (!(sender instanceof Player)) {
				Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
				return true;
			}
			String location = args[0];
			Player player = (Player) sender;
			try {
				location = codec.encode(location);
			} catch (EncoderException e1) {

			}
			try{
				URL url=new URL("http://toma.webcrow.jp/jaoget.php?location=" + location);
				// URL接続
				HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
				connect.setRequestMethod("GET");//プロトコルの設定
				InputStream in=connect.getInputStream();//ファイルを開く
				String data;//ネットから読んだデータを保管する変数を宣言
				if(location.equalsIgnoreCase("list")){
					Method.SendMessage(sender, cmd, "----- Location List -----");
					data = readString(in);//1行読み取り
					while (data != null) {//読み取りが成功していれば
						data = codec.decode(data, StandardCharsets.UTF_8.name());
						Method.SendMessage(sender, cmd, data);
						data = readString(in);//次を読み込む
					}
					Method.SendMessage(sender, cmd, "-------------------------");
					return true;
				}else{
					data = readString(in);
					if(data.equalsIgnoreCase("NOLOCATION")){
						Method.SendMessage(sender, cmd, "その名前の場所は登録されていません。/dt listで場所を確認してください。");
						return true;
					}else{
						String[] datas = data.split(",", 0);
						String x = datas[0];
						String y = datas[1];
						String z = datas[2];
						String world = datas[3];
						location = codec.decode(location, StandardCharsets.UTF_8.name());
						if(dynamic.containsKey(player.getName())){
							if(dynamic_teleporter.containsKey(player.getName())){
								Method.SendMessage(sender, cmd, "ダイナミックテレポート失敗	: 現在テレポート中です。");
								return true;
							}else if(task != null){
								Method.SendMessage(sender, cmd, "ダイナミックテレポート失敗	: 現在他の方がテレポート中です。");
								return true;
							}else{
								Method.SendMessage(sender, cmd, "ダイナミックテレポートしています…");
								player.setFlying(true);
								Location loc = new Location(Bukkit.getServer().getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
								dynamic_teleporter.put(player.getName(), new upteleport(plugin, player, location, loc).runTaskTimer(plugin, 0, 5));
								return true;
							}
						}else{
							MyMaid.TitleSender.setTime_second(player, 2, 5, 2);
							MyMaid.TitleSender.sendTitle(player, "", ChatColor.AQUA +  "You have been teleported to " + location + "!");
							Location loc = new Location(Bukkit.getServer().getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
							player.teleport(loc);
							Bukkit.broadcastMessage(ChatColor.GRAY + "[" + player.getName() + ": " + player.getName() + " は " + location + " にワープしました]");
							return true;
						}
					}
				}

			}catch(Exception e){
				//例外処理が発生したら、表示する
				e.printStackTrace();
				Method.SendMessage(sender, cmd, "エラーが発生しました。詳しくはサーバーログを確認してください。");
			}
		}else if(args.length >= 2){
			String p;
			p = args[0];
			String text = "";
			int c = 1;
			while(args.length > c){
				text += args[c];
				if(args.length != (c+1)){
					text+=" ";
				}
				c++;
			}
			for(Player player: Bukkit.getServer().getOnlinePlayers()) {
				if(player.getName().equalsIgnoreCase(p)) {
					String location = text;
					try {
						location = codec.encode(location);
					} catch (EncoderException e1) {

					}
					try{
						URL url=new URL("http://toma.webcrow.jp/jaoget.php?location=" + location);
						// URL接続
						HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
						connect.setRequestMethod("GET");//プロトコルの設定
						InputStream in=connect.getInputStream();//ファイルを開く
						String data;//ネットから読んだデータを保管する変数を宣言
						if(location.equalsIgnoreCase("list")){
							Method.SendMessage(sender, cmd, "----- Location List -----");
							data = readString(in);//1行読み取り
							while (data != null) {//読み取りが成功していれば
								data = codec.decode(data, StandardCharsets.UTF_8.name());
								Method.SendMessage(sender, cmd, data);
								data = readString(in);//次を読み込む
							}
							Method.SendMessage(sender, cmd, "-------------------------");
							return true;
						}else{
							data = readString(in);
							if(data.equalsIgnoreCase("NOLOCATION")){
								Method.SendMessage(sender, cmd, "その名前の場所は登録されていません。/dt listで場所を確認してください。");
								return true;
							}else{
								String[] datas = data.split(",", 0);
								String x = datas[0];
								String y = datas[1];
								String z = datas[2];
								String world = datas[3];
								location = codec.decode(location, StandardCharsets.UTF_8.name());
								if(dynamic.containsKey(player.getName())){
									if(dynamic_teleporter.containsKey(player.getName())){
										Method.SendMessage(sender, cmd, "ダイナミックテレポート失敗	: 現在テレポート中です。");
									}else{
										Method.SendMessage(sender, cmd, "ダイナミックテレポートしています…");
										player.setFlying(true);
										Location loc = new Location(Bukkit.getServer().getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
										dynamic_teleporter.put(player.getName(), new upteleport(plugin, player, location, loc).runTaskTimer(plugin, 0, 1));
										return true;
									}
								}else{
									MyMaid.TitleSender.setTime_second(player, 2, 5, 2);
									MyMaid.TitleSender.sendTitle(player, "", ChatColor.AQUA +  "You have been teleported to " + location + "!");
									Location loc = new Location(Bukkit.getServer().getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
									player.teleport(loc);
									Bukkit.broadcastMessage(ChatColor.GRAY + "[" + player.getName() + ": " + player.getName() + " は " + location + " にワープしました]");
									return true;
								}
							}
						}

					}catch(Exception e){
						//例外処理が発生したら、表示する
						System.out.println(e);
						Method.SendMessage(sender, cmd, "エラーが発生しました。詳しくはサーバーログを確認してください。");
					}
					return true;
				}
			}
			Method.SendMessage(sender, cmd, "ユーザーが見つかりません");
			return true;
		}
		return false;
	}

	String[] datas;
    URLCodec codec = new URLCodec();
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (args.length == 2) {
            if (args[1].length() == 0) { // /testまで
            	try{
					URL url=new URL("http://toma.webcrow.jp/jaoget.php?tab=all");
					// URL接続
					HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
					connect.setRequestMethod("GET");//プロトコルの設定
					InputStream in=connect.getInputStream();//ファイルを開く

					String data;//ネットから読んだデータを保管する変数を宣言
					data = readString(in);
					if(data == null){
						return null;
					}
					data = codec.decode(data, StandardCharsets.UTF_8.name());
					if(!data.contains(",")){
						return Collections.singletonList(data);
					}
					datas = data.split(",", 0);
					return Arrays.asList(datas);
				}catch(Exception e){
					//例外処理が発生したら、表示する
					System.out.println(e);
					Method.SendMessage(sender, cmd, "エラーが発生しました。詳しくはサーバーログを確認してください。");
				}
            } else{
            	try{
					URL url=new URL("http://toma.webcrow.jp/jaoget.php?tab=" + args[1]);
					// URL接続
					HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
					connect.setRequestMethod("GET");//プロトコルの設定
					connect.setRequestProperty("Accept-Language", "jp");
					connect.setRequestProperty("Content-Type","text/html;charset=utf-8");
					InputStream in=connect.getInputStream();//ファイルを開く
					String data;//ネットから読んだデータを保管する変数を宣言
					data = readString(in);
					if(data == null){
						return null;
					}
					data = codec.decode(data, StandardCharsets.UTF_8.name());
					if(!data.contains(",")){
						return Collections.singletonList(data);
					}
					datas = data.split(",", 0);
					return Arrays.asList(datas);
				}catch(Exception e){
					//例外処理が発生したら、表示する
					System.out.println(e);
					Method.SendMessage(sender, cmd, "エラーが発生しました。詳しくはサーバーログを確認してください。");
				}
            }
        }else if(args.length == 1){
        	if (args[0].length() != 0 && cmd.getName().equalsIgnoreCase("dt")) {
        		try{
					URL url=new URL("http://toma.webcrow.jp/jaoget.php?tab=" + args[0]);
					// URL接続
					HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
					connect.setRequestMethod("GET");//プロトコルの設定
					connect.setRequestProperty("Accept-Language", "jp");
					connect.setRequestProperty("Content-Type","text/html;charset=utf-8");
					InputStream in=connect.getInputStream();//ファイルを開く
					String data;//ネットから読んだデータを保管する変数を宣言
					data = readString(in);
					if(data == null){
						return null;
					}
					data = codec.decode(data, StandardCharsets.UTF_8.name());
					if(!data.contains(",")){
						return Collections.singletonList(data);
					}
					datas = data.split(",", 0);
					return Arrays.asList(datas);
				}catch(Exception e){
					//例外処理が発生したら、表示する
					System.out.println(e);
					Method.SendMessage(sender, cmd, "エラーが発生しました。詳しくはサーバーログを確認してください。");

				}
        	}
        }
        //JavaPlugin#onTabComplete()を呼び出す
        return plugin.onTabComplete(sender, cmd, alias, args);
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
	private Location getTeleportToLoc(Player player, Location loc, Location toloc, String location){
		if(loc.getBlockX() > toloc.getBlockX()) {
			//現在位置よりも行きたい方向はX:-1
			loc.add(-1, 0, 0);
		}else if(loc.getBlockX() < toloc.getBlockX()) {
			//現在位置よりも行きたい方向はX:+1
			loc.add(1, 0, 0);
		}else if(loc.getBlockY() > toloc.getBlockY()) {
			//現在位置よりも行きたい方向はY:-1
			loc.add(0, -1, 0);
		}else if(loc.getBlockY() < toloc.getBlockY()){
			//現在位置よりも行きたい方向はY:+1
			loc.add(0, 1, 0);
		}else if(loc.getBlockZ() > toloc.getBlockZ()){
			//現在位置よりも行きたい方向はZ:-1
			loc.add(0, 0, -1);
		}else if(loc.getBlockZ() < toloc.getBlockZ()) {
			//現在位置よりも行きたい方向はZ:+1
			loc.add(0, 0, 1);
		}else{
			return null;
		}
		if (!loc.getBlock().getType().isSolid() && !loc.clone().add(0, 1, 0).getBlock().getType().isSolid()){
			player.teleport(loc);
			return loc;
		}else{
			loc.add(0, 1, 0);
			if(taskcount > 4){
				player.sendMessage("[DT] " + ChatColor.GREEN + "ダイナミックテレポート失敗	: 不明なエラー");
				task.cancel();
				task = null;
				return null;
			}
			taskcount++;
			return getTeleportToLoc(player, loc, toloc, location);
		}
	}
	private class upteleport extends BukkitRunnable{
		Player player;
		String location;
		Location toloc;
    	public upteleport(JavaPlugin plugin, Player player, String location, Location toloc) {
    		this.player = player;
    		this.location = location;
    		this.toloc = toloc;
    	}
		@Override
		public void run() {
			task = new teleport(plugin, player, location, toloc).runTaskTimer(plugin, 0, 5);
		}
	}
	private class teleport extends BukkitRunnable{
		Player player;
		String location;
		Location toloc;
    	public teleport(JavaPlugin plugin, Player player, String location, Location toloc) {
    		this.player = player;
    		this.location = location;
    		this.toloc = toloc;
    	}
		@Override
		public void run() {
			Location loc = player.getLocation();
			if((loc = getTeleportToLoc(player, loc, toloc, location)) == null){
				if(dynamic_teleporter.containsKey(player.getName())){
					player.playSound(player.getLocation(), Sound.ANVIL_USE, 1, 1);
		        	MyMaid.TitleSender.setTime_second(player, 2, 5, 2);
					MyMaid.TitleSender.sendTitle(player, "", ChatColor.AQUA +  "You have been teleported to " + location + "!");
					Bukkit.broadcastMessage(ChatColor.GRAY + "[" + player.getName() + ": " + player.getName() + " は " + location + " にワープしました]");
					dynamic_teleporter.get(player.getName()).cancel();
					dynamic_teleporter.remove(player.getName());
				}
			}else{
				cancel();
			}

		}
	}
}
