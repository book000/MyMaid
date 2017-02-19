package com.jaoafa.mymaid.EventHandler;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.jaoafa.mymaid.Command.Eye;

public class EyeMove implements Listener {
	JavaPlugin plugin;
	public EyeMove(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e){
		Player player = e.getPlayer();

		String eyeplayer = null;
		for(Entry<String, String> eye : Eye.eyelist.entrySet()) {
	   	    if(player.getName().equalsIgnoreCase(eye.getValue())){
	   	    	eyeplayer = eye.getKey();
	   	    	if(eyeplayer == null){
	   				return;
	   			}
	   			Player eyep = null;
	   			for(Player online: Bukkit.getServer().getOnlinePlayers()) {
	   				if(online.getName().equalsIgnoreCase(eyeplayer)){
	   					eyep = online;
	   				}
	   			}
	   			if(eyep == null){
	   				return;
	   			}
	   			Vector vector = eyep.getLocation().toVector().subtract(player.getLocation().toVector());

	   			Location loc = eyep.getLocation().setDirection(vector.setX(-vector.getX()));
	   			loc = eyep.getLocation().setDirection(vector.setY(-vector.getY()));
	   			loc = eyep.getLocation().setDirection(vector.setZ(-vector.getZ()));
	   			eyep.teleport(loc);
	   	    }
	   	}

	}
}
