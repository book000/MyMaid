package xyz.jaoafa.mymaid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import xyz.jaoafa.mymaid.SKKColors.SKKColors;

public class WorldAllowCommand {
	static JavaPlugin plugin;
	static File file;
	static Map<String, Map<String, List<String>>> WorldCommand = new HashMap<String, Map<String, List<String>>>();

	/*
	 * 「+*」はすべてのコマンドをAllow(使用可能)
	 *  →Disallowが記載されている場合はそちらを優先
	 * 「-*」はすべてのコマンドをDisallow(使用不可能)
	 *  →Allowが記載されている場合はそちらを優先
	 *
	 * 「+command」はAllow(使用可能)
	 * 「-command」はDisallow(使用不可能)
	 */

	/**
	 * 使用できるコマンド情報をロードしたりデータを保存したり初期設定をします。
	 * @param plugin プラグインのJavaPluginを指定
	 * @return 初期設定を完了したかどうか
	 * @author mine_book000
	 */
	public static boolean first(JavaPlugin plugin){
		WorldAllowCommand.plugin = plugin;
		// 設定ファイルがなければ作成
		File file = new File(plugin.getDataFolder(), "WorldCommand.yml");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				BugReport.report(e);
				return false;
			}
			WorldAllowCommand.file = file;
			Save();
		}else{
			WorldAllowCommand.file = file;
			Load();
		}
		return true;
	}

	/**
	 * ログインメッセージをセーブします。
	 * @return 完了したかどうか
	 * @author mine_book000
	 */
	public static boolean Save(){
		FileConfiguration data = YamlConfiguration.loadConfiguration(file);
		for(World world : Bukkit.getWorlds()){
			if(WorldCommand.containsKey(world.getName())){
				// 設定がメモリ上に存在する
				Map<String, List<String>> TypeList = WorldCommand.get(world.getName());
				if(TypeList.containsKey("DefaultAllCommand") && TypeList.containsKey("CommandList")){
					// ワールド設定欄があってその中の設定があるとき


				}else{
					// ワールド設定欄があるのにその中の設定が存在しないとき

					List<String> DefaultAllCommand = new ArrayList<String>();
					// 本当はこんな管理したくないんだけどObject管理もそれはそれでアレなので.get(0)が判定ということで
					DefaultAllCommand.add("Allow"); // DefaultAllCommandのDefault値はAllow
					TypeList.put("DefaultAllCommand", DefaultAllCommand);

					List<String> CommandList = new ArrayList<String>();
					TypeList.put("CommandList", CommandList);

					data.set("DefaultAllCommand", DefaultAllCommand.get(0)); // 保存時はString
					data.set("CommandList", CommandList); // 保存時もList
				}
			}else{
				// ワールド設定欄さえないとき

				Map<String, List<String>> TypeList = new HashMap<String, List<String>>();
				List<String> DefaultAllCommand = new ArrayList<String>();
				// 本当はこんな管理したくないんだけどObject管理もそれはそれでアレなので.get(0)が判定ということで
				DefaultAllCommand.add("Allow"); // DefaultAllCommandのDefault値はAllow
				TypeList.put("DefaultAllCommand", DefaultAllCommand);

				List<String> CommandList = new ArrayList<String>();
				TypeList.put("CommandList", CommandList);

				data.set("DefaultAllCommand", DefaultAllCommand.get(0)); // 保存時はString
				data.set("CommandList", CommandList); // 保存時もList


			}
		}
		try {
			data.save(file);
			return true;
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			BugReport.report(e);
			return false;
		}
	}

	/**
	 * ログインメッセージをロードします。
	 * @return 完了したかどうか
	 * @author mine_book000
	 */
	public static boolean Load(){
		FileConfiguration data = YamlConfiguration.loadConfiguration(file);
		if(data.contains("JoinMessageList")){
			//MessageList = data.getStringList("JoinMessageList");
		}else{
			return false;
		}
		if(data.contains("LastText")){
			Map<String, Object> LastText = data.getConfigurationSection("LastText").getValues(true);
			if(LastText.size() != 0){
				for(Entry<String, Object> p: LastText.entrySet()){
					String LastTextStr = (String) p.getValue();
					SKKColors.LastText.put(p.getKey(), LastTextStr);
				}
			}
		}else{
			return false;
		}
		return true;
	}

	/**
	 * 使用できるコマンド情報をロードする。
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws Exception 何かしらのExceptionが発生したときに発生(FileNotFoundException, IOException)
	*/
	public static boolean LoadWorldCommandData() throws Exception{
		JSONParser parser = new JSONParser();
		String json = "";
		try{
			JavaPlugin plugin = MyMaid.getJavaPlugin();
	    	File file = new File(plugin.getDataFolder() + File.separator + "WorldCommand.json");
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
		Map<String, Map<String, String>> WorldCommand = new HashMap<String, Map<String, String>>();
		//List<String> CommandList = new ArrayList<String>();
		for(World world : Bukkit.getWorlds()){
			if(obj.containsKey(world.getName())){
				// 設定が存在する
				JSONObject TypeList = (JSONObject) obj.get(world.getName());
				if(TypeList.containsKey("DefaultAllCommand") && TypeList.containsKey("CommandList")){
					continue;
				}
				// DefaultAllCommand: Allow / Disallow
				// CommandList: JSONArray
				String DefaultAllCommand = (String) TypeList.get("DefaultAllCommand");
				JSONArray CommandList = (JSONArray) TypeList.get("CommandList");
				for(Object o : CommandList){
					//
				}

			}
		}
		return true;
	}
}
