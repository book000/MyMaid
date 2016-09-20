package xyz.jaoafa.mymaid.EventHandler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class DefaultCheck implements Listener {
	JavaPlugin plugin;
	public DefaultCheck(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String,Boolean> def = new HashMap<String,Boolean>();
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClickEvent(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getInventory();
		ItemStack[] is = inventory.getContents();
		Boolean flag = false;
		String item ="";
		for(int n=0; n != is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.TNT){
				flag = true;
				if(item.equalsIgnoreCase("")){
					item = "TNT";
				}else{
					item += ", TNT";
				}
			}
			if(hand.getType() == Material.LAVA_BUCKET){
				flag = true;
				if(item.equalsIgnoreCase("")){
					item = "LAVA_BUCKET";
				}else{
					item += ", LAVA_BUCKET";
				}
			}
			if(hand.getType() == Material.LAVA){
				flag = true;
				if(item.equalsIgnoreCase("")){
					item = "LAVA";
				}else{
					item += ", LAVA";
				}
			}
			if(hand.getType() == Material.WATER_BUCKET){
				flag = true;
				if(item.equalsIgnoreCase("")){
					item = "WATER_BUCKET";
				}else{
					item += ", WATER_BUCKET";
				}
			}
			if(hand.getType() == Material.WATER){
				flag = true;
				if(item.equalsIgnoreCase("")){
					item = "WATER";
				}else{
					item += ", WATER";
				}
			}
		}
		if(flag){
			if(!PermissionsEx.getUser(player).inGroup("Default")){
				return;
			}
			if(def.containsKey(player.getName())){
				return;
			}
			def.put(player.getName(), true);
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")){
					p.sendMessage("[DefaultCheck] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」がアイテム「" + item + "」を所持しています。");
				}
			}
		}
		}
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockPlacekEvent(BlockPlaceEvent event) {
			Player player = event.getPlayer();
			Block hand = event.getBlock();
			String item = "";
			Boolean flag = false;
			if(hand.getType() == Material.TNT){
				flag = true;
				if(item.equalsIgnoreCase("")){
					item = "TNT";
				}else{
					item += ", TNT";
				}
			}
			if(hand.getType() == Material.LAVA_BUCKET){
				flag = true;
				if(item.equalsIgnoreCase("")){
					item = "LAVA_BUCKET";
				}else{
					item += ", LAVA_BUCKET";
				}
			}
			if(hand.getType() == Material.LAVA){
				flag = true;
				if(item.equalsIgnoreCase("")){
					item = "LAVA";
				}else{
					item += ", LAVA";
				}
			}
			if(hand.getType() == Material.WATER_BUCKET){
				flag = true;
				if(item.equalsIgnoreCase("")){
					item = "WATER_BUCKET";
				}else{
					item += ", WATER_BUCKET";
				}
			}
			if(hand.getType() == Material.WATER){
				flag = true;
				if(item.equalsIgnoreCase("")){
					item = "WATER";
				}else{
					item += ", WATER";
				}
			}
			if(flag){
				if(!PermissionsEx.getUser(player).inGroup("Default")){
					return;
				}
				if(def.containsKey(player.getName())){
					return;
				}
				def.put(player.getName(), true);
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(PermissionsEx.getUser(p).inGroup("Admin")){
						p.sendMessage("[DefaultCheck] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」がアイテム「" + item + "」を設置しました。");
					}
				}

			}
	}
}
