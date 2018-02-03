package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.PermissionsManager;

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
		String p = args[0];
		Player player = (Player) sender; //コマンド実行者を代入
		Player changeplayer = Bukkit.getPlayer(p);
		if(changeplayer == null){
			Method.SendMessage(sender, cmd, "そのプレイヤーはオフラインです。");
			return true;
		}
		String group = PermissionsManager.getPermissionMainGroup(changeplayer);
		if(group.equalsIgnoreCase("Regular")){
			Method.SendMessage(sender, cmd, "そのプレイヤーは既にRegular権限です。");
			return true;
		}
		if(group.equalsIgnoreCase("Limited")){
			Method.SendMessage(sender, cmd, "そのプレイヤーはLimited権限です。");
			return true;
		}
		Method.SendMessage(sender, cmd, "プレイヤー「" + changeplayer.getName() + "」をRegular権限に引き上げます…");
		/*Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
		for(String g : groups){
			if(PermissionsEx.getUser(p).inGroup(g)){
				PermissionsEx.getUser(p).removeGroup(g);
			}
		}
		PermissionsEx.getUser(p).addGroup("Regular");
		*/
		PermissionsManager.setPermissionsGroup(changeplayer, "Regular");
		changeplayer.setOp(true);
		Method.SendMessage(sender, cmd, "プレイヤー「"+p+"」をRegular権限に引き上げました。");
		Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + "「" + player.getName() + "」によってプレイヤー「" + changeplayer.getName() + "」が常連になりました。");
		changeplayer.sendMessage("[MyMaid] " + ChatColor.GREEN + "常連権限への昇格、おめでとうごさいます。");
		changeplayer.sendMessage("[MyMaid] " + ChatColor.GREEN + "常連権限としてサーバーで活動する際の説明を記載しました。ぜひご覧ください。");
		changeplayer.sendMessage("[MyMaid] " + ChatColor.GREEN + "https://jaoafa.com/rule/regular_getplayer");
		return true;
	}
}
