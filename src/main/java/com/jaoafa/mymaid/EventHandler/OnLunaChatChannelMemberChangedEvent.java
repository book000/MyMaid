package com.jaoafa.mymaid.EventHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.ucchyocean.lc.channel.ChannelPlayer;
import com.github.ucchyocean.lc.event.LunaChatChannelMemberChangedEvent;
import com.jaoafa.mymaid.Command.DOT;

public class OnLunaChatChannelMemberChangedEvent implements Listener {
	JavaPlugin plugin;
	public OnLunaChatChannelMemberChangedEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLunaChatChannelMemberChangedEvent(LunaChatChannelMemberChangedEvent e){
		if(!e.getChannel().getName().equals("_CHAT_JAIL_")){
			return;
		}
		if(e.getMembersBefore().size() > e.getMembersAfter().size()){
			e.getMembersBefore().removeAll(e.getMembersAfter());
			ChannelPlayer player = e.getMembersBefore().get(0);
			DOT.dotcount_stop.remove(player.getName());
		}
	}
}
