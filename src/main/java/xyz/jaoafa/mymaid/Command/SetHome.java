package xyz.jaoafa.mymaid.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.MySQL;

public class SetHome implements CommandExecutor {
	JavaPlugin plugin;
	public SetHome(JavaPlugin plugin) {
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
			Location loc = player.getLocation();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String create_at = sdf.format(new Date());
			try {
				ResultSet res = statement.executeQuery("SELECT * FROM home WHERE uuid = '" + player.getUniqueId().toString() + "' AND name = 'default'");
				if(res.next()){
					Method.SendMessage(sender, cmd, "「default」というホームは存在しています。「/home remove default」と打つことでホームを削除できます。");
					return true;
				}
				statement.executeUpdate("INSERT INTO home (player, uuid, name, world, x, y, z, yaw, pitch, create_at) VALUES ('" + player.getName() + "', '" + player.getUniqueId().toString() + "', 'default', '" + loc.getWorld().getName() + "', " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", " + loc.getYaw() + ", " + loc.getPitch() + ", '" + create_at + "');");
				Method.SendMessage(sender, cmd, "「default」としてホームを設定しました。「/home」と打つことでテレポートできます。");
				return true;
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				Method.SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
				return true;
			}
		}else if(args.length == 1){
			String name = args[0];
			Location loc = player.getLocation();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String create_at = sdf.format(new Date());
			try {
				ResultSet res = statement.executeQuery("SELECT * FROM home WHERE uuid = '" + player.getUniqueId().toString() + "' AND name = '" + name + "'");
				if(res.next()){
					Method.SendMessage(sender, cmd, "「" + name + "」というホームは存在しています。「/home remove " + name + "」と打つことでホームを削除できます。");
					return true;
				}
				statement.executeUpdate("INSERT INTO home (player, uuid, name, world, x, y, z, yaw, pitch, create_at) VALUES ('" + player.getName() + "', '" + player.getUniqueId().toString() + "', '" + name + "', '" + loc.getWorld().getName() + "', " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ", " + loc.getYaw() + ", " + loc.getPitch() + ", '" + create_at + "');");
				Method.SendMessage(sender, cmd, "「" + name + "」としてホームを設定しました。「/home " + name + "」と打つことでテレポートできます。");
				return true;
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				Method.SendMessage(sender, cmd, "再度実行しなおすと動作するかもしれません。");
				return true;
			}
		}
		Method.SendMessage(sender, cmd, "----- SetHome Help -----");
		Method.SendMessage(sender, cmd, "/sethome: いまいる位置をdefaultとしてホームを設定します。");
		Method.SendMessage(sender, cmd, "defaultについては「/home」だけでテレポートできます。");
		Method.SendMessage(sender, cmd, "/sethome <Name>: いまいる位置をNameとしてホームを設定します。");
		return true;
	}
}
