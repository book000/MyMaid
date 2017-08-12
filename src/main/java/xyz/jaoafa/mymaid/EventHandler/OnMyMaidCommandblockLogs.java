package xyz.jaoafa.mymaid.EventHandler;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.BugReport;

public class OnMyMaidCommandblockLogs implements Listener {
	JavaPlugin plugin;
	public OnMyMaidCommandblockLogs(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR)
    public void onBlockRedstoneEvent(BlockRedstoneEvent event){
    	if(event.getOldCurrent() == 0 && event.getNewCurrent() > 0){
    		if(event.getBlock().getType() == Material.COMMAND){
        		CommandBlock cmdb = (CommandBlock)event.getBlock().getState();
        		cmdb.getCommand();
        		double min = 1.79769313486231570E+308;
	        	Player min_player = null;
				for(Player p: Bukkit.getServer().getOnlinePlayers()){
					Location location_p = p.getLocation();
					double distance;
					try{
						distance = cmdb.getLocation().distance(location_p);
					}catch(java.lang.IllegalArgumentException e){
						distance = -1;
					}

	            	if(distance < min){
	            		if(!p.getWorld().getName().equals(cmdb.getWorld().getName())){
	            			continue;
	            		}
	            		min = distance;
	            		min_player = p;
	            	}
	        	}
				if(min_player == null){
					return;
				}
				if(cmdb.getCommand().startsWith("/testfor")){
					return;
				}else if(cmdb.getCommand().startsWith("testfor")){
					return;
				}else if(cmdb.getCommand().startsWith("/testforblock")){
					return;
				}else if(cmdb.getCommand().startsWith("testforblock")){
					return;
				}else if(cmdb.getCommand().startsWith("/testforblocks")){
					return;
				}else if(cmdb.getCommand().startsWith("testforblocks")){
					return;
				}else if(cmdb.getCommand().equals("")){
					return;
				}

				log(plugin, cmdb.getCommand(), min_player, min, cmdb.getWorld().getName(), cmdb.getX(), cmdb.getY(), cmdb.getZ());
    		}
        }
    }
    @SuppressWarnings("unchecked")
    void log(final JavaPlugin plugin, final String command, final Player player, final double distance, final String world, final int x, final int y, final int z) {
    	try{new BukkitRunnable() {
    		@Override
    		public void run() {
    			JSONObject obj = new JSONObject();
    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    			SimpleDateFormat sdfall = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    			String file = plugin.getDataFolder() + File.separator + "commandblocklog" + File.separator + sdf.format(new Date()) + ".log";

    			String play = "";
    			for(Player playing: Bukkit.getServer().getOnlinePlayers()){
    				play += playing.getName() + ", ";
    			}
    			play = play.substring(0, play.length() - 2);

    			obj.put("world", world);
    			obj.put("x", x);
        		obj.put("y", y);
        		obj.put("z", z);
        		obj.put("command", command);
        		obj.put("player", player.getName());
        		obj.put("distance", distance);
        		obj.put("players", play);

        		Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
        		for(String group : groups){
        			if(PermissionsEx.getUser(player).inGroup(group)){
        				obj.put("permission", group);
        			}
        		}
        		obj.put("time", sdfall.format(new Date()));

                	String str = obj.toJSONString();
                	FileWriter writer = null;
                	try {
                		writer = new FileWriter(file, true);
                		writer.write(str + "\r\n");
                		writer.flush();
                	} catch (Exception e) {
                		e.printStackTrace();
                	} finally {
                		if ( writer != null ) {
                			try {
                				writer.close();
                			} catch (Exception e) {
                			}
                		}
                	}
                }
    		}.runTaskAsynchronously(plugin);
		}catch(java.lang.NoClassDefFoundError e){
			BugReport.report(e);
		}

    }
}
