package xyz.jaoafa.mymaid.EventHandler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class AntiBlockUnderDestroy {
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
		if(!(PermissionsEx.getUser(player).inGroup("QPPE") && PermissionsEx.getUser(player).inGroup("Default"))){
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

		if(y == (oldy - 1)){
			if(destroycount.containsKey(player.getName()) && destroycount.get(player.getName()) >= 5){
				player.sendMessage("[CheckBlockDestory] " + ChatColor.RED + "荒らし対策のため、ブロックの直下掘りは禁止されています。");
				event.setCancelled(true);
				destroycount.put(player.getName(), destroycount.get(player.getName()) + 1);
			}else{
				destroycount.put(player.getName(), 1);
			}
		}
		destroy.put(player.getName(), loc);
	}
}
