package xyz.jaoafa.mymaid.Command;

import java.lang.management.ManagementFactory;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Data implements CommandExecutor {
	JavaPlugin plugin;
	public Data(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		// time をなんか更新
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		int msec = cal.get(Calendar.MILLISECOND);

		sender.sendMessage("[DATA] " + ChatColor.GREEN + "uptime: " + year + "/" + month + "/" + day + " " + hour + ":" + min + ":" + sec + "." + msec);
		sender.sendMessage("[DATA] " + ChatColor.GREEN + "Runtimemax : " + (Runtime.getRuntime().maxMemory() / 1024 / 1024));
		sender.sendMessage("[DATA] " + ChatColor.GREEN + "Runtimetotal: " + (Runtime.getRuntime().totalMemory() / 1024 / 1024));
		sender.sendMessage("[DATA] " + ChatColor.GREEN + "Runtimefree: " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		List<World> worlds = Bukkit.getServer().getWorlds();
		for (World w : worlds)
		{
			String Name = w.getName();
			String worldType = null;
			switch (w.getEnvironment())
			{
			case NORMAL:
				worldType = "Normal";
				break;
			case NETHER:
				worldType = "Nether";
				break;
			case THE_END:
				worldType = "The End";
				break;
			default:
				break;
			}

			int tileEntities = 0;

			try
			{
				for (Chunk chunk : w.getLoadedChunks())
				{
					tileEntities += chunk.getTileEntities().length;
				}
			}
			catch (java.lang.ClassCastException ex)
			{
				Bukkit.getLogger().log(Level.SEVERE, "Corrupted chunk data on world " + w, ex);
			}
			sender.sendMessage("[DATA] " + ChatColor.GREEN + "World: " + Name + "(" + worldType + ") LoadedChunk: " + w.getLoadedChunks().length + " Size: " + w.getEntities().size() + " tileEntity: " + tileEntities);
		}


		return true;
	}
}
