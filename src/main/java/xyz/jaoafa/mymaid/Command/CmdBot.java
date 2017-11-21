package xyz.jaoafa.mymaid.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;

public class CmdBot implements CommandExecutor {
	JavaPlugin plugin;
	public CmdBot(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	// BotChat | player - bottype
	public static Map<String, BotType> botchat = new HashMap<String, BotType>();

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			// コマンド実行者と会話
			if(!(sender instanceof Player)){
				Method.SendMessage(sender, cmd, "このコマンドはゲーム内での実行できます。");
				return true;
			}
			Player player = (Player) sender;
			botchat.put(player.getName(), BotType.UserLocal);
			Method.SendMessage(sender, cmd, "jaotanとの一対一の会話機能をオンにしました。");
			return true;
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("help")){
				sendHelp(sender, cmd, commandLabel);
				return true;
			}else if(args[0].equalsIgnoreCase("disable")){
				// コマンド実行者との会話を終了
				if(!(sender instanceof Player)){
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内での実行できます。");
					return true;
				}
				Player player = (Player) sender;
				if(!botchat.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "jaotanとの一対一の会話機能はオフになっていました。");
					return true;
				}
				botchat.remove(player.getName());
				Method.SendMessage(sender, cmd, "jaotanとの一対一の会話機能をオフにしました。");
				return true;
			}else{
				Player player = Bukkit.getPlayer(args[0]);
				if(player == null){
					Method.SendMessage(sender, cmd, "指定されたプレイヤーは見つかりません。");
					return true;
				}
				botchat.put(player.getName(), BotType.UserLocal);
				Method.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」のjaotanとの一対一の会話機能をオンにしました。");
				Method.SendMessage(sender, cmd, "BotTypeを変更するには/bot type <BotTypeID>を使用します。");
				Method.SendMessage(sender, cmd, "停止するには/bot disableを使用します。");
				return true;
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("disable")){
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null){
					Method.SendMessage(sender, cmd, "指定されたプレイヤーは見つかりません。");
					return true;
				}
				if(!botchat.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」のjaotanとの一対一の会話機能はオフになっていました。");
					return true;
				}
				botchat.remove(player.getName());
				Method.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」のjaotanとの一対一の会話機能をオフにしました。");
				return true;
			}else if(args[0].equalsIgnoreCase("type")){
				if(!(sender instanceof Player)){
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内での実行できます。");
					return true;
				}
				Player player = (Player) sender;
				if(!botchat.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "jaotanとの一対一の会話機能はオフになっています。");
					return true;
				}
				if(!isNumber(args[1])){
					Method.SendMessage(sender, cmd, "指定されたBotTypeは数値ではありません。");
					return true;
				}
				int i = Integer.parseInt(args[1]);
				BotType type = BotType.searchByID(i);
				if(type == BotType.UNKNOWN){
					Method.SendMessage(sender, cmd, "指定されたBotTypeは見つかりません。");
					return true;
				}
				botchat.put(player.getName(), type);
				Method.SendMessage(sender, cmd, "jaotanとの一対一の会話機能のBotTypeを「" + type.getName() + "」に変更しました。");
				return true;
			}
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("type")){
				Player player = Bukkit.getPlayer(args[2]);
				if(player == null){
					Method.SendMessage(sender, cmd, "指定されたプレイヤーは見つかりません。");
					return true;
				}
				if(!botchat.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "jaotanとの一対一の会話機能はオフになっています。");
					return true;
				}
				if(!isNumber(args[1])){
					Method.SendMessage(sender, cmd, "指定されたBotTypeは数値ではありません。");
					return true;
				}
				int i = Integer.parseInt(args[1]);
				BotType type = BotType.searchByID(i);
				if(type == BotType.UNKNOWN){
					Method.SendMessage(sender, cmd, "指定されたBotTypeは見つかりません。");
					return true;
				}
				botchat.put(player.getName(), type);
				Method.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」とのjaotanとの一対一の会話機能のBotTypeを「" + type.getName() + "」に変更しました。");
				return true;
			}
		}
		sendHelp(sender, cmd, commandLabel);
		return true;
	}
	public boolean isNumber(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static void sendHelp(CommandSender sender, Command cmd, String commandLabel){
		Method.SendMessage(sender, cmd, "----- Bot -----");
		Method.SendMessage(sender, cmd, "jaotanとサーバ内で会話できるように設定したり、解除したりできます。");
		Method.SendMessage(sender, cmd, "/bot: jaotanと会話するように設定します。");
		Method.SendMessage(sender, cmd, "/bot type <BotTypeID>: jaotanの発言BotTypeを変更します。");
		Method.SendMessage(sender, cmd, "/bot type <BotTypeID> <Player>: 指定したPlayerとのjaotanの発言BotTypeを変更します。");
		Method.SendMessage(sender, cmd, "/bot disable: 自分との会話を解除します。");
		Method.SendMessage(sender, cmd, "/bot <Player>: 指定したPlayerとjaotanが会話するように設定します。");
		Method.SendMessage(sender, cmd, "/bot disable <Player>: 指定したPlayerとjaotanとの会話を解除します。");
		Method.SendMessage(sender, cmd, "※BotTypeIDは以下の種類があります。");
		for(BotType type : BotType.values()){
			if(type == BotType.UNKNOWN) continue;
			Method.SendMessage(sender, cmd, type.getId() +  ": " + type.getName());
		}
	}

	public static boolean isPlayerBotChat(Player player){
		return botchat.containsKey(player.getName()); // true: botchat中、false: 通常チャット中
	}

	public static BotType getPlayerBotType(Player player){
		if(!botchat.containsKey(player.getName())){
			return BotType.UNKNOWN;
		}
		return botchat.get(player.getName());
	}

	public static String getBotChatResult(Player player, String text){
		if(!isPlayerBotChat(player)){
			return null;
		}
		BotType type = getPlayerBotType(player);
		switch(type){
		case UserLocal: return getUserLocalChat(player.getName(), text);
		case A3RT: return getA3RTChat(text);
		case DOCOMO: return getDocomoChat(text);
		case CotogotoNoby: return getCotogotoNobyChat(text);
		case UNKNOWN: return text;
		}
		return text;
	}

	public static String getOnePlayerChatResult(String text){
		return getCotogotoNobyChat(text);
	}

	public static String UserLocalAPIKEY = null;
	private static String getUserLocalChat(String nickname, String text){
		if(UserLocalAPIKEY == null){
			return "データを取得できませんでした。(UserLocal 1)";
		}

		try {
			nickname = URLEncoder.encode(nickname, "UTF-8");
			text = URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			BugReport.report(e);
			return "データを取得できませんでした。(UserLocal 2)";
		}

		String url = "https://chatbot-api.userlocal.jp/api/chat?key=" + UserLocalAPIKEY + "&bot_name=jaotan&user_id=" + nickname + "&user_name=" + nickname + "&message=" + text;
		JSONObject obj = getHttpJson(url, null);
		if(obj == null){
			return "データを取得できませんでした。(UserLocal 3)";
		}

		String status = (String) obj.get("status");
		if(status.equalsIgnoreCase("success")){
			return (String) obj.get("result");
		}else{
			return "データを取得できませんでした。(UserLocal 4)";
		}
	}

	public static String A3RTAPIKEY = null;
	private static String getA3RTChat(String text){
		if(A3RTAPIKEY == null){
			return "データを取得できませんでした。(A3RT 1)";
		}
		String url = "https://api.a3rt.recruit-tech.co.jp/talk/v1/smalltalk";

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/x-www-form-urlencoded");

		Map<String, String> contents = new HashMap<String, String>();
		contents.put("apikey", A3RTAPIKEY);
		contents.put("query", text);

		JSONObject obj = postHttpJsonByForm(url, headers, contents);
		if(obj == null){
			return "データを取得できませんでした。(A3RT 2)";
		}

		String message = (String) obj.get("message");
		if(message.equalsIgnoreCase("ok")){
			JSONArray results = (JSONArray) obj.get("results");
			JSONObject result = (JSONObject) results.get(0);
			String reply = (String) result.get("reply");
			return reply;
		}else{
			return "データを取得できませんでした。(A3RT 3)";
		}
	}

	// Tips: Docomoの雑談対話APIにはしりとり機能があるが、現状の実装ではそれを無視して実装する。
	// 無視せず実装する機会がある場合には、contextとmodeをなにかしらの形で保持して次回に持ち込む
	public static String DocomoAPIKEY = null;
	private static String getDocomoChat(String text){
		if(DocomoAPIKEY == null){
			return "データを取得できませんでした。(Docomo 1)";
		}
		String url = "https://api.apigw.smt.docomo.ne.jp/dialogue/v1/dialogue?APIKEY=" + DocomoAPIKEY;

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		Map<String, String> contents = new HashMap<String, String>();
		contents.put("utt", text);

		JSONObject obj = postHttpJsonByJson(url, headers, contents);
		if(obj == null){
			return "データを取得できませんでした。(Docomo 2)";
		}

		if(obj.containsKey("utt")){
			return (String) obj.get("utt");
		}else{
			return "データを取得できませんでした。(Docomo 3)";
		}
	}

	public static String CotogotoNobyAPIKEY = null;
	private static String getCotogotoNobyChat(String text){
		if(CotogotoNobyAPIKEY == null){
			return "データを取得できませんでした。(CotogotoNoby 1)";
		}
		try {
			text = URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			BugReport.report(e);
			return "データを取得できませんでした。(CotogotoNoby 2)";
		}
		String url = "https://www.cotogoto.ai/webapi/noby.json?appkey=" + CotogotoNobyAPIKEY + "&text=" + text;
		JSONObject obj = getHttpJson(url, null);
		if(obj == null){
			return "データを取得できませんでした。(CotogotoNoby 3)";
		}

		if(obj.containsKey("text")){
			return (String) obj.get("text");
		}else{
			return "データを取得できませんでした。(CotogotoNoby 4)";
		}
	}

	private static JSONObject getHttpJson(String address, Map<String, String> headers){
		StringBuilder builder = new StringBuilder();
		try{
			URL url = new URL(address);

			HttpURLConnection connect = (HttpURLConnection)url.openConnection();
			connect.setRequestMethod("GET");
			if(headers != null){
				for(Map.Entry<String, String> header : headers.entrySet()) {
					connect.setRequestProperty(header.getKey(), header.getValue());
				}
			}

			connect.connect();

			if(connect.getResponseCode() != HttpURLConnection.HTTP_OK){
				InputStream in = connect.getErrorStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				in.close();
				connect.disconnect();

				System.out.println("[MyMaid] URLGetConnected(Error): " + address);
				System.out.println("[MyMaid] Response: " + connect.getResponseMessage());
				BugReport.report(new IOException(builder.toString()));
				return null;
			}

			InputStream in = connect.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			in.close();
			connect.disconnect();
			System.out.println("[MyMaid] URLGetConnected: " + address);
			System.out.println("[MyMaid] Data: " + builder.toString());
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(builder.toString());
			JSONObject json = (JSONObject) obj;
			return json;
		}catch(Exception e){
			BugReport.report(e);
			return null;
		}
	}

	private static JSONObject postHttpJsonByForm(String address, Map<String, String> headers, Map<String, String> contents){
		StringBuilder builder = new StringBuilder();
		try{
			URL url = new URL(address);

			HttpURLConnection connect = (HttpURLConnection)url.openConnection();
			connect.setRequestMethod("POST");
			if(headers != null){
				for(Map.Entry<String, String> header : headers.entrySet()){
					connect.setRequestProperty(header.getKey(), header.getValue());
				}
			}

			connect.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connect.getOutputStream());
			List<String> list = new ArrayList<>();
			for(Map.Entry<String, String> content : contents.entrySet()){
				list.add(content.getKey() + "=" + content.getValue());
			}
			String param = implode(list, "&");
			out.write(param);
			out.close();

			connect.connect();

			if(connect.getResponseCode() != HttpURLConnection.HTTP_OK){
				InputStream in = connect.getErrorStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				in.close();
				connect.disconnect();

				System.out.println("[MyMaid] URLGetConnected(Error): " + address);
				System.out.println("[MyMaid] Response: " + connect.getResponseMessage());
				BugReport.report(new IOException(builder.toString()));
				return null;
			}

			InputStream in = connect.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			in.close();
			connect.disconnect();
			System.out.println("[MyMaid] URLPostConnected: " + address);
			System.out.println("[MyMaid] Data: " + builder.toString());
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(builder.toString());
			JSONObject json = (JSONObject) obj;
			return json;
		}catch(Exception e){
			BugReport.report(e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static JSONObject postHttpJsonByJson(String address, Map<String, String> headers, Map<String, String> contents){
		StringBuilder builder = new StringBuilder();
		try{
			URL url = new URL(address);

			HttpURLConnection connect = (HttpURLConnection)url.openConnection();
			connect.setRequestMethod("POST");
			if(headers != null){
				for(Map.Entry<String, String> header : headers.entrySet()){
					connect.setRequestProperty(header.getKey(), header.getValue());
				}
			}

			connect.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connect.getOutputStream());
			//List<String> list = new ArrayList<>();
			JSONObject paramobj = new JSONObject();
			for(Map.Entry<String, String> content : contents.entrySet()){
				//list.add(content.getKey() + "=" + content.getValue());
				paramobj.put(content.getKey(), content.getValue());
			}
			//String param = implode(list, "&");
			out.write(paramobj.toJSONString());
			out.close();

			connect.connect();

			if(connect.getResponseCode() != HttpURLConnection.HTTP_OK){
				InputStream in = connect.getErrorStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				in.close();
				connect.disconnect();

				System.out.println("[MyMaid] URLGetConnected(Error): " + address);
				System.out.println("[MyMaid] Response: " + connect.getResponseMessage());
				BugReport.report(new IOException(builder.toString()));
				return null;
			}

			InputStream in = connect.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			in.close();
			connect.disconnect();
			System.out.println("[MyMaid] URLPostConnected: " + address);
			System.out.println("[MyMaid] Data: " + builder.toString());
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(builder.toString());
			JSONObject json = (JSONObject) obj;
			return json;
		}catch(Exception e){
			BugReport.report(e);
			return null;
		}
	}

	public static <T> String implode(List<T> list, String glue) {
		StringBuilder sb = new StringBuilder();
		for (T e : list) {
			sb.append(glue).append(e);
		}
		return sb.substring(glue.length());
	}

	public enum BotType {
		UserLocal(1, "UserLocal サポートチャットボット"), // BotID: 1
		A3RT(2, "A3RT Talk API"), // BotID: 2
		DOCOMO(3, "docomo 雑談対話API"), // BotID: 3
		CotogotoNoby(4, "Cotogoto Noby API"), // BotID: 4

		UNKNOWN(-1, "NotFound");

		private Integer id;
		private String name;
		BotType(Integer id, String name) {
			this.id = id;
			this.name = name;
		}

		public Integer getId(){
			return id;
		}

		public String getName(){
			return name;
		}

		public static BotType searchByID(int id){
			for(BotType type : values()){
				if(type.getId() == id){
					return type;
				}
			}

			return UNKNOWN;
		}
	}
}
