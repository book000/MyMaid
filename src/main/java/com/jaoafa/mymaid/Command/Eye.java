package com.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Method;

public class Eye implements CommandExecutor {
	JavaPlugin plugin;
	public Eye(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	//Eye状態
	public static Map<String, String> eyelist = new HashMap<String, String>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		if(args.length != 1){
			if(eyelist.containsKey(player.getName())){
				Method.SendMessage(sender, cmd, "追跡中のプレイヤー:"+eyelist.get(player.getName()));
			}else{
				Method.SendMessage(sender, cmd, "追跡していません");
			}
			return true;
		}
		String playername = args[0];
		if(player.getName().equals(playername)){
			Method.SendMessage(sender, cmd, "自分は指定できません。");
			return true;
		}
		if(playername.equals("clear")){
			Method.SendMessage(sender, cmd, "削除しました");
			Eye.eyelist.remove(player.getName());
			return true;
		}
		Player eye = null;
		for(Player online: Bukkit.getServer().getOnlinePlayers()) {
			if(online.getName().equalsIgnoreCase(playername)){
				eye = online;
			}
		}
		if(eye == null){
			Method.SendMessage(sender, cmd, "指定されたプレイヤーが見つかりませんでした。");
			return true;
		}
		eyelist.put(player.getName(), eye.getName());
		Method.SendMessage(sender, cmd, "プレイヤー「" + eye.getName() + "」を追跡するように設定しました。");
		return true;
	}
}
