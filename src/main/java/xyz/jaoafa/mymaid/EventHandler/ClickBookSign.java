package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.plugin.java.JavaPlugin;

public class ClickBookSign {
	JavaPlugin plugin;
	public ClickBookSign(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	/*
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		Block block = event.getClickedBlock();
		Material material = block.getType();
		Player player = event.getPlayer();
		if (material == Material.SIGN_POST || material == Material.SIGN) {

		}
	}
	*/
}
