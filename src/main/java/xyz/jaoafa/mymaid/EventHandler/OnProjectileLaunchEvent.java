package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class OnProjectileLaunchEvent implements Listener {
	JavaPlugin plugin;
	public OnProjectileLaunchEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event){
		if (!(event.getEntity().getShooter() instanceof org.bukkit.entity.Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(!PermissionsEx.getUser(player).inGroup("QPPE")){
			return;
		}
		event.setCancelled(true);
	}
}
