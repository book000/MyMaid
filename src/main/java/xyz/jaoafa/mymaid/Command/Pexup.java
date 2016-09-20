package xyz.jaoafa.mymaid.Command;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Method;

public class Pexup implements CommandExecutor {
	JavaPlugin plugin;
	public Pexup(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			Method.SendMessage(sender, cmd, "このコマンドは1つの引数が必要です。");
			return true;
		}
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはプレイヤーのみの使用に限られています。");
			return true;
		}
		String p;
		p = args[0];
		Player player = (Player) sender; //コマンド実行者を代入
		if(PermissionsEx.getUser(p).inGroup("Regular")){
			Method.SendMessage(sender, cmd, "そのユーザーは既にRegular権限です。");
			return true;
		}
		if(PermissionsEx.getUser(p).inGroup("Limited")){
			Method.SendMessage(sender, cmd, "そのユーザーはLimited権限です。");
			return true;
		}
		Method.SendMessage(sender, cmd, "ユーザー「" + p + "」をRegular権限に引き上げます…");
		Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
		for(String group : groups){
			if(PermissionsEx.getUser(p).inGroup(group)){
				PermissionsEx.getUser(p).removeGroup(group);
			}
		}
		PermissionsEx.getUser(p).addGroup("Regular");
		PermissionsEx.getUser(p).getPlayer().setOp(true);
		Method.SendMessage(sender, cmd, "ユーザー「"+p+"」をRegular権限に引き上げました。");
		Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + "「" + player.getName() + "」によってユーザー「" + p + "」が常連になりました。");
		for(Player pl: Bukkit.getServer().getOnlinePlayers()) {
			if(p.equalsIgnoreCase(pl.getName())){
				pl.sendMessage("[MyMaid] " + ChatColor.GREEN + "常連権限への昇格、おめでとうごさいます。");
				pl.sendMessage("[MyMaid] " + ChatColor.GREEN + "常連権限としてサーバーで活動する際の説明を記載しました。ぜひご覧ください。");
				pl.sendMessage("[MyMaid] " + ChatColor.GREEN + "https://jaoafa.xyz/rule/regular_getplayer");
				return true;
			}
		}
		return true;
	}
}
