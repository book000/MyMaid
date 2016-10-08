package xyz.jaoafa.mymaid.EventHandler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
	public static Map<String,String> def = new HashMap<String,String>();
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
				if(hand.getType() == Material.FLINT_AND_STEEL){
					flag = true;
					if(item.equalsIgnoreCase("")){
						item = "FLINT_AND_STEEL";
					}else{
						item += ", FLINT_AND_STEEL";
					}
				}
				if(hand.getType() == Material.FIRE){
					flag = true;
					if(item.equalsIgnoreCase("")){
						item = "FIRE";
					}else{
						item += ", FIRE";
					}
				}
				if(hand.getType() == Material.FIREBALL){
					flag = true;
					if(item.equalsIgnoreCase("")){
						item = "FIREBALL";
					}else{
						item += ", FIREBALL";
					}
				}
			}
			if(flag){
				if(!PermissionsEx.getUser(player).inGroup("Default")){
					return;
				}
				if(PermissionsEx.getUser(player).inGroup("Regular")){
					return;
				}
				if(def.containsKey(player.getName())){
					if(!def.get(player.getName()).equalsIgnoreCase(item)){
						return;
					}
				}
				def.put(player.getName(), item);
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(PermissionsEx.getUser(p).inGroup("Admin")){
						p.sendMessage("[DefaultCheck] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」がアイテム「" + item + "」を所持しています。");
						p.sendMessage("[DefaultCheck] " + ChatColor.GREEN + "すぐにテレポートせず、様子を見てください。スペクテイターモードで確認するのもアリです。");
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
		if(hand.getType() == Material.FLINT_AND_STEEL){
			flag = true;
			if(item.equalsIgnoreCase("")){
				item = "FLINT_AND_STEEL";
			}else{
				item += ", FLINT_AND_STEEL";
			}
		}
		if(hand.getType() == Material.FIRE){
			flag = true;
			if(item.equalsIgnoreCase("")){
				item = "FIRE";
			}else{
				item += ", FIRE";
			}
		}
		if(hand.getType() == Material.FIREBALL){
			flag = true;
			if(item.equalsIgnoreCase("")){
				item = "FIREBALL";
			}else{
				item += ", FIREBALL";
			}
		}
		if(flag){
			if(!PermissionsEx.getUser(player).inGroup("Default")){
				return;
			}
			if(def.containsKey(player.getName())){
				if(!def.get(player.getName()).equalsIgnoreCase(item)){
					return;
				}
			}
			def.put(player.getName(), item);
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")){
					p.sendMessage("[DefaultCheck] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」がアイテム「" + item + "」を設置しました。");
					p.sendMessage("[DefaultCheck] " + ChatColor.GREEN + "すぐにテレポートせず、様子を見てください。スペクテイターモードで確認するのもアリです。");
					}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player)) {
			return;
		}
		Player damager = (Player) event.getDamager(); //殴った人
		Entity entity = event.getEntity(); //殴られたエンティティ
		if(entity.getType() == EntityType.WOLF){
			Wolf wolf = (Wolf)entity;
			AnimalTamer tamer = wolf.getOwner();
			if (tamer instanceof Player) {
				Player tamerplayer = (Player) tamer;
				wolf.setHealth(wolf.getMaxHealth());
				wolf.teleport(tamerplayer);
				tamerplayer.sendMessage("[DefaultCheck] " + ChatColor.GREEN + "プレイヤー「" + damager.getName() + "」があなたの飼っている狼を攻撃しようとしたのでテレポートさせました。");
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(PermissionsEx.getUser(p).inGroup("Admin")){
						p.sendMessage("[DefaultCheck] " + ChatColor.GREEN + "プレイヤー「" + damager.getName() + "」がプレイヤー「" + tamerplayer.getName() + "」の飼っている狼を攻撃しようとしたのでテレポートさせました。");
					}
				}
			}
			if (tamer instanceof OfflinePlayer) {
				OfflinePlayer tamerplayer = (OfflinePlayer) tamer;
				wolf.setHealth(wolf.getMaxHealth());
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(PermissionsEx.getUser(p).inGroup("Admin")){
						p.sendMessage("[DefaultCheck] " + ChatColor.GREEN + "プレイヤー「" + damager.getName() + "」がプレイヤー「" + tamerplayer.getName() + "」の飼っている狼を攻撃しようとしました。");
					}
				}
			}
		}else if(entity.getType() == EntityType.OCELOT){
			Ocelot ocelot = (Ocelot)entity;
			AnimalTamer tamer = ocelot.getOwner();
			if (tamer instanceof Player) {
				Player tamerplayer = (Player) tamer;
				ocelot.setHealth(ocelot.getMaxHealth());
				ocelot.teleport(tamerplayer);
				tamerplayer.sendMessage("[DefaultCheck] " + ChatColor.GREEN + "プレイヤー「" + damager.getName() + "」があなたの飼っている猫を攻撃しようとしたのでテレポートさせました。");
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(PermissionsEx.getUser(p).inGroup("Admin")){
						p.sendMessage("[DefaultCheck] " + ChatColor.GREEN + "プレイヤー「" + damager.getName() + "」がプレイヤー「" + tamerplayer.getName() + "」の飼っている猫を攻撃しようとしたのでテレポートさせました。");
					}
				}
			}
			if (tamer instanceof OfflinePlayer) {
				OfflinePlayer tamerplayer = (OfflinePlayer) tamer;
				ocelot.setHealth(ocelot.getMaxHealth());
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(PermissionsEx.getUser(p).inGroup("Admin")){
						p.sendMessage("[DefaultCheck] " + ChatColor.GREEN + "プレイヤー「" + damager.getName() + "」がプレイヤー「" + tamerplayer.getName() + "」の飼っている猫を攻撃しようとしました。");
					}
				}
			}
		}else{
			if(entity.isDead()){
				String name = "名前無";
				if(!entity.getName().equals("")){
					name = entity.getName();
				}
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(PermissionsEx.getUser(p).inGroup("Admin")){
						p.sendMessage("[DefaultCheck] " + ChatColor.GREEN + "プレイヤー「" + damager.getName() + "」が「" + name + "(" + entity.getType() + ")」を殺害しました。");
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBurnEvent(BlockBurnEvent event) {
		event.setCancelled(true);
	}

}
