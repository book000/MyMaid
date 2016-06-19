package xyz.jaoafa.mymaid.Command;

import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

public class MyMaid_NetworkApi implements CommandExecutor {
	JavaPlugin plugin;
	public MyMaid_NetworkApi(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("unchecked")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		/*
		if (!(sender instanceof RemoteConsoleCommandSender)) {
			sender.sendMessage("[MYMAID_NETWORKAPI] " + ChatColor.GREEN + "You do not have permission to perform this command.");
			return true;
		}
		*/
		if(args.length != 2){
			sender.sendMessage("args err");
		}
		if(args[0].equalsIgnoreCase("server/user")){
			Player player = plugin.getServer().getPlayer(args[1]);

			double health = player.getHealth();
			double maxhealth = player.getMaxHealth();
			Location location = player.getLocation();
			Location eye = player.getEyeLocation();
			double exp = player.getExp();
			GameMode gamemode = player.getGameMode();
			Inventory inventory = player.getInventory();
			Inventory enderinvenory = player.getEnderChest();
			double foodlevel = player.getFoodLevel();
			String world = player.getWorld().getName();

			JSONObject invobj = new JSONObject();
			ItemStack[] is = inventory.getContents();

			for(int n=0; n != is.length; n++)
			{
				JSONObject invitemobj = new JSONObject();
				try{
					invitemobj.put("amount", is[n].getAmount());
					invitemobj.put("item", is[n].getType().name());
					invitemobj.put("name", is[n].getItemMeta().getDisplayName());
					invitemobj.put("lore", is[n].getItemMeta().hasLore());
					invitemobj.put("maxstack", is[n].getMaxStackSize());
					JSONObject enchant = new JSONObject();
					Map<Enchantment, Integer> enchantMap = is[n].getEnchantments();
					for(Map.Entry<Enchantment, Integer> e : enchantMap.entrySet()) {
					    enchant.put(e.getKey().getName(), e.getValue());
					}
					invitemobj.put("enchant", enchant);
				}catch(java.lang.NullPointerException e){

				}

				invobj.put(n, invitemobj);
			}

			JSONObject endinvobj = new JSONObject();
			is = enderinvenory.getContents();

			for(int n=0; n != is.length; n++)
			{
				JSONObject invitemobj = new JSONObject();
				try{
					invitemobj.put("amount", is[n].getAmount());
					invitemobj.put("item", is[n].getType().name());
					invitemobj.put("name", is[n].getItemMeta().getDisplayName());
					invitemobj.put("lore", is[n].getItemMeta().hasLore());
					invitemobj.put("maxstack", is[n].getMaxStackSize());
					JSONObject enchant = new JSONObject();
					Map<Enchantment, Integer> enchantMap = is[n].getEnchantments();
					for(Map.Entry<Enchantment, Integer> e : enchantMap.entrySet()) {
					    enchant.put(e.getKey().getName(), e.getValue());
					}
					invitemobj.put("enchant", enchant);
				}catch(java.lang.NullPointerException e){

				}

				endinvobj.put(n, invitemobj);
			}

			JSONObject obj = new JSONObject();
			obj.put("health", health);
			obj.put("maxhealth", maxhealth);
			obj.put("locationx", location.getX());
			obj.put("locationy", location.getY());
			obj.put("locationz", location.getZ());
			obj.put("eyex", eye.getX());
			obj.put("eyey", eye.getY());
			obj.put("eyez", eye.getZ());
			obj.put("exp", exp);
			obj.put("gamemode", gamemode.name());
			obj.put("inventory", invobj);
			obj.put("enderinvenory", endinvobj);
			obj.put("foodlevel", foodlevel);
			obj.put("world", world);
			sender.sendMessage(obj.toJSONString());
		}
		return true;
	}
}
