package xyz.jaoafa.mymaid.Command;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Method;

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
		String g = "";
		Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
		for(String group : groups){
			if(PermissionsEx.getUser(player).inGroup(group)){
				g = group;
			}
		}
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
		}else{
			Method.SendMessage(sender, cmd, "不明なエラーが発生しました。");
			return true;
		}
	}
}
