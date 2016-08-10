package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.CuboidRegionSelector;
import com.sk89q.worldedit.regions.RegionSelector;

import xyz.jaoafa.mymaid.Method;

@SuppressWarnings("deprecation")
public class Guard implements CommandExecutor {
	JavaPlugin plugin;
	public Guard(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (sender instanceof Player) {
			Player player = (Player) sender;
			LocalSession session = WorldEdit.getInstance().getSession(player.getName());
			if (session != null) {
				// セレクタが立方体セレクタか判定
				if (!(session.getRegionSelector() instanceof CuboidRegionSelector)){
					Method.SendMessage(sender, cmd, "WorldEditの選択範囲を立方体にしてください。");
				}
				try {
					RegionSelector regionSelector = session.getRegionSelector(session.getSelectionWorld());
					String worldname = regionSelector.getRegion().getWorld().getName();
					int x1 = regionSelector.getRegion().getMinimumPoint().getBlockX();
					int y1 = regionSelector.getRegion().getMinimumPoint().getBlockY();
					int z1 = regionSelector.getRegion().getMinimumPoint().getBlockZ();
					int x2 = regionSelector.getRegion().getMaximumPoint().getBlockX();
					int y2 = regionSelector.getRegion().getMaximumPoint().getBlockY();
					int z2 = regionSelector.getRegion().getMaximumPoint().getBlockZ();
					World world = Bukkit.getWorld(worldname);
					Map<String,Location> WE = new HashMap<String,Location>();
					for(int x = x1; x <= x2; x++){
						for(int y = y1; y <= y2; y++){
							for(int z = z1; z <= z2; z++){
								Location loc = new Location(world, x, y, z);
								WE.put(player.getName(), loc);
							}
						}
					}
					
				} catch (IncompleteRegionException ex) {
					Method.SendMessage(sender, cmd, "範囲を2つ指定してください。");
				} catch(java.lang.NullPointerException ex){
					Method.SendMessage(sender, cmd, "範囲を指定してください。");
				}
			}
		}
		return true;
	}
}
