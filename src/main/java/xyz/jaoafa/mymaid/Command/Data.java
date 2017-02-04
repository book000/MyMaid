package xyz.jaoafa.mymaid.Command;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.MySQL;

public class Data implements CommandExecutor {
	JavaPlugin plugin;
	public Data(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Statement statement = null;
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
				return true;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			e.printStackTrace();
			return true;
		}

		statement = MySQL.check(statement);
		if(args.length == 1){
			try {
				InetAddress ia = InetAddress.getByName(args[0]);
				try {
					ResultSet res = statement.executeQuery("SELECT * FROM log WHERE ip = '" + ia.getHostAddress() + "' ORDER BY id DESC");
					ResultSetMetaData rsmd = res.getMetaData();
				    int count = rsmd.getColumnCount();
					if(count != 0){
						ArrayList<String> players = new ArrayList<String>();
						Method.SendMessage(sender, cmd, "--- IPアドレス「" + ia.getHostAddress() + "」からのデータ ---");
						Method.SendMessage(sender, cmd, "ホスト: " + ia.getHostName());
						Method.SendMessage(sender, cmd, "IPから見つかったログイン者:");
						String playerdata = "";
						while(res.next()){
							String player = res.getString("player");
							if(!players.contains(player)){
								players.add(player);
								playerdata += player + ", ";
							}
						}
						if(playerdata.length() > 0){
							playerdata = playerdata.substring(0, playerdata.length()-2);
						}
						Method.SendMessage(sender, cmd, playerdata);
						return true;
					}else{
						Method.SendMessage(sender, cmd, "データ無し");
						return true;
					}
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
					Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					e.printStackTrace();
					return true;
				}
			} catch (UnknownHostException e) {
				//プレイヤー？UUID？
				if(Method.isUUID(args[0])){
					try {
						ResultSet res = statement.executeQuery("SELECT * FROM log WHERE uuid = '" + args[0] + "' ORDER BY id DESC");
						ResultSetMetaData rsmd = res.getMetaData();
					    int count = rsmd.getColumnCount();
						if(count != 0){
							ArrayList<String> ips = new ArrayList<String>();
							Method.SendMessage(sender, cmd, "--- UUID「" + args[0] + "」からのデータ ---");
							if(!res.next()){
								Method.SendMessage(sender, cmd, "データ無し");
								return true;
							}
							Method.SendMessage(sender, cmd, "プレイヤー: " + res.getString("player"));
							res.last();
							Method.SendMessage(sender, cmd, "初ログイン: " + res.getString("time"));
							res.first();
							Method.SendMessage(sender, cmd, "最終ログイン: " + res.getString("time"));
							Method.SendMessage(sender, cmd, "過去のログイン元IP:");
							while(res.next()){
								String ip = res.getString("ip");
								String time = res.getString("time");
								if(!ips.contains(ip)){
									Method.SendMessage(sender, cmd, "| " + ip + ": " + time);
									ips.add(ip);
								}
							}
							return true;
						}else{
							Method.SendMessage(sender, cmd, "データ無し");
							return true;
						}
					} catch (SQLException ex) {
						// TODO 自動生成された catch ブロック
						Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
						Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
						ex.printStackTrace();
						return true;
					}
				}
				//Player
				try {
					ResultSet res = statement.executeQuery("SELECT * FROM log WHERE player = '" + args[0] + "' ORDER BY id DESC");
					ResultSetMetaData rsmd = res.getMetaData();
				    int count = rsmd.getColumnCount();
					if(count != 0){
						ArrayList<String> ips = new ArrayList<String>();
						if(!res.next()){
							Method.SendMessage(sender, cmd, "データ無し");
							return true;
						}
						Method.SendMessage(sender, cmd, "--- Player「" + args[0] + "」からのデータ ---");
						Method.SendMessage(sender, cmd, "プレイヤー: " + res.getString("uuid"));
						res.last();
						Method.SendMessage(sender, cmd, "初ログイン: " + res.getString("time"));
						res.first();
						Method.SendMessage(sender, cmd, "最終ログイン: " + res.getString("time"));
						Method.SendMessage(sender, cmd, "過去のログイン元IP:");
						while(res.next()){
							String ip = res.getString("ip");
							String time = res.getString("time");
							if(!ips.contains(ip)){
								Method.SendMessage(sender, cmd, "| " + ip + ": " + time);
								ips.add(ip);
							}
						}
						return true;
					}else{
						Method.SendMessage(sender, cmd, "データ無し");
						return true;
					}
				} catch (SQLException ex) {
					// TODO 自動生成された catch ブロック
					Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
					Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					ex.printStackTrace();
					return true;
				}
			}
		}
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		// time をなんか更新
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int min = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		int msec = cal.get(Calendar.MILLISECOND);

		Method.SendMessage(sender, cmd, "uptime: " + year + "/" + month + "/" + day + " " + hour + ":" + min + ":" + sec + "." + msec);
		Method.SendMessage(sender, cmd, "Runtimemax : " + (Runtime.getRuntime().maxMemory() / 1024 / 1024));
		Method.SendMessage(sender, cmd, "Runtimetotal: " + (Runtime.getRuntime().totalMemory() / 1024 / 1024));
		Method.SendMessage(sender, cmd, "Runtimefree: " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		List<World> worlds = Bukkit.getServer().getWorlds();
		for (World w : worlds)
		{
			String Name = w.getName();
			String worldType = null;
			switch (w.getEnvironment())
			{
			case NORMAL:
				worldType = "Normal";
				break;
			case NETHER:
				worldType = "Nether";
				break;
			case THE_END:
				worldType = "The End";
				break;
			default:
				break;
			}

			int tileEntities = 0;

			try
			{
				for (Chunk chunk : w.getLoadedChunks())
				{
					tileEntities += chunk.getTileEntities().length;
				}
			}
			catch (java.lang.ClassCastException ex)
			{
				Bukkit.getLogger().log(Level.SEVERE, "Corrupted chunk data on world " + w, ex);
				Method.SendMessage(sender, cmd, "Corrupted chunk data on world " + w + "(" + ex.getMessage() + ")");
			}
			Method.SendMessage(sender, cmd, "World: " + Name + "(" + worldType + ") LoadedChunk: " + w.getLoadedChunks().length + " Size: " + w.getEntities().size() + " tileEntity: " + tileEntities);
		}


		return true;
	}
}
