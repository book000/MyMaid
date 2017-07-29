package xyz.jaoafa.mymaid.EventHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.MyMaid;

public class OnSummer2017 implements Listener {
	JavaPlugin plugin;
	public OnSummer2017(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	// プレイヤー:オンライン時間(ミリ秒。1000で割る必要がある)
	static Map<String, Long> summer2017online = new HashMap<String, Long>();
	// プレイヤー:ワールドログイン時間
	static Map<String, Long> summer2017counter = new HashMap<String, Long>();

	@EventHandler
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		if(event.getFrom().getName().equals(player.getWorld().getName())){
			return;
		}
		if(event.getFrom().getName().equalsIgnoreCase("Summer2017")){
			// Summer2017からどこか - カウンターを停止
			EndCounter(player);
		}else if(player.getWorld().getName().equalsIgnoreCase("Summer2017")){
			// どこかからSummer2017 - カウンターを開始
			StartCounter(player);
		}
	}
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event){
		Player player = event.getPlayer();
		if(player.getWorld().getName().equalsIgnoreCase("Summer2017")){
			// Summer2017にJoin - カウンターを開始
			StartCounter(player);
		}
	}
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(player.getWorld().getName().equalsIgnoreCase("Summer2017")){
			// Summer2017からQuit - カウンターを停止
			EndCounter(player);
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

	public static Long getSummerCounter(Player player){
		if(!Loaded){
			try {
				LoadSummerData();
			} catch (Exception e) {
				return new Long(0);
			}
		}
		if(summer2017online.containsKey(player.getUniqueId().toString())){
			if(summer2017counter.get(player.getUniqueId().toString()) == -1){
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

		JSONObject ALLJSON = new JSONObject();
		ALLJSON.put("Online", OnlineJSON);

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
		} catch (ParseException e1) {
			obj = new JSONObject();
		}
		if(obj.containsKey("Online")){
			for(Entry<String, Long> one : (Set<Map.Entry<String, Long>>) ((JSONObject) obj.get("Online")).entrySet()){
				summer2017online.put(one.getKey(), one.getValue());
			}
		}
		Loaded = true;
		return true;
	}
}
