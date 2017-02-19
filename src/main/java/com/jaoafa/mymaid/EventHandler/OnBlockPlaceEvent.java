package com.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Command.Prison;

public class OnBlockPlaceEvent implements Listener {
	JavaPlugin plugin;
	public OnBlockPlaceEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e){
    	Player player = e.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!player.getLocation().getWorld().getName().equalsIgnoreCase("Jao_Afa")){
    		return;
    	}
    	World World = Bukkit.getServer().getWorld("Jao_Afa");
	   	Location prison = new Location(World, 1767, 70, 1767);
	   	if(prison.distance(e.getBlock().getLocation()) <= 150){
	   		if(e.getBlock().getType() == Material.COMMAND){
	   			player.sendMessage("[PRISON] " + ChatColor.GREEN + "楽園にコマンドブロックを設置できません。");
		   		e.setCancelled(true);
		   		return;
	   		}
	   	}
  		if(!Prison.prison.containsKey(player.getName())){
  			return;
  		}
  		if(Prison.prison_block.get(player.getName())){
  			return;
  		}
  		e.setCancelled(true);
  		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはブロックを置けません。");
  		Bukkit.getLogger().info("[JAIL] "+player.getName()+"==>あなたはブロックを置けません。");
    }
}
