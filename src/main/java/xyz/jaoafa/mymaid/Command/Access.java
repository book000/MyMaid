package xyz.jaoafa.mymaid.Command;

import java.net.InetAddress;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.MyMaid;

public class Access implements CommandExecutor {
	JavaPlugin plugin;
	public Access(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 1){
			for(Player player: Bukkit.getServer().getOnlinePlayers()) {
				if(player.getName().equalsIgnoreCase(args[1])) {
					InetAddress ip = player.getAddress().getAddress();
					String data = MyMaid.url_access("http://toma.webcrow.jp/jao.php?file=access.php&i="+ip);
					if(data.equalsIgnoreCase("NO")){
						sender.sendMessage("[PCA] " + ChatColor.GREEN + "このユーザー「"+player.getName()+"」がアクセスしたページ:なし");
					}else if(data.indexOf(",") == -1){
						for(Player p: Bukkit.getServer().getOnlinePlayers()) {
							if(p.hasPermission("pin_code_auth.joinmsg")) {
								p.sendMessage("[PCA] " + ChatColor.GREEN + "このユーザー「"+player.getName()+"」がアクセスしたページ:"+data+"");
							}
						}
						Bukkit.getLogger().info("このユーザーがアクセスしたページ:"+data+"");
					}else{
						String[] access = data.split(",", 0);
						String accesstext = "";
						for (String one: access){
							accesstext += "「"+one+"」";
						}
						for(Player p: Bukkit.getServer().getOnlinePlayers()) {
							if(p.hasPermission("pin_code_auth.joinmsg")) {
								p.sendMessage("[PCA] " + ChatColor.GREEN + "このユーザー「"+player.getName()+"」がアクセスしたページ:"+accesstext+"など");
							}
						}
						Bukkit.getLogger().info("このユーザーがアクセスしたページ:"+accesstext+"など");
					}
				}
			}


		}

		return true;
	}
}
