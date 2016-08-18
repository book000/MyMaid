package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Command.Ded;

public class OnPlayerDeathEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerDeathEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e){
		Player player = e.getEntity();
		Location loc = player.getLocation();
		Ded.ded.put(player.getName(), loc);
		player.sendMessage("[DED] " + ChatColor.GREEN + "死亡した場所に戻るには「/ded」コマンドが使用できます。");
	}
}
