package xyz.jaoafa.mymaid.EventHandler;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Command.AFK;
import xyz.jaoafa.mymaid.Command.MyBlock;

public class OnQuitGame implements Listener {
	JavaPlugin plugin;
	public OnQuitGame(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuitGame(PlayerQuitEvent event){
  		Player player = event.getPlayer();
  		if((Bukkit.getServer().getOnlinePlayers().size() - 1) == 0 && MyMaid.nextbakrender){
  			MyMaid.nextbakrender = false;
  			OnExplosion.tntexplode = true;
  		}
  		for(Player p: Bukkit.getServer().getOnlinePlayers()){
  			if(MyBlock.myblock.containsKey(p.getName())){
  				player.showPlayer(p);
  			}
		}
  		if(MyBlock.myblock.containsKey(player.getName())){
  			for(Player p: Bukkit.getServer().getOnlinePlayers()){
				p.showPlayer(player);
			}
  			MyBlock.myblock.remove(player.getName());
  		}
  		if(AFK.tnt.containsKey(player.getName())){
  			if(AFK.tnt.get(player.getName()) != null){
  				AFK.tnt.get(player.getName()).cancel();
  			}
  			AFK.tnt.remove(player.getName());
  		}
  		MyMaid.TitleSender.resetTitle(player);
  		ItemStack[] is = player.getInventory().getArmorContents();
		if(is[3].getType() == Material.ICE){
			ItemStack[] after={
					new ItemStack(is[0]),
					new ItemStack(is[1]),
					new ItemStack(is[2]),
					new ItemStack(Material.AIR)};
			player.getInventory().setArmorContents(after);
			player.updateInventory();
		}
		//Dynmap_Teleporter.dynamic_teleporter.get(player.getName()).cancel();
		//Dynmap_Teleporter.dynamic_teleporter.remove(player.getName());
		Date Date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GOLD + "■" + ChatColor.WHITE + "jaotan: 現在『" + (Bukkit.getServer().getOnlinePlayers().size() - 1) + "人』がログインしています");
		InetAddress ip = player.getAddress().getAddress();
		String name = player.getName();
		UUID uuid = player.getUniqueId();
		String host = player.getAddress().getHostName();
		//Bukkit.broadcastMessage(player.getPlayerTime()+"");
		Bukkit.getLogger().info("------------------------------------------");
		Bukkit.getLogger().info("Player:"+name+" Log out.");
		Bukkit.getLogger().info("PlayerUUID:"+uuid);
		Bukkit.getLogger().info("PlayerIP:"+ip);
		Bukkit.getLogger().info("PlayerHost:"+host);
		Bukkit.getLogger().info("------------------------------------------");
		try{
			new netaccess(plugin, player).runTaskAsynchronously(plugin);
		}catch(java.lang.NoClassDefFoundError e){
			BugReport.report(e);
		}
  	}
	private class netaccess extends BukkitRunnable{
		Player player;
    	public netaccess(JavaPlugin plugin, Player player) {
    		this.player = player;
    	}
		@Override
		public void run() {
			Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
			String pex = "";
			for(String group : groups){
				if(PermissionsEx.getUser(player).inGroup(group)){
					if(PermissionsEx.getUser(player).inGroup("Default")){
						if(PermissionsEx.getUser(player).inGroup("Regular")){
							pex = "Regular";
						}
					}else{
						pex = group;
					}
				}
			}
			Method.url_jaoplugin("pex", "p="+player.getName()+"&u="+player.getUniqueId()+"&pex="+pex);
		}
	}
}
