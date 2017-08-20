package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class OnPlayerChangedWorldEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerChangedWorldEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(ignoreCancelled = true)
	public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		String world = player.getWorld().getName();

		player.sendMessage("[WorldTips] " + ChatColor.GREEN + "このワールドは「" + world + "」というワールドです。");

		if(world.equalsIgnoreCase("Jao_Afa")){
			player.sendMessage("[WorldTips] " + ChatColor.GREEN + "適用規約: サーバルール・方針・利用規約・Jao_Afaワールドルール");
			player.sendMessage("[WorldTips] " + ChatColor.GREEN + "ワールド詳細ページ: https://jaoafa.com/community/jao_afa");
		}else if(world.equalsIgnoreCase("Jao_Afa_nether")){
			player.sendMessage("[WorldTips] " + ChatColor.GREEN + "適用規約: サーバルール・方針・利用規約・Jao_Afa_netherワールドルール");
			player.sendMessage("[WorldTips] " + ChatColor.GREEN + "ワールド詳細ページ: https://jaoafa.com/community/jao_afa_nether");
		}else if(world.equalsIgnoreCase("Jao_Afa_the_end")){
			player.sendMessage("[WorldTips] " + ChatColor.GREEN + "適用規約: サーバルール・方針・利用規約・Jao_Afa_the_endワールドルール");
			player.sendMessage("[WorldTips] " + ChatColor.GREEN + "ワールド詳細ページ: https://jaoafa.com/community/jao_afa_the_end");
		}else if(world.equalsIgnoreCase("ReJao_Afa")){
			player.sendMessage("[WorldTips] " + ChatColor.GREEN + "適用規約: サーバルール・方針・利用規約・ReJao_Afaワールドルール");
			player.sendMessage("[WorldTips] " + ChatColor.GREEN + "ワールド詳細ページ: https://jaoafa.com/community/rejao_afa");
		}else if(world.equalsIgnoreCase("SandBox")){
			player.sendMessage("[WorldTips] " + ChatColor.GREEN + "適用規約: 利用規約・SandBoxワールドルール");
			player.sendMessage("[WorldTips] " + ChatColor.GREEN + "ワールド詳細ページ: https://jaoafa.com/community/sandbox");
		}else if(world.startsWith("Summer2017")){
			player.sendMessage("[WorldTips] " + ChatColor.GREEN + "適用規約: サーバルール・方針・利用規約・Summer2017ワールドルール");
			player.sendMessage("[WorldTips] " + ChatColor.GREEN + "ワールド関連記事: https://jaoafa.com/blog/tomachi/event201708");
		}
	}
}
