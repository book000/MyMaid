package xyz.jaoafa.mymaid.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.MySQL;
import xyz.jaoafa.mymaid.PermissionsManager;

public class Vote implements CommandExecutor {
	JavaPlugin plugin;
	public Vote(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static FileConfiguration conf;
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					String group = PermissionsManager.getPermissionMainGroup(p);
					if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
						p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。(NoVote/ClassNotFoundException | SQLException)通常は再起動で直りますが直らない場合は開発者に連絡を行ってください。");
						p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
					}
				}
				return true;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			for(Player p: Bukkit.getServer().getOnlinePlayers()) {
				String group = PermissionsManager.getPermissionMainGroup(p);
				if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。(NoVote/SQLException)通常は再起動で直りますが直らない場合は開発者に連絡を行ってください。");
					p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
				}
			}
			return true;
		}

		statement = MySQL.check(statement);

		if(args.length != 1){
			Method.SendMessage(sender, cmd, "引数にはプレイヤー名かUUIDを設定してください。");
			return true;
		}

		String player_or_uuid = args[0];

		try{
			UUID uuid = UUID.fromString(player_or_uuid);

			try {
				ResultSet res = statement.executeQuery("SELECT * FROM vote WHERE uuid = '" + uuid.toString() +"'");
				Calendar cal = Calendar.getInstance();
				cal.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
				long today9 = cal.getTimeInMillis() / 1000L;

				cal.add(Calendar.DAY_OF_MONTH, -1);
				long yesterday9 = cal.getTimeInMillis() / 1000L;

				long now = System.currentTimeMillis() / 1000L;

				boolean checktype; // true: 今日の9時 / false: 昨日の9時
				if(today9 <= now){
					checktype = true;
				}else{
					checktype = false;
				}


				if(res.next()){
					String player = res.getString("player");
					long lasttime = Long.parseLong(res.getString("lasttime"));
					if(checktype){
						if(lasttime < today9){
							Method.SendMessage(sender, cmd, player + "はまだ本日このサーバに投票していないみたいです！");
							Method.SendMessage(sender, cmd, "よろしければ投票をお願いします！ https://jaoafa.com/vote");
						}else{
							Method.SendMessage(sender, cmd, player + "はすでに本日このサーバに投票しています！");
						}
					}else{
						if(lasttime < yesterday9){
							Method.SendMessage(sender, cmd, player + "まだ本日このサーバに投票していないみたいです！");
							Method.SendMessage(sender, cmd, "よろしければ投票をお願いします！ https://jaoafa.com/vote");
						}else{
							Method.SendMessage(sender, cmd, player + "はすでに本日このサーバに投票しています！");
						}
					}
				}else{
					Method.SendMessage(sender, cmd, "指定されたUUIDの投票履歴は見つかりませんでした。");
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					String group = PermissionsManager.getPermissionMainGroup(p);
					if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
						p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。(NoVote/SQLException)通常は再起動で直りますが直らない場合は開発者に連絡を行ってください。");
						p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
					}
				}
				return true;
			}
		}catch(IllegalArgumentException exception){
			try {
				ResultSet res = statement.executeQuery("SELECT * FROM vote WHERE player = '" + player_or_uuid +"'");
				Calendar cal = Calendar.getInstance();
				cal.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
				cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 9, 0, 0);
				long today9 = cal.getTimeInMillis() / 1000L;

				cal.add(Calendar.DAY_OF_MONTH, -1);
				long yesterday9 = cal.getTimeInMillis() / 1000L;

				long now = System.currentTimeMillis() / 1000L;

				boolean checktype; // true: 今日の9時 / false: 昨日の9時
				if(today9 <= now){
					checktype = true;
				}else{
					checktype = false;
				}


				if(res.next()){
					String player = res.getString("player");
					long lasttime = Long.parseLong(res.getString("lasttime"));
					if(checktype){
						if(lasttime < today9){
							Method.SendMessage(sender, cmd, player + "はまだ本日このサーバに投票していないみたいです！");
							Method.SendMessage(sender, cmd, "よろしければ投票をお願いします！ https://jaoafa.com/vote");
						}else{
							Method.SendMessage(sender, cmd, player + "はすでに本日このサーバに投票しています！");
						}
					}else{
						if(lasttime < yesterday9){
							Method.SendMessage(sender, cmd, player + "はまだ本日このサーバに投票していないみたいです！");
							Method.SendMessage(sender, cmd, "よろしければ投票をお願いします！ https://jaoafa.com/vote");
						}else{
							Method.SendMessage(sender, cmd, player + "はすでに本日このサーバに投票しています！");
						}
					}
				}else{
					Method.SendMessage(sender, cmd, "指定されたUUIDの投票履歴は見つかりませんでした。");
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					String group = PermissionsManager.getPermissionMainGroup(p);
					if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
						p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。(NoVote/SQLException)通常は再起動で直りますが直らない場合は開発者に連絡を行ってください。");
						p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
					}
				}
				return true;
			}
		}
		return true;
	}
}
