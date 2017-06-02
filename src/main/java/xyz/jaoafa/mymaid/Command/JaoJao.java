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

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Pointjao;
import xyz.jaoafa.mymaid.Discord.Discord;
import xyz.jaoafa.mymaid.SKKColors.SKKColors;

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
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;

		int use = 1;
		if(!Pointjao.hasjao(player, use)){
		 	 Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
		 	 return true;
		}
		Pointjao.usejao(player, use, "jaojaoコマンド実行の為");

		String Msg = SKKColors.getPlayerSKKChatColor(player) + "■" + ChatColor.WHITE + player.getName();

		Date Date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + Msg + ": jaojao");
		Discord.send("**" + player.getName() + "**: jaojao");
		Method.SendTipsALL("このjaojaoって言うのはこのサーバからログアウトする前にする挨拶だよ！やってみよう！");
		return true;
	}
}
