package xyz.jaoafa.mymaid.EventHandler;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.github.ucchyocean.lc.channel.Channel;
import com.github.ucchyocean.lc.channel.ChannelPlayer;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.MyMaid.dot;
import xyz.jaoafa.mymaid.Command.BON;
import xyz.jaoafa.mymaid.Command.Color;
import xyz.jaoafa.mymaid.Command.DOT;
import xyz.jaoafa.mymaid.Command.Prison;
import xyz.jaoafa.mymaid.Command.WO;

public class OnAsyncPlayerChatEvent implements Listener {
	JavaPlugin plugin;
	public OnAsyncPlayerChatEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}


	String oldtext = "";
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent e){
		if(e.getMessage().equalsIgnoreCase("afa") && oldtext.equalsIgnoreCase("jao")){
			Method.SendTipsALL("このjao afaって言うのはこのサーバにログインした時にする挨拶だよ！やってみよう！");
		}
		if(e.getMessage().equalsIgnoreCase("jaojao")){
			Method.SendTipsALL("このjaojaoって言うのはこのサーバからログアウトする前にする挨拶だよ！やってみよう！");
		}
		if(e.getMessage().equals("を")){
			if(WO.nowwo){
				WO.stopwo = true;
			}
			if(BON.nowbon){
				BON.stopbon = true;
			}
		}
		if(e.getMessage().equals(".")){
			if(e.getPlayer().isSleeping()){
				e.getPlayer().sendMessage("[.] " + ChatColor.GREEN + "ベットで寝ながらは違反だゾ！");
				e.setCancelled(true);
			}else{
				if(!DOT.run.containsKey(e.getPlayer().getName())){
					if(DOT.runwait.containsKey(e.getPlayer().getName())){
						int section = DOT.runwait.get(e.getPlayer().getName());
						e.getPlayer().sendMessage("[.] " + ChatColor.GREEN + "ピリオド計測開始っ(" +  section + "秒部門)");
						DOT.runwait.remove(e.getPlayer().getName());
						DOT.dotcount_stop.put(e.getPlayer().getName(), true);
						BukkitTask bt = null;
						try{
							bt = new MyMaid.dot_section(plugin, e.getPlayer(), MyMaid.lunachatapi, section).runTaskLater(plugin, section * 20);
						}catch(NoClassDefFoundError ex){
							e.getPlayer().sendMessage("[.] " + ChatColor.GREEN + "ピリオド計測に失敗しました。");
						}
						if(bt == null){
							e.getPlayer().sendMessage("[.] " + ChatColor.GREEN + "ピリオド計測に失敗しました。");
						}
						DOT.run.put(e.getPlayer().getName(), bt);
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
						DOT.kana.put(e.getPlayer().getName(), MyMaid.lunachatapi.isPlayerJapanize(e.getPlayer().getName()));
						MyMaid.lunachatapi.setPlayersJapanize(e.getPlayer().getName(), false);
					}
				}else{
					DOT.success.put(e.getPlayer().getName(), DOT.success.get(e.getPlayer().getName()) + 1);
				}
			}
		}else{
			if(DOT.run.containsKey(e.getPlayer().getName())){
				if(e.getPlayer().isSleeping()){
					e.getPlayer().sendMessage("[.] " + ChatColor.GREEN + "ベットで寝ながらは違反だゾ！");
					e.setCancelled(true);
				}else{
					DOT.unsuccess.put(e.getPlayer().getName(), DOT.unsuccess.get(e.getPlayer().getName()) + 1);
				}
			}
		}
		if(!DOT.dotcount_stop.containsKey(e.getPlayer().getName())){
			if(e.getMessage().equals(".")){
				if(!DOT.dottotask.containsKey(e.getPlayer().getName())){
					DOT.dotto.put(e.getPlayer().getName(), 1);
					DOT.dottotask.put(e.getPlayer().getName(), new dot(plugin, e.getPlayer()).runTaskLater(plugin, 20L));
				}else{
					DOT.dotto.put(e.getPlayer().getName(), DOT.dotto.get(e.getPlayer().getName()) + 1);
				}
				//e.setMessage(dotto.get(e.getPlayer().getName())+"");
			}else{
				if(DOT.dottotask.containsKey(e.getPlayer().getName())){
					DOT.dottotask.get(e.getPlayer().getName()).cancel();
					DOT.dottotask.remove(e.getPlayer().getName());
				}
			}
		}
		/*
		if(!DOT.dotcount_stop.containsKey(e.getPlayer().getName())){
			if(!DOT.doomtask.containsKey(e.getPlayer().getName())){
				DOT.doom.put(e.getPlayer().getName(), 1);
				DOT.doomtask.put(e.getPlayer().getName(), new doom(plugin, e.getPlayer()).runTaskLater(plugin, 20L));
			}else{
				DOT.doom.put(e.getPlayer().getName(), DOT.doom.get(e.getPlayer().getName()) + 1);
			}
		}
		*/
		String Msg = e.getFormat();
		if(e.getPlayer().hasPermission("mymaid.pex.limited")){
				Msg = e.getFormat().replaceFirst("%1", ChatColor.BLACK + "■" + ChatColor.WHITE + "%1");
				e.setFormat(Msg);
	  	}else if(Prison.prison.containsKey(e.getPlayer().getName())){
			Msg = e.getFormat().replaceFirst("%1", ChatColor.DARK_GRAY + "■" + ChatColor.WHITE + "%1");
			e.setFormat(Msg);
	  	}else if(Color.color.containsKey(e.getPlayer().getName())){
			Msg = e.getFormat().replaceFirst("%1", Color.color.get(e.getPlayer().getName()) + "■" + ChatColor.WHITE + "%1");
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
		oldtext = e.getMessage();
	}

}
