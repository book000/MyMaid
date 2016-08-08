package xyz.jaoafa.mymaid.EventHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Command.Prison;

public class OnPlayerJoinEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerJoinEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
  		if(Bukkit.getServer().getOnlinePlayers().size() >= 1 && !MyMaid.nextbakrender){
  			MyMaid.nextbakrender = true;
  		}
  		UUID uuid = event.getPlayer().getUniqueId();
  		String data = Method.url_jaoplugin("joinvote", "u="+uuid);
  		String[] arr = data.split("###", 0);
  		String result = arr[0];
  		MyMaid.chatcolor.put(event.getPlayer().getName(), arr[1]);
  		if(!result.equalsIgnoreCase("null")){
  			event.setJoinMessage(ChatColor.YELLOW + event.getPlayer().getName() + ChatColor.YELLOW + ", " + ChatColor.YELLOW +result+" joined the game.");
  		}
  		if(event.getPlayer().hasPermission("mymaid.pex.limited")){
  			event.setJoinMessage(ChatColor.RED + event.getPlayer().getName() + ChatColor.YELLOW + " joined the game.");
		}
  		Date Date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GRAY + "■" + ChatColor.WHITE + "jaotan: 現在『" + Bukkit.getServer().getOnlinePlayers().size() + "人』がログインしています");
		Player player = event.getPlayer();
		new TabListSKKReloader(plugin, player).runTaskLater(plugin, 10L);

  	}
	private class TabListSKKReloader extends BukkitRunnable{
    	Player player;
    	public TabListSKKReloader(JavaPlugin plugin, Player player) {
    		this.player = player;
    	}
		@Override
		public void run() {
			if(player.hasPermission("mymaid.pex.limited")){
				player.setPlayerListName(ChatColor.BLACK + "■" + ChatColor.WHITE + player.getName());
			}else if(Prison.prison.containsKey(player.getName())){
				player.setPlayerListName(ChatColor.DARK_GRAY + "■" + ChatColor.WHITE + player.getName());
			}else if(MyMaid.chatcolor.containsKey(player.getName())){
				int i = Integer.parseInt(MyMaid.chatcolor.get(player.getName()));
				if(i >= 0 && i <= 5){
					player.setPlayerListName(ChatColor.WHITE + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 6 && i <= 19){
					player.setPlayerListName(ChatColor.DARK_BLUE + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 20 && i <= 33){
					player.setPlayerListName(ChatColor.BLUE + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 34 && i <= 47){
					player.setPlayerListName(ChatColor.AQUA + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 48 && i <= 61){
					player.setPlayerListName(ChatColor.DARK_AQUA  + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 62 && i <= 76){
					player.setPlayerListName(ChatColor.DARK_GREEN + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 77 && i <= 89){
					player.setPlayerListName(ChatColor.GREEN + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 90 && i <= 103){
					player.setPlayerListName(ChatColor.DARK_GRAY + "■" + ChatColor.YELLOW + player.getName());
				}else if(i >= 104 && i <= 117){
					player.setPlayerListName(ChatColor.GOLD + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 118 && i <= 131){
					player.setPlayerListName(ChatColor.RED + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 132 && i <= 145){
					player.setPlayerListName(ChatColor.DARK_RED + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 146 && i <= 159){
					player.setPlayerListName(ChatColor.DARK_PURPLE + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 160){
					player.setPlayerListName(ChatColor.LIGHT_PURPLE + "■" + ChatColor.WHITE + player.getName());
				}
			}else{
				player.setPlayerListName(ChatColor.GRAY + "■" + ChatColor.WHITE + player.getName());
			}
		}
	}
}
