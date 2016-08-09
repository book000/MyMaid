package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.jaoafa.mymaid.Method;


public class Unko implements CommandExecutor {
	JavaPlugin plugin;
	public Unko(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		int count = 1;
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		Bukkit.broadcastMessage("DOOMING DOOMING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! FOOOOOOOOOOOOOO!!!!!!!!!!!!!!! OREWA " + player.getName() + "!!!!!!!!!!!!!!!!!!!!");
		for(Player to: Bukkit.getServer().getOnlinePlayers()) {
			new Unko_Unko(plugin, player, to).runTaskLater(plugin, (20 * count));
			count++;
		}
		return true;
	}
	private class Unko_Unko extends BukkitRunnable{
    	Player player;
    	Player to;
    	public Unko_Unko(JavaPlugin plugin, Player player, Player to) {
    		this.player = player;
    		this.to = to;
    	}
		@Override
		public void run() {
			player.teleport(to);
		}
	}
}
