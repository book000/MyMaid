package xyz.jaoafa.mymaid;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Pointjao {
	JavaPlugin plugin;
	public Pointjao(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String, Integer> jao = new HashMap<String, Integer>();

	/*
	 	int use = 0;
		if(!Pointjao.hasjao(player, use)){
		 	 Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
		 	 return true;
		}
		Pointjao.usejao(player, use);
	 */

	private static String getuuidtostring(Player player){
		return ""+player.getUniqueId();
  	}

	public static int getjao(Player player){
		if(!jao.containsKey(getuuidtostring(player))){
			jao.put(getuuidtostring(player), 0);
		}
		int now = jao.get(getuuidtostring(player));
		return now;
  	}

	public static int getjao(String uuid){
		if(!jao.containsKey(uuid)){
			jao.put(uuid, 0);
		}
		int now = jao.get(uuid);
		return now;
  	}

	public static boolean hasjao(Player player, int hasjao){
		int now = getjao(player);
		if(now >= hasjao){
			return true;
		}else{
			return false;
		}
	}

	public static boolean usejao(Player player, int usejao){
		int now = getjao(player);
		if(!hasjao(player, usejao)){
			return false;
		}
		int newjao = now - usejao;
		jao.put(getuuidtostring(player), newjao);
		MyMaid.conf.set("jao",Pointjao.jao);
		player.sendMessage("[POINT] " + ChatColor.GREEN + usejao + "ポイントを使用しました。現在" + newjao + "ポイント持っています。");
		return true;
	}

	public static boolean addjao(Player player, int addjao){
		int now = getjao(player);
		int newjao = now + addjao;
		jao.put(getuuidtostring(player), newjao);
		MyMaid.conf.set("jao",Pointjao.jao);
		player.sendMessage("[POINT] " + ChatColor.GREEN + addjao + "ポイントを追加しました。現在" + newjao + "ポイント持っています。");
		return true;
	}

	public static boolean addjao(String uuid, int addjao){
		int now = getjao(uuid);
		int newjao = now + addjao;
		jao.put(uuid, newjao);
		MyMaid.conf.set("jao",Pointjao.jao);
		return true;
	}
}
