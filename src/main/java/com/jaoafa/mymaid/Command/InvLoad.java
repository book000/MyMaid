package com.jaoafa.mymaid.Command;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Method;

public class InvLoad implements CommandExecutor {
	JavaPlugin plugin;
	public InvLoad(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("unchecked")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length != 1){
			Method.SendMessage(sender, cmd, "引数が不正です。");
			return true;
		}
		for(final Player player: Bukkit.getServer().getOnlinePlayers()) {
			if(player.getName().equalsIgnoreCase(args[0])){
				if(!InvSave.Saveinv.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」を復旧できません。");
					return true;
				}
				Map<Integer, Map<String, Object>> pi = InvSave.Saveinv.get(player.getName());
				Map<Integer, Map<String, Object>> armor = InvSave.Savearmor.get(player.getName());
				Location bed = InvSave.Savebed.get(player.getName());

				player.getInventory().clear();

				for(Map.Entry<Integer, Map<String, Object>> data : pi.entrySet()) {
					ItemStack item = new ItemStack((Material)data.getValue().get("Material"));
					item.setAmount((Integer)data.getValue().get("Amount"));
					item.setData((MaterialData)data.getValue().get("Data"));
					item.setDurability((short)data.getValue().get("Durability"));
					item.addEnchantments((Map<Enchantment, Integer>)data.getValue().get("Enchantments"));
					item.setItemMeta((ItemMeta)data.getValue().get("ItemMeta"));
					player.getInventory().setItem(data.getKey(), item);
				}
				for(Map.Entry<Integer, Map<String, Object>> data : armor.entrySet()) {
					ItemStack item = new ItemStack((Material)data.getValue().get("Material"));
					item.setAmount((Integer)data.getValue().get("Amount"));
					item.setData((MaterialData)data.getValue().get("Data"));
					item.setDurability((short)data.getValue().get("Durability"));
					item.addEnchantments((Map<Enchantment, Integer>)data.getValue().get("Enchantments"));
					item.setItemMeta((ItemMeta)data.getValue().get("ItemMeta"));
					if(data.getKey() == 0){
						player.getInventory().setHelmet(item);
					}else if(data.getKey() == 1){
						player.getInventory().setChestplate(item);
					}else if(data.getKey() == 2){
						player.getInventory().setLeggings(item);
					}else if(data.getKey() == 3){
						player.getInventory().setBoots(item);
					}else{
						Bukkit.broadcastMessage(data.getKey() + " " + data.getValue().get("Material"));
					}
				}
				player.setBedSpawnLocation(bed, true);
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						player.updateInventory();
					}
				}, 01L);
				Method.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」を復旧しました。");
				return true;
			}
		}
		Method.SendMessage(sender, cmd, "プレイヤーが見つかりません。");
		return true;
	}
}
