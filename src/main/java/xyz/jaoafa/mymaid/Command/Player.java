package xyz.jaoafa.mymaid.Command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.PermissionsManager;

public class Player implements CommandExecutor {
	JavaPlugin plugin;
	public Player(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			if (!(sender instanceof org.bukkit.entity.Player)) {
				Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
				return true;
			}
			org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
			String MainGroup = PermissionsManager.getPermissionMainGroup(player);
			List<String> groups = PermissionsManager.getPermissionGroupList(player);

			for(String group : groups){
				if(MainGroup.equalsIgnoreCase(group)){
					Method.SendMessage(player, cmd, "You Permission group \"" + group +"\" (Main)");
				}else{
					Method.SendMessage(player, cmd, "You Permission group \"" + group +"\"");
				}
			}
		}else if(args.length == 1){
			String p = args[0];

			if(p == null){
				Method.SendMessage(sender, cmd, "引数にnullを指定できません。"); // 本来ありえなさそうだけど
				return true;
			}

			try{
				String MainGroup = PermissionsManager.getPermissionMainGroup(p);
				if(MainGroup == null){
					Method.SendMessage(sender, cmd, "メイングループを取得できませんでした。");
					return true;
				}
				List<String> groups = PermissionsManager.getPermissionGroupList(p);
				for(String group : groups){
					if(MainGroup.equalsIgnoreCase(group)){
						Method.SendMessage(sender, cmd, "You Permission group \"" + group +"\" (Main)");
					}else{
						Method.SendMessage(sender, cmd, "You Permission group \"" + group +"\"");
					}
				}
			}catch(IllegalArgumentException e){
				Method.SendMessage(sender, cmd, "プレイヤーを取得できません。");
				return true;
			}
		}else{
			Method.SendMessage(sender, cmd, "引数が適していません。");
		}
		return true;
	}
}
