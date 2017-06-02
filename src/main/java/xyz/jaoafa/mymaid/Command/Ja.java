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
import org.bukkit.scheduler.BukkitRunnable;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Pointjao;
import xyz.jaoafa.mymaid.Discord.Discord;
import xyz.jaoafa.mymaid.SKKColors.SKKColors;

public class Ja implements CommandExecutor {
	JavaPlugin plugin;
	public Ja(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/* onCommand jf
	 * jao afaします。
	 * /jf */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		// 変数定義
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;

		int use = 2;
		if(!Pointjao.hasjao(player, use)){
		 	 Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
		 	 return true;
		}
		Pointjao.usejao(player, use, "jaコマンド実行の為");

		String Msg = SKKColors.getPlayerSKKChatColor(player) + "■" + ChatColor.WHITE + player.getName();

		Date Date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + Msg + ": jai");
		Discord.send("**" + Msg + "**: jai");
		try{
			new jai_uwa(Msg, player).runTaskLater(plugin, 60);
		}catch(java.lang.NoClassDefFoundError e){
			Method.SendMessage(sender, cmd, BugReport.report(e));
		}
		return true;
	}
	private class jai_uwa extends BukkitRunnable{
		Player player;
		String Msg;
		public jai_uwa(String Msg, Player player) {
			this.Msg = Msg;
			this.player = player;
		}
		@Override
		public void run() {
			Date Date = new Date();
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + Msg + ": uwa");
			Discord.send("**" + player.getName() + "**: uwa");
			cancel();
		}
	}
}
