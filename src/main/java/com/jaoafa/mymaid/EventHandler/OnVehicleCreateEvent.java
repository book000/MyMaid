package com.jaoafa.mymaid.EventHandler;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OnVehicleCreateEvent implements Listener {
	JavaPlugin plugin;
	public OnVehicleCreateEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onVehicleCreateEvent(VehicleCreateEvent e){
		Vehicle vehicle = e.getVehicle();
		if(vehicle.getType() == EntityType.MINECART_TNT){
			vehicle.remove();
		}
	}
}
