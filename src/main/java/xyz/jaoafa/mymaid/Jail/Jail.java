package xyz.jaoafa.mymaid.Jail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Discord.Discord;

public class Jail {
	private static Set<String> Jail = new HashSet<String>();
	private static Map<String, Boolean> block = new HashMap<String, Boolean>();
	private static Map<String, Boolean> area = new HashMap<String, Boolean>();
	private static Map<String, Boolean> lasttext = new HashMap<String, Boolean>();

	/**
	 * Jailにプレイヤーを追加
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 追加したプレイヤー
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean JailAdd(Player player, CommandSender banned_by){
		if(Jail.contains(player.getName())){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーはすでに牢獄にいるため追加できません。");
			return false;
		}
		Jail.add(player.getName());
		block.put(player.getName(), false); // 設置破壊不可
		area.put(player.getName(), false); // 範囲外移動
		lasttext.put(player.getName(), false); // まだ遺言を残してない

		World Jao_Afa = Bukkit.getServer().getWorld("Jao_Afa");
		Location minami = new Location(Jao_Afa, 1767, 70, 1767);
		player.teleport(minami); // テレポート

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "やあ。" + player.getName() + "クン。どうも君はなにかをして南の楽園に来てしまったみたいなんだ");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "なにをしてしまったのは知らないけどなにかをしたからここに来たんだと思うんだ。");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "ちょっとやったことを反省してみるのもいいかもしれないね");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "あ、そうだ、今の君に人権はないよ。");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "あと、「/testment <LastText>」で遺言を残せるよ！");

		Method.url_jaoplugin("eban", "p=" + player.getName() +"&u=" + player.getUniqueId().toString() +"&b=" + banned_by.getName() + "&r=&nosend");
		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を牢獄リストに追加しました。");
		JailBackupSaveTxt(player.getName(), JailType.ADD, banned_by.getName(), "");
		Discord.send("223582668132974594", "***EBan[追加]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によってJailリストに追加されました。");
		return true;
	}

	/**
	 * Jailにプレイヤーを追加
	 * @param cmd コマンド情報
	 * @param player オフラインのプレイヤー
	 * @param banned_by 追加したプレイヤー
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean JailAdd(OfflinePlayer player, CommandSender banned_by){
		if(Jail.contains(player.getName())){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーはすでに牢獄にいるため追加できません。");
			return false;
		}
		Jail.add(player.getName());
		block.put(player.getName(), false); // 設置破壊不可
		area.put(player.getName(), false); // 範囲外移動
		lasttext.put(player.getName(), false); // まだ遺言を残してない

		Method.url_jaoplugin("eban", "p=" + player.getName() +"&u=" + player.getUniqueId().toString() +"&b=" + banned_by.getName() + "&r=&nosend");
		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を牢獄リストに追加しました。");
		JailBackupSaveTxt(player.getName(), JailType.ADD, banned_by.getName(), "");
		Discord.send("223582668132974594", "***EBan[追加]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によってJailリストに追加されました。");
		return true;
	}

	/**
	 * Jailに理由つきでプレイヤーを追加
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 追加したプレイヤー
	 * @param reason 理由
	 * @param InvRemove インベントリを削除するか
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean JailAdd(Player player, CommandSender banned_by, String reason, boolean InvRemove){
		if(Jail.contains(player.getName())){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーはすでに牢獄にいるため追加できません。");
			return false;
		}
		Jail.add(player.getName());
		block.put(player.getName(), false); // 設置破壊不可
		area.put(player.getName(), false); // 範囲外移動
		lasttext.put(player.getName(), false); // まだ遺言を残してない

		if(InvRemove){
			player.getInventory().clear();
			player.getEnderChest().clear();
		}

		World Jao_Afa = Bukkit.getServer().getWorld("Jao_Afa");
		Location minami = new Location(Jao_Afa, 1767, 70, 1767);
		player.teleport(minami); // テレポート

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "やあ。" + player.getName() + "クン。どうも君はなにかをして南の楽園に来てしまったみたいなんだ");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "話を聞けば、「" + reason + "」という理由でここにきたみたいだね。");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "ちょっとやったことを反省してみるのもいいかもしれないね");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "あ、そうだ、今の君に人権はないよ。");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "あと、「/testment <LastText>」で遺言を残せるよ！");

		Method.url_jaoplugin("eban", "p=" + player.getName() +"&u=" + player.getUniqueId().toString() +"&b=" + banned_by.getName() + "&r=" + reason + "&nosend");
		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を「" + reason + "」という理由で牢獄リストに追加しました。");
		Discord.send("223582668132974594", "***EBan[追加]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によって「" + reason + "」という理由でJailリストに追加されました。");
		JailBackupSaveTxt(player.getName(), JailType.ADD, banned_by.getName(), reason);
		return true;
	}

	/**
	 * Jailに理由つきでプレイヤーを追加
	 * @param cmd コマンド情報
	 * @param player オフラインのプレイヤー
	 * @param banned_by 追加したプレイヤー
	 * @param reason 理由
	 * @param InvRemove インベントリを削除するか
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean JailAdd(OfflinePlayer player, CommandSender banned_by, String reason){
		if(Jail.contains(player.getName())){
			// 既に牢獄にいるので無理
			banned_by.sendMessage("[JAIL] " + ChatColor.GREEN + "指定されたプレイヤーはすでに牢獄にいるため追加できません。");
			return false;
		}
		Jail.add(player.getName());
		block.put(player.getName(), false); // 設置破壊不可
		area.put(player.getName(), false); // 範囲外移動
		lasttext.put(player.getName(), false); // まだ遺言を残してない

		Method.url_jaoplugin("eban", "p=" + player.getName() +"&u=" + player.getUniqueId().toString() +"&b=" + banned_by.getName() + "&r=" + reason + "&nosend");
		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を「" + reason + "」という理由で牢獄リストに追加しました。");
		Discord.send("223582668132974594", "***EBan[追加]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によって「" + reason + "」という理由でJailリストに追加されました。");
		JailBackupSaveTxt(player.getName(), JailType.ADD, banned_by.getName(), reason);
		return true;
	}

	/**
	 * Jailに理由つきでプレイヤーを追加
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 追加したプレイヤー
	 * @param reason 理由
	 * @param InvRemove インベントリを削除するか
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean JailAdd(Player player, OfflinePlayer banned_by, String reason, boolean InvRemove){
		if(Jail.contains(player.getName())){
			// 既に牢獄にいるので無理
			return false;
		}
		Jail.add(player.getName());
		block.put(player.getName(), false); // 設置破壊不可
		area.put(player.getName(), false); // 範囲外移動
		lasttext.put(player.getName(), false); // まだ遺言を残してない

		if(InvRemove){
			player.getInventory().clear();
			player.getEnderChest().clear();
		}

		World Jao_Afa = Bukkit.getServer().getWorld("Jao_Afa");
		Location minami = new Location(Jao_Afa, 1767, 70, 1767);
		player.teleport(minami); // テレポート

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "やあ。" + player.getName() + "クン。どうも君はなにかをして南の楽園に来てしまったみたいなんだ");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "話を聞けば、「" + reason + "」という理由でここにきたみたいだね。");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "ちょっとやったことを反省してみるのもいいかもしれないね");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "あ、そうだ、今の君に人権はないよ。");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "あと、「/testment <LastText>」で遺言を残せるよ！");

		Method.url_jaoplugin("eban", "p=" + player.getName() +"&u=" + player.getUniqueId().toString() +"&b=" + banned_by.getName() + "&r=" + reason + "&nosend");
		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を「" + reason + "」という理由で牢獄リストに追加しました。");
		Discord.send("223582668132974594", "***EBan[追加]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() +"」によって「" + reason + "」という理由でJailリストに追加されました。");
		if(banned_by.getName().equalsIgnoreCase("jaotan")){
			Discord.Filesend("223582668132974594", MyMaid.getJavaPlugin().getDataFolder() + File.separator + "jailadd.png");
		}
		JailBackupSaveTxt(player.getName(), JailType.ADD, banned_by.getName(), reason);
		return true;
	}

	/**
	 * Jailにいるプレイヤーリストを送信
	 * @param sender コマンド実行者
	 * @param cmd コマンド情報
	 * @author mine_book000
	*/
	public static void SendList(CommandSender sender, Command cmd){
		Method.SendMessage(sender, cmd, "------ 牢獄リスト ------");
		for(String player : Jail){
			String text = "";
			if(area.containsKey(player)){
				if(area.get(player)){
					text = "過度移動許可";
				}else{
					text = "過度移動不許可";
				}
			}else{
				text = "情報無し";
			}
			text += " ";
			if(block.containsKey(player)){
				if(block.get(player)){
					text += "設置破壊許可";
				}else{
					text += "設置破壊不許可";
				}
			}else{
				text += "情報無し";
			}
			Method.SendMessage(sender, cmd, player + " " + text);
		}
		Method.SendMessage(sender, cmd, "------------------------");
	}

