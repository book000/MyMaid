package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Command.DedRain;

public class OnWeatherChangeEvent implements Listener {
	JavaPlugin plugin;
	public OnWeatherChangeEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.NORMAL)
	public void onRainStart(WeatherChangeEvent event) {
		if (!event.isCancelled()) {
			boolean setting = DedRain.flag;
			if (event.toWeatherState() && setting) {
				event.setCancelled(true);
			}
		}
	}
}
