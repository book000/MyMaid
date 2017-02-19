package com.jaoafa.mymaid.Command;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.jaoafa.mymaid.Method;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Pin implements CommandExecutor {
	JavaPlugin plugin;
	public Pin(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		String code = "";
		String time = "";
		String status = "";
		String pin = "";

		if (args.length == 0){
			Method.SendMessage(sender, cmd, "エラーが発生しました。PINコードを入力してください。");
			Bukkit.getLogger().info("ERROR! PINコードが入力されませんでした。");
			return true;
		}else{
			pin = args[0]; //入力されたコードを代入
			player = (Player) sender; //コマンド実行者を代入

			//4桁の半角数字かどうかを確認
			String regex = "^[0-9][0-9][0-9][0-9]$"; //正規表現
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(pin);
			if (!m.find()){
				sender.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラーが発生しました。4桁の半角数字を入力してください。");
				Bukkit.getLogger().info("ERROR! 4桁の半角数字が入力されませんでした。");
				return true;
			}
			code = Method.url_access("http://nubesco.jaoafa.com/plugin/pin/code.txt");
			time = Method.url_access("http://nubesco.jaoafa.com/plugin/pin/close_time.txt");
			status = Method.url_access("http://nubesco.jaoafa.com/plugin/pin/status.txt");
		}

		long utc = System.currentTimeMillis();
		String utcs=String.valueOf(utc);
		utcs = utcs.substring(0, utcs.length()-3);
		utc = Long.parseLong(utcs);
		long times = Long.parseLong(time);
		if(utc > times){
			sender.sendMessage("[MyMaid] " + ChatColor.GREEN + "PINコードの有効期限が切れています。再度取得してください。(ErrorCode:1)");
			Bukkit.getLogger().info("ERROR! 有効期間が切れ、無効のPINコードが入力されました。Now:\"" + utc + "\" PINClose:\"" + times + "\"");
			return true;
		}
		if(status.trim().equals("used")){
			sender.sendMessage("[MyMaid] " + ChatColor.GREEN + "PINコードが既に使用されています。再度取得してください。(ErrorCode:2)");
			Bukkit.getLogger().info("ERROR! 既に使用されているコードが入力されました。 PINStatus:\"" + status + "\"");
			return true;
		}
		if(!code.trim().equals(pin.trim())){
			sender.sendMessage("[MyMaid] " + ChatColor.GREEN + "PINコードが誤っています。再度確認し正しいコードを入力してください。(ErrorCode:3)");
			Bukkit.getLogger().info("ERROR! コードが誤っています。Input PIN:\"" + pin.trim() + "\" NetWork PIN:\"" + code.trim() + "\"");
			return true;
		}else{
			Bukkit.broadcastMessage("[MyMaid] " + ChatColor.GREEN + player.getName() + "さんが認証を通過しました。");
			Collection<String> groups = PermissionsEx.getPermissionManager().getGroupNames();
			for(String group : groups){
				if(PermissionsEx.getUser(player).inGroup(group)){
					PermissionsEx.getUser(player).removeGroup(group);
				}
			}
			PermissionsEx.getUser(player).addGroup("Default");
			sender.sendMessage("[MyMaid] " + ChatColor.GREEN + "登録しました。当鯖にお越しいただきありがとうございます。");
			sender.sendMessage("[MyMaid] " + ChatColor.GREEN + "是非当鯖の宣伝をよろしくおねがいします！");
			sender.sendMessage("[MyMaid] " + ChatColor.GREEN + "minecraft.jpで投票する: https://minecraft.jp/servers/jaoafa.com");
			Bukkit.getLogger().info("\"" + player.getName() + "\"さんが登録されました。Input PIN:\"" + pin.trim() + "\" NetWork PIN:\"" + code.trim() + "\" PINStatus:\"" + status + "\" Now:\"" + utc + "\" PINClose:\"" + times + "\"");
			Method.url_jaoplugin("pin/close", "code=" + code.trim() + "&p=" + player.getName());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mt tw " + player.getName() + "さんが認証を通過しました！ #jao_pin_code");
		}
		return true;
	}
}
