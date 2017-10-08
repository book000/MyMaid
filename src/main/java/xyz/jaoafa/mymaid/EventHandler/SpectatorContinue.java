package xyz.jaoafa.mymaid.EventHandler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

//https://github.com/book000/MyMaid/issues/11
public class SpectatorContinue implements Listener {
	JavaPlugin plugin;
	public SpectatorContinue(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	Map<String, String> spectator = new HashMap<String, String>();

	@EventHandler
	public void onPlayerClick(PlayerInteractAtEntityEvent event){
		Player player = event.getPlayer();
		Entity e = event.getRightClicked();
		if(!(e instanceof Player)){
			return;
		}
		spectator.put(player.getName(), e.getName());
		player.sendMessage("[Spectator] " + "あなたは「" + player.getName() + "」にくっつきました。");
	}

	@EventHandler
	public void onGameModeChange(PlayerGameModeChangeEvent event){
		Player player = event.getPlayer();
		if(player.getGameMode() == GameMode.SPECTATOR && event.getNewGameMode() != GameMode.SPECTATOR){
			player.sendMessage("[Spectator] " + "あなたは「" + player.getName() + "」から離れました。");
			if(spectator.containsKey(player.getName())){
				spectator.remove(player.getName());
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player player = event.getPlayer();
		for(Map.Entry<String, String> one : spectator.entrySet()) {
			String name = one.getKey(); // くっついている人
			String name2 = one.getValue(); // くっつかれてる人
			if(!player.getName().equalsIgnoreCase(name2)){
				continue;
			}
			Player p = Bukkit.getPlayerExact(name);
			if(p == null){
				continue;
			}
			if(p.getGameMode() != GameMode.SPECTATOR){
				continue;
			}
			p.setSpectatorTarget(player);
			p.sendMessage("[Spectator] " + "あなたがくっついていたプレイヤー「" + name2 + "」が死亡したため再度");
		}
	}

	@EventHandler
	public void onPlayerWorldChange(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		for(Map.Entry<String, String> one : spectator.entrySet()) {
			String name = one.getKey(); // くっついている人
			String name2 = one.getValue(); // くっつかれてる人
			if(!player.getName().equalsIgnoreCase(name2)){
				continue;
			}
			Player p = Bukkit.getPlayerExact(name);
			if(p == null){
				continue;
			}
			if(p.getGameMode() != GameMode.SPECTATOR){
				continue;
			}
			p.setSpectatorTarget(player);
			p.sendMessage("[Spectator] " + "あなたがくっついていたプレイヤー「" + name2 + "」が死亡したため再度");
		}
	}
}
