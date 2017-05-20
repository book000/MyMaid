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

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Discord.Discord;

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
			if(tnt.containsKey(player.getName())){
				if(tnt.get(player.getName()) != null){
					tnt.get(player.getName()).cancel();
				}
			}
			AFK.tnt.remove(player.getName());
			MyMaid.TitleSender.resetTitle(player);
			Discord.send(sender.getName() + " is now online!");
			Bukkit.broadcastMessage(ChatColor.DARK_GRAY + sender.getName() + " is now online!");
			String listname = player.getPlayerListName().replaceAll(player.getName(), ChatColor.WHITE + player.getName());
			player.setPlayerListName(listname);
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
			Discord.send(player.getName() + " is afk!");
			if(tnt.containsKey(player.getName())){
				if(tnt.get(player.getName()) != null){
					tnt.get(player.getName()).cancel();
				}
			}
			AFK.tnt.remove(player.getName());
			String listname = player.getPlayerListName().replaceAll(player.getName(), ChatColor.DARK_GRAY + player.getName());
			player.setPlayerListName(listname);
			MyMaid.TitleSender.setTime_tick(player, 0, 99999999, 0);
			MyMaid.TitleSender.sendTitle(player, ChatColor.RED + "AFK NOW!", ChatColor.BLUE + "" + ChatColor.BOLD + "When you are back, please enter the command '/afk'.");
			MyMaid.TitleSender.setTime_tick(player, 0, 99999999, 0);
			try{
				BukkitTask task = new AFK.afking(plugin, player).runTaskTimer(plugin, 0L, 5L);
				AFK.tnt.put(player.getName(), task);
			}catch(java.lang.NoClassDefFoundError e){
				BugReport.report(e);
				AFK.tnt.put(player.getName(), null);
			}
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
			String listname = player.getPlayerListName();
			if(!listname.contains(ChatColor.DARK_GRAY + player.getName())){
				listname = listname.replaceAll(player.getName(), ChatColor.DARK_GRAY + player.getName());
				player.setPlayerListName(listname);
			}
		}
	}
}
