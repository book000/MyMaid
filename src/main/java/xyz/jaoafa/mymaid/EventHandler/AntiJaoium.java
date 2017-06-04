package xyz.jaoafa.mymaid.EventHandler;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import xyz.jaoafa.mymaid.Jail.Jail;

public class AntiJaoium implements Listener {
	JavaPlugin plugin;
	public AntiJaoium(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	/**
	 * jaoiumと判定されるアイテムかどうか
	 * @param list PotionEffectのList
	 * @return jaoiumかどうか
	 * @author mine_book000
	 */
	private boolean isjaoium(List<PotionEffect> list){
		Boolean jaoium = false;
		for (PotionEffect po : list) {
			if(po.getType().equals(PotionEffectType.HEAL)){
				if(po.getAmplifier() == 29){
					// アウト
					jaoium = true;
				}
				if(po.getAmplifier() == 125){
					// アウト
					jaoium = true;
				}
			}
			if(po.getType().equals(PotionEffectType.HEALTH_BOOST)){
				if(po.getAmplifier() == -7){
					// アウト
					jaoium = true;
				}
			}
		}
		return jaoium;
	}
	/**
	 * jaoiumと判定されるアイテムかどうか(ディスペンサーで放ってよいかどうか)
	 * @param list PotionEffectのList
	 * @return jaoiumかどうか
	 * @author mine_book000
	 */
	private boolean isjaoium_indispenser(List<PotionEffect> list){
		Boolean jaoium = false;
		for (PotionEffect po : list) {
			if(po.getType().equals(PotionEffectType.HEALTH_BOOST)){
				if(po.getAmplifier() == -7){
					// アウト
					jaoium = true;
				}
			}
		}
		return jaoium;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void InvClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getInventory();
		Inventory clickedinventory = event.getClickedInventory();
		ItemStack[] is = inventory.getContents();
		if(Jail.isJail(player)){
			return;
		}
		Boolean jaoium = false;
		for(int n=0; n != is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		if(clickedinventory != null) {
			is = clickedinventory.getContents();
			for(int n=0; n != is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					jaoium = isjaoium(potion.getCustomEffects());
					if(jaoium){
						inventory.clear(n);
					}
				}
			}
			if(jaoium){
				clickedinventory.clear();
			}
		}
		if(jaoium){
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event){
		Player player = event.getPlayer();
		Inventory inventory = player.getInventory();
		Inventory enderchestinventory = player.getEnderChest();
		ItemStack[] is = inventory.getContents();
		Boolean jaoium = false;
		for(int n=0; n != is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		if(enderchestinventory != null) {
			is = enderchestinventory.getContents();
			for(int n=0; n != is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					jaoium = isjaoium(potion.getCustomEffects());
					if(jaoium){
						inventory.clear(n);
					}
				}
			}
			if(jaoium){
				enderchestinventory.clear();
			}
		}
		if(jaoium){
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
		}
	}

    @SuppressWarnings("deprecation")
	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		Player player = event.getPlayer();
		Inventory inventory = player.getInventory();
		Inventory enderchestinventory = player.getEnderChest();
		ItemStack[] is = inventory.getContents();
		Boolean jaoium = false;
		for(int n=0; n != is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		if(enderchestinventory != null) {
			is = enderchestinventory.getContents();
			for(int n=0; n != is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					jaoium = isjaoium(potion.getCustomEffects());
					if(jaoium){
						inventory.clear(n);
					}
				}
			}
			if(jaoium){
				enderchestinventory.clear();
			}
		}
		if(jaoium){
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event){
		if (!(event.getEntity().getShooter() instanceof org.bukkit.entity.Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		Inventory inventory = player.getInventory();
		Inventory enderchestinventory = player.getEnderChest();
		ItemStack[] is = inventory.getContents();
		Boolean jaoium = false;
		for(int n=0; n != is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		if(enderchestinventory != null) {
			is = enderchestinventory.getContents();
			for(int n=0; n != is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					jaoium = isjaoium(potion.getCustomEffects());
					if(jaoium){
						inventory.clear(n);
					}
				}
			}
			if(jaoium){
				enderchestinventory.clear();
			}
		}
		if(jaoium){
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
			event.setCancelled(true);
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onPotionSplashEvent(PotionSplashEvent event){
		if (!(event.getEntity().getShooter() instanceof org.bukkit.entity.Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		Inventory inventory = player.getInventory();
		Inventory enderchestinventory = player.getEnderChest();
		ItemStack[] is = inventory.getContents();
		Boolean jaoium = false;
		for(int n=0; n != is.length; n++)
		{
			if(is[n] == null){
				continue;
			}
			ItemStack hand = is[n];
			if(hand.getType() == Material.POTION){
				PotionMeta potion = (PotionMeta) hand.getItemMeta();
				jaoium = isjaoium(potion.getCustomEffects());
				if(jaoium){
					inventory.clear(n);
				}
			}
		}
		if(jaoium){
			inventory.clear();
		}
		if(enderchestinventory != null) {
			is = enderchestinventory.getContents();
			for(int n=0; n != is.length; n++)
			{
				if(is[n] == null){
					continue;
				}
				ItemStack hand = is[n];
				if(hand.getType() == Material.POTION){
					PotionMeta potion = (PotionMeta) hand.getItemMeta();
					jaoium = isjaoium(potion.getCustomEffects());
					if(jaoium){
						inventory.clear(n);
					}
				}
			}
			if(jaoium){
				enderchestinventory.clear();
			}
		}
		if(jaoium){
			Bukkit.broadcastMessage("[jaoium_Checker] " + ChatColor.GREEN + "プレイヤー「" + player.getName() + "」からjaoiumと同等の性能を持つアイテムが検出されました。");
			Jail.JailAdd(player, Bukkit.getOfflinePlayer("jaotan"), "jaoium所持", true);
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void OnBlockDispenseEvent(BlockDispenseEvent event){
		Boolean jaoium = false;
		ItemStack is = event.getItem();
		if(is.getType() == Material.POTION){
			PotionMeta potion = (PotionMeta) is.getItemMeta();
			jaoium = isjaoium_indispenser(potion.getCustomEffects());
		}
		if(jaoium){
			event.setCancelled(true);
		}
	}
}
