package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import eu.manuelgu.discordmc.MessageAPI;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Command.AFK;

public class OnPlayerMoveAFK implements Listener {
	JavaPlugin plugin;
	public OnPlayerMoveAFK(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e){
		Player player = e.getPlayer();
		MyMaid.afktime.put(player.getName(), System.currentTimeMillis());
		if(!AFK.tnt.containsKey(player.getName())){
			return;
	   	}
		AFK.tnt.get(player.getName()).cancel();
		AFK.tnt.remove(player.getName());
		ItemStack[] is = player.getInventory().getArmorContents();
		if(is[3].getType() == Material.ICE){
			ItemStack[] after={
				new ItemStack(is[0]),
				new ItemStack(is[1]),
				new ItemStack(is[2]),
				new ItemStack(Material.AIR)};
			player.getInventory().setArmorContents(after);
			player.updateInventory();
		}
		MyMaid.TitleSender.resetTitle(player);
		Bukkit.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " is now online!");
		MessageAPI.sendToDiscord(player.getName() + " is now online!");
	}
}
