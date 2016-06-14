package xyz.jaoafa.mymaid.EventHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Command.AFK;

public class OnQuitGame implements Listener {
	JavaPlugin plugin;
	public OnQuitGame(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuitGame(PlayerQuitEvent event){
  		Player player = event.getPlayer();
  		if((Bukkit.getServer().getOnlinePlayers().size() - 1) == 0 && MyMaid.nextbakrender){
  			MyMaid.nextbakrender = false;
  		}
  		try {
  			AFK.tnt.get(player.getName()).cancel();
  		}catch(NullPointerException e){

  		}
  		player.sendMessage("[AFK] " + ChatColor.GREEN + "AFK false");
  		MyMaid.TitleSender.resetTitle(player);
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
		Date Date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GRAY + "■" + ChatColor.WHITE + "jaotan: 現在『" + (Bukkit.getServer().getOnlinePlayers().size() - 1) + "人』がログインしています");
  	}
}
