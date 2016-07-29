package xyz.jaoafa.mymaid.Command;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.net.URLCodec;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Ip_To_Host implements CommandExecutor {
	JavaPlugin plugin;
	public Ip_To_Host(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/* onCommand chat
	 * 話者を偽装します。
	 * /chat [Player] [text...] */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){

    		if(args.length == 0){
    			Method.SendMessage(sender, cmd, "このコマンドは1つの引数が必要です。");
				return false;
    		}
    		String regex = "^([0-9]+)\\.([0-9]+)\\.([0-9]+)\\.([0-9]+)$"; //正規表現
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(args[0]);
			if (!m.find()){
				Method.SendMessage(sender, cmd, "エラーが発生しました。IPアドレスを入力してください");
				return true;
			}
			try{
				URL url=new URL("http://toma.webcrow.jp/jaoiphost.php?i=" + args[0]);
				// URL接続
				HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
				connect.setRequestMethod("GET");//プロトコルの設定
				InputStream in=connect.getInputStream();//ファイルを開く
				String data;//ネットから読んだデータを保管する変数を宣言
				data = readString(in);

				if(data == null){
					Method.SendMessage(sender, cmd, "エラーが発生しました。");
					return true;
				}
				if(data.equalsIgnoreCase("Err")){
					Method.SendMessage(sender, cmd, "エラーが発生しました。ホストが取得できなかったか、処理に失敗した可能性があります。");
					return true;
				}
				String[] datas = data.split("###", 0);
				URLCodec codec = new URLCodec();
				String country = codec.decode(datas[0], StandardCharsets.UTF_8.name());
				Method.SendMessage(sender, cmd, "IPアドレス:「" + args[0] + "」のホスト名は「" + datas[1] + "」(" + country + ")です。");
				return true;

			}catch(Exception e){
				//例外処理が発生したら、表示する
				System.out.println(e);
				Method.SendMessage(sender, cmd, "エラーが発生しました。詳しくはサーバーログを確認してください。");
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
}
