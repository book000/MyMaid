package xyz.jaoafa.mymaid;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Method {
	JavaPlugin plugin;
	public Method(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static String url_jaoplugin(String filename, String arg){
		return url_access("http://nubesco.jaoafa.com/plugin/" + filename + ".php?" + arg);
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
	public static String url_access_post(String address, String text){
		System.out.println("[MyMaid] URLConnect Start:"+address);

		final String TWO_HYPHEN = "--";
	    final String EOL = "\r\n";
	    final String BOURDARY = String.format("%x", new Random().hashCode());
	    final String CHARSET = "UTF-8";

		// 送信するコンテンツを成形する
        StringBuilder contentsBuilder = new StringBuilder();
        int iContentsLength = 0;

        contentsBuilder.append(String.format("%s%s%s", TWO_HYPHEN, BOURDARY, EOL));
        contentsBuilder.append(String.format("Content-Disposition: form-data; name=\"text\"%s", EOL));
        contentsBuilder.append(EOL);
        contentsBuilder.append(text);
        contentsBuilder.append(EOL);

        // コンテンツの長さを取得
        try {
            // StringBuilderを文字列に変化してからバイト長を取得しないと
            // 実際送ったサイズと異なる場合があり、コンテンツを正しく送信できなくなる
            iContentsLength = contentsBuilder.toString().getBytes(CHARSET).length;
            Bukkit.broadcastMessage(""+iContentsLength);
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

     // サーバへ接続する
        HttpURLConnection connection = null;
        DataOutputStream os = null;
        BufferedReader br = null;
        String result = "";
        try {
            URL url = new URL(address);

            connection = (HttpURLConnection)url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);

            // キャッシュを使用しない
            connection.setUseCaches(false);

            // HTTPストリーミングを有効にする
            connection.setChunkedStreamingMode(0);

            // リクエストヘッダを設定する
            // リクエストメソッドの設定
            connection.setRequestMethod("POST");

            // 持続接続を設定
            connection.setRequestProperty("Connection", "Keep-Alive");

            // ユーザエージェントの設定（必須ではない）
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (jaoafa.com)");

            // POSTデータの形式を設定
            connection.setRequestProperty("Content-Type", String.format("text/plain; boundary=%s", BOURDARY));
            // POSTデータの長さを設定
            connection.setRequestProperty("Content-Length", String.valueOf(iContentsLength));


            // データを送信する
            os = new DataOutputStream(connection.getOutputStream());
            os.writeBytes(contentsBuilder.toString());


            // レスポンスを受信する
            int iResponseCode = connection.getResponseCode();

            // 接続が確立したとき
            if (iResponseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder resultBuilder = new StringBuilder();
                String line = "";

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                // レスポンスの読み込み
                while ((line = br.readLine()) != null) {
                    resultBuilder.append(String.format("%s%s", line, EOL));
                }
                result = resultBuilder.toString();
            }
            // 接続が確立できなかったとき
            else {
                result = String.valueOf(iResponseCode);
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("[MyMaid] URLConnect Err:"+address);
			return "";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("[MyMaid] URLConnect Err:"+address);
			return "";
        } finally {
            // 開いたら閉じる
            try {
                if (br != null) br.close();
                if (os != null) {
                    os.flush();
                    os.close();
                }
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("[MyMaid] URLConnect Err:"+address);
    			return "";
            }
        }
        System.out.println("[MyMaid] URLConnect End:"+address);
		System.out.println(result);
		return result;
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
  		Date Date = new Date();
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
	public static void SendMessage(CommandSender sender, Command cmd, String text) {
		sender.sendMessage("[" + cmd.getName().toUpperCase() +"] " + ChatColor.GREEN + text);
	}
	public static boolean CheckQroup(Player player, String group) {
		if(PermissionsEx.getUser(player).inGroup(group)){
			return true;
		}
		return false;
	}

}
