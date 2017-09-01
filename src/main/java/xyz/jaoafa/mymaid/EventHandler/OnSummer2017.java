package xyz.jaoafa.mymaid.EventHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Pointjao;

public class OnSummer2017 implements Listener {
	JavaPlugin plugin;
	public OnSummer2017(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	// プレイヤー:オンライン時間(ミリ秒。1000で割る必要がある)
	static Map<String, Long> summer2017online = new HashMap<String, Long>();
	// プレイヤー:ワールドログイン時間
	static Map<String, Long> summer2017counter = new HashMap<String, Long>();
	// プレイヤー:ポイント配布が行われているか
	static Map<String, Boolean> summer2017distribution = new HashMap<String, Boolean>();

	@EventHandler
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		if(event.getFrom().getName().equals(player.getWorld().getName())){
			return;
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date August_start = format.parse("2017/08/01 00:00:00");
			Date August_end = format.parse("2017/08/31 23:59:59");

			Date September_start = format.parse("2017/09/01 00:00:00");
			Date September_end = format.parse("2017/09/30 23:59:59");

			if(Method.isPeriod(August_start, August_end)){
				// 8月はオンライン時間計測
				if(event.getFrom().getName().equalsIgnoreCase("Summer2017")){
					// Summer2017からどこか - カウンターを停止
					EndCounter(player);
				}else if(player.getWorld().getName().equalsIgnoreCase("Summer2017")){
					// どこかからSummer2017 - カウンターを開始
					StartCounter(player);
				}
			}else if(Method.isPeriod(September_start, September_end)){
				// 9月は配布
				if(player.getWorld().getName().equalsIgnoreCase("Summer2017")){
					PointDistribution(player);
				}
			}
		} catch (ParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event){
		Player player = event.getPlayer();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date August_start = format.parse("2017/08/01 00:00:00");
			Date August_end = format.parse("2017/08/31 23:59:59");

			Date September_start = format.parse("2017/09/01 00:00:00");
			Date September_end = format.parse("2017/09/30 23:59:59");

			if(Method.isPeriod(August_start, August_end)){
				// 8月はオンライン時間計測
				if(player.getWorld().getName().equalsIgnoreCase("Summer2017")){
					// Summer2017にJoin - カウンターを開始
					StartCounter(player);
				}
			}else if(Method.isPeriod(September_start, September_end)){
				// 9月は配布
				if(player.getWorld().getName().equalsIgnoreCase("Summer2017")){
					PointDistribution(player);
				}
			}
		} catch (ParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event){
		Player player = event.getPlayer();
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date August_start = format.parse("2017/08/01 00:00:00");
			Date August_end = format.parse("2017/08/31 23:59:59");

			Date September_start = format.parse("2017/09/01 00:00:00");
			Date September_end = format.parse("2017/09/30 23:59:59");

			if(Method.isPeriod(August_start, August_end)){
				// 8月はオンライン時間計測
				if(player.getWorld().getName().equalsIgnoreCase("Summer2017")){
					// Summer2017からQuit - カウンターを停止
					EndCounter(player);
				}
			}else if(Method.isPeriod(September_start, September_end)){
				// 9月は配布
				if(player.getWorld().getName().equalsIgnoreCase("Summer2017")){
					PointDistribution(player);
				}
			}
		} catch (ParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private void StartCounter(Player player){
		String uuid = player.getUniqueId().toString();
		if(!Loaded){
			try {
				LoadSummerData();
			} catch (Exception e) {
				return;
			}
		}
		if(!summer2017online.containsKey(uuid)){
			summer2017online.put(uuid, new Long(0));
		}
		summer2017counter.put(uuid, System.currentTimeMillis());
	}
	private void EndCounter(Player player){
		String uuid = player.getUniqueId().toString();
		if(!summer2017online.containsKey(uuid)){
			summer2017online.put(uuid, new Long(0));
		}
		if(!summer2017counter.containsKey(uuid)){
			return;
		}
		if(summer2017counter.get(uuid) == -1){
			return;
		}
		long join = summer2017counter.get(uuid);
		long now = System.currentTimeMillis();
		if(now > Long.parseLong("1504191599000")){
			// 2017/08/31 23:59:59より後
			now = Long.parseLong("1504191599000");
		}
		long time = now - join;
		long all = summer2017online.get(uuid);
		summer2017online.put(uuid, all + time);
		summer2017counter.put(uuid, new Long(-1));
		try {
			SaveSummerData();
		} catch (Exception e) {
			return;
		}
	}

	private boolean PointDistribution(Player player){
		String uuid = player.getUniqueId().toString();
		if(!summer2017online.containsKey(uuid)){
			return false;
		}
		if(summer2017distribution.containsKey(uuid)){
			if(summer2017distribution.get(uuid)){
				// trueなら配布済み
				return false;
			}
		}
		long onlineTimeMills = summer2017online.get(uuid); // オンライン時間(ミリ秒)
		int onlineTime = (int) (onlineTimeMills / 1000); // 秒に
		onlineTime = onlineTime / 60; // 分に
		onlineTime = onlineTime / 3; // 3分に(ポイント数)

		int Rank = getPlayerRank(onlineTimeMills);

		Pointjao.addjao(player, onlineTime, "2017年8月イベントのオンライン時間ポイント配布分");

		Long counter = OnSummer2017.getSummerCounter(player);
		int time = (int) (counter / 1000);

		player.sendMessage("[Summer2017] " + ChatColor.GREEN + "2017年8月イベントのポイントを配布しました！");
		player.sendMessage("[Summer2017] " + ChatColor.GREEN + "あなたの8月中サバイバルワールドオンライン時間: " + (time/3600) + "時間" + ((time % 3600) / 60) + "分" + (time % 60) + "秒 (" + time + "秒)");
		player.sendMessage("[Summer2017] " + ChatColor.GREEN + "順位: " + Rank + "位");

		summer2017distribution.put(uuid, true);

		try {
			SaveSummerData();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private int getPlayerRank(Long onlineTimeMills){
		Map<String, Long> online = new HashMap<String, Long>(summer2017online);
		List<Long> list = new ArrayList<Long>();
		for(Entry<String, Long> one : online.entrySet()) {
			list.add(one.getValue());
		}
		Collections.sort(list, Comparator.reverseOrder());
		if(list.indexOf(onlineTimeMills) != -1){
			return list.indexOf(onlineTimeMills) + 1;
		}else{
			return -1;
		}
	}

	public static Long getSummerCounter(Player player){
		if(!Loaded){
			try {
				LoadSummerData();
			} catch (Exception e) {
				return new Long(0);
			}
		}
		if(summer2017online.containsKey(player.getUniqueId().toString())){
			if(!summer2017counter.containsKey(player.getUniqueId().toString()) || summer2017counter.get(player.getUniqueId().toString()) == -1){
				return summer2017online.get(player.getUniqueId().toString());
			}else{
				Long now = System.currentTimeMillis() - summer2017counter.get(player.getUniqueId().toString());
				Long time = summer2017online.get(player.getUniqueId().toString()) + now;
				return time;
			}
		}
		return new Long(0);
	}

	static Boolean Loaded = false;

	/**
	 * Summer情報をセーブする。
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws Exception IOExceptionの発生時に発生
	*/
	@SuppressWarnings("unchecked")
	public static boolean SaveSummerData() throws Exception{

		JSONObject OnlineJSON = new JSONObject();
		for(Entry<String, Long> one : summer2017online.entrySet()) {
			OnlineJSON.put(one.getKey(), one.getValue());
		}

		JSONObject DistributionJSON = new JSONObject();
		for(Entry<String, Boolean> one : summer2017distribution.entrySet()) {
			DistributionJSON.put(one.getKey(), one.getValue());
		}

		JSONObject ALLJSON = new JSONObject();
		ALLJSON.put("Online", OnlineJSON);
		ALLJSON.put("Distribution", DistributionJSON);

		try{
			JavaPlugin plugin = MyMaid.getJavaPlugin();
	    	File file = new File(plugin.getDataFolder() + File.separator + "Summer.json");
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
	 * Summer情報をロードする。
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws Exception 何かしらのExceptionが発生したときに発生(FileNotFoundException, IOException)
	*/
	@SuppressWarnings("unchecked")
	public static boolean LoadSummerData() throws Exception{
		JSONParser parser = new JSONParser();
		String json = "";
		try{
			JavaPlugin plugin = MyMaid.getJavaPlugin();
	    	File file = new File(plugin.getDataFolder() + File.separator + "Summer.json");
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
		} catch (org.json.simple.parser.ParseException e1) {
			obj = new JSONObject();
		}
		if(obj.containsKey("Online")){
			for(Entry<String, Long> one : (Set<Map.Entry<String, Long>>) ((JSONObject) obj.get("Online")).entrySet()){
				summer2017online.put(one.getKey(), one.getValue());
			}
		}
		if(obj.containsKey("Distribution")){
			for(Entry<String, Boolean> one : (Set<Map.Entry<String, Boolean>>) ((JSONObject) obj.get("Distribution")).entrySet()){
				summer2017distribution.put(one.getKey(), one.getValue());
			}
		}
		Loaded = true;
		return true;
	}
}
