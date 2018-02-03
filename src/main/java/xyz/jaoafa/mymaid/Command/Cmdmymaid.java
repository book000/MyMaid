package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.PermissionsManager;
import xyz.jaoafa.mymaid.Pointjao;
import xyz.jaoafa.mymaid.WorldAllowCommand;
import xyz.jaoafa.mymaid.Jail.Jail;
import xyz.jaoafa.mymaid.SKKColors.SKKColors;

public class Cmdmymaid implements CommandExecutor {
	JavaPlugin plugin;
	public Cmdmymaid(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 3){
			if(args[0].equalsIgnoreCase("jaoadd")){
				if (!(sender instanceof org.bukkit.entity.Player)) {
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}
				Player p = (org.bukkit.entity.Player) sender;
				String group = PermissionsManager.getPermissionMainGroup(p);
				if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator")){
					Method.SendMessage(sender, cmd, "このコマンドは管理部のみ使用可能です。");
				}
				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					if(player.getName().equalsIgnoreCase(args[1])){
						int add;
						try{
							add = Integer.parseInt(args[2]);
						} catch (NumberFormatException nfe) {
							Method.SendMessage(sender, cmd, "ポイントには数値を指定してください。");
							return true;
						}
						Pointjao.addjao(player, add, "管理部からのポイント追加処理");
						Method.SendMessage(sender, cmd, player.getName() + "に" + add + "ポイントを追加しました。");
						return true;
					}
				}
				Method.SendMessage(sender, cmd, "プレイヤーが見つかりません。");
				return true;
			}else if(args[0].equalsIgnoreCase("jaouse")){
				if (!(sender instanceof org.bukkit.entity.Player)) {
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}
				Player p = (org.bukkit.entity.Player) sender;
				String group = PermissionsManager.getPermissionMainGroup(p);
				if(!group.equalsIgnoreCase("Admin") && !group.equalsIgnoreCase("Moderator")){
					Method.SendMessage(sender, cmd, "このコマンドは管理部のみ使用可能です。");
				}
				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					if(player.getName().equalsIgnoreCase(args[1])){
						int add;
						try{
							add = Integer.parseInt(args[2]);
						} catch (NumberFormatException nfe) {
							Method.SendMessage(sender, cmd, "ポイントには数値を指定してください。");
							return true;
						}
						Pointjao.usejao(player, add, "管理部からのポイント減算処理");
						Method.SendMessage(sender, cmd, player.getName() + "から" + add + "ポイントを減らしました。");
						return true;
					}
				}
				Method.SendMessage(sender, cmd, "プレイヤーが見つかりません。");
				return true;
			}
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("pointload")){
				try {
					Pointjao.Loadjao();
					Method.SendMessage(sender, cmd, "MyMaid Point Data Loaded!");
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					Method.SendMessage(sender, cmd, "MyMaid Point Data Load Err...");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("pointsave")){
				try {
					Pointjao.Savejao();
					Method.SendMessage(sender, cmd, "MyMaid Point Data Saved!");
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					Method.SendMessage(sender, cmd, "MyMaid Point Data Save Err...");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("jailload")){
				try {
					Jail.LoadJailData();
					Method.SendMessage(sender, cmd, "MyMaid Jail Data Loaded!");
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					Method.SendMessage(sender, cmd, "MyMaid Jail Data Load Err...");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("jailsave")){
				try {
					Jail.SaveJailData();
					Method.SendMessage(sender, cmd, "MyMaid Jail Data Saved!");
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					Method.SendMessage(sender, cmd, "MyMaid Jail Data Save Err...");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("skkload")){
				if(SKKColors.Load()){
					Method.SendMessage(sender, cmd, "MyMaid SKK Data Loaded!");
				}else{
					Method.SendMessage(sender, cmd, "MyMaid SKK Data Load Err...");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("skksave")){
				if(SKKColors.Save()){
					Method.SendMessage(sender, cmd, "MyMaid SKK Data Saved!");
				}else{
					Method.SendMessage(sender, cmd, "MyMaid SKK Data Save Err...");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("worldcmdload")){
				if(WorldAllowCommand.Load()){
					Method.SendMessage(sender, cmd, "MyMaid WorldCommand Data Loaded!");
				}else{
					Method.SendMessage(sender, cmd, "MyMaid WorldCommand Data Load Err...");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("worldcmdsave")){
				if(WorldAllowCommand.Save()){
					Method.SendMessage(sender, cmd, "MyMaid WorldCommand Data Saved!");
				}else{
					Method.SendMessage(sender, cmd, "MyMaid WorldCommand Data Save Err...");
				}
				return true;
			}
		}

		Method.SendMessage(sender, cmd, "---- MyMaid Command ----");
		Method.SendMessage(sender, cmd, "/mymaid jaoadd <Player> <JaoPoint>: 管理部からのポイント追加処理を行います。");
		Method.SendMessage(sender, cmd, "/mymaid jaouse <Player> <JaoPoint>: 管理部からのポイント減算処理を行います。");
		Method.SendMessage(sender, cmd, "/mymaid pointload: jaoポイントデータをロードします。");
		Method.SendMessage(sender, cmd, "/mymaid pointsave: jaoポイントデータをセーブします。");
		Method.SendMessage(sender, cmd, "/mymaid jailload: Jail情報をロードします。");
		Method.SendMessage(sender, cmd, "/mymaid jailsave: Jail情報をセーブします。");
		Method.SendMessage(sender, cmd, "/mymaid skkload: 投票数ログインメッセージ情報をロードします。");
		Method.SendMessage(sender, cmd, "/mymaid skksave: 投票数ログインメッセージ情報をセーブします。");
		Method.SendMessage(sender, cmd, "/mymaid worldcmdload: コマンドの使用可否情報をロードします。");
		Method.SendMessage(sender, cmd, "/mymaid worldcmdsave: コマンドの使用可否情報をセーブします。");
		return true;
	}
}
