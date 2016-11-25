package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class OnHeadClick implements Listener {
	JavaPlugin plugin;
	public OnHeadClick(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onHeadClick(PlayerInteractEvent event) {
	    if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
	    if(event.getPlayer().getItemInHand().getType() != Material.STICK) return;
	    Block clickedBlock = event.getClickedBlock();
	    Material material = clickedBlock.getType();
	    if (material == Material.SKULL) {
	        Skull skull = (Skull) clickedBlock.getState();
	        Player player = event.getPlayer();
	        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw "+player.getName()+" [\"\",{\"text\":\"このユーザー「"+skull.getOwner()+"」のユーザーページを開く\",\"color\":\"aqua\",\"underlined\":true,\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://jaoafa.com/user/"+skull.getOwner()+"\"}}]");
	    }
	}
}
