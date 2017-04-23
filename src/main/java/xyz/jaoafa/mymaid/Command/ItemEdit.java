package xyz.jaoafa.mymaid.Command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class ItemEdit implements CommandExecutor {
	JavaPlugin plugin;
	public ItemEdit(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		/*
		  * /itemedit name <Name>
		  * /itemedit lore <Lore>
		 */
		ItemStack is = player.getInventory().getItemInHand();
		if(is.getType() == Material.AIR){
			Method.SendMessage(sender, cmd, "何かブロックを持ってください。");
			return true;
		}
		if(args.length >= 2){
			if(args[0].equalsIgnoreCase("name")){
				ItemMeta meta = is.getItemMeta();
				String text = "";
				int c = 1;
				while(args.length > c){
					text += args[c];
					if(args.length != (c+1)){
						text += " ";
					}
					c++;
				}
				meta.setDisplayName(text);
				is.setItemMeta(meta);
				player.getInventory().setItemInHand(is);
				player.updateInventory();
				Method.SendMessage(sender, cmd, "Nameを更新しました。");
				return true;
			}else if(args[0].equalsIgnoreCase("lore")){
				ItemMeta meta = is.getItemMeta();
				List<String> lore = new ArrayList<String>();
				int c = 1;
				while(args.length > c){
					lore.add(args[c]);
					c++;
				}
				meta.setLore(lore);
				is.setItemMeta(meta);
				player.getInventory().setItemInHand(is);
				player.updateInventory();
				Method.SendMessage(sender, cmd, "Loreを更新しました。");
				return true;
			}
		}
		Method.SendMessage(sender, cmd, "----- ItemEdit -----");
		Method.SendMessage(sender, cmd, "/itemedit name <Name...>: 持っているアイテムのNameを変更します。");
		Method.SendMessage(sender, cmd, "/itemedit lore <Lore...>: 持っているアイテムのLoreを変更します。");
		return true;
	}
}
