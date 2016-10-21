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
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Dynmap_Compass implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public Dynmap_Compass(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String, String> dcdata = new HashMap<String, String>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		URLCodec codec = new URLCodec();
		if(args.length >= 2){
			if(args[0].equalsIgnoreCase("set")){
				if (!(sender instanceof Player)) {
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
					return true;
				}
				Player player = (Player) sender;
				int c = 1;
				String text = "";
				while(args.length > c){
					text += args[c];
					if(args.length != (c+1)){
						text+=" ";
					}
					c++;
				}
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

					data = readString(in);
					if(data.equalsIgnoreCase("NOLOCATION")){
						Method.SendMessage(sender, cmd, "その名前の場所は登録されていません。");
						return true;
					}else{
						String[] datas = data.split(",", 0);
						String x = datas[0];
						String y = datas[1];
						String z = datas[2];
						String world = datas[3];
						location = codec.decode(location, StandardCharsets.UTF_8.name());

						Location loc = new Location(Bukkit.getServer().getWorld(world), Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
						loc.add(0.5f,0f,0.5f);
						dcdata.put(player.getName(), location);
						player.setCompassTarget(loc);
						Method.SendMessage(sender, cmd, "コンパスの方向をDynmapのマーカー地点「" + location + "」にセットしました。");
						Method.SendMessage(sender, cmd, "リセットするには/dc clearを実行してください。");
						return true;
					}

				}catch(Exception e){
					//例外処理が発生したら、表示する
					System.out.println(e);
					Method.SendMessage(sender, cmd, "エラーが発生しました。詳しくはサーバーログを確認してください。");
				}
				return true;
			}else{
				Method.SendMessage(sender, cmd, "--- Dynmap Compass Help ---");
				Method.SendMessage(sender, cmd, "/dc set <DynmapMarkerName>: コンパスの方向をDynmapのマーカー地点に設定します。");
				Method.SendMessage(sender, cmd, "/dc show: コンパスの方向が設定されているDynmapのマーカー地点名を表示します。");
				Method.SendMessage(sender, cmd, "/dc clear: コンパスの方向をリセットします。");
				return true;
			}
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("show")){
				if (!(sender instanceof Player)) {
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}
				Player player = (Player) sender;
				if(!dcdata.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "DCで指定された方向設定は行われていません。");
					return true;
				}
				String location = dcdata.get(player.getName());
				Method.SendMessage(sender, cmd, "コンパスの方向は、Dynmapのマーカー地点「" + location + "」にセットされています。");
				return true;
			}else if(args[0].equalsIgnoreCase("clear")){
				if (!(sender instanceof Player)) {
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}
				Player player = (Player) sender;
				if(!dcdata.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "DCで指定された方向設定は行われていません。");
					return true;
				}
				dcdata.remove(player.getName());
				player.setCompassTarget(player.getWorld().getSpawnLocation());
				Method.SendMessage(sender, cmd, "コンパスの方向をリセットしました。");
				return true;
			}else{
				Method.SendMessage(sender, cmd, "--- Dynmap Compass Help ---");
				Method.SendMessage(sender, cmd, "/dc set <DynmapMarkerName>: コンパスの方向をDynmapのマーカー地点に設定します。");
				Method.SendMessage(sender, cmd, "/dc show: コンパスの方向が設定されているDynmapのマーカー地点名を表示します。");
				Method.SendMessage(sender, cmd, "/dc clear: コンパスの方向をリセットします。");
				return true;
			}
		}else{
			Method.SendMessage(sender, cmd, "--- Dynmap Compass Help ---");
			Method.SendMessage(sender, cmd, "/dc set <DynmapMarkerName>: コンパスの方向をDynmapのマーカー地点に設定します。");
			Method.SendMessage(sender, cmd, "/dc show: コンパスの方向が設定されているDynmapのマーカー地点名を表示します。");
			Method.SendMessage(sender, cmd, "/dc clear: コンパスの方向をリセットします。");
			return true;
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
		String[] datas;
	    URLCodec codec = new URLCodec();
	    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
			if (args.length == 2) {
	            if (args[0].equalsIgnoreCase("set") && args[1].length() == 0) { // /testまで
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
	            }
			}else if (args.length == 3) {
				if (args[0].equalsIgnoreCase("set") && args[1].length() != 0) { // /testまで
		            	try{
							URL url=new URL("http://toma.webcrow.jp/jaoget.php?tab=" + args[1]);
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
		          }
	        }
	        //JavaPlugin#onTabComplete()を呼び出す
	        return plugin.onTabComplete(sender, cmd, alias, args);
		}
}
