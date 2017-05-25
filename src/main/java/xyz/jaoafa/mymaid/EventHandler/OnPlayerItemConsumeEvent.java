package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;


public class OnPlayerItemConsumeEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerItemConsumeEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event){
		Player player = event.getPlayer();
		if(event.getItem().getType() != Material.POTION){
			return;
		}
		if(!PermissionsEx.getUser(player).inGroup("QPPE")){
			return;
		}
		event.setCancelled(true);
	}
}
