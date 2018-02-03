package xyz.jaoafa.mymaid.EventHandler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.PermissionsManager;

public class AntiBlockUnderDestroy implements Listener {
	JavaPlugin plugin;
	public AntiBlockUnderDestroy(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String, Location> destroy = new HashMap<String, Location>();
	public static Map<String, Integer> destroycount = new HashMap<String, Integer>();
	@EventHandler
	public void onAntiBlockUnderDestroy(BlockBreakEvent event){
		Player player = event.getPlayer();
		Location loc = event.getBlock().getLocation();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();

		if (!(player instanceof Player)) {
			return;
		}

		String group = PermissionsManager.getPermissionMainGroup(player);

		int destroyOK;
		if(group.equalsIgnoreCase("QPPE")){
			destroyOK = 3;
		}else if(group.equalsIgnoreCase("Default")){
			destroyOK = 5;
		}else{
			return;
		}
		if(!destroy.containsKey(player.getName())){
			destroy.put(player.getName(), loc);
			return;
		}
		Location oldLoc = destroy.get(player.getName());
		int oldx = oldLoc.getBlockX();
		int oldy = oldLoc.getBlockY();
		int oldz = oldLoc.getBlockZ();

		if(oldx != x && oldz != z){
			return;
		}

		if(y == (oldy - 1) || (oldy - y) >= 3){
			if(destroycount.containsKey(player.getName())){
				if(destroycount.get(player.getName()) >= destroyOK){
					player.sendMessage("[CheckBlockDestory] " + ChatColor.RED + "荒らし対策のため、ブロックの直下掘りは禁止されています。");
					event.setCancelled(true);
					return;
				}else{
					destroycount.put(player.getName(), destroycount.get(player.getName()) + 1);
				}
			}else{
				destroycount.put(player.getName(), 1);
			}
		}else{
			if(destroycount.containsKey(player.getName()) && destroycount.get(player.getName()) >= 5){
				destroycount.put(player.getName(), 0);
			}
		}
		destroy.put(player.getName(), loc);
	}
}
