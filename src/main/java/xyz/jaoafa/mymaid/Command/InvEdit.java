package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class InvEdit implements CommandExecutor {
	JavaPlugin plugin;
	public InvEdit(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player p = (Player) sender;
		if(args.length == 1){
			for(Player player: Bukkit.getServer().getOnlinePlayers()) {
				if(player.getName().equalsIgnoreCase(args[0])){
					PlayerInventory inv = player.getInventory();
					p.openInventory(inv);
					Method.SendMessage(sender, cmd, "インベントリの編集には細心の注意を払ってください。");
					return true;
				}
			}
			Method.SendMessage(sender, cmd, "ユーザーが見つかりませんでした。");
			return true;
		}else{
			Method.SendMessage(sender, cmd, "引数が適していません。");
			return true;
		}
	}
}
