package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Command.Prison;

public class OnPlayerPickupItemEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerPickupItemEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent e){
    	Player player = e.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
  		if(!Prison.prison.containsKey(player.getName())){
  			return;
  		}
  		e.setCancelled(true);
    }
}
