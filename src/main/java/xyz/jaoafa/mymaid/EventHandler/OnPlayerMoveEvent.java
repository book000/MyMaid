package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Command.Prison;

public class OnPlayerMoveEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerMoveEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e){
		Location to = e.getTo();
		Player player = e.getPlayer();
	   		if(!Prison.prison.containsKey(player.getName())){
	   			return;
	   		}
	   		if(Prison.prison.get(player.getName())){
	   			return;
	   		}
	   		World World = Bukkit.getServer().getWorld("Jao_Afa");
	   		Location prison = new Location(World, 1767, 70, 1767);
	   		if(prison.distance(to) >= 150){
	   			player.sendMessage("[PRISON] " + ChatColor.GREEN + "あなたは南の楽園から出られません！");
	   			e.setCancelled(true);
	   			try{
	   				if(prison.distance(to) >= 200){
		   				player.teleport(prison);
		   			}
	   			}catch(java.lang.IllegalArgumentException ex){
	   				player.teleport(prison);
	   			}

	   		}
	}
}
