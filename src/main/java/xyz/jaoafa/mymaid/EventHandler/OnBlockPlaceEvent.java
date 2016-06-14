package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Command.Prison;

public class OnBlockPlaceEvent {
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
