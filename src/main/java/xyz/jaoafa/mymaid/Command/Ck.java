package xyz.jaoafa.mymaid.Command;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Ck implements CommandExecutor {
	JavaPlugin plugin;
	public Ck(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if ((sender instanceof BlockCommandSender)) {
			BlockCommandSender cmdb = (BlockCommandSender) sender;
			List<Entity> entitys = cmdb.getBlock().getWorld().getEntities();
			double min = 1.79769313486231570E+308;
        	org.bukkit.entity.Player player = null;
			for(org.bukkit.entity.Player p: Bukkit.getServer().getOnlinePlayers()){
				Location location_p = p.getLocation();
				double distance;
				try{
					distance = cmdb.getBlock().getLocation().distance(location_p);
				}catch(java.lang.IllegalArgumentException e){
					distance = -1;
				}

            	if(distance < min){
            		if(!p.getWorld().getName().equals(cmdb.getBlock().getWorld().getName())){
            			continue;
            		}
            		min = distance;
            		player = p;
            	}
        	}
			Minecart minecart = null;
			min = 1.79769313486231570E+308;
			for (Iterator<Entity> i = entitys.iterator(); i.hasNext();) {
				Entity entity = i.next();
				if ( !(entity instanceof RideableMinecart) ) {
					minecart = null;
		            continue;
		        }
				Location location_p = entity.getLocation();
				double distance;
				try{
					distance = cmdb.getBlock().getLocation().distance(location_p);
				}catch(java.lang.IllegalArgumentException e){
					continue;
				}

            	if(distance < min){
            		if(!entity.getWorld().getName().equals(cmdb.getBlock().getWorld().getName())){
            			minecart = null;
            			continue;
            		}
            		min = distance;
            		minecart = (Minecart) entity;
            	}
            	if(player == null){
            		Method.SendMessage(sender, cmd, "プレイヤーが見つかりません。");
            		return true;
            	}
            	if(minecart != null && player.isInsideVehicle() && player.getVehicle().getUniqueId().equals(minecart.getUniqueId())){
            		minecart = null;
					continue;
            	}
            	if(args.length == 1){
    				if(min > Integer.parseInt(args[0])){
    					minecart = null;
    					continue;
    				}
    			}
            	if(minecart != null){
            		minecart.remove();
            		Method.SendMessage(sender, cmd, "トロッコ「" + minecart.getName() + "(UUID:" + minecart.getUniqueId() +")」を削除しました。");
            	}
			}

			if(minecart == null){
				Method.SendMessage(sender, cmd, "削除できるトロッコはありませんでした。");
			}
			return true;

		}
		Method.SendMessage(sender, cmd, "コマンドブロックのみ実行可能です。");
		return true;
	}
}
