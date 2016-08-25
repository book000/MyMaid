package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Command.Cmdsearch;

public class OnBlockRedstoneEvent implements Listener {
	JavaPlugin plugin;
	public OnBlockRedstoneEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onBlockRedstoneEvent(BlockRedstoneEvent e){
    	if(e.getOldCurrent() == 0 && e.getNewCurrent() > 0){
    		if(e.getBlock().getType() == Material.COMMAND){
        		CommandBlock cmdb = (CommandBlock)e.getBlock().getState();
        		cmdb.getCommand();
        		for(Player player: Bukkit.getServer().getOnlinePlayers()){
        			if(Cmdsearch.start.containsKey(player.getName())){
        				if(cmdb.getCommand().startsWith(Cmdsearch.start.get(player.getName()))){
        					double min = 1.79769313486231570E+308;
        		        	Player min_player = null;
        					for(Player p: Bukkit.getServer().getOnlinePlayers()){
        						Location location_p = p.getLocation();
        		            	double distance = cmdb.getLocation().distance(location_p);
        		            	if(distance < min){
        		            		min = distance;
        		            		min_player = p;
        		            	}
        		        	}
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Found Cmdb(start)");
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "XYZ: " + cmdb.getX() + " " + cmdb.getY() + " " + cmdb.getZ());
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Near Player: " + min_player.getName() + " (" + min + "Block)");
        				}
    				}
    				if(Cmdsearch.end.containsKey(player.getName())){
    					if(cmdb.getCommand().endsWith(Cmdsearch.start.get(player.getName()))){
        					double min = 1.79769313486231570E+308;
        		        	Player min_player = null;
        					for(Player p: Bukkit.getServer().getOnlinePlayers()){
        						Location location_p = p.getLocation();
        		            	double distance = cmdb.getLocation().distance(location_p);
        		            	if(distance < min){
        		            		min = distance;
        		            		min_player = p;
        		            	}
        		        	}
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Found Cmdb(end)");
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "XYZ: " + cmdb.getX() + " " + cmdb.getY() + " " + cmdb.getZ());
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Near Player: " + min_player.getName() + " (" + min + "Block)");
        				}
    				}
    				if(Cmdsearch.in.containsKey(player.getName())){
    					if(cmdb.getCommand().indexOf(Cmdsearch.start.get(player.getName())) != -1){
        					double min = 1.79769313486231570E+308;
        		        	Player min_player = null;
        					for(Player p: Bukkit.getServer().getOnlinePlayers()){
        						Location location_p = p.getLocation();
        		            	double distance = cmdb.getLocation().distance(location_p);
        		            	if(distance < min){
        		            		min = distance;
        		            		min_player = p;
        		            	}
        		        	}
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Found Cmdb(in)");
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "XYZ: " + cmdb.getX() + " " + cmdb.getY() + " " + cmdb.getZ());
        					player.sendMessage("[CmdSearch] " + ChatColor.GREEN + "Near Player: " + min_player + " (" + min + "Block)");
        				}
    				}
            	}
        	}
    	}
    }
}
