package xyz.jaoafa.mymaid.Discord;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import xyz.jaoafa.mymaid.MyMaid;

public class DiscordChatEvent {
	JavaPlugin plugin;
	public DiscordChatEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventSubscriber
    public void ChatEvent(MessageReceivedEvent event) {
		if(!event.getMessage().getChannel().getID().equalsIgnoreCase(Discord.channel.getID())){
			return;
		}
		String content = event.getMessage().getContent();
		List<IUser> mentions = event.getMessage().getMentions();
		List<IRole> roleMentions = event.getMessage().getRoleMentions();

		for (IUser u : mentions) {
            String name = u.getNicknameForGuild(Discord.guild).get();
            String id = u.getID();

            // User name
            content = content.replaceAll("<@" + id + ">", "@" + name);
            // Nick name
            content = content.replaceAll("<@!" + id + ">", "@" + name);
        }
		for (IRole r : roleMentions) {
            String roleName = r.getName();
            String roleId = r.getID();

            content = content.replaceAll("<@&" + roleId + ">", "@" + roleName);
        }
		String author = event.getMessage().getAuthor().getNicknameForGuild(Discord.guild).orElseGet(() -> event.getMessage().getAuthor().getName());
		Bukkit.broadcastMessage(ChatColor.AQUA + "(Discord) " + ChatColor.RESET + author + ": " + content);
		org.bukkit.entity.Player fake = Bukkit.getPlayer(author);
		MyMaid.dynmapbridge.chat(fake, content);
	}
}
