package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.PermissionsManager;
import xyz.jaoafa.mymaid.Discord.Discord;

public class OnPlayerKickEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerKickEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKickEvent(PlayerKickEvent e){
		if(e.getReason().equals("disconnect.spam")){
			e.setCancelled(true);
			return;
		}else if(e.getReason().equals("Illegal characters in chat")){
			e.setCancelled(true);
			return;
		}else if(e.getReason().equalsIgnoreCase("You are sending too many packets!") ||
				e.getReason().equalsIgnoreCase("You are sending too many packets, :(")){
			/*
			EmbedBuilder embed = new EmbedBuilder();
			embed.withTitle("警告！！");
			embed.withDescription("プレイヤーがパケットを送信しすぎてKickされました。ハッククライアントの可能性があります。");
			embed.withAuthorName(e.getPlayer().getName());
			embed.withAuthorUrl("https://jaoafa.com/user/" + e.getPlayer().getUniqueId().toString());
			embed.withAuthorIcon("https://crafatar.com/avatars/" + e.getPlayer().getUniqueId().toString());
			embed.withColor(Color.ORANGE);
			embed.appendField("プレイヤー", e.getPlayer().getName(), true);
			embed.appendField("理由", e.getReason(), false);

			Discord.send("223582668132974594", "", embed.build());
			 */
			Discord.send("223582668132974594", ":interrobang:プレイヤー「" + e.getPlayer().getName() +"」がパケットを送信しすぎてKickされました。\n"
					+ "ハッククライアントの可能性があります。\n"
					+ "Reason: " + e.getReason());


		}
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			String group = PermissionsManager.getPermissionMainGroup(p);
			if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
				p.sendMessage("[KickReason] " + ChatColor.GREEN + e.getPlayer().getName() + " Kick Reason: 「" + e.getReason() + "」");
			}
		}
	}
}
