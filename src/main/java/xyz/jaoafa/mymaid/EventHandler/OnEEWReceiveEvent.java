package xyz.jaoafa.mymaid.EventHandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.ittekikun.plugin.eewalert.EEWReceiveEvent;

import xyz.jaoafa.mymaid.Method;

public class OnEEWReceiveEvent implements Listener {
	JavaPlugin plugin;
	public OnEEWReceiveEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
    @EventHandler
	public void onEEWReceiveEvent(EEWReceiveEvent e){
    	//取得するもん取ってwebサーバーと連携
    	// RTかどうか
    	if(e.eew.isRetweet){
    		//Bukkit.getLogger().info("[MyEEW] 地震情報を受信しましたが無視されました。(RT)");
    		return;
    	}
    	String[] data = e.getRawArray();

    	String separator = ",";

    	StringBuilder sb = new StringBuilder();
    	for (String str : data) {
    	    if (sb.length() > 0) {
    	        sb.append(separator);
    	    }
    	    sb.append(str);
    	}

    	String result = Method.url_jaoplugin("eew", "u=" + sb.toString().replaceAll(" ", "_"));
    	//result
    	if(result.equalsIgnoreCase("null")){
    		//Bukkit.getLogger().info("[MyEEW] 地震情報を受信しましたが無視されました。(NetworkReturn)");
    		return;
    	}else if(result.equalsIgnoreCase("print")){
    		Bukkit.broadcastMessage("[" + ChatColor.GOLD + "MyEEW" + ChatColor.RESET + "] " + ChatColor.RED + "------------ 地震速報 ------------");
    		Bukkit.broadcastMessage("[" + ChatColor.GOLD + "MyEEW" + ChatColor.RESET + "] " + ChatColor.RED + "地震ID: " + data[5]);
    		Bukkit.broadcastMessage("[" + ChatColor.GOLD + "MyEEW" + ChatColor.RESET + "] " + ChatColor.RED + "発生時刻/発表時刻: " + data[6] + "/" + data[2]);
    		Bukkit.broadcastMessage("[" + ChatColor.GOLD + "MyEEW" + ChatColor.RESET + "] " + ChatColor.RED + "震源地(海陸判定): " + data[9] + " (" + data[13] + ")");
    		Bukkit.broadcastMessage("[" + ChatColor.GOLD + "MyEEW" + ChatColor.RESET + "] " + ChatColor.RED + "最大震度(マグニチュード): " + data[12] + " (" + data[11] + "M)");
    		Bukkit.broadcastMessage("[" + ChatColor.GOLD + "MyEEW" + ChatColor.RESET + "] " + ChatColor.RED + "震源の深さ: " + data[10] + "km");
    		Bukkit.broadcastMessage("[" + ChatColor.GOLD + "MyEEW" + ChatColor.RESET + "] " + ChatColor.RED + "警報有無: " + data[14]);
    		Bukkit.broadcastMessage("[" + ChatColor.GOLD + "MyEEW" + ChatColor.RESET + "] " + ChatColor.RED + "震源に近い住民の方は安全に確認して必要であれば避難して下さい。");
    		Bukkit.broadcastMessage("[" + ChatColor.GOLD + "MyEEW" + ChatColor.RESET + "] " + ChatColor.RED + "----------------------------------");
    		Bukkit.getLogger().info("[MyEEW] 地震情報を受信し、全プレイヤーに送信しました。。");
    	}
    }
}
