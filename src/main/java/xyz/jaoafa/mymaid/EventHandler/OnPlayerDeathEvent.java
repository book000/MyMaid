package xyz.jaoafa.mymaid.EventHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import xyz.jaoafa.mymaid.Command.Ded;

public class OnPlayerDeathEvent implements Listener {
	JavaPlugin plugin;
	public OnPlayerDeathEvent(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent e){
		Player player = e.getEntity();
		Location loc = player.getLocation();
		Ded.ded.put(player.getName(), loc);
		player.sendMessage("[DED] " + ChatColor.GREEN + "死亡した場所に戻るには「/ded」コマンドが使用できます。");

		JSONParser parser = new JSONParser();
		String json = "";
		try{
			File file = new File(plugin.getDataFolder() + File.separator + "DEDMSG.json");
			BufferedReader br = new BufferedReader(new FileReader(file));

			String brseparator = System.getProperty("line.separator");

			String str;
			while((str = br.readLine()) != null){
				json += str + brseparator;
			}
			br.close();
		}catch(FileNotFoundException e1){
			System.out.println(e1);
		}catch(IOException e1){
			System.out.println(e1);
		}
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(json);
		} catch (ParseException e1) {
			obj = new JSONObject();
		}
		String message = "";
		Boolean boo = false;
		for(Entry<String, ?> one : (Set<Map.Entry<String,?>>) obj.entrySet()){

			for(Entry<String, ?> data : (Set<Map.Entry<String, ?>>) ((JSONObject) one.getValue()).entrySet()){
				if(data.getKey().equals("message")){
					message = (String)data.getValue();
					if(boo){
						break;
					}else{
						continue;
					}
				}
				JSONObject arr;
				try {
					arr = (JSONObject) parser.parse(data.getValue().toString());
				} catch (ParseException e1) {
					continue;
				}
				int x = new Integer(arr.get("x").toString());
				int y = new Integer(arr.get("y").toString());
				int z = new Integer(arr.get("z").toString());
				if(loc.getWorld().getName().equals(((String)arr.get("world")))){
					try{
						if(loc.getBlockX() == x){
							if(loc.getBlockY() == y){
								if(loc.getBlockZ() == z){
									//Bukkit.broadcastMessage("ok?");
									boo = true;
								}
							}
						}
					}catch(ClassCastException cce){
						continue;
					}
				}
			}
		}
		if(boo){
			e.setDeathMessage(message.replaceAll("%player%", player.getName()));
		}
	}
}
