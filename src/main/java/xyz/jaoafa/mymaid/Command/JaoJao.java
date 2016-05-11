package xyz.jaoafa.mymaid.Command;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class JaoJao implements CommandExecutor {
	JavaPlugin plugin;
	public JaoJao(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/* onCommand jf
	 * jao afaします。
	 * /jf */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		// 変数定義
		if (!(sender instanceof Player)) {
			sender.sendMessage("[JAOJAO] " + ChatColor.GREEN + "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		final Player player = (Player) sender;
		Date Date = new Date();
		SimpleDateFormat H = new SimpleDateFormat("H");
		SimpleDateFormat m = new SimpleDateFormat("m");
		SimpleDateFormat s = new SimpleDateFormat("s");
		String Hs = H.format(Date);
		String ms = m.format(Date);
		String ss = s.format(Date);
		String date = String.format("%02d", Integer.parseInt(Hs)) + ":" + String.format("%02d", Integer.parseInt(ms)) + ":" + String.format("%02d", Integer.parseInt(ss));
		Bukkit.broadcastMessage(ChatColor.GRAY + "[" + date +"]"  + ChatColor.WHITE + player.getName() +  ": jaojao");
		return true;
	}
}
