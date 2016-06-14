package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OnPlayerCommand implements Listener {
	JavaPlugin plugin;
	public OnPlayerCommand(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e){
    	String command = e.getMessage();
    	Player player = e.getPlayer();
    	String[] args = command.split(" ", 0);
    	if(args.length == 2){
    		if(args[0].equalsIgnoreCase("/kill")){
        		if(args[1].equalsIgnoreCase("@e")){
        			player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @eはサーバー内のすべてのエンティティが削除されてしまうので使用できません");
        			e.setCancelled(true);
        			return;
        		}
        		if(args[1].equalsIgnoreCase("@a")){
        			player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @aはサーバー内のすべてのプレイヤーが殺害されてしまうので使用できません");
        			e.setCancelled(true);
        			return;
        		}
        		if(args[1].startsWith("@e")){
        			if(player.hasPermission("mymaid.pex.provisional") || player.hasPermission("mymaid.pex.default")){
        				player.sendMessage("[COMMAND] " + ChatColor.GREEN + "└( ・з・)┘");
            			e.setCancelled(true);
            			return;
        			}
        		}
        	}
    		if(args[0].equalsIgnoreCase("/minecraft:kill")){
        		if(args[1].equalsIgnoreCase("@e")){
        			player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @eはサーバー内のすべてのエンティティが削除されてしまうので使用できません");
        			e.setCancelled(true);
        			return;
        		}
        		if(args[1].equalsIgnoreCase("@a")){
        			player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @aはサーバー内のすべてのプレイヤーが殺害されてしまうので使用できません");
        			e.setCancelled(true);
        			return;
        		}
        		if(args[1].startsWith("@e")){
        			if(player.hasPermission("mymaid.pex.provisional") || player.hasPermission("mymaid.pex.default")){
        				player.sendMessage("[COMMAND] " + ChatColor.GREEN + "└( ・з・)┘");
            			e.setCancelled(true);
            			return;
        			}
        		}
        	}
    	}
	}
}
