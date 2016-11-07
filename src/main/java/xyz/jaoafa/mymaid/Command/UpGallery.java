package xyz.jaoafa.mymaid.Command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class UpGallery implements CommandExecutor {
	JavaPlugin plugin;
	public UpGallery(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 2){
			if (!(sender instanceof org.bukkit.entity.Player)) {
				Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
				return true;
			}
			org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
			String title = args[0];
			String url = args[1];
			String name = player.getName();
			UUID uuid = player.getUniqueId();
			String result = Method.url_jaoplugin("upgallery", "t="+title+"&p="+name+"&u="+uuid+"&url="+url);
			if(result.equalsIgnoreCase("ok")){
				Method.SendMessage(sender, cmd, "アップロードしました。");
			}else{
				Method.SendMessage(sender, cmd, "アップロードできませんでした。");
			}
			return true;
		}else{
			Method.SendMessage(sender, cmd, "/upgallery <TITLE> <URL>: ギャラリーにアップロードします。");
			return true;
		}
	}
}
