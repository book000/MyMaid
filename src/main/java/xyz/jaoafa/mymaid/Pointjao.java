package xyz.jaoafa.mymaid;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * ポイントシステム
 * @author mine_book000
*/
public class Pointjao {
	JavaPlugin plugin;
	public Pointjao(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	//ポイント情報
	public static Map<String, Integer> jao = new HashMap<String, Integer>();

	/*
	 	int use = 0;
		if(!Pointjao.hasjao(player, use)){
		 	 Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
		 	 return true;
		}
		Pointjao.usejao(player, use);
	 */

	/**
	 * プレイヤーのUUIDをStringで取得
	 * @param player 取得するプレイヤー
	 * @return Stringに変換したUUID
	 * @author mine_book000
	*/
	private static String getuuidtostring(Player player){
		return ""+player.getUniqueId();
  	}
	/**
	 * プレイヤーからポイントを取得
	 * @param player 取得するプレイヤー
	 * @return 取得したポイント
	 * @author mine_book000
	*/
	public static int getjao(Player player){
		if(!jao.containsKey(getuuidtostring(player))){
			jao.put(getuuidtostring(player), 0);
		}
		int now = jao.get(getuuidtostring(player));
		return now;
  	}
	/**
	 * UUIDからポイントを取得
	 * @param uuid 取得するUUID
	 * @return 取得したポイント
	 * @author mine_book000
	*/
	public static int getjao(String uuid){
		if(!jao.containsKey(uuid)){
			jao.put(uuid, 0);
		}
		int now = jao.get(uuid);
		return now;
  	}
	/**
	 * プレイヤーが指定したポイントを所持しているかどうか確認
	 * @param player 確認するプレイヤー
	 * @param hasjao 所持確認するポイント数
	 * @return 所持していたらtrue、していなかったらfalse
	 * @author mine_book000
	*/
	public static boolean hasjao(Player player, int hasjao){
		int now = getjao(player);
		if(now >= hasjao){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * プレイヤーからポイントを減算
	 * @param player プレイヤー
	 * @param usejao 減算するポイント
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean usejao(Player player, int usejao, String reason){
		int now = getjao(player);
		if(!hasjao(player, usejao)){
			return false;
		}
		int newjao = now - usejao;
		jao.put(getuuidtostring(player), newjao);
		MyMaid.conf.set("jao",Pointjao.jao);
		player.sendMessage("[POINT] " + ChatColor.GREEN + usejao + "ポイントを使用しました。現在" + newjao + "ポイント持っています。");
		try {
			String type = "Use";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			UpdateQuery("INSERT INTO jaopoint (player, uuid, type, point, reason, nowpoint, description, date) VALUES ('" + player.getName() + "', '" + player.getUniqueId().toString() +"', '" +  type + "', " + usejao + ", '" + reason + "', " + newjao + ", 'プラグイン', '" + sdf.format(new Date()) + "');");
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[POINT] " + ChatColor.GREEN + "明細の書き込みに失敗しました。開発者に連絡を行ってください。");
		}
		return true;
	}
	/**
	 * プレイヤーからポイントを減算
	 * @param player プレイヤー
	 * @param addjao 減算するポイント
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean addjao(Player player, int addjao, String reason){
		int now = getjao(player);
		int newjao = now + addjao;
		jao.put(getuuidtostring(player), newjao);
		MyMaid.conf.set("jao",Pointjao.jao);
		player.sendMessage("[POINT] " + ChatColor.GREEN + addjao + "ポイントを追加しました。現在" + newjao + "ポイント持っています。");
		try {
			String type = "Add";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			UpdateQuery("INSERT INTO jaopoint (player, uuid, type, point, reason, nowpoint, description, date) VALUES ('" + player.getName() + "', '" + player.getUniqueId().toString() +"', '" +  type + "', " + addjao + ", '" + reason + "', " + newjao + ", 'プラグイン', '" + sdf.format(new Date()) + "');");
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[POINT] " + ChatColor.GREEN + "明細の書き込みに失敗しました。開発者に連絡を行ってください。");
		}
		return true;
	}
	/**
	 * UUIDにポイントを追加
	 * @param uuid UUID
	 * @param addjao 追加するポイント
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean addjao(String uuid, int addjao, String reason){
		int now = getjao(uuid);
		int newjao = now + addjao;
		jao.put(uuid, newjao);
		MyMaid.conf.set("jao",Pointjao.jao);
		try {
			String type = "Add";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			UpdateQuery("INSERT INTO jaopoint (player, uuid, type, point, reason, nowpoint, description, date) VALUES ('', '" + uuid +"', '" +  type + "', " + addjao + ", '" + reason + "', " + newjao + ", 'プラグイン', '" + sdf.format(new Date()) + "');");
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * MySQLにアップデートクエリ送信
	 * @param query
	 * @return ?
	 * @author mine_book000
	 * @return
	 */
	private static int UpdateQuery(String query) throws Exception{
		Statement statement = null;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				throw new Exception("ClassNotFound/SQLException");
			}
		} catch (SQLException e) {
			throw new Exception("SQLException");
		}
		statement = MySQL.check(statement);
		return statement.executeUpdate(query);
	}
}
