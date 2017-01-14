package xyz.jaoafa.mymaid.EventHandler;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import eu.manuelgu.discordmc.MessageAPI;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Pointjao;
import xyz.jaoafa.mymaid.Command.Prison;

public class OnVotifierEvent implements Listener {
	JavaPlugin plugin;
	public OnVotifierEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onVotifierEvent(VotifierEvent event) {

        Vote vote = event.getVote();
        try{
        	new votifier(plugin, vote).runTaskAsynchronously(plugin);
		}catch(java.lang.NoClassDefFoundError e){
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。通常は再起動で直りますが直らない場合は開発者に連絡を行ってください。");
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
				}
			}
		}
    }

	private class votifier extends BukkitRunnable{
		Vote vote;
    	public votifier(JavaPlugin plugin, Vote vote) {
    		this.vote = vote;
    	}
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			String name = vote.getUsername();
	        String i;
	        if (Bukkit.getPlayer(vote.getUsername()) == null) {
	        	i = Method.url_jaoplugin("vote", "p="+name);
	        	String uuid = Method.url_jaoplugin("point", "p="+name);
	        	Pointjao.addjao("" + uuid, 20);
	        } else {
	        	Player player;
	        	if (Bukkit.getPlayer(name) == null) {
	        		player = Bukkit.getOfflinePlayer(name).getPlayer();
	            } else {
	            	player = Bukkit.getPlayer(name);
	            }

	        	UUID uuid = player.getUniqueId();
	        	i = Method.url_jaoplugin("vote", "p="+name+"&u="+uuid);
	        	Pointjao.addjao(player, 20);
	        	if(player.hasPermission("mymaid.pex.limited")){
					player.setPlayerListName(ChatColor.BLACK + "■" + ChatColor.WHITE + player.getName());
				}else if(Prison.prison.containsKey(player.getName())){
					player.setPlayerListName(ChatColor.DARK_GRAY + "■" + ChatColor.WHITE + player.getName());
				}else if(MyMaid.chatcolor.containsKey(player.getName())){
					int i1 = Integer.parseInt(i);
					if(i1 >= 0 && i1 <= 5){
						player.setPlayerListName(ChatColor.WHITE + "■" + ChatColor.WHITE + player.getName());
					}else if(i1 >= 6 && i1 <= 19){
						player.setPlayerListName(ChatColor.DARK_BLUE + "■" + ChatColor.WHITE + player.getName());
					}else if(i1 >= 20 && i1 <= 33){
						player.setPlayerListName(ChatColor.BLUE + "■" + ChatColor.WHITE + player.getName());
					}else if(i1 >= 34 && i1 <= 47){
						player.setPlayerListName(ChatColor.AQUA + "■" + ChatColor.WHITE + player.getName());
					}else if(i1 >= 48 && i1 <= 61){
						player.setPlayerListName(ChatColor.DARK_AQUA  + "■" + ChatColor.WHITE + player.getName());
					}else if(i1 >= 62 && i1 <= 76){
						player.setPlayerListName(ChatColor.DARK_GREEN + "■" + ChatColor.WHITE + player.getName());
					}else if(i1 >= 77 && i1 <= 89){
						player.setPlayerListName(ChatColor.GREEN + "■" + ChatColor.WHITE + player.getName());
					}else if(i1 >= 90 && i1 <= 103){
						player.setPlayerListName(ChatColor.YELLOW + "■" + ChatColor.WHITE + player.getName());
					}else if(i1 >= 104 && i1 <= 117){
						player.setPlayerListName(ChatColor.GOLD + "■" + ChatColor.WHITE + player.getName());
					}else if(i1 >= 118 && i1 <= 131){
						player.setPlayerListName(ChatColor.RED + "■" + ChatColor.WHITE + player.getName());
					}else if(i1 >= 132 && i1 <= 145){
						player.setPlayerListName(ChatColor.DARK_RED + "■" + ChatColor.WHITE + player.getName());
					}else if(i1 >= 146 && i1 <= 159){
						player.setPlayerListName(ChatColor.DARK_PURPLE + "■" + ChatColor.WHITE + player.getName());
					}else if(i1 >= 160){
						player.setPlayerListName(ChatColor.LIGHT_PURPLE + "■" + ChatColor.WHITE + player.getName());
					}
				}else{
					player.setPlayerListName(ChatColor.GRAY + "■" + ChatColor.WHITE + player.getName());
				}
	        }
	        Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + "プレイヤー「" + name + "」が投票をしました！(現在の投票数:" + i + "回)");
	        Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + "投票をよろしくお願いします！ https://bitly.com/jfvote");
	        MessageAPI.sendToDiscord("プレイヤー「" + name + "」が投票をしました！(現在の投票数:" + i + "回)");
	        MessageAPI.sendToDiscord("投票をよろしくお願いします！ https://bitly.com/jfvote");
		}
	}
}
