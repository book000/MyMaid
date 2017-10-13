package xyz.jaoafa.mymaid.EventHandler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;

//https://github.com/book000/MyMaid/issues/11
public class SpectatorContinue implements Listener {
	JavaPlugin plugin;
	public SpectatorContinue(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String, String> spectator = new HashMap<String, String>();

	@EventHandler
	public void onPlayerClick(EntityDamageByEntityEvent event){
		if(event.getEntityType() != EntityType.PLAYER){
			return;
		}
		Entity e_damager = event.getDamager(); // ダメージを与えた方?
		Entity e_entity = event.getEntity(); // ダメージを受けた方?
		if(!(e_damager instanceof Player)){
			return;
		}
		if(!(e_entity instanceof Player)){
			return;
		}
		Player damager = (Player) e_damager; // ダメージを与えた方?
		Player entity = (Player) e_entity; // ダメージを受けた方?

		if(spectator.containsKey(damager.getName())){
			return;
		}

		if(damager.getGameMode() != GameMode.SPECTATOR){
			return;
		}

		spectator.put(damager.getName(), entity.getName());
		damager.sendMessage("[Spectator] " + ChatColor.GREEN + "あなたは「" + entity.getName() + "」にくっつきました。");
	}

	@EventHandler
	public void onGameModeChange(PlayerGameModeChangeEvent event){
		Player player = event.getPlayer();
		if(player.getGameMode() == GameMode.SPECTATOR && event.getNewGameMode() != GameMode.SPECTATOR){
			if(player.getSpectatorTarget() == null){
				return;
			}
			Entity e = player.getSpectatorTarget();
			if(!(e instanceof Player)){
				return;
			}

			player.sendMessage("[Spectator] " + ChatColor.GREEN + "あなたは「" + e.getName() + "」から離れました。");
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
			p.setSpectatorTarget(null);
			p.setSpectatorTarget(player);
			PacketPlayOutCamera camera = new PacketPlayOutCamera((net.minecraft.server.v1_8_R3.Entity) player);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(camera);
			p.sendMessage("[Spectator] " + ChatColor.GREEN + "あなたがくっついていたプレイヤー「" + name2 + "」が死亡したため再度くっつきました");
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
			p.setSpectatorTarget(null);
			p.setSpectatorTarget(player);
			PacketPlayOutCamera camera = new PacketPlayOutCamera((net.minecraft.server.v1_8_R3.Entity) player);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(camera);
			p.sendMessage("[Spectator] " + ChatColor.GREEN + "あなたがくっついていたプレイヤー「" + name2 + "」が死亡したため再度くっつきました");
		}
	}

	@EventHandler
	public void onShift(PlayerToggleSneakEvent event){
		Player player = event.getPlayer();
		if(!event.isSneaking()){
			return;
		}
		if(player.getGameMode() != GameMode.SPECTATOR){
			return;
		}
		if(player.getSpectatorTarget() == null){
			return;
		}
		Entity e = player.getSpectatorTarget();
		if(!(e instanceof Player)){
			return;
		}
		player.sendMessage("[Spectator] " + ChatColor.GREEN + "あなたは「" + e.getName() + "」から離れました。");
		if(spectator.containsKey(player.getName())){
			spectator.remove(player.getName());
		}
	}
}
