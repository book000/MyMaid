package xyz.jaoafa.mymaid.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Dynamic implements CommandExecutor {
	JavaPlugin plugin;
	public Dynamic(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Method.SendMessage(sender, cmd, "現在ダイナミックテレポートは利用できません。");
		return true;
		/*
		Player player = (Player) sender;
		if(args.length != 1){
			Method.SendMessage(sender, cmd, "引数が適切ではありません。");
			return true;
		}
		if(args[0].equalsIgnoreCase("true")){
			Dynmap_Teleporter.dynamic.put(player.getName(), true);
			Method.SendMessage(sender, cmd, "trueに設定しました。");
			return true;
		}else if(args[0].equalsIgnoreCase("false")){
			Dynmap_Teleporter.dynamic.remove(player.getName());
			Method.SendMessage(sender, cmd, "falseに設定しました。");
			return true;
		}else if(args[0].equalsIgnoreCase("stop")){
			if(Dynmap_Teleporter.dynamic_teleporter.containsKey(player.getName())){
				Dynmap_Teleporter.dynamic_teleporter.get(player.getName()).cancel();
				Dynmap_Teleporter.dynamic_teleporter.remove(player.getName());
				Method.SendMessage(sender, cmd, "ダイナミックテレポートを強制終了しました。");
			}else{
				Method.SendMessage(sender, cmd, "ダイナミックテレポートの強制終了に失敗しました。");
			}
			return true;
		}else{
			Method.SendMessage(sender, cmd, "第一引数にはtrue, false, stopを指定してください。");
			return true;
		}
		*/
	}
}
