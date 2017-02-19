package com.jaoafa.mymaid.EventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Command.DOT;

public class OnPlayerBedEnterEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerBedEnterEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
		Player player = event.getPlayer();
		DOT.bed.put(player.getName(), true);
		if(DOT.run.containsKey(player.getName())){
			player.sendMessage("ベットで寝ながらは違反だゾ！");
			event.setCancelled(true);
			return;
		}
	}
}
