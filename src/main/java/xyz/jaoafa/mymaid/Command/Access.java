package xyz.jaoafa.mymaid.Command;

import java.net.InetAddress;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Access implements CommandExecutor {
	JavaPlugin plugin;
	public Access(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 1){
			for(Player player: Bukkit.getServer().getOnlinePlayers()) {
				if(player.getName().equalsIgnoreCase(args[0])) {
					InetAddress ip = player.getAddress().getAddress();
					String data = Method.url_jaoplugin("access", "i="+ip);
					if(data.equalsIgnoreCase("NO")){
						Method.SendMessage(sender, cmd, "このユーザー「"+player.getName()+"」がアクセスしたページ:なし");
						return true;
					}else if(data.indexOf(",") == -1){
						Method.SendMessage(sender, cmd, "このユーザー「"+player.getName()+"」がアクセスしたページ:"+data+"");
						Bukkit.getLogger().info("このユーザーがアクセスしたページ:"+data+"");
						return true;
					}else{
						String[] access = data.split(",", 0);
						String accesstext = "";
						for (String one: access){
							accesstext += "「"+one+"」";
						}
						Method.SendMessage(sender, cmd, "このユーザー「"+player.getName()+"」がアクセスしたページ:"+accesstext+"など");
						Bukkit.getLogger().info("このユーザーがアクセスしたページ:"+accesstext+"など");
						return true;
					}
				}
				Method.SendMessage(sender, cmd, "ユーザーが見つかりませんでした。");
				return true;
			}
		}else{
			Method.SendMessage(sender, cmd, "引数が適していません。");
			return true;
		}
		return false;
	}
}
