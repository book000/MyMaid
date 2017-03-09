package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class OnInventoryCreativeEvent {
	JavaPlugin plugin;
	public OnInventoryCreativeEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryCreativeEvent(InventoryCreativeEvent event) {
		ItemStack is = event.getCurrentItem();
		HumanEntity he = event.getWhoClicked();
		if(he.getType() != EntityType.PLAYER){
			return;
		}
		Player player = (Player) he;
		if(is.getType() == Material.BOW){
			if(!player.hasPermission("modifyworld.items.have.bow")){
				Method.SendTipsPlayer(player, "弓が使えない？ルールを読んでみることをお勧めします！ https://jaoafa.com/rule");
			}
		}
	}
}
