package xyz.jaoafa.mymaid.Command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Hat implements CommandExecutor {
	JavaPlugin plugin;
	public Hat(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof org.bukkit.entity.Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		PlayerInventory inv = player.getInventory();
		ItemStack hand = player.getItemInHand();
		if(hand.getType() == Material.AIR){
			Method.SendMessage(sender, cmd, "手にブロックを持ってください。");
			return true;
		}
		ItemStack head = inv.getHelmet();
		if(head.getType() != Material.AIR){
			inv.removeItem(head);
		}
		inv.setHelmet(hand);
		player.setItemInHand(head);
		Method.SendMessage(sender, cmd, "持っていたブロックを頭にのせました。");
		return true;
	}
}
