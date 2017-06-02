package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.SKKColors.SKKColors;

public class SSK implements CommandExecutor {
	JavaPlugin plugin;
	public SSK(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			Player player = (Player) sender;
			SKKColors.UpdatePlayerSKKColor(player);
			SKKColors.setPlayerSKKTabList(player);
			ChatColor color = SKKColors.getPlayerSKKChatColor(player);
			int i = SKKColors.getPlayerVoteCount(player);

	  		Method.SendMessage(sender, cmd, player.getName() + "'s SKK -> " +  i + " (" + color + "■)");
		}else if(args.length == 1){
			for(Player player: Bukkit.getServer().getOnlinePlayers()) {
				if(player.getName().equalsIgnoreCase(args[0])){
					SKKColors.UpdatePlayerSKKColor(player);
					SKKColors.setPlayerSKKTabList(player);
					ChatColor color = SKKColors.getPlayerSKKChatColor(player);
					int i = SKKColors.getPlayerVoteCount(player);

			  		Method.SendMessage(sender, cmd, player.getName() + "'s SKK -> " +  i + " (" + color + "■" +  ChatColor.GREEN +")");
				}
    		}
		}
		return true;
	}
}
