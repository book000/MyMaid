package xyz.jaoafa.mymaid.Discord;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage.Attachment;

public class DiscordChatEvent {
	JavaPlugin plugin;
	public DiscordChatEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventSubscriber
    public void ChatEvent(MessageReceivedEvent event) {
		if(event.getMessage().getChannel().getLongID() != new Long(Discord.channel.getLongID())){
			return;
		}
		/*String content = event.getMessage().getContent();
		List<IUser> mentions = event.getMessage().getMentions();
		List<IRole> roleMentions = event.getMessage().getRoleMentions();
		List<IEmoji> emojis = event.getMessage().getGuild().getEmojis();*/
		String content = event.getMessage().getFormattedContent();
/*
		for (IUser u : mentions) {
            String name = u.getNicknameForGuild(Discord.guild).get();
            String id = u.getLongID();

            // User name
            content = content.replaceAll("<@" + id + ">", "@" + name);
            // Nick name
            content = content.replaceAll("<@!" + id + ">", "@" + name);
        }
		for (IRole r : roleMentions) {
            String roleName = r.getName();
            long roleId = r.getLongID();

            content = content.replaceAll("<@&" + roleId + ">", "@" + roleName);
        }
		for(IEmoji emoji : emojis){
			String EmojiName = emoji.getName();
			long EmojiID = emoji.getLongID();

			content = content.replaceAll("<:" + EmojiName + ":" + EmojiID + ">", ":" + EmojiName + ":");
		}
*/
		String author = event.getMessage().getAuthor().getNicknameForGuild(Discord.guild);
		if(author == null) author = event.getMessage().getAuthor().getName();
		Bukkit.broadcastMessage(ChatColor.AQUA + "(Discord) " + ChatColor.RESET + author + ": " + content);

		List<Attachment> embeds = event.getMessage().getAttachments();
		for(Attachment embed : embeds){
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw @a [\"\",{\"text\":\"(Discord) \",\"color\":\"aqua\"},{\"text\":\"" + author + "(File): \",\"color\":\"none\"},{\"text\":\"" + embed.getFilename() + "\",\"underlined\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + embed.getUrl() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"" + embed.getUrl() +" を開きます。\"}]}},\"color\":\"none\"}]");
		}
	}
}
