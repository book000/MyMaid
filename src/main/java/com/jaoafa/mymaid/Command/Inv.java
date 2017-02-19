package com.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Method;

public class Inv implements CommandExecutor {
	JavaPlugin plugin;
	public Inv(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player p = (Player) sender;
		if(args.length == 1){
			for(Player player: Bukkit.getServer().getOnlinePlayers()) {
				if(player.getName().equalsIgnoreCase(args[0])){
					PlayerInventory inv = player.getInventory();
					Inventory inventory = Bukkit.getServer().createInventory(null, InventoryType.PLAYER, player.getName() + "のインベントリ");
					inventory.setItem(27, inv.getItem(0));
					inventory.setItem(28, inv.getItem(1));
					inventory.setItem(29, inv.getItem(2));
					inventory.setItem(30, inv.getItem(3));
					inventory.setItem(31, inv.getItem(4));
					inventory.setItem(32, inv.getItem(5));
					inventory.setItem(33, inv.getItem(6));
					inventory.setItem(34, inv.getItem(7));
					inventory.setItem(35, inv.getItem(8));
					ItemStack[] invdata = inv.getContents();
					for(int n=0; n != invdata.length; n++)
					{
						if(n <= 8) continue;
						inventory.setItem(n-9, inv.getItem(n));
					}
					p.openInventory(inventory);
					return true;
				}
			}
			Method.SendMessage(sender, cmd, "ユーザーが見つかりませんでした。");
			return true;
		}else{
			Method.SendMessage(sender, cmd, "引数が適していません。");
			return true;
		}
	}
}
