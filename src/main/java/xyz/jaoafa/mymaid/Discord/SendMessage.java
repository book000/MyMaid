package xyz.jaoafa.mymaid.Discord;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class SendMessage {
	static void sendToDiscord(IChannel channel, String message){
		try {
			new MessageBuilder(Discord.client).appendContent(ChatColor.stripColor(message)).withChannel(channel).build();
		} catch (DiscordException e) {
            Bukkit.getLogger().info("Discordへのメッセージの送信に失敗しました。(DiscordException)");
            e.printStackTrace();
            return;
        } catch (MissingPermissionsException e) {
        	Bukkit.getLogger().info("Discordへのメッセージの送信に失敗しました。(MissingPermissionsException)");
        	e.printStackTrace();
        	return;
        } catch (RateLimitException e) {
        	Bukkit.getLogger().info("Discordへのメッセージの送信に失敗しました。(RateLimitException)");
        	e.printStackTrace();
        	return;
		}
	}
}
