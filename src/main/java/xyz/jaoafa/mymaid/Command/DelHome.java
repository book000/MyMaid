package xyz.jaoafa.mymaid.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.MySQL;

public class DelHome implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public DelHome(JavaPlugin plugin) {
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
				Method.SendMessage(sender, cmd, BugReport.report(e1));
				Method.SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
				return true;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return true;
		}

		statement = MySQL.check(statement);

		if(args.length == 1){
			String name = args[0];
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
		Method.SendMessage(sender, cmd, "----- delhome help -----");
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
