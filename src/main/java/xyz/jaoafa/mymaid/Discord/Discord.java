package xyz.jaoafa.mymaid.Discord;

import java.util.ArrayDeque;
import java.util.Queue;

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
import sx.blah.discord.util.RequestBuffer;
import xyz.jaoafa.mymaid.MyMaid;

public class Discord {
	static JavaPlugin plugin;
	String token;
	public static IDiscordClient client = null;
	static IGuild guild = null;;
	static IChannel channel = null;

	public Discord(JavaPlugin plugin, String token) {
		Discord.plugin = plugin;
		this.token = token;
	}


	public void start(){
		try {
			client = new ClientBuilder().withToken(token).build();
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
		RequestBuffer.request(() ->{
			try {
				channel.sendMessage(message);
			} catch (MissingPermissionsException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				plugin.getLogger().info("Discordへのメッセージ送信に失敗しました。(MissingPermissionsException)");
			} catch (DiscordException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				plugin.getLogger().info("Discordへのメッセージ送信に失敗しました。(DiscordException)");
			}
		});
		return true;
	}

	public static boolean send(IChannel channel, String message){
		if(channel == null){
			return false;
		}
		RequestBuffer.request(() ->{
			try {
				channel.sendMessage(message);
			} catch (MissingPermissionsException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				plugin.getLogger().info("Discordへのメッセージ送信に失敗しました。(MissingPermissionsException)");
			} catch (DiscordException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				plugin.getLogger().info("Discordへのメッセージ送信に失敗しました。(DiscordException)");
			}
		});
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
			plugin.getLogger().info("Discordへのメッセージ送信に失敗しました。(指定されたチャンネルが見つかりません。)");
			return false;
		}
		send(channel, message);
		return true;
	}

	public static Queue<String> SendData = new ArrayDeque<String>();
	public static BukkitTask task = null;
	public static void Queuesend(String message){
		SendData.add(message);
		if(task != null){
			return;
		}
		task = new MyMaid.QueueDiscordSendData(plugin).runTaskTimer(plugin, 0, 10);
	}

	public static String format(String message){
		if(guild == null){
			plugin.getLogger().info("Discordサーバへの接続に失敗しました。(Guildが見つかりません。)");
			return message;
		}
		for (IRole role : guild.getRoles()) {
			message = message.replaceAll("@" + role.getName(), "<@&" + role.getID() + ">");
		}

		for (IUser user : guild.getUsers()){
			if(!(user.getNicknameForGuild(guild).orElseGet(()->"").equalsIgnoreCase(""))){
				message = message.replaceAll("@" + user.getNicknameForGuild(guild).get(), "<@" + user.getID() + ">");
			}
			message = message.replaceAll("@" + user.getName(), "<@" + user.getID() + ">");
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
			Discord.guild = event.getGuild();
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
}
