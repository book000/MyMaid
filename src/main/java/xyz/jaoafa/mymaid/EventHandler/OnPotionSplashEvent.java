package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.PermissionsManager;

public class OnPotionSplashEvent implements Listener {
	JavaPlugin plugin;
	public OnPotionSplashEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onPotionSplashEvent(PotionSplashEvent event){
		if (!(event.getEntity().getShooter() instanceof org.bukkit.entity.Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(!group.equalsIgnoreCase("QPPE")){
			return;
		}
		event.setCancelled(true);
	}
}
