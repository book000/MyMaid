package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.PermissionsManager;

public class DiscordLink implements CommandExecutor {
	JavaPlugin plugin;
	public DiscordLink(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		if(args.length != 1){
			Method.SendMessage(sender, cmd, "引数は1つのみにしてください。(/discordlink <AuthID>)");
			return true;
		}
		String g = PermissionsManager.getPermissionMainGroup(player);
		String result = Method.url_jaoplugin("disauth", "player=" + player.getName() + "&uuid=" + player.getUniqueId() + "&authid=" + args[0] + "&pex=" + g);
		if(result.equalsIgnoreCase("Err")){
			Method.SendMessage(sender, cmd, "AuthIDは英数字のみ受け付けています。");
			return true;
		}else if(result.equalsIgnoreCase("NF")){
			Method.SendMessage(sender, cmd, "指定されたAuthIDは見つかりませんでした。");
			return true;
		}else if(result.equalsIgnoreCase("ok")){
			Method.SendMessage(sender, cmd, "アカウントのリンクが完了しました。");
			return true;
		}else if(result.equalsIgnoreCase("AlreadyThis")){
			Method.SendMessage(sender, cmd, "すでにあなたのMinecraftアカウントと接続されています。");
			return true;
		}else if(result.equalsIgnoreCase("AlreadyMC")){
			Method.SendMessage(sender, cmd, "すでにあなたのMinecraftアカウントは別のDiscordアカウントに接続されています。");
			return true;
		}else if(result.equalsIgnoreCase("AlreadyDis")){
			Method.SendMessage(sender, cmd, "アカウントリンク要求をしたDiscordアカウントは既に他のMinecraftアカウントと接続されています。");
			return true;
		}else if(result.equalsIgnoreCase("ANF")){
			Method.SendMessage(sender, cmd, "アカウントリンク要求をしたDiscordアカウントは既に当サーバのDiscordチャンネルから退出しています。");
			return true;
		}else{
			Method.SendMessage(sender, cmd, "不明なエラーが発生しました。");
			return true;
		}
	}
}
