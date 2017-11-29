package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.MinecraftJPVoteMissFiller.CustomEvent.VoteMissFillerEvent;

import xyz.jaoafa.mymaid.Discord.Discord;

public class OnVoteMissFillerEvent implements Listener {
	JavaPlugin plugin;
	public OnVoteMissFillerEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onVoteMissFillerEvent(VoteMissFillerEvent event){
		String player = event.getStringPlayer();
		Discord.send("254166905852657675", ":mailbox_with_mail: **投票自動補填通知**: " + player + "の投票が受信されていなかったため、自動補填を行います。");
		OnVotifierEvent.VoteReceive(player);
	}
}
