package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Command.Prison;

public class OnPlayerCommandPreprocessEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerCommandPreprocessEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e){
    	Player player = e.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
  		if(!Prison.prison.containsKey(player.getName())){
  			return;
  		}
  		String command = e.getMessage();
    	String[] args = command.split(" ", 0);
    	if(args.length >= 2){
    		if(args[0].equalsIgnoreCase("/jail")){
    			if(args[1].equalsIgnoreCase("lasttext")){
    				return;
    			}
    		}
		}
  		e.setCancelled(true);
  		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはコマンドを実行できません。");
  		Bukkit.getLogger().info("[JAIL] "+player.getName()+"==>あなたはコマンドを実行できません。");
    }
}
