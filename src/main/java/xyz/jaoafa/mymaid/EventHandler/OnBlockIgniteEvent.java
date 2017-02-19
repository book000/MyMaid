package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Command.Prison;

public class OnBlockIgniteEvent implements Listener {
	JavaPlugin plugin;
	public OnBlockIgniteEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onBlockIgniteEvent(BlockIgniteEvent e){
    	Player player = e.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
  		if(!Prison.prison.containsKey(player.getName())){
  			return;
  		}
  		e.setCancelled(true);
  		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはブロックを着火できません。");
  		Bukkit.getLogger().info("[JAIL] "+player.getName()+"==>あなたはブロックを着火できません。");
    }
}
