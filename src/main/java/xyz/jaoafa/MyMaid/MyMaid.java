package xyz.jaoafa.mymaid;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.jaoafa.mymaid.Command.Chat;
import xyz.jaoafa.mymaid.Command.Dynmap_Teleporter;
import xyz.jaoafa.mymaid.Command.Gamemode_Change;
import xyz.jaoafa.mymaid.Command.Ip_To_Host;
import xyz.jaoafa.mymaid.Command.Jf;
import xyz.jaoafa.mymaid.EventHandler.PlayerCommand;

public class MyMaid extends JavaPlugin implements Listener {
	Boolean nextbakrender = false;
	@Override
    public void onEnable() {
    	getLogger().info("(c) jao Minecraft Server MyMaid Project.");
    	getLogger().info("Product by tomachi.");
    	getServer().getPluginManager().registerEvents(this, this);
    	getServer().getPluginManager().registerEvents(new PlayerCommand(this), this);
		this.getServer().getScheduler().runTaskTimer(this, new World_saver(), 0L, 36000L);
		this.getServer().getScheduler().runTaskTimer(this, new Dynmap_Update_Render(), 0L, 36000L);

		getCommand("chat").setExecutor(new Chat(this));
		getCommand("jf").setExecutor(new Jf(this));
		getCommand("dt").setExecutor(new Dynmap_Teleporter(this));
		getCommand("g").setExecutor(new Gamemode_Change(this));
		getCommand("iphost").setExecutor(new Ip_To_Host(this));
    }

    @Override
    public void onDisable() {

    }
    private class World_saver extends BukkitRunnable{
		@Override
		public void run() {
			if(nextbakrender){
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
			}

		}
	}

    private class Dynmap_Update_Render extends BukkitRunnable{
		@Override
		public void run() {
			if(nextbakrender){
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "dynmap updaterender Jao_Afa 0 0");
			}
		}
	}

  	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
  		nextbakrender = true;
  	}
  	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuitGame(PlayerQuitEvent event){
  		nextbakrender = false;
  	}
}
