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

import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WorldAllowCommand {
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
	 * 使用できるコマンド情報をロードする。
	 * @return 実行できたかどうか
	 * @author mine_book000
	 * @throws Exception 何かしらのExceptionが発生したときに発生(FileNotFoundException, IOException)
	*/
	public static boolean LoadAllowCommandData() throws Exception{
		JSONParser parser = new JSONParser();
		String json = "";
		try{
			JavaPlugin plugin = MyMaid.getJavaPlugin();
	    	File file = new File(plugin.getDataFolder() + File.separator + "AllowCommand.json");
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
		List<String> AllowOrDisallowCmds = new ArrayList<String>();

		return true;
	}
}
