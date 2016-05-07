package xyz.jaoafa.mymaid.Command;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Dynmap_Teleporter implements CommandExecutor {
	JavaPlugin plugin;
	public Dynmap_Teleporter(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/* onCommand dt
	 * DynmapのMarkerにテレポートします
	 * /dt <Player> [markername] */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
	    URLCodec codec = new URLCodec();
		if(args.length == 0){
			sender.sendMessage("[DT] " + ChatColor.GREEN + "このコマンドは1つまたは2つの引数が必要です。");
			return false;
		}else if(args.length == 1){
			// コマンド実行者がプレイヤーかどうか
			if (!(sender instanceof Player)) {
				sender.sendMessage("[DT] " + ChatColor.GREEN + "このコマンドはゲーム内から実行してください。");
				Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
				return true;
			}
			String location = args[0];
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
					sender.sendMessage("[DT] " + ChatColor.GREEN + "----- Location List -----");
					data = readString(in);//1行読み取り
					while (data != null) {//読み取りが成功していれば
						data = codec.decode(data, StandardCharsets.UTF_8.name());
						sender.sendMessage("[DT] " + ChatColor.GREEN + data);
						data = readString(in);//次を読み込む
					}
					sender.sendMessage("[DT] " + ChatColor.GREEN + "-------------------------");
					return true;
				}else{
					data = readString(in);
					if(data.equalsIgnoreCase("NOLOCATION")){
						sender.sendMessage("[DT] " + ChatColor.GREEN + "その名前の場所は登録されていません。/dt listで場所を確認してください。");
						return true;
					}else{
						String[] datas = data.split(",", 0);
						String x = datas[0];
						String y = datas[1];
						String z = datas[2];
						String world = datas[3];
						Bukkit.dispatchCommand(sender, "mvtp " + world);
						Bukkit.dispatchCommand(sender, "tp " + x + " " + y + " " + z);
						return true;
					}
				}

			}catch(Exception e){
				//例外処理が発生したら、表示する
				System.out.println(e);
				sender.sendMessage("エラーが発生しました。詳しくはサーバーログを確認してください。");
			}
		}else if(args.length == 2){
			String p;
			p = args[0];
			for(Player player: Bukkit.getServer().getOnlinePlayers()) {
				if(player.getName().equalsIgnoreCase(p)) {
					if(Bukkit.dispatchCommand(player, "dt " + args[1])){
						sender.sendMessage("[DT] " + ChatColor.GREEN + "正常に処理を実行しました。");
					}else{
						sender.sendMessage("[DT] " + ChatColor.GREEN + "エラーが発生しました。");
					}
					return true;
				}
			}
			sender.sendMessage("[DT] " + ChatColor.GREEN + "ユーザーが見つかりません");
			return true;
		}
		return false;
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
}