	/**
	 * 遺言を残す
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param LastText 遺言
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean JailLastText(Command cmd, Player player, String LastText){
		if(!Jail.contains(player.getName())){
			// 既に牢獄にいないので無理
			Method.SendMessage(player, cmd, "あなたはすでに牢獄にいないため遺言を残せません。");
			return false;
		}
		if(lasttext.containsKey(player.getName())){
			if(lasttext.get(player.getName())){
				// 既に残してる
				Method.SendMessage(player, cmd, "あなたはすでに遺言を残しています。");
				return false;
			}
		}
		lasttext.put(player.getName(), true);
		Method.url_jaoplugin("eban", "p=" + player.getName() + "&lasttext=" + LastText.replaceAll(" ", "_"));
		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」が遺言を残しました。遺言:「" + LastText +"」");
		Discord.send("223582668132974594", "***EBan[遺言]***: プレイヤー「" + player.getName() +"」が「" + LastText + "」という遺言を残しました。");
		JailBackupSaveTxt(player.getName(), JailType.LASTTEXT, "", LastText);
		return true;
	}

	/**
	 * 範囲外に移動できるかどうかを設定する
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 実行者情報
	 * @param after 変更後の設定
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean JailArea(Command cmd, Player player, CommandSender banned_by, Boolean after){
		if(!Jail.contains(player.getName())){
			// 既に牢獄にいないので無理
			Method.SendMessage(banned_by, cmd, "指定されたプレイヤーはすでに牢獄にいないため設定できません。");
			return false;
		}
		area.put(player.getName(), after);
		if(after){
			Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を範囲外に移動できるよう設定しました。");
			Discord.send("223582668132974594", "***EBan[範囲外移動]***: プレイヤー「" + player.getName() +"」が範囲外に移動できるよう設定しました。");
			JailBackupSaveTxt(player.getName(), JailType.AREATRUE, banned_by.getName(), "");
		}else{
			Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を範囲外に移動できないよう設定しました。");
			Discord.send("223582668132974594", "***EBan[範囲外移動]***: プレイヤー「" + player.getName() +"」が範囲外に移動できないよう設定しました。");
			JailBackupSaveTxt(player.getName(), JailType.AREAFALSE, banned_by.getName(), "");
		}
		return true;
	}

	/**
	 * ブロックを設置破壊できるかどうかを設定する
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 実行者情報
	 * @param after 変更後の設定
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean JailBlock(Command cmd, Player player, CommandSender banned_by, Boolean after){
		if(!Jail.contains(player.getName())){
			// 既に牢獄にいないので無理
			Method.SendMessage(banned_by, cmd, "指定されたプレイヤーはすでに牢獄にいないため設定できません。");
			return false;
		}
		block.put(player.getName(), after);
		if(after){
			Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」がブロックを設置破壊できるよう設定しました。");
			Discord.send("223582668132974594", "***EBan[ブロック編集]***: プレイヤー「" + player.getName() +"」がブロックを設置破壊できるよう設定しました。");
			JailBackupSaveTxt(player.getName(), JailType.BLOCKTRUE, banned_by.getName(), "");
		}else{
			Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」がブロックを設置破壊できるよう設定しました。");
			Discord.send("223582668132974594", "***EBan[ブロック編集]***: プレイヤー「" + player.getName() +"」がブロックを設置破壊できるよう設定しました。");
			JailBackupSaveTxt(player.getName(), JailType.BLOCKFALSE, banned_by.getName(), "");
		}
		return true;
	}

	/**
	 * Jailからプレイヤーを削除
	 * @param cmd コマンド情報
	 * @param player プレイヤー
	 * @param banned_by 実行者情報
	 * @return 実行できたかどうか
	 * @author mine_book000
	*/
	public static boolean JailRemove(Command cmd, Player player, CommandSender banned_by){
		if(!Jail.contains(player.getName())){
			// 既に牢獄にいないので無理
			Method.SendMessage(banned_by, cmd, "指定されたプレイヤーはすでに牢獄にいないため削除できません。");
			return false;
		}
		Jail.remove(player.getName());
		block.remove(player.getName());
		area.remove(player.getName());

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		player.sendMessage(ChatColor.GRAY + "["+ sdf.format(new Date()) + "]" + ChatColor.GOLD + "■jaotan" + ChatColor.WHITE +  ": " + "じゃあな…！");
		Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を牢獄リストから削除しました。");
		JailBackupSaveTxt(player.getName(), JailType.REMOVE, banned_by.getName(), "");
		Discord.send("223582668132974594", "***EBan[削除]***: プレイヤー「" + player.getName() +"」が「" + banned_by.getName() + "」によってJailリストから削除されました");
		return true;
	}

