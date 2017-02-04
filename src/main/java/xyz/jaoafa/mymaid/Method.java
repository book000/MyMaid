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

/**
 * 主要メゾット
 * @author mine_book000
 */
public class Method {
	JavaPlugin plugin;
	public Method(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	/**
	 * jMSプラグインAPIアクセス
	 * @param filename ファイル名(拡張子無し)
	 * @param arg 引数(最初の?無し)
	 * @return 取得した情報
	 * @author mine_book000
	 */
	public static String url_jaoplugin(String filename, String arg){
		return url_access("http://nubesco.jaoafa.com/plugin/" + filename + ".php?" + arg);
	}
	/**
	 * ネットワークGETアクセス
	 * @param address 取得したいURL
	 * @return 取得した情報
	 * @author mine_book000
	 */
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
	/**
	 * ネットワークPOSTアクセス
	 * @param address 取得したいURL
	 * @param text 送信するテキスト
	 * @return 取得した情報
	 * @author mine_book000
	 */
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
	/**
	 * InputStreamから1行読む
	 * @param in 読み込み元のInputStream
	 * @return 読み込んだテキスト
	 * @author mine_book000
	 */
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
	/**
	 * チャット偽造
	 * @param player 喋らせるプレイヤー
	 * @param text 喋らせる内容
	 * @return 偽造したテキスト
	 * @author mine_book000
	 */
	public static String chatmaker(String player, String text){
		Date Date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String date = sdf.format(Date);
		String send = ChatColor.GRAY + "["+ date + "]" + ChatColor.WHITE + player +  ": " + text;
		return send;
	}
	/**
	 * 時間差を求める
	 * @param startTime 開始時間
	 * @param endTime 終了時間
	 * @return フォーマットした時間差テキスト
	 * @author mine_book000
	 */
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
	/**
	 * 「[COMMAND] Text」に変換した上でsenderに送信する
	 * @param sender 送信先
	 * @param cmd コマンド情報
	 * @param text 送信テキスト
	 * @author mine_book000
	 */
	public static void SendMessage(CommandSender sender, Command cmd, String text) {
		sender.sendMessage("[" + cmd.getName().toUpperCase() +"] " + ChatColor.GREEN + text);
	}
	/**
	 * 指定したプレイヤーのPermissionsExにおけるグループに入っているかどうか確認する
	 * @param player 取得するプレイヤー
	 * @param group PermissionsExのグループ名
	 * @return 入っていたらtrue、入っていなかったらfalse
	 * @author mine_book000
	 */
	public static boolean CheckQroup(Player player, String group) {
		if(PermissionsEx.getUser(player).inGroup(group)){
			return true;
		}
		return false;
	}
	/**
	 * UUIDかどうか？
	 * @param uuid UUIDかどうかチェックする文字列
	 * @return UUIDだったらtrue、そうでなかったらfalse
	 * @see https://b.0218.jp/20140424133801.html
	 */
	public static Boolean isUUID(String uuid) {
		boolean isUUID = false;

		if (uuid.length() == 36) {
		    String[] parts = uuid.split("-");

		    if (parts.length == 5) {
		        if ((parts[0].length() == 8) &&
		            (parts[1].length() == 4) &&
		            (parts[2].length() == 4) &&
		            (parts[3].length() == 4) &&
		            (parts[4].length() == 12))
		        {
		            isUUID = true;
		        }
		    }
		}

		return isUUID;
	}

}
