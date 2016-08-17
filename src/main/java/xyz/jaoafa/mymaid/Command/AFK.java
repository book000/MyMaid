package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;

public class AFK implements CommandExecutor{
	JavaPlugin plugin;
	public AFK(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String,BukkitTask> tnt = new HashMap<String,BukkitTask>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		final Player player = (Player) sender;
		ItemStack[] is = player.getInventory().getArmorContents();
		if(is[3].getType() == Material.ICE){
			ItemStack[] after={
					new ItemStack(is[0]),
					new ItemStack(is[1]),
					new ItemStack(is[2]),
					new ItemStack(Material.AIR)};
			player.getInventory().setArmorContents(after);
			player.updateInventory();
			Method.SendMessage(sender, cmd, "AFK false");
			try {
				tnt.get(player.getName()).cancel();
			}catch(Exception e){

			}
			MyMaid.TitleSender.resetTitle(player);
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + sender.getName() + " is now online!");
		}else{
			ItemStack[] after={
					new ItemStack(is[0]),
					new ItemStack(is[1]),
					new ItemStack(is[2]),
					new ItemStack(Material.ICE)};
			player.getInventory().setArmorContents(after);
			player.updateInventory();
			Method.SendMessage(sender, cmd, "AFK true");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + " is afk!");
			try {
			tnt.get(player.getName()).cancel();
			}catch(Exception e){

			}
			MyMaid.TitleSender.setTime_tick(player, 0, 99999999, 0);
			MyMaid.TitleSender.sendTitle(player, ChatColor.RED + "AFK NOW!", ChatColor.BLUE + "" + ChatColor.BOLD + "When you are back, please enter the command '/afk'.");
			MyMaid.TitleSender.setTime_tick(player, 0, 99999999, 0);
			tnt.put(player.getName(), new afking(plugin, player).runTaskTimer(plugin, 0L, 5L));

		}
		return true;
	}
	static public class afking extends BukkitRunnable{
		JavaPlugin plugin;
		Player player;
    	public afking(JavaPlugin plugin, Player player) {
    		this.plugin = plugin;
    		this.player = player;
    	}
		@Override
		public void run() {
			//player.getWorld().playSound(player.getLocation(),Sound.EXPLODE,1,1);
			player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
		}

	}
}