	/**
	 * Jailリストにいるかどうか調べる
	 * @param player プレイヤー
	 * @return 居たらtrue、居なかったらfalse
	 * @author mine_book000
	*/
	public static boolean isJail(Player player){
		return Jail.contains(player.getName());
	}

	/**
	 * 南の楽園外に出れるかどうか調べる
	 * @param player プレイヤー
	 * @return 出れるならtrue、出れなければfalse
	 * @author mine_book000
	*/
	public static boolean isJailArea(Player player){
		if(area.containsKey(player.getName())){
			if(area.get(player.getName())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	/**
	 * ブロックを設置破壊できるかどうか調べる
	 * @param player プレイヤー
	 * @return できるならtrue、できないならfalse
	 * @author mine_book000
	*/
	public static boolean isJailBlock(Player player){
		if(block.containsKey(player.getName())){
			if(block.get(player.getName())){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	/**
	 * Jail情報をセーブする。
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws Exception IOExceptionの発生時に発生
	*/
	@SuppressWarnings("unchecked")
	public static boolean SaveJailData() throws Exception{
		JSONArray JailJSON = new JSONArray();
		for(String player : Jail){
			JailJSON.add(player);
		}

		JSONObject BlockJSON = new JSONObject();
		for(Entry<String, Boolean> one : block.entrySet()) {
			BlockJSON.put(one.getKey(), one.getValue());
		}

		JSONObject AreaJSON = new JSONObject();
		for(Entry<String, Boolean> one : area.entrySet()) {
			AreaJSON.put(one.getKey(), one.getValue());
		}

		JSONObject LastTextJSON = new JSONObject();
		for(Entry<String, Boolean> one : lasttext.entrySet()) {
			LastTextJSON.put(one.getKey(), one.getValue());
		}

		JSONObject ALLJSON = new JSONObject();
		ALLJSON.put("Jail", JailJSON);
		ALLJSON.put("Block", BlockJSON);
		ALLJSON.put("Area", AreaJSON);
		ALLJSON.put("LastText", LastTextJSON);

		try{
			JavaPlugin plugin = MyMaid.getJavaPlugin();
	    	File file = new File(plugin.getDataFolder() + File.separator + "jail.json");
	    	FileWriter filewriter = new FileWriter(file);

	    	filewriter.write(ALLJSON.toJSONString());

	    	filewriter.close();
	    }catch(IOException e){
	    	BugReport.report(e);
	    	throw new Exception("IOException");
	    }
		return true;
	}

	/**
	 * Jail情報をロードする。
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws Exception 何かしらのExceptionが発生したときに発生(FileNotFoundException, IOException)
	*/
	@SuppressWarnings("unchecked")
	public static boolean LoadJailData() throws Exception{
		JSONParser parser = new JSONParser();
		String json = "";
		try{
			JavaPlugin plugin = MyMaid.getJavaPlugin();
	    	File file = new File(plugin.getDataFolder() + File.separator + "jail.json");
			BufferedReader br = new BufferedReader(new FileReader(file));

			String separator = System.getProperty("line.separator");

			String str;
			while((str = br.readLine()) != null){
				json += str + separator;
			}
			br.close();
		}catch(FileNotFoundException e1){
			BugReport.report(e1);
			throw new FileNotFoundException(e1.getMessage());
		}catch(IOException e1){
			BugReport.report(e1);
			throw new IOException(e1.getMessage());
		}
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(json);
		} catch (ParseException e1) {
			obj = new JSONObject();
		}
		JSONArray JailJSON;
		if(obj.containsKey("Jail")){
			JailJSON = (JSONArray) obj.get("Jail");
			for (int i = 0; i < JailJSON.size(); i++){
				String player = (String) JailJSON.get(i);
				Jail.add(player);
			}
		}
		if(obj.containsKey("Block")){
			for(Entry<String, Boolean> one : (Set<Map.Entry<String, Boolean>>) ((JSONObject) obj.get("Block")).entrySet()){
				block.put(one.getKey(), one.getValue());
			}
		}
		if(obj.containsKey("Area")){
			for(Entry<String, Boolean> one : (Set<Map.Entry<String, Boolean>>) ((JSONObject) obj.get("Area")).entrySet()){
				area.put(one.getKey(), one.getValue());
			}
		}
		if(obj.containsKey("LastText")){
			for(Entry<String, Boolean> one : (Set<Map.Entry<String, Boolean>>) ((JSONObject) obj.get("LastText")).entrySet()){
				lasttext.put(one.getKey(), one.getValue());
			}
		}
		return true;
	}

	public enum JailType {
		ADD("追加"),
		REMOVE("削除"),
		LASTTEXT("遺言"),
		AREATRUE("移動可"),
		AREAFALSE("移動不可"),
		BLOCKTRUE("ブロック設置破壊可"),
		BLOCKFALSE("ブロック設置破壊不可");

		private String name;
	    JailType(String name) {
	        this.name = name;
	    }
	}
	private static void JailBackupSaveTxt(String player, JailType type, String by, String reason){
		try{
			File file = new File(MyMaid.getInstance().getDataFolder() + File.separator + "jaillog.txt");

			if(file.exists()){
				file.createNewFile();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String text = "["+ sdf.format(new Date()) + "|" + type.name + "] ";
			StringBuffer TextBuf = new StringBuffer();
			TextBuf.append(text);

			if(type == JailType.ADD){
				TextBuf.append(player + "が" + by + "によって追加されました。(理由: " + reason + ")");
			}else if(type == JailType.REMOVE){
				TextBuf.append(player + "が" + by + "によって解除されました。");
			}else if(type == JailType.LASTTEXT){
				TextBuf.append(player + "が遺言を記載しました。(" + reason +")");
			}else if(type == JailType.AREATRUE){
				TextBuf.append(by + "が" + player + "の牢獄外移動を許可しました。");
			}else if(type == JailType.AREAFALSE){
				TextBuf.append(by + "が" + player + "の牢獄外移動を禁止しました。");
			}else if(type == JailType.BLOCKTRUE){
				TextBuf.append(by + "が" + player + "のブロック設置破壊を許可しました。");
			}else if(type == JailType.BLOCKFALSE){
				TextBuf.append(by + "が" + player + "のブロック設置破壊を禁止しました。");
			}

			text = TextBuf.toString();

			FileWriter filewriter = new FileWriter(file, true);

			filewriter.write(text + System.getProperty("line.separator"));

			filewriter.close();
		}catch(IOException e){
			BugReport.report(e);
		}
	}
}
