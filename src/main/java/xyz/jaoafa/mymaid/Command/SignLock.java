package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.MyMaid;

public class SignLock implements CommandExecutor {
	JavaPlugin plugin;
	public SignLock(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			sender.sendMessage("[Sign] " + ChatColor.GREEN + "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		org.bukkit.block.Sign sign;
		try {
			sign = xyz.jaoafa.mymaid.Command.Sign.signlist.get(player.getName());
		}catch(Exception e){
			sender.sendMessage("[Sign] " + ChatColor.GREEN + "看板が選択されていません。");
			return true;
		}
		int x = sign.getX();
		int y = sign.getY();
		int z = sign.getZ();
		String result = MyMaid.url_access("http://toma.webcrow.jp/jao.php?file=signlock.php&lock&p="+player.getName()+"&x="+x+"&y="+y+"&z="+z);
		if(result.equalsIgnoreCase("Err")){
			sender.sendMessage("[Sign] " + ChatColor.GREEN + "ロック出来ませんでした。");
		}else{
			sender.sendMessage("[Sign] " + ChatColor.GREEN + "ロックしました。");
		}
		return true;
	}
}
