package com.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Command.Prison;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class OnBlockBreakEvent implements Listener {
	JavaPlugin plugin;
	public OnBlockBreakEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e){
    	Player player = e.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(PermissionsEx.getUser(player).inGroup("QPPE")){
    		if(e.getBlock().getType() == Material.COMMAND){
				player.sendMessage("[COMMANDCHECK] " + ChatColor.GREEN + "QPPE権限ではコマンドブロックを破壊することができません。");
				e.setCancelled(true);
			}
		}
    	if(PermissionsEx.getUser(player).inGroup("Default")){
			if(!PermissionsEx.getUser(player).inGroup("Regular")){
				if(e.getBlock().getType() == Material.COMMAND){
					player.sendMessage("[COMMANDCHECK] " + ChatColor.GREEN + "Default権限ではコマンドブロックを破壊することができません。");
					e.setCancelled(true);
				}
			}
		}
  		if(!Prison.prison.containsKey(player.getName())){
  			return;
  		}
  		if(Prison.prison_block.get(player.getName())){
  			return;
  		}
  		e.setCancelled(true);
  		player.sendMessage("[JAIL] " + ChatColor.GREEN + "あなたはブロックを壊せません。");
  		Bukkit.getLogger().info("[JAIL] "+player.getName()+"==>あなたはブロックを壊せません。");
    }
}
