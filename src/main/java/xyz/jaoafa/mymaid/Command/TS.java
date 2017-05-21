package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Pointjao;

public class TS implements CommandExecutor {
	JavaPlugin plugin;
	public TS(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		Location startLoc = player.getLocation().subtract(32, 0, 32);

		int use = 100;
		if(!Pointjao.hasjao(player, use)){
		 	 Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
		 	 return true;
		}
		Pointjao.usejao(player, use, "tsコマンド実行の為");

		int count = 1;
		Throwable ex = null;

		for(int x = startLoc.getBlockX(); x < startLoc.getBlockX() + 32 * 2; x++){
			for(int y = startLoc.getBlockY(); y < startLoc.getBlockY() + 32 * 2; y++){
				for(int z = startLoc.getBlockZ(); z < startLoc.getBlockZ() + 32 * 2; z++){
					Location loc = new Location(startLoc.getWorld(), x, y, z);
					try{
						new TS_DOOM(plugin, loc).runTaskLater(plugin, (count));
						count++;
					}catch(java.lang.NoClassDefFoundError e){
						ex = e;
					}
				}
			}
		}
		if(ex != null){
			BugReport.report(ex);
		}
		return true;
	}
	private class TS_DOOM extends BukkitRunnable{
		Location loc;
    	public TS_DOOM(JavaPlugin plugin, Location loc) {
    		this.loc = loc;
    	}
		@Override
		public void run() {
			loc.getWorld().strikeLightning(loc);
		}
	}
}
