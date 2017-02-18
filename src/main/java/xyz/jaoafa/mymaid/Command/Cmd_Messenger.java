package xyz.jaoafa.mymaid.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Messenger;
import xyz.jaoafa.mymaid.Method;

public class Cmd_Messenger {
	JavaPlugin plugin;
	public Cmd_Messenger(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("broadcast")){
				Messenger.BroadcastMessage();
				return true;
			}else if(args[0].equalsIgnoreCase("list")){
				Method.SendMessage(sender, cmd, "----- Message List -----");
				int i = 0;
				for(String message : Messenger.getMessages()) {
					Method.SendMessage(sender, cmd, "[" + i + "] " + message);
					i++;
				}
				return true;
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("add")){
				if (sender instanceof Player) {
					Player p = (Player) sender;
					if(!PermissionsEx.getUser(p).inGroup("Admin")){
						Method.SendMessage(sender, cmd, "このコマンドは管理部のみ使用可能です。");
						return true;
					}
				}else if(!(sender instanceof ConsoleCommandSender)){
					Method.SendMessage(sender, cmd, "このコマンドはサーバ内もしくはコンソールから実行可能です。");
					return true;
				}

				String message = args[1];
				boolean result = Messenger.Add(message);
				if(result){
					Method.SendMessage(sender, cmd, "メッセージ「" + message + "」の追加処理に成功しました。");
				}else{
					Method.SendMessage(sender, cmd, "メッセージ「" + message + "」の追加処理に失敗しました。");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("del")){
				if (sender instanceof Player) {
					Player p = (Player) sender;
					if(!PermissionsEx.getUser(p).inGroup("Admin")){
						Method.SendMessage(sender, cmd, "このコマンドは管理部のみ使用可能です。");
						return true;
					}
				}else if(!(sender instanceof ConsoleCommandSender)){
					Method.SendMessage(sender, cmd, "このコマンドはサーバ内もしくはコンソールから実行可能です。");
					return true;
				}

				int i = Integer.parseInt(args[1]);
				if(!Messenger.Contains(i)){
					Method.SendMessage(sender, cmd, "指定されたメッセージID「" + args[1] + "」は存在しません。");
				}
				String message = Messenger.Get(i);
				boolean result = Messenger.Del(i);
				if(result){
					Method.SendMessage(sender, cmd, "メッセージ「" + message + "」の削除に成功しました。");
				}else{
					Method.SendMessage(sender, cmd, "メッセージ「" + message + "」の削除に失敗しました。");
				}
				return true;
			}
		}
		Method.SendMessage(sender, cmd, "----- Messenger Help -----");
		Method.SendMessage(sender, cmd, "/messenger list: メッセージのリストを表示します。");
		Method.SendMessage(sender, cmd, "/messenger broadcast: メッセージを今すぐにランダムに1つ放送します。");
		Method.SendMessage(sender, cmd, "/messenger add <Message>: メッセージを追加します。");
		Method.SendMessage(sender, cmd, "/messenger del <MessageID>: 指定されたメッセージIDのメッセージを削除します");
		return true;
	}
}
