package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
		final Player player = (Player) sender;
		ItemStack[] is=sender.getServer().getPlayer(sender.getName()).getInventory().getArmorContents();
		if(is[3].getType() == Material.ICE){
			ItemStack[] after={
					new ItemStack(is[0]),
					new ItemStack(is[1]),
					new ItemStack(is[2]),
					new ItemStack(Material.AIR)};
			player.getInventory().setArmorContents(after);
			player.updateInventory();
			sender.sendMessage("[AFK] " + ChatColor.GREEN + "AFK false");
			Bukkit.dispatchCommand(sender, "gamerule sendCommandFeedback false");

			Bukkit.dispatchCommand(sender, "title " + player.getName() + " reset");
			Bukkit.dispatchCommand(sender, "gamerule sendCommandFeedback true");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + sender.getName() + " is now online!");
		}else{
			ItemStack[] after={
					new ItemStack(is[0]),
					new ItemStack(is[1]),
					new ItemStack(is[2]),
					new ItemStack(Material.ICE)};
			player.getInventory().setArmorContents(after);
			player.updateInventory();
			sender.sendMessage("[AFK] " + ChatColor.GREEN + "AFK true");
			
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " is afk!");
			try {
				new BukkitRunnable() {
					@Override
					public void run() {
						player.getWorld().playSound(player.getLocation(),Sound.EXPLODE,1,1);
					}
				}.runTaskLater(plugin, 20);
			}catch(Exception e){
				Bukkit.dispatchCommand(sender, "gamerule sendCommandFeedback false");
				Bukkit.dispatchCommand(sender, "title " + player.getName() + " times 0 2147483647 0");
				Bukkit.dispatchCommand(sender, "title " + player.getName() + " subtitle {text:\"When you are back, please enter the command '/afk'.\",color:blue,bold:true}");
				Bukkit.dispatchCommand(sender, "title " + player.getName() + " title {text:\"AFK NOW!\",color:red,bold:true}");
				Bukkit.dispatchCommand(sender, "gamerule sendCommandFeedback true");
			}
			
		}
		return true;
	}
}
