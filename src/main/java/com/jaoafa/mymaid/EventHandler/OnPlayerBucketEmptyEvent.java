package com.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Command.Prison;

public class OnPlayerBucketEmptyEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerBucketEmptyEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent e){
    	Player player = e.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
  		if(!Prison.prison.containsKey(player.getName())){
  			return;
  		}
  		e.setCancelled(true);
  		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたは水や溶岩を撒けません。");
  		Bukkit.getLogger().info("[JAIL] "+player.getName()+"==>あなたは水や溶岩を撒けません。");
    }
}
