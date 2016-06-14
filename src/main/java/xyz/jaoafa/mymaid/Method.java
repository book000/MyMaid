package xyz.jaoafa.mymaid;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Method {
	JavaPlugin plugin;
	public Method(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static String url_jaoplugin(String filename, String arg){
		return url_access("http://toma.webcrow.jp/jao.php?file=" + filename + ".php&" + arg);
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
	public static String chatmaker(String player, String text){
  		Date Date = new Date();;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String date = sdf.format(Date);
		String send = ChatColor.GRAY + "["+ date + "]" + ChatColor.WHITE + player +  ": " + text;
		return send;
  	}
	public static String format(long startTime, long endTime) {
        Calendar start = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        Calendar end = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        Calendar result = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
        start.setTimeInMillis(startTime);
        end.setTimeInMillis(endTime);
        start.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        end.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        result.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        long sa = end.getTimeInMillis() - start.getTimeInMillis() - result.getTimeZone().getRawOffset();
        result.setTimeInMillis(sa);
        SimpleDateFormat sdf = new SimpleDateFormat("ss.SSSSS");
        return sdf.format(result.getTime());
    }
}
