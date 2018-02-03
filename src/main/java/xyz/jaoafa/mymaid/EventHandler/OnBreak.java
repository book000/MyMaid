package xyz.jaoafa.mymaid.EventHandler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.Crops;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.PermissionsManager;

public class OnBreak implements Listener {
	JavaPlugin plugin;
	List<Material> LDNoBreak = new ArrayList<Material>();
	public OnBreak(JavaPlugin plugin) {
		this.plugin = plugin;

		LDNoBreak.add(Material.BARRIER);
		LDNoBreak.add(Material.COMMAND);
		LDNoBreak.add(Material.ITEM_FRAME);
	}
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		if (!(player instanceof Player)) {
			return;
		}
		Block block = event.getBlock();
		String group = PermissionsManager.getPermissionMainGroup(player);
		if(group.equalsIgnoreCase("QPPE") || group.equalsIgnoreCase("Default")){
			// QPPEとDefaultは一部ブロックを破壊できない
			if(LDNoBreak.contains(block.getType())){
				event.setCancelled(true);
			}
		}

		if(block.getWorld().getName().equalsIgnoreCase("Summer2017")){
			return;
		}
		if(block.getType() == Material.CROPS){
			Crops crop = (Crops) block.getState().getData();
			if(crop.getState() == CropState.RIPE){
				if(player.getItemInHand().getType() != Material.DIAMOND_SWORD &&
						player.getItemInHand().getType() != Material.GOLD_SWORD &&
						player.getItemInHand().getType() != Material.IRON_SWORD &&
						player.getItemInHand().getType() != Material.STONE_SWORD &&
						player.getItemInHand().getType() != Material.WOOD_SWORD){
					event.setCancelled(true);
				}
			}
		}
	}
}
