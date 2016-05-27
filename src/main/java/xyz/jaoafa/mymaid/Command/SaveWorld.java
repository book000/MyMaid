package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SaveWorld implements CommandExecutor {
	JavaPlugin plugin;
	public SaveWorld(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		boolean str;
		str = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
		if(str){
			sender.sendMessage("ワールドセーブに成功しました。");
			return true;
		}else{
			sender.sendMessage("ワールドセーブに失敗しました。");
			return true;
		}
	}
}
