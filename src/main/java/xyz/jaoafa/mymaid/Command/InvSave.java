package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class InvSave implements CommandExecutor {
	JavaPlugin plugin;
	public InvSave(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String,Map<Integer, Map<String,Object>>> Saveinv = new HashMap<String,Map<Integer, Map<String,Object>>>();
	public static Map<String,Map<Integer, Map<String,Object>>> Savearmor = new HashMap<String,Map<Integer, Map<String,Object>>>();
	public static Map<String,Location> Savebed = new HashMap<String,Location>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length != 1){
			Method.SendMessage(sender, cmd, "引数が不正です。");
			return true;
		}
		for(Player player: Bukkit.getServer().getOnlinePlayers()) {
			if(player.getName().equalsIgnoreCase(args[0])){
				ItemStack[] pi = player.getInventory().getContents();
				ItemStack[] armor = player.getInventory().getArmorContents();
				Location bed = player.getBedSpawnLocation();

				Map<Integer, Map<String,Object>> inv = new HashMap<Integer, Map<String,Object>>();
				int count = 0;
				if(pi.length != 0){
					for(ItemStack it : pi){
						if(it != null){
							Map<String,Object> item = new HashMap<String,Object>();
							item.put("Material", it.getType());
							item.put("Amount", it.getAmount());
							item.put("Data", it.getData());
							item.put("Durability", it.getDurability());
							item.put("Enchantments", it.getEnchantments());
							item.put("ItemMeta", it.getItemMeta());
	                        inv.put(count, item);
	                    }
						count++;
					}
				}

				Saveinv.put(player.getName(), inv);
				Map<Integer, Map<String,Object>> arm = new HashMap<Integer, Map<String,Object>>();
				count = 0;
				if(armor.length != 0){
					for(ItemStack it : armor){
						if(it != null){
							Map<String,Object> item = new HashMap<String,Object>();
							item.put("Material", it.getType());
							item.put("Amount", it.getAmount());
							item.put("Data", it.getData());
							item.put("Durability", it.getDurability());
							item.put("Enchantments", it.getEnchantments());
							item.put("ItemMeta", it.getItemMeta());
	                        arm.put(count, item);
	                    }
						count++;
					}
				}

				Savearmor.put(player.getName(), arm);
				Savebed.put(player.getName(), bed);
				Method.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」を保存しました。");
				return true;
			}
		}
		Method.SendMessage(sender, cmd, "プレイヤーが見つかりません。");
		return true;
	}
}
