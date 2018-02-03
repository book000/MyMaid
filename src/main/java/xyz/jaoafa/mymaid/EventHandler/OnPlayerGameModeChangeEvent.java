package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.PermissionsManager;

public class OnPlayerGameModeChangeEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerGameModeChangeEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
		Player player = event.getPlayer();
		if(event.getNewGameMode() == GameMode.SPECTATOR){
			String group = PermissionsManager.getPermissionMainGroup(player);
			if(group.equalsIgnoreCase("QPPE") || group.equalsIgnoreCase("Default")){
				player.sendMessage("[GAMEMODE] " + ChatColor.GREEN + "処理に失敗しました。");
				event.setCancelled(true);
				return;
			}

			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dynmap hide " + player.getName());
		}else{
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dynmap show " + player.getName());
		}
	}
}
