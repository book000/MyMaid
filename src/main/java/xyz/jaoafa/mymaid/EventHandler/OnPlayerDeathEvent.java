package xyz.jaoafa.mymaid.EventHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
import xyz.jaoafa.mymaid.BugReport;
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
		if(!player.getWorld().getName().equalsIgnoreCase("Summer2017")){
			Ded.ded.put(player.getName(), loc);
			player.sendMessage("[DED] " + ChatColor.GREEN + "死亡した場所に戻るには「/ded」コマンドが使用できます。");
		}

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
			BugReport.report(e1);
		}catch(IOException e1){
			BugReport.report(e1);
		}
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(json);
		} catch (ParseException e1) {
			obj = new JSONObject();
		}
		String message = "";
		Boolean boo = false;
		for(Entry<String, JSONObject> one : (Set<Map.Entry<String,JSONObject>>) obj.entrySet()){
			int x1 = Integer.parseInt(String.valueOf(one.getValue().get("x1")));
			int x2 = Integer.parseInt(String.valueOf(one.getValue().get("x2")));
			int y1 = Integer.parseInt(String.valueOf(one.getValue().get("y1")));
			int y2 = Integer.parseInt(String.valueOf(one.getValue().get("y2")));
			int z1 = Integer.parseInt(String.valueOf(one.getValue().get("z1")));
			int z2 = Integer.parseInt(String.valueOf(one.getValue().get("z2")));
			if(x1 <= loc.getBlockX() && x2 >= loc.getBlockX()){
				if(y1 <= loc.getBlockY() && y2 >= loc.getBlockY()){
					if(z1 <= loc.getBlockZ() && z2 >= loc.getBlockZ()){
						boo = true;
						message = (String) one.getValue().get("message");
					}
				}
			}


		}
		if(boo){
			e.setDeathMessage(message.replaceAll("%player%", player.getName()));
		}

		// -----

		parser = new JSONParser();
		json = "";
		try{
			File file = new File(plugin.getDataFolder() + File.separator + "AUTOHEAL.json");
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
		try {
			obj = (JSONObject) parser.parse(json);
		} catch (ParseException e1) {
			obj = new JSONObject();
		}
		message = "";
		boo = false;
		for(Entry<String, JSONObject> one : (Set<Map.Entry<String,JSONObject>>) obj.entrySet()){
			int x1 = Integer.parseInt(String.valueOf(one.getValue().get("x1")));
			int x2 = Integer.parseInt(String.valueOf(one.getValue().get("x2")));
			int y1 = Integer.parseInt(String.valueOf(one.getValue().get("y1")));
			int y2 = Integer.parseInt(String.valueOf(one.getValue().get("y2")));
			int z1 = Integer.parseInt(String.valueOf(one.getValue().get("z1")));
			int z2 = Integer.parseInt(String.valueOf(one.getValue().get("z2")));
			if(x1 <= loc.getBlockX() && x2 >= loc.getBlockX()){
				if(y1 <= loc.getBlockY() && y2 >= loc.getBlockY()){
					if(z1 <= loc.getBlockZ() && z2 >= loc.getBlockZ()){
						boo = true;
					}
				}
			}


		}
		if(boo){
			player.sendMessage("[AUTORESPAWN] " + ChatColor.GREEN + "死んだ場所がオートリスポーン地点だったため自動でリスポーンしました。");
			Respawn(player, 1);
		}
	}
	public void Respawn(final Player player,int Time){
		Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {

			@Override
			public void run() {
				((CraftPlayer)player).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
			}
		},Time);
	}
}
