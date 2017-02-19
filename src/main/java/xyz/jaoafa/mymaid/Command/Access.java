package xyz.jaoafa.mymaid.Command;

import java.net.InetAddress;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Method;

public class Access implements CommandExecutor {
	JavaPlugin plugin;
	public Access(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 1){
			Player cmdplayer = (Player) sender;
			for(Player player: Bukkit.getServer().getOnlinePlayers()) {
				if(player.getName().equalsIgnoreCase(args[0])) {
					InetAddress ip = player.getAddress().getAddress();
					try{
						new netaccess(plugin, player, cmd, ip, cmdplayer).runTaskAsynchronously(plugin);
					}catch(java.lang.NoClassDefFoundError e){
						for(Player p: Bukkit.getServer().getOnlinePlayers()) {
							if(PermissionsEx.getUser(p).inGroup("Admin")) {
								p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。通常は再起動で直りますが直らない場合は開発者に連絡を行ってください。");
								p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
							}
						}
					}

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
	private class netaccess extends BukkitRunnable{
		Player player;
		Command cmd;
		InetAddress ip;
		Player cmdplayer;
    	public netaccess(JavaPlugin plugin, Player player, Command cmd, InetAddress ip, Player cmdplayer) {
    		this.player = player;
    		this.cmd = cmd;
    		this.ip = ip;
    		this.cmdplayer = cmdplayer;
    	}
		@Override
		public void run() {
			String data = Method.url_jaoplugin("access", "i="+ip);
			if(data.equalsIgnoreCase("NO")){
				Method.SendMessage(cmdplayer, cmd, "このユーザー「"+player.getName()+"」がアクセスしたページ:なし");
				return;
			}else if(data.indexOf(",") == -1){
				Method.SendMessage(cmdplayer, cmd, "このユーザー「"+player.getName()+"」がアクセスしたページ:"+data+"");
				Bukkit.getLogger().info("このユーザーがアクセスしたページ:"+data+"");
				return;
			}else{
				String[] access = data.split(",", 0);
				String accesstext = "";
				for (String one: access){
					accesstext += "「"+one+"」";
				}
				Method.SendMessage(cmdplayer, cmd, "このユーザー「"+player.getName()+"」がアクセスしたページ:"+accesstext+"など");
				Bukkit.getLogger().info("このユーザーがアクセスしたページ:"+accesstext+"など");
				return;
			}
		}

	}
}
