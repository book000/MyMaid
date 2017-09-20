package xyz.jaoafa.mymaid.Discord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Pattern;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import sx.blah.discord.Discord4J;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.GuildCreateEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.MySQL;

public class Discord {
	JavaPlugin plugin;
	String token;
	public static IDiscordClient client = null;
	static IGuild guild = null;
	static IChannel channel = null;

	public Discord(JavaPlugin plugin, String token) {
		this.plugin = plugin;
		this.token = token;
	}

	public void start(){
		try {
			setClient(new ClientBuilder().withToken(token).build());
		} catch (DiscordException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			plugin.getLogger().info("Discordへの接続に失敗しました。(DiscordException Token)");
			plugin.getLogger().info("Disable MyMaid...");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
			return;
		}
		plugin.getLogger().info("Discordへの接続に成功しました。");
		Discord4J.disableAudio();

		//リスナー
		client.getDispatcher().registerListener(this);
		client.getDispatcher().registerListener(new DiscordChatEvent(plugin));
		client.getDispatcher().registerListener(new OnTypingEvent(plugin));

		try {
			client.login();
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

		plugin.getServer().getPluginManager().registerEvents(new BukkitChatEvent(plugin), plugin);
		plugin.getServer().getPluginManager().registerEvents(new BukkitListener(plugin), plugin);
		plugin.getServer().getPluginManager().registerEvents(new OnDynmapWebChat(plugin), plugin);

		getLinkedAccount();
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
	public static boolean isChannel(String channelid_or_name){
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
			BugReport.report(e);
		}
	}

	public static String format(String message){
		if(guild == null){
			MyMaid.getJavaPlugin().getLogger().info("Discordサーバへの接続に失敗しました。(Guildが見つかりません。)");
			return message;
		}
		for(Map.Entry<String, String> one : LinkedAccount.entrySet()){
			String player = one.getKey();
			String disid = one.getValue();
			message = message.replaceAll("@" + Pattern.quote(player), "<@&" + disid + ">");
		}

		for (IRole role : guild.getRoles()) {
			message = message.replaceAll("@" + Pattern.quote(role.getName()), "<@&" + role.getID() + ">");
		}

		for (IUser user : guild.getUsers()){
			if(!(user.getNicknameForGuild(guild).orElseGet(()->"").equalsIgnoreCase(""))){
				message = message.replaceAll("@" + Pattern.quote(user.getNicknameForGuild(guild).get()), "<@" + user.getID() + ">");
			}
			message = message.replaceAll("@" + Pattern.quote(user.getName()), "<@" + user.getID() + ">");
		}
		for (IChannel channel : guild.getChannels()) {
			message = message.replaceAll("#" + Pattern.quote(channel.getName()), "<#" + channel.getID() + ">");
		}
		return message;
	}

	@EventSubscriber
    public void onGuildCreate(GuildCreateEvent event) {
		if (event.getGuild() == null) {
            return;
        }

		if(event.getGuild().getID().equalsIgnoreCase("189377932429492224")){
			plugin.getLogger().info("DiscordGuildを選択しました。" + event.getGuild().getName());
			setGuild(event.getGuild());
		}

		if(guild == null){
			plugin.getLogger().info("Discordサーバへの接続に失敗しました。(Guildが見つかりません。)");
		}

		for (IChannel channel : event.getGuild().getChannels()) {
			if(!channel.getID().equalsIgnoreCase("250613942106193921")){
				continue;
			}
			if(!channel.getGuild().getID().equalsIgnoreCase(Discord.guild.getID())){
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
}
