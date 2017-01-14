package xyz.jaoafa.mymaid.EventHandler;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import xyz.jaoafa.mymaid.Command.Eye;

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
	   	    }
	   	}
		if(eyeplayer == null){
			return;
		}
		Player eye = null;
		for(Player online: Bukkit.getServer().getOnlinePlayers()) {
			if(online.getName().equalsIgnoreCase(eyeplayer)){
				eye = online;
			}
		}
		if(eye == null){
			Eye.eyelist.remove(eyeplayer);
			return;
		}
		Vector vector = eye.getLocation().toVector().subtract(player.getLocation().toVector());

		Location loc = eye.getLocation().setDirection(vector.setX(-vector.getX()));
		loc = eye.getLocation().setDirection(vector.setY(-vector.getY()));
		loc = eye.getLocation().setDirection(vector.setZ(-vector.getZ()));
		eye.teleport(loc);
	}
}
