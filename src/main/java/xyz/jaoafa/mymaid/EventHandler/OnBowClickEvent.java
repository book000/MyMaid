package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class OnBowClickEvent implements Listener {
	JavaPlugin plugin;
	public OnBowClickEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		ItemStack is = event.getCurrentItem();
		HumanEntity he = event.getWhoClicked();
		if(he.getType() != EntityType.PLAYER){
			return;
		}
		Player player = (Player) he;
		if(is == null){
			return;
		}
		if(is.getType() == Material.BOW){
			if(!player.hasPermission("modifyworld.items.have.bow")){
				Method.SendTipsPlayer(player, "弓が使えない？ルールを読んでみることをお勧めします！ https://jaoafa.com/rule");
			}
		}
	}
}
