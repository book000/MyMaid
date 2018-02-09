package xyz.jaoafa.mymaid.Jail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EBan_Event implements Listener {
	JavaPlugin plugin;
	public EBan_Event(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event){ // 南の楽園外に出られるかどうか
		Location to = event.getTo();
		Player player = event.getPlayer();
		if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
		World World = Bukkit.getServer().getWorld("Jao_Afa");
		Location prison = new Location(World, 1767, 70, 1767);
		try{
			if(prison.distance(to) >= 150){
				player.sendMessage("[EBan] " + ChatColor.GREEN + "あなたは南の楽園から出られません！");
				event.setCancelled(true);

				if(prison.distance(to) >= 200){
					player.teleport(prison);
				}

			}
		}catch(java.lang.IllegalArgumentException ex){
			player.teleport(prison);
		}
	}
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		Player player = event.getPlayer();
		if (!(player instanceof Player)) {
			return;
		}
		if(!player.getLocation().getWorld().getName().equalsIgnoreCase("Jao_Afa")){
			return;
		}
		if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
		event.setCancelled(true);
		player.sendMessage("[EBan] " + ChatColor.GREEN + "あなたはブロックを置けません。");
		Bukkit.getLogger().info("[EBan] "+player.getName()+"==>あなたはブロックを置けません。");
	}
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event){
		Player player = event.getPlayer();
		if (!(player instanceof Player)) {
			return;
		}
		if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
		event.setCancelled(true);
		player.sendMessage("[EBan] " + ChatColor.GREEN + "あなたはブロックを壊せません。");
		Bukkit.getLogger().info("[EBan] "+player.getName()+"==>あなたはブロックを壊せません。");
	}
	@EventHandler
    public void onBlockIgniteEvent(BlockIgniteEvent event){
    	Player player = event.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
  		event.setCancelled(true);
  		player.sendMessage("[EBan] " + ChatColor.GREEN + "あなたはブロックを着火できません。");
  		Bukkit.getLogger().info("[EBan] "+player.getName()+"==>あなたはブロックを着火できません。");
    }
	@EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event){
    	Player player = event.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
  		event.setCancelled(true);
  		player.sendMessage("[EBan] " + ChatColor.GREEN + "あなたは水や溶岩を撒けません。");
  		Bukkit.getLogger().info("[EBan] "+player.getName()+"==>あなたは水や溶岩を撒けません。");
    }
	@EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event){
    	Player player = event.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
  		event.setCancelled(true);
    }
	@EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event){
    	Player player = event.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
  		event.setCancelled(true);
  		player.sendMessage("[EBan] " + ChatColor.GREEN + "あなたはコマンドを実行できません。");
  		Bukkit.getLogger().info("[EBan] "+player.getName()+"==>あなたはコマンドを実行できません。");
    }
	@EventHandler
    public void onProjectileLaunchEvent(ProjectileLaunchEvent event){
		if (!(event.getEntity().getShooter() instanceof org.bukkit.entity.Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
		event.setCancelled(true);
	}
	@EventHandler
    public void onPotionSplashEvent(PotionSplashEvent event){
		if (!(event.getEntity().getShooter() instanceof org.bukkit.entity.Player)) {
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(!EBan.isEBan(player)){ // EBanされてる
			return;
		}
		event.setCancelled(true);
	}
}

