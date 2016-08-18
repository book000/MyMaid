package xyz.jaoafa.mymaid.EventHandler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Command.Prison;

public class OnAsyncPlayerChatEvent implements Listener {
	JavaPlugin plugin;
	public OnAsyncPlayerChatEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String,Integer> dotto = new HashMap<String,Integer>();
	public static Map<String,BukkitTask> dottotask = new HashMap<String,BukkitTask>();
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e){
		if(e.getMessage().equals(".")){
			if(!dottotask.containsKey(e.getPlayer().getName())){
				dotto.put(e.getPlayer().getName(), 1);
				dottotask.put(e.getPlayer().getName(), new dot(plugin, e.getPlayer()).runTaskLater(plugin, 20L));
			}else{
				dotto.put(e.getPlayer().getName(), dotto.get(e.getPlayer().getName()) + 1);
			}
			//e.setMessage(dotto.get(e.getPlayer().getName())+"");
		}else{
			if(dottotask.containsKey(e.getPlayer().getName())){
				dottotask.get(e.getPlayer().getName()).cancel();
				dottotask.remove(e.getPlayer().getName());
			}
		}
		String Msg = e.getFormat();
		if(e.getPlayer().hasPermission("mymaid.pex.limited")){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.BLACK + "■" + ChatColor.WHITE + "%1");
				e.setFormat(Msg);
	  	}else if(Prison.prison.containsKey(e.getPlayer().getName())){
			Msg = e.getFormat().replaceFirst("%1", ChatColor.DARK_GRAY + "■" + ChatColor.WHITE + "%1");
			e.setFormat(Msg);
  		}else if(MyMaid.chatcolor.containsKey(e.getPlayer().getName())){
	  		int i = Integer.parseInt(MyMaid.chatcolor.get(e.getPlayer().getName()));
			if(i >= 0 && i <= 5){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.WHITE + "■" + ChatColor.WHITE + "%1");
			}else if(i >= 6 && i <= 19){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.DARK_BLUE + "■" + ChatColor.WHITE + "%1");
			}else if(i >= 20 && i <= 33){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.BLUE + "■" + ChatColor.WHITE + "%1");
			}else if(i >= 34 && i <= 47){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.AQUA + "■" + ChatColor.WHITE + "%1");
			}else if(i >= 48 && i <= 61){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.DARK_AQUA + "■" + ChatColor.WHITE + "%1");
			}else if(i >= 62 && i <= 76){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.DARK_GREEN + "■" + ChatColor.WHITE + "%1");
			}else if(i >= 77 && i <= 89){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.GREEN + "■" + ChatColor.WHITE + "%1");
			}else if(i >= 90 && i <= 103){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.YELLOW + "■" + ChatColor.WHITE + "%1");
			}else if(i >= 104 && i <= 117){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.GOLD + "■" + ChatColor.WHITE + "%1");
			}else if(i >= 118 && i <= 131){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.RED + "■" + ChatColor.WHITE + "%1");
			}else if(i >= 132 && i <= 145){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.DARK_RED + "■" + ChatColor.WHITE + "%1");
			}else if(i >= 146 && i <= 159){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.DARK_PURPLE + "■" + ChatColor.WHITE + "%1");
			}else if(i >= 160){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.LIGHT_PURPLE + "■" + ChatColor.WHITE + "%1");
			}
			e.setFormat(Msg);
		}else{

			Msg = e.getFormat().replaceFirst("%1", ChatColor.GRAY + "■" + ChatColor.WHITE + "%1");
			e.setFormat(Msg);
		}
	}
	private class dot extends BukkitRunnable{
		Player player;
    	public dot(JavaPlugin plugin, Player player) {
    		this.player = player;
    	}
		@Override
		public void run() {
			if(dotto.containsKey(player.getName())){
				int dot = dotto.get(player.getName());
				if(dot != 1){
					dotto.remove(player.getName());
					dottotask.remove(player.getName());
					Bukkit.broadcastMessage(ChatColor.DARK_GRAY + player.getName() + "'s DOTTO COUNTER: " + dot + "/1s");
				}
			}
		}

	}
}
