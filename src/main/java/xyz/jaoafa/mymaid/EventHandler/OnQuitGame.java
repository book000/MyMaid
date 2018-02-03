package xyz.jaoafa.mymaid.EventHandler;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.PermissionsManager;
import xyz.jaoafa.mymaid.Command.AFK;
import xyz.jaoafa.mymaid.Command.MyBlock;
import xyz.jaoafa.mymaid.Discord.Discord;

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
  		/*if((Bukkit.getServer().getOnlinePlayers().size() - 1) == 1){
			CmdBot.type = CmdBot.BotType.getRandomBotType();
			Discord.send("**[CmdBot]** ぼっち用jaotanおしゃべりAPIを「" + CmdBot.type.getName() + "」に設定しました。");
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				if(PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) {
					p.sendMessage("[CmdBot] " + ChatColor.GREEN + "ぼっち用jaotanおしゃべりAPIを「" + CmdBot.type.getName() + "」に設定しました。");
				}
			}
		}*/
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
  		if(AFK.getAFKing(player)){
  			AFK.setAFK_False(player);
  		}
		//Dynmap_Teleporter.dynamic_teleporter.get(player.getName()).cancel();
		//Dynmap_Teleporter.dynamic_teleporter.remove(player.getName());
		Date Date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GOLD + "■" + ChatColor.WHITE + "jaotan: 現在『" + (Bukkit.getServer().getOnlinePlayers().size() - 1) + "人』がログインしています");
		Discord.setGame("Online: " + (Bukkit.getServer().getOnlinePlayers().size() - 1) + " players.");
		String header = "[\"\",{\"text\":\"jao \",\"color\":\"gold\"},{\"text\":\"Minecraft \",\"color\":\"yellow\"},{\"text\":\"Server\",\"color\":\"aqua\"},{\"text\":\"\n\",\"color\":\"none\"},{\"text\":\"Online: \",\"color\":\"none\"},{\"text\":\"" + (Bukkit.getServer().getOnlinePlayers().size() - 1) + "\",\"color\":\"none\"}]";
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			String footer = Method.getPlayerListFooter(p);
			Method.setPlayerListHeaderFooterByJSON(p, header, footer);
		}
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
			String group = PermissionsManager.getPermissionMainGroup(player);
			Method.url_jaoplugin("pex", "p="+player.getName()+"&u="+player.getUniqueId()+"&pex="+group);
		}
	}
}
