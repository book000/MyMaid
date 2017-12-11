package xyz.jaoafa.mymaid.Discord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONObject;

import sx.blah.discord.Discord4J;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.MySQL;

public class Discord {
	JavaPlugin plugin;
	static String token;
	public static IDiscordClient client = null;
	static IGuild guild = null;
	static IChannel channel = null;

	public Discord(JavaPlugin plugin, String token) {
		this.plugin = plugin;
		Discord.token = token;
	}

	public void start(){
		try {
			ClientBuilder builder = new ClientBuilder()
				.withToken(token)
				.registerListener(this)
				.registerListener(new DiscordChatEvent(plugin))
				.registerListener(new OnTypingEvent(plugin))
				.registerListener(new Bot(plugin));
			Discord4J.disableAudio();
			setClient(builder.login());
		} catch (DiscordException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			plugin.getLogger().info("Discordへの接続に失敗しました。(Login DiscordException)");
			plugin.getLogger().info("Disable MyMaid...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		} catch (RateLimitException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			plugin.getLogger().info("Discordへの接続に失敗しました。(Login RateLimitException)");
			plugin.getLogger().info("Disable MyMaid...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}

		if (Discord4J.LOGGER instanceof Discord4J.Discord4JLogger) {
			((Discord4J.Discord4JLogger) Discord4J.LOGGER).setLevel(Discord4J.Discord4JLogger.Level.NONE);
		}

		plugin.getServer().getPluginManager().registerEvents(new BukkitChatEvent(plugin), plugin);
		plugin.getServer().getPluginManager().registerEvents(new BukkitListener(plugin), plugin);
		plugin.getServer().getPluginManager().registerEvents(new OnDynmapWebChat(plugin), plugin);

		getLinkedAccount();
		try{
			Discord.setGame("Welcome to jao Minecraft Server.");
		}catch(java.lang.NullPointerException e){
			// Nothing
		}

	}

	public void end(){
		try {
			client.logout();
		} catch (DiscordException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			plugin.getLogger().info("Discordへの接続解除に失敗しました。");
		}
	}

	public static boolean send(String message){
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bot " + token);
		headers.put("User-Agent", "DiscordBot (https://jaoafa.com, v0.0.1)");

		Map<String, String> contents = new HashMap<String, String>();
		contents.put("content", message);
		return postHttpJsonByJson("https://discordapp.com/api/channels/250613942106193921/messages", headers, contents);
	}


	@SuppressWarnings("unchecked")
	private static boolean postHttpJsonByJson(String address, Map<String, String> headers, Map<String, String> contents){
		StringBuilder builder = new StringBuilder();
		try{
			URL url = new URL(address);

			/*TrustManager[] tm = { new X509TrustManager() {
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
					// TODO 自動生成されたメソッド・スタブ

				}
				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws CertificateException {
					// TODO 自動生成されたメソッド・スタブ

				}
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					// TODO 自動生成されたメソッド・スタブ
					return null;
				}
			}};
			SSLContext sslcontext = SSLContext.getInstance("SSL");
			sslcontext.init(null, tm, null);
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			 */
			HttpsURLConnection connect = (HttpsURLConnection)url.openConnection();
			connect.setRequestMethod("POST");
			// connect.setSSLSocketFactory(sslcontext.getSocketFactory());
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
			//Bukkit.getLogger().info(paramobj.toJSONString());
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

				Bukkit.getLogger().warning("DiscordWARN: " + builder.toString());
				return false;
			}

			InputStream in = connect.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			in.close();
			connect.disconnect();
			//JSONParser parser = new JSONParser();
			//Object obj = parser.parse(builder.toString());
			//JSONObject json = (JSONObject) obj;
			return true;
		}catch(Exception e){
			e.printStackTrace();
			//BugReport.report(e);
			return false;
		}
	}
/*
	public static boolean send(String message){
		if(channel == null){
			return false;
		}
		try {
			channel.sendMessage(message);
		} catch (MissingPermissionsException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(MissingPermissionsException)");
			return false;
		} catch (DiscordException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(DiscordException)");
			return false;
		}catch (RateLimitException e){
			// Since I want to retry try to add it.
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(RateLimitException)");
			return false;
		}
		return true;
	}*/

	public static boolean Filesend(IChannel channel, String path){
		File file = new File(path);
		if(!file.exists()){
			return false;
		}
		if(channel == null){
			return false;
		}
		try {
			channel.sendFile(file);
		} catch (MissingPermissionsException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(MissingPermissionsException)");
			return false;
		} catch (DiscordException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(DiscordException)");
			return false;
		}catch (RateLimitException e){
			// Since I want to retry try to add it.
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(RateLimitException)");
			return false;
		} catch (FileNotFoundException e) {
			// Since I want to retry try to add it.
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(FileNotFoundException)");
			return false;
		}
		return true;
	}

	public static boolean Filesend(String channelid_or_name, String path){
		IChannel channel = null;
		for (IChannel one : guild.getChannels()) {
			if(one.getLongID() != new Long(channelid_or_name)){
				continue;
			}
			channel = one;
		}
		if(channel == null){
			for (IChannel one : guild.getChannels()) {
				if(!one.getName().equalsIgnoreCase(channelid_or_name)){
					continue;
				}
				channel = one;
			}
		}
		if(channel == null){
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(指定されたチャンネルが見つかりません。)");
			return false;
		}
		Filesend(channel, path);
		return true;
	}

	public static boolean retrysend(String message){
		if(channel == null){
			return false;
		}
		try {
			channel.sendMessage(message);
		} catch (MissingPermissionsException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(MissingPermissionsException)");
			return false;
		} catch (DiscordException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(DiscordException)");
			return false;
		}catch (RateLimitException e){
			// Since I want to retry try to add it.
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(RateLimitException)");
			return false;
		}
		return true;
	}

	public static boolean send(IChannel channel, String message){
		if(channel == null){
			return false;
		}
		try {
			channel.sendMessage(message);
		} catch (MissingPermissionsException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(MissingPermissionsException)");
			return false;
		} catch (DiscordException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(DiscordException)");
			return false;
		}catch (RateLimitException e){
			// Since I want to retry try to add it.
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(RateLimitException)");
			return false;
		}
		return true;
	}


	public static boolean send(IChannel channel, String message, EmbedObject embed){
		if(channel == null){
			return false;
		}
		try {
			channel.sendMessage(message, embed, false);
		} catch (MissingPermissionsException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(MissingPermissionsException)");
			return false;
		} catch (DiscordException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(DiscordException)");
			return false;
		}catch (RateLimitException e){
			// Since I want to retry try to add it.
			e.printStackTrace();
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(RateLimitException)");
			return false;
		}
		return true;
	}

	public static boolean send(String channelid, String message){
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		headers.put("Authorization", "Bot " + token);
		headers.put("User-Agent", "DiscordBot (https://jaoafa.com, v0.0.1)");

		Map<String, String> contents = new HashMap<String, String>();
		headers.put("content", message);
		return postHttpJsonByJson("https://discordapp.com/api/channels/" + channelid + "/messages", headers, contents);
	}

	/*
	public static boolean send(String channelid_or_name, String message){
		IChannel channel = null;
		for (IChannel one : guild.getChannels()) {
			if(!one.getID().equalsIgnoreCase(channelid_or_name)){
				continue;
			}
			channel = one;
        }
		if(channel == null){
			for (IChannel one : guild.getChannels()) {
				if(!one.getName().equalsIgnoreCase(channelid_or_name)){
					continue;
				}
				channel = one;
	        }
		}
		if(channel == null){
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(指定されたチャンネルが見つかりません。)");
			return false;
		}
		send(channel, message);
		return true;
	}
	 */

	public static boolean send(String channelid_or_name, String message, EmbedObject embed){
		IChannel channel = null;
		for (IChannel one : guild.getChannels()) {
			if(one.getLongID() != new Long(channelid_or_name)){
				continue;
			}
			channel = one;
		}
		if(channel == null){
			for (IChannel one : guild.getChannels()) {
				if(!one.getName().equalsIgnoreCase(channelid_or_name)){
					continue;
				}
				channel = one;
			}
		}
		if(channel == null){
			MyMaid.getJavaPlugin().getLogger().info("Discordへのメッセージ送信に失敗しました。(指定されたチャンネルが見つかりません。)");
			return false;
		}
		send(channel, message, embed);
		return true;
	}

	public static boolean isChannel(String channelid_or_name){
		IChannel channel = null;
		try {
			for (IChannel one : guild.getChannels()) {
				if(one.getLongID() != new Long(channelid_or_name)){
					continue;
				}
				channel = one;
			}
		}catch(NumberFormatException e){
			for (IChannel one : guild.getChannels()) {
				if(!one.getName().equalsIgnoreCase(channelid_or_name)){
					continue;
				}
				channel = one;
			}
		}
		if(channel == null){
			return false;
		}
		return true;
	}
	public static Queue<String> SendData = new ArrayDeque<String>();
	public static BukkitTask task = null;
	/*
	public static void Queuesend(String message){
		SendData.add(message);
		if(task != null){
			return;
		}
		task = new MyMaid.QueueDiscordSendData(MyMaid.getJavaPlugin()).runTaskTimer(MyMaid.getJavaPlugin(), 0, 10);
	}
	 */

	// LinkedAccount: MinecraftID | DiscordID
	public static Map<String, String> LinkedAccount = new HashMap<String, String>();
	public static void getLinkedAccount(){
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				BugReport.report(e1);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return;
		}
		statement = MySQL.check(statement);
		try {
			ResultSet res = statement.executeQuery("SELECT * FROM discordlink");
			while(res.next()){
				String disid = res.getString("disid");
				String player = res.getString("player");
				LinkedAccount.put(player, disid);
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public static String format(String message){
		/*if(guild == null){
			MyMaid.getJavaPlugin().getLogger().info("Discordサーバへの接続に失敗しました。(Guildが見つかりません。)");
			return message;
		}*/
		for(Map.Entry<String, String> one : LinkedAccount.entrySet()){
			String player = one.getKey();
			String disid = one.getValue();
			message = message.replaceAll("@" + Pattern.quote(player), "<@" + disid + ">");
		}
		/*
		for (IRole role : guild.getRoles()) {
			message = message.replaceAll("@" + Pattern.quote(role.getName()), "<@&" + role.getLongID() + ">");
		}

		for (IUser user : guild.getUsers()){
			if(user.getNicknameForGuild(guild) != null){
				message = message.replaceAll("@" + Pattern.quote(user.getNicknameForGuild(guild)), "<@" + user.getLongID() + ">");
			}
			message = message.replaceAll("@" + Pattern.quote(user.getName()), "<@" + user.getLongID() + ">");
		}
		for (IChannel channel : guild.getChannels()) {
			message = message.replaceAll("#" + Pattern.quote(channel.getName()), "<#" + channel.getLongID() + ">");
		}
		*/
		return message;
	}

	@EventSubscriber
	public void onGuildCreate(GuildCreateEvent event) {
		if (event.getGuild() == null) {
			return;
		}

		if(event.getGuild().getLongID() == new Long("189377932429492224")){
			plugin.getLogger().info("DiscordGuildを選択しました。" + event.getGuild().getName());
			setGuild(event.getGuild());
		}

		if(guild == null){
			plugin.getLogger().info("Discordサーバへの接続に失敗しました。(Guildが見つかりません。)");
		}

		for (IChannel channel : event.getGuild().getChannels()) {
			if(channel.getLongID() != new Long("250613942106193921")){
				continue;
			}
			if(channel.getGuild().getLongID() != new Long(Discord.guild.getLongID())){
				continue;
			}
			Discord.channel = channel;
		}
		if(channel == null){
			plugin.getLogger().info("Discordサーバへの接続に失敗しました。(Channelが見つかりません。)");
		}
		plugin.getLogger().info("Discordサーバへの接続に完了しました。ID: " + event.getClient().getOurUser().getName());
	}
	private void setClient(IDiscordClient client){
		Discord.client = client;
	}
	private void setGuild(IGuild guild){
		Discord.guild = guild;
	}
	public static void setGame(String game){
		return;
		/*
		if(Discord.client == null) return;
		Discord.client.changePlayingText(game);*/
	}
}
