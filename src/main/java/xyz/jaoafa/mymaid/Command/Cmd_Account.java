package xyz.jaoafa.mymaid.Command;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;

public class Cmd_Account implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Account(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	String url = "https://jaoafa.com/wp/wp-login.php";

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		if(args.length == 0){
			JSONObject json = getJson(player, "wp_getaccount");
			if(getStatus(json)){
				Method.SendMessage(sender, cmd, "--- jaoAccount Data ---");
				Method.SendMessage(sender, cmd, "ID: " + json.get("userlogin"));
				Method.SendMessage(sender, cmd, "URL: " + url);
				Method.SendMessage(sender, cmd, "パスワードはセキュリティ保護のため、確認するにはリセットする必要があります。");
				Method.SendMessage(sender, cmd, "/account resetを使ってパスワードをリセットしてください。");
			}else{
				Method.SendMessage(sender, cmd, "jaoアカウントが存在しませんでした。");
				Method.SendMessage(sender, cmd, "/account createを使ってアカウントを作成してください！");
			}

			return true;
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("reset")){
				JSONObject json = getJson(player, "wp_psreset");
				if(getStatus(json)){
					Method.SendMessage(sender, cmd, "jaoアカウントのパスワードリセットに成功しました。");
					Method.SendMessage(sender, cmd, "ID: " + json.get("id"));
					Method.SendMessage(sender, cmd, "Password: " + json.get("password"));
					Method.SendMessage(sender, cmd, "URL: " + url);
					Method.SendMessage(sender, cmd, "パスワードはセキュリティ保護のため、ログイン後変更することをお勧めします。");
				}else{
					Method.SendMessage(sender, cmd, "jaoアカウントのパスワードリセットに失敗しました。");
				}
				return true;
			}else if(args[0].equalsIgnoreCase("create")){
				if(getStatus(getJson(player, "wp_getaccount"))){
					Method.SendMessage(sender, cmd, "jaoアカウントは既に作成されています。");
				}else{
					JSONObject json = getJson(player, "wp_create");
					if(getStatus(json)){
						Method.SendMessage(sender, cmd, "jaoアカウントを作成しました！");
						Method.SendMessage(sender, cmd, "ID: " + json.get("id"));
						Method.SendMessage(sender, cmd, "Password: " + json.get("password"));
						Method.SendMessage(sender, cmd, "URL: " + url);
					}else{
						Method.SendMessage(sender, cmd, "jaoアカウントの作成に失敗しました…。");
					}
				}
				return true;
			}
		}else if(args.length >= 2){
			if(args[0].equalsIgnoreCase("setdesc")){
				String description = "";
				int c = 1;
				while(args.length > c){
					description += args[c];
					if(args.length != (c+1)){
						description += " ";
					}
					c++;
				}
				JSONObject json;
				try {
					json = getJson(player, "wp_setdesc", "desc=" + URLEncoder.encode(description, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					json = getJson(player, "wp_setdesc", "desc=" + description);
				}
				if(getStatus(json)){
					Method.SendMessage(sender, cmd, "自己紹介の変更に成功しました。");
				}else{
					if(((String) json.get("error")).equalsIgnoreCase("User Not Found")){
						Method.SendMessage(sender, cmd, "jaoアカウントが存在しませんでした。");
						Method.SendMessage(sender, cmd, "/account createを使ってアカウントを作成してください！");
					}else{
						Method.SendMessage(sender, cmd, "自己紹介の変更に失敗しました…。");
					}
				}
			}
			return true;
		}
		Method.SendMessage(sender, cmd, "--- jaoAccount Help ---");
		Method.SendMessage(sender, cmd, "/account: jaoアカウントの情報を表示します。");
		Method.SendMessage(sender, cmd, "/account create: jaoアカウントを作成します。");
		Method.SendMessage(sender, cmd, "/account reset: jaoアカウントのパスワードをリセットします。");
		Method.SendMessage(sender, cmd, "/account setdesc <Description>: jaoアカウントに自己紹介を追加します。");
		return true;
	}

	public static JSONObject getJson(Player player, String file){
		String data = Method.url_jaoplugin(file, "id="+player.getName()+"&uuid="+player.getUniqueId());
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(data);
		} catch (ParseException e2) {
			BugReport.report(e2);
			return null;
		}
		return obj;
	}
	public static JSONObject getJson(Player player, String file, String args){
		String data = Method.url_jaoplugin(file, "id="+player.getName()+"&uuid="+player.getUniqueId() + "&" + args);
		JSONParser parser = new JSONParser();
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(data);
		} catch (ParseException e2) {
			BugReport.report(e2);
			return null;
		}
		return obj;
	}
	public static boolean getStatus(JSONObject json){
		if(json.containsKey("status")){
			return ((boolean) json.get("status"));
		}
		return false;
	}
}
