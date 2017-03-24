package xyz.jaoafa.mymaid.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.MySQL;

public class Home implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public Home(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof org.bukkit.entity.Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;

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
				Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				Method.SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
				return true;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return true;
		}

		statement = MySQL.check(statement);

		if(args.length == 0){
			try {
				ResultSet res = statement.executeQuery("SELECT * FROM home WHERE uuid = '" + player.getUniqueId().toString() + "' AND name = 'default'");
				if(res.next()){
					Location loc = new Location(Bukkit.getWorld(res.getString("world")), res.getDouble("x"), res.getDouble("y"), res.getDouble("z"), res.getFloat("yaw"), res.getFloat("pitch"));
					player.teleport(loc);
					Method.SendMessage(sender, cmd, "ホーム「default」にテレポートしました。");
					return true;
				}else{
					Method.SendMessage(sender, cmd, "ホーム「default」は見つかりませんでした。");
					return true;
				}

			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				Method.SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
				return true;
			}
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("list")){
				try {
					ResultSet res = statement.executeQuery("SELECT * FROM home WHERE uuid = '" + player.getUniqueId().toString() + "'");
					Method.SendMessage(sender, cmd, "----- " + player.getName() + "さんのホームリスト -----");
					int i = 0;
					while(res.next()){
						String name = res.getString("name");
						String world = res.getString("world");
						double x = res.getDouble("x");
						double y = res.getDouble("y");
						double z = res.getDouble("z");
						float yaw = res.getFloat("yaw");
						float pitch = res.getFloat("pitch");
						Method.SendMessage(sender, cmd, name + ": " + world + " " + x + " " + y + " " + z + "(" + yaw + " " + pitch + ")");
						i++;
					}
					if(i == 0){
						Method.SendMessage(sender, cmd, "見つかりませんでした。");
						return true;
					}
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
					Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					Method.SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
					return true;
				}
				return true;
			}

			String name = args[0];
			try {
				ResultSet res = statement.executeQuery("SELECT * FROM home WHERE uuid = '" + player.getUniqueId().toString() + "' AND name = '" + name + "'");
				if(res.next()){
					Location loc = new Location(Bukkit.getWorld(res.getString("world")), res.getDouble("x"), res.getDouble("y"), res.getDouble("z"), res.getFloat("yaw"), res.getFloat("pitch"));
					player.teleport(loc);
					Method.SendMessage(sender, cmd, "ホーム「" + name + "」にテレポートしました。");
					return true;
				}else{
					Method.SendMessage(sender, cmd, "ホーム「" + name + "」は見つかりませんでした。");
					return true;
				}

			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				Method.SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
				return true;
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("remove")){
				String name = args[1];
				try {
					ResultSet res = statement.executeQuery("SELECT * FROM home WHERE uuid = '" + player.getUniqueId().toString() + "' AND name = '" + name + "'");
					if(res.next()){
						int id = res.getInt("id");
						statement.executeUpdate("DELETE FROM home WHERE id = " + id);
						Method.SendMessage(sender, cmd, "ホーム「" + name + "」の削除に成功しました。");
						return true;
					}else{
						Method.SendMessage(sender, cmd, "ホーム「" + name + "」の削除に失敗しました。");
						return true;
					}
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
					Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					Method.SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
					return true;
				}
			}
		}
		Method.SendMessage(sender, cmd, "----- home help -----");
		Method.SendMessage(sender, cmd, "/home: デフォルトのホームにテレポートします。");
		Method.SendMessage(sender, cmd, "/home <Name>: Nameのホームにテレポートします");
		Method.SendMessage(sender, cmd, "/home remove <Name>: Nameを削除します。");
		return true;
	}
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (!(sender instanceof org.bukkit.entity.Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return plugin.onTabComplete(sender, cmd, alias, args);
		}
		org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;

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
				Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				Method.SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
				return plugin.onTabComplete(sender, cmd, alias, args);
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return plugin.onTabComplete(sender, cmd, alias, args);
		}

		statement = MySQL.check(statement);


		if (args.length == 1) {
			if(args[0].length() == 0){
				try {
					ResultSet res = statement.executeQuery("SELECT * FROM home WHERE uuid = '" + player.getUniqueId().toString() + "'");
					List<String> returndata = new ArrayList<String>();
					while(res.next()){
						returndata.add(res.getString("name"));
					}
					return returndata;
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
					Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					Method.SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
					return plugin.onTabComplete(sender, cmd, alias, args);
				}
			}else{
				try {
					String name = args[0];
					ResultSet res = statement.executeQuery("SELECT * FROM home WHERE uuid = '" + player.getUniqueId().toString() + "' AND name LIKE '" + name + "%'");
					List<String> returndata = new ArrayList<String>();
					while(res.next()){
						returndata.add(res.getString("name"));
					}
					return returndata;
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
					Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					Method.SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
					return plugin.onTabComplete(sender, cmd, alias, args);
				}
			}
		}
		return plugin.onTabComplete(sender, cmd, alias, args);
	}
}
