package xyz.jaoafa.mymaid.Command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class VarCmd implements CommandExecutor {
	JavaPlugin plugin;
	public VarCmd(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			Method.SendMessage(sender, cmd, "引数が適切ではありません。");
			return true;
		}
		String text = "";
		int c = 0;
		while(args.length > c){
			text += args[c];
			if(args.length != (c+1)){
				text += " ";
			}
			c++;
		}
		text = StringUtils.stripStart(text, "/");

		// ----- 事前定義(予約済み変数) ----- //

		SimpleDateFormat sdf_Year = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		text = text.replaceAll("\\$" + "DateTime_Year" + "\\$", sdf_Year.format(new Date()));
		SimpleDateFormat sdf_Month = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		text = text.replaceAll("\\$" + "DateTime_Month" + "\\$", sdf_Month.format(new Date()));
		SimpleDateFormat sdf_Day = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		text = text.replaceAll("\\$" + "DateTime_Day" + "\\$", sdf_Day.format(new Date()));

		SimpleDateFormat sdf_Hour = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		text = text.replaceAll("\\$" + "DateTime_Hour" + "\\$", sdf_Hour.format(new Date()));
		SimpleDateFormat sdf_Minute = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		text = text.replaceAll("\\$" + "DateTime_Minute" + "\\$", sdf_Minute.format(new Date()));
		SimpleDateFormat sdf_Second = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		text = text.replaceAll("\\$" + "DateTime_Second" + "\\$", sdf_Second.format(new Date()));

		text = text.replaceAll("\\$" + "PlayerCount" + "\\$", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));

		// ----- 事前定義(予約済み変数) ----- //

		for(Map.Entry<String, String> e : Var.var.entrySet()) {
			text = text.replaceAll("\\$" + e.getKey() + "\\$", e.getValue());
		}

		Bukkit.dispatchCommand(sender, text);
		Method.SendMessage(sender, cmd, "コマンド「" + text + "」を実行しました。");
		return true;
	}
}
