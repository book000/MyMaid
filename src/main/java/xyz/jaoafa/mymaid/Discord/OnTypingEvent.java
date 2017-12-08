package xyz.jaoafa.mymaid.Discord;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.TypingEvent;

public class OnTypingEvent {
	JavaPlugin plugin;
	public OnTypingEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventSubscriber
    public void onTypingEvent(TypingEvent event) {
		if(event.getChannel() == null){
			Bukkit.getLogger().warning("ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
		if(event.getChannel().getLongID() != new Long(Discord.channel.getLongID())){
			return;
		}
		String nickname = event.getUser().getNicknameForGuild(Discord.guild);
		if(nickname == null) event.getUser().getName();
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("[\"\",{\"text\":\"(Discord)\",\"color\":\"aqua\"},{\"text\":\" " + nickname +"(@" + event.getUser().getName() + "#" + event.getUser().getDiscriminator() + ") が入力中…\"}]"), (byte) 2);
		for(Player player: Bukkit.getServer().getOnlinePlayers()){
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
		}

	}
}
