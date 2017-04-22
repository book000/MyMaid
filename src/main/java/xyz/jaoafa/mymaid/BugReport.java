package xyz.jaoafa.mymaid;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Discord.Discord;

public class BugReport {
	static JavaPlugin plugin;
	static String folder;
	public static void start(){
		plugin = MyMaid.getJavaPlugin();
		Bukkit.getLogger().info("BugReportを起動しました。");
	}

	public static void first(){
		String Path = plugin.getDataFolder() + File.separator + "bugreport" + File.separator;
		File folder = new File(Path);
		if(folder.exists()){
			return;
		}
		if(folder.mkdir()){
			Bukkit.getLogger().info("BugReportのリポートディレクトリの作成に成功しました。");
			BugReport.folder = Path;
		}else{
			Bukkit.getLogger().info("BugReportのリポートディレクトリの作成に失敗しました。");
		}
	}

	public static String reportformat(String id){
		String text = "システムエラーが発生しました。\n"
				+ "報告IDは「" + id + "」です。\n"
				+ "時間をおいて実行しても同様のエラーが発生する場合は管理部に報告IDを知らせてください。";
		return text;
	}

	public static String report(Exception exception){
		String id = CreateReportID();
		File file = getReportFile(id);
		FileWrite(exception, file);
		AdminSend(exception);
		DiscordSend(exception, id);
		exception.printStackTrace();
		return id;
	}

	private static String CreateReportID(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
		String date = sdf.format(new Date());
		return date;
	}

	private static File getReportFile(String id){
		String filepath = folder + id + ".json";
		File file = new File(filepath);
		return file;
	}

	private static void FileWrite(Exception exception, File file){
		try {
			FileWriter fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);
			exception.printStackTrace(pw);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private static void AdminSend(Exception exception){
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			if(PermissionsEx.getUser(p).inGroup("Admin") || PermissionsEx.getUser(p).inGroup("Moderator")) {
				p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。");
				p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + exception.getMessage());
			}
		}
	}

	private static void DiscordSend(Exception exception, String id){
		StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter( sw );
        exception.printStackTrace(pw);
		Discord.send("293856671799967744", "MyMaidでエラーが発生しました。" + "\n"
					+ sw.toString() + "\n"
					+ "報告ID: " + id);
	}
}
