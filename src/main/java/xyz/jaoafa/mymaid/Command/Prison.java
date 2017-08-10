package xyz.jaoafa.mymaid.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Jail.Jail;

public class Prison implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public Prison(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 2){
			if(args[0].equalsIgnoreCase("add")){
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null){
					OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[1]);
					if(offplayer == null){
						Method.SendMessage(sender, cmd, "プレイヤー情報を読み込めません。");
						return true;
					}
					Jail.JailAdd(offplayer, sender);
					return true;
				}
				Jail.JailAdd(player, sender);
				return true;
			}else if(args[0].equalsIgnoreCase("remove")){
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null){
					Method.SendMessage(sender, cmd, "プレイヤー情報を読み込めません。");
					return true;
				}
				Jail.JailRemove(cmd, player, sender);
				return true;
			}
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("area")){
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null){
					Method.SendMessage(sender, cmd, "プレイヤー情報を読み込めません。");
					return true;
				}
				boolean after = Boolean.valueOf(args[2]);
				Jail.JailArea(cmd, player, sender, after);
				return true;
			}else if(args[0].equalsIgnoreCase("block")){
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null){
					Method.SendMessage(sender, cmd, "プレイヤー情報を読み込めません。");
					return true;
				}
				boolean after = Boolean.valueOf(args[2]);
				Jail.JailBlock(cmd, player, sender, after);
				return true;
			}else if(args[0].equalsIgnoreCase("add")){
				Player player = Bukkit.getPlayer(args[1]);
				String text = "";
				int c = 2;
				while(args.length > c){
					text += args[c];
					if(args.length != (c+1)){
						text += " ";
					}
					c++;
				}
				if(player == null){
					OfflinePlayer offplayer = Bukkit.getOfflinePlayer(args[1]);
					if(offplayer == null){
						Method.SendMessage(sender, cmd, "プレイヤー情報を読み込めません。");
						return true;
					}
					Jail.JailAdd(offplayer, sender, text);
					return true;
				}

				Jail.JailAdd(player, sender, text, false);
				return true;
			}
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("list")){
				Jail.SendList(sender, cmd);
				return true;
			}
		}
		Method.SendMessage(sender, cmd, "---- Jail ----");
		Method.SendMessage(sender, cmd, "/jail add <Player> [Reason] - プレイヤーを南の楽園に移動し、出られなくします。");
		Method.SendMessage(sender, cmd, "/jail remove <Player> - プレイヤーを南の楽園リストから削除します。");
		Method.SendMessage(sender, cmd, "/jail list - 南の楽園リストを表示します。");
		Method.SendMessage(sender, cmd, "/jail area <Player> <true|false> - プレイヤーが南の楽園から出られるかどうかを設定します。");
		Method.SendMessage(sender, cmd, "/jail block <Player> <true|false> - プレイヤーがコマンドを実行できるかを設定します。");
		Method.SendMessage(sender, cmd, "/jail lasttext <Text> - 遺言を残します。");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			if (args[0].length() == 0) {
				return Arrays.asList("add", "remove", "list", "area", "block", "lasttext");
			} else {
				//入力されている文字列と先頭一致
				if ("add".startsWith(args[0])) {
					return Collections.singletonList("add");
				} else if ("remove".startsWith(args[0])) {
					return Collections.singletonList("remove");
				} else if ("list".startsWith(args[0])) {
					return Collections.singletonList("list");
				} else if ("area".startsWith(args[0])) {
					return Collections.singletonList("area");
				} else if ("block".startsWith(args[0])) {
					return Collections.singletonList("block");
				} else if ("lasttext".startsWith(args[0])) {
					return Collections.singletonList("lasttext");
				}
			}
		}
		//JavaPlugin#onTabComplete()を呼び出す
		return plugin.onTabComplete(sender, command, alias, args);
	}

}