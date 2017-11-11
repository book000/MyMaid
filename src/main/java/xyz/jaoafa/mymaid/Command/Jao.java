package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Pointjao;

public class Jao implements CommandExecutor {
	JavaPlugin plugin;
	public Jao(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){

		if(args.length == 1){
			if(args[0].equalsIgnoreCase("history")){
				if (!(sender instanceof Player)) {
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
					return true;
				}
				Method.SendMessage(sender, cmd, "仕様策定中ですのでサイトでご確認ください。 https://jaoafa.com/point/check.php?uuid=" + ((Player) sender).getUniqueId().toString());
			}else if(args[0].equalsIgnoreCase("help")){
				// ヘルプ
				Method.SendMessage(sender, cmd, "/jao: あなたのjaoポイント残高を確認します。");
				Method.SendMessage(sender, cmd, "/jao history: あなたのjaoポイントを履歴を表示します。");
				Method.SendMessage(sender, cmd, "/jao help: このヘルプを表示します。");
				Method.SendMessage(sender, cmd, "/jao add <Player> <Point> <Reason>: PlayerにReasonという理由でPointを追加します。");
				Method.SendMessage(sender, cmd, "/jao use <Player> <Point> <Reason>: PlayerからReasonという理由でPointを減算します。");
				return true;
			}
			Player player = Bukkit.getPlayer(args[0]);
			if(player == null){
				Method.SendMessage(sender, cmd, "プレイヤーが見つかりません。");
				return true;
			}
			int now = Pointjao.getjao(player);
			Method.SendMessage(sender, cmd, "現在" + player.getName() + "が所持しているポイント数は" + now + "ポイントです。");
			return true;
		}else if(args.length >= 4){
			// /jao add player point reason
			// /jao use player point reason
			if(args[0].equalsIgnoreCase("add")){
				// /jao add player point reason
				@SuppressWarnings("deprecation")
				OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[1]);
				if(offplayer == null){
					Method.SendMessage(sender, cmd, "プレイヤーが見つかりません。");
					return true;
				}

				int point;
				try{
					point = Integer.parseInt(args[2]);
				}catch(NumberFormatException e){
					Method.SendMessage(sender, cmd, "ポイントには数値を指定してください。");
					return true;
				}
				if(point <= 0){
					Method.SendMessage(sender, cmd, "ポイントは1以上を指定してください。");
					return true;
				}

				String reason = "";
				int c = 3;
				while(args.length > c){
					reason += args[c];
					if(args.length != (c+1)){
						reason += " ";
					}
					c++;
				}
				if (sender instanceof Player) {
					reason += " (Player: " + sender.getName() + ")";
				}else if (sender instanceof BlockCommandSender) {
					BlockCommandSender bcs = (BlockCommandSender) sender;
					Block block = bcs.getBlock();
					reason += " (CmdBlock: " + block.getX() + " " + block.getY() + " " + block.getZ() + ")";
				}else if (sender instanceof CommandMinecart) {
					CommandMinecart cm = (CommandMinecart) sender;
					Location loc = cm.getLocation();
					reason += " (CmdCart: " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + ")";
				}else if (sender instanceof RemoteConsoleCommandSender) {
					reason += " (Rcon)";
				}else if (sender instanceof ConsoleCommandSender) {
					reason += " (Console)";
				}else if (sender instanceof ProxiedCommandSender) {
					ProxiedCommandSender pcs = (ProxiedCommandSender) sender;
					CommandSender Callee_sender = pcs.getCallee(); // コマンドの呼び出しに使用されているCommandSenderを返します。(コマンド実行させられているCommandSender？)
					CommandSender Caller_sender = pcs.getCaller(); // このプロキシされたコマンドをトリガしたCommandSenderを返します。(executeコマンドを実行したCommandSender？)

					reason += " (Execute: " + Callee_sender.getName() + " [" + Callee_sender + "] => " + Caller_sender.getName() + " [" + Caller_sender + "])";
				}else{
					reason += " (実行元特定不能: " + sender.getName() + "|" + sender.toString() + ")";
				}

				boolean bool = Pointjao.addjao(offplayer, point, reason);
				if(bool){
					Method.SendMessage(sender, cmd, "プレイヤー「" +  offplayer.getName() + "」に" + point + "を追加しました。");
				}else{
					Method.SendMessage(sender, cmd, "プレイヤー「" +  offplayer.getName() + "」に" + point + "を追加できませんでした。");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("use")){
				// /jao use player point reason
				@SuppressWarnings("deprecation")
				OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[1]);
				if(offplayer == null){
					Method.SendMessage(sender, cmd, "プレイヤーが見つかりません。");
					return true;
				}

				int point;
				try{
					point = Integer.parseInt(args[2]);
				}catch(NumberFormatException e){
					Method.SendMessage(sender, cmd, "ポイントには数値を指定してください。");
					return true;
				}
				if(point <= 0){
					Method.SendMessage(sender, cmd, "ポイントは1以上を指定してください。");
					return true;
				}

				String reason = "";
				int c = 3;
				while(args.length > c){
					reason += args[c];
					if(args.length != (c+1)){
						reason += " ";
					}
					c++;
				}
				if (sender instanceof Player) {
					reason += " (Player: " + sender.getName() + ")";
				}else if (sender instanceof BlockCommandSender) {
					BlockCommandSender bcs = (BlockCommandSender) sender;
					Block block = bcs.getBlock();
					reason += " (CmdBlock: " + block.getX() + " " + block.getY() + " " + block.getZ() + ")";
				}else if (sender instanceof CommandMinecart) {
					CommandMinecart cm = (CommandMinecart) sender;
					Location loc = cm.getLocation();
					reason += " (CmdCart: " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + ")";
				}else if (sender instanceof RemoteConsoleCommandSender) {
					reason += " (Rcon)";
				}else if (sender instanceof ConsoleCommandSender) {
					reason += " (Console)";
				}else if (sender instanceof ProxiedCommandSender) {
					ProxiedCommandSender pcs = (ProxiedCommandSender) sender;
					CommandSender Callee_sender = pcs.getCallee(); // コマンドの呼び出しに使用されているCommandSenderを返します。(コマンド実行させられているCommandSender？)
					CommandSender Caller_sender = pcs.getCaller(); // このプロキシされたコマンドをトリガしたCommandSenderを返します。(executeコマンドを実行したCommandSender？)

					reason += " (Execute: " + Callee_sender.getName() + " [" + Callee_sender + "] => " + Caller_sender.getName() + " [" + Caller_sender + "])";
				}else{
					reason += " (実行元特定不能: " + sender.getName() + "|" + sender.toString() + ")";
				}

				boolean bool = Pointjao.usejao(offplayer, point, reason);
				if(bool){
					Method.SendMessage(sender, cmd, "プレイヤー「" +  offplayer.getName() + "」から" + point + "を減算しました。");
				}else{
					Method.SendMessage(sender, cmd, "プレイヤー「" +  offplayer.getName() + "」から" + point + "を減算できませんでした。");
				}
				return true;
			}
		}
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		int now = Pointjao.getjao(player);
		Method.SendMessage(sender, cmd, "現在あなたが所持しているポイント数は" + now + "ポイントです。");
		return true;
	}
}
