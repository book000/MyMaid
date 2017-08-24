package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OnBoatNoDestroy implements Listener {
	JavaPlugin plugin;
	public OnBoatNoDestroy(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onVehicleDestroyEvent(VehicleDestroyEvent event){
		Vehicle v = event.getVehicle();
		if(v.getType() != EntityType.BOAT){
			return;
		}
		if(event.getAttacker() != null){
			return;
		}
		event.setCancelled(true);
	}
}
