package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OnSignClick implements Listener {
	JavaPlugin plugin;
	public OnSignClick(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onSignClick(PlayerInteractEvent event) {
	    if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
	    if(event.getPlayer().getItemInHand().getType() != Material.STICK) return;
	    Block clickedBlock = event.getClickedBlock();
	    Material material = clickedBlock.getType();
	    if (material == Material.SIGN_POST || material == Material.WALL_SIGN) {
	        Sign sign = (Sign) clickedBlock.getState();
	        xyz.jaoafa.mymaid.Command.Sign.signlist.put(event.getPlayer().getName(), sign);
	        int x = sign.getX();
	        int y = sign.getY();
	        int z = sign.getZ();
	        event.getPlayer().sendMessage("[Sign] " + ChatColor.GREEN + "看板を選択しました。[" + x + " " + y + " " + z + "]");
	    }
	}
}
