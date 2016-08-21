package xyz.jaoafa.mymaid.EventHandler;

import java.util.Collection;
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

import com.github.ucchyocean.lc.LunaChatAPI;
import com.github.ucchyocean.lc.channel.Channel;
import com.github.ucchyocean.lc.channel.ChannelPlayer;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Command.DOT;
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
			if(DOT.bed.containsKey(e.getPlayer().getName())){
				e.getPlayer().sendMessage("[.] " + ChatColor.GREEN + "ベットで寝ながらは違反だゾ！");
			}else{
				if(!DOT.run.containsKey(e.getPlayer().getName())){
					if(DOT.runwait.containsKey(e.getPlayer().getName())){
						e.getPlayer().sendMessage("[.] " + ChatColor.GREEN + "ピリオド計測開始っ");
						DOT.runwait.remove(e.getPlayer().getName());
						DOT.dotcount_stop.put(e.getPlayer().getName(), true);
						DOT.run.put(e.getPlayer().getName(), new dot_60s(plugin, e.getPlayer(), MyMaid.lunachatapi).runTaskLater(plugin, 1200L));
						DOT.success.put(e.getPlayer().getName(), 1);
						DOT.unsuccess.put(e.getPlayer().getName(), 0);
						Collection<Channel> channels = MyMaid.lunachatapi.getChannels();
						boolean chan = true;
						for(Channel channel: channels){
							if(channel.getName().equals("_DOT_")){
								chan = false;
							}
						}
						if(chan){
							MyMaid.lunachatapi.createChannel("_DOT_");
						}
						if(MyMaid.lunachatapi.getChannel("_DOT_").isBroadcastChannel()){
							MyMaid.lunachatapi.getChannel("_DOT_").setBroadcast(false);
						}
						MyMaid.lunachatapi.getChannel("_DOT_").addMember(ChannelPlayer.getChannelPlayer(e.getPlayer().getName()));
						MyMaid.lunachatapi.setDefaultChannel(e.getPlayer().getName(), "_DOT_");
					}
				}else{
					DOT.success.put(e.getPlayer().getName(), DOT.success.get(e.getPlayer().getName()) + 1);
				}
			}
		}else{
			if(DOT.run.containsKey(e.getPlayer().getName())){
				if(DOT.bed.containsKey(e.getPlayer().getName())){
					e.getPlayer().sendMessage("[.] " + ChatColor.GREEN + "ベットで寝ながらは違反だゾ！");
				}else{
						DOT.unsuccess.put(e.getPlayer().getName(), DOT.unsuccess.get(e.getPlayer().getName()) + 1);
				}
			}else{
				if(DOT.bed.containsKey(e.getPlayer().getName())){
					e.getPlayer().sendMessage("[.] " + ChatColor.GREEN + "ベットで寝ながらは違反だゾ！");
				}else{
					DOT.unsuccess.put(e.getPlayer().getName(), 1);
				}
			}
		}
		if(!DOT.dotcount_stop.containsKey(e.getPlayer().getName())){
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
				}else{
					dotto.remove(player.getName());
					dottotask.remove(player.getName());
				}
			}
		}

	}
	private class dot_60s extends BukkitRunnable{
		Player player;
		LunaChatAPI lunachatapi;
    	public dot_60s(JavaPlugin plugin, Player player, LunaChatAPI lunachatapi) {
    		this.player = player;
    		this.lunachatapi = lunachatapi;
    	}
		@Override
		public void run() {
			//Bukkit.broadcastMessage(DOT.success.toString() + DOT.unsuccess.toString());
			int success;
			if(DOT.success.containsKey(player.getName())){
				success = DOT.success.get(player.getName());
			}else{
				success = 0;
			}
			int unsuccess;
			if(DOT.unsuccess.containsKey(player.getName())){
				try{
					unsuccess = DOT.unsuccess.get(player.getName());
				}catch(NullPointerException e){
					unsuccess = 0;
				}
			}else{
				unsuccess = 0;
			}
			Bukkit.broadcastMessage("[.] " + ChatColor.GREEN + player.getName() + "のピリオド対決の結果: 成功回数" + success + " 失敗回数" + unsuccess);
			DOT.run.get(player.getName()).cancel();
			DOT.run.remove(player.getName());
			DOT.dotcount_stop.remove(player.getName());
			DOT.success.remove(player.getName());
			DOT.unsuccess.remove(player.getName());
			lunachatapi.getChannel("_DOT_").removeMember(ChannelPlayer.getChannelPlayer(player.getName()));
			Method.url_jaoplugin("dot", "p=" + player.getName() + "&u=" + player.getUniqueId() + "&success=" + success + "&unsuccess=" + unsuccess);
		}

	}
}
