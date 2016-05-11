package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AFK implements CommandExecutor{
	JavaPlugin plugin;
	public AFK(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			sender.sendMessage("[DT] " + ChatColor.GREEN + "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		ItemStack[] is=sender.getServer().getPlayer(sender.getName()).getInventory().getArmorContents();
		if(is[3].getType() == Material.ICE){
			ItemStack[] after={
					new ItemStack(is[0]),
					new ItemStack(is[1]),
					new ItemStack(is[2]),
					new ItemStack(Material.AIR)};
			sender.getServer().getPlayer(sender.getName()).getInventory().setArmorContents(after);
			sender.getServer().getPlayer(sender.getName()).updateInventory();
			sender.sendMessage("[AFK] " + ChatColor.GREEN + "AFK false");
			Bukkit.dispatchCommand(sender, "title " + sender.getName() + " reset");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + sender.getName() + " is now online!");
		}else{
			ItemStack[] after={
					new ItemStack(is[0]),
					new ItemStack(is[1]),
					new ItemStack(is[2]),
					new ItemStack(Material.ICE)};
			sender.getServer().getPlayer(sender.getName()).getInventory().setArmorContents(after);
			sender.getServer().getPlayer(sender.getName()).updateInventory();
			sender.sendMessage("[AFK] " + ChatColor.GREEN + "AFK true");
			Bukkit.dispatchCommand(sender, "title " + sender.getName() + " times 0 2147483647 0");
			Bukkit.dispatchCommand(sender, "title " + sender.getName() + " subtitle {text:\"When you are back, please enter the command '/afk'.\",color:blue}");
			Bukkit.dispatchCommand(sender, "title " + sender.getName() + " title {text:\"AFK NOW!\",color:red}");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + sender.getName() + " is afk!");
		}
		return true;
	}
}
