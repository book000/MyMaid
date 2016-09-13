package xyz.jaoafa.mymaid.EventHandler;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MoveLocationName implements Listener {
	JavaPlugin plugin;
	public MoveLocationName(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String, String> moveloc = new HashMap<String, String>();
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e){

		Player player = e.getPlayer();
		Location loc = player.getLocation();
		//new messageset(plugin, player, loc).runTaskAsynchronously(plugin);
	}
	/*
	private class messageset extends BukkitRunnable{
		JavaPlugin plugin;
		Player player;
		Location loc;
    	public messageset(JavaPlugin plugin, Player player, Location loc) {
    		this.plugin = plugin;
    		this.player = player;
    		this.loc = loc;
    	}
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			JSONParser parser = new JSONParser();
			String json = "";
			try{
				File file = new File(plugin.getDataFolder() + File.separator + "where.json");
				BufferedReader br = new BufferedReader(new FileReader(file));

				String brseparator = System.getProperty("line.separator");

				String str;
				while((str = br.readLine()) != null){
					json += str + brseparator;
				}
				br.close();
			}catch(FileNotFoundException e1){
				System.out.println(e1);
				return;
			}catch(IOException e1){
				System.out.println(e1);
				return;
			}
			JSONObject obj = null;
			try {
				obj = (JSONObject) parser.parse(json);
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
				return;
			}
			String location = "";
			String user = "";
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
							location = (String) one.getValue().get("location");
							user = (String) one.getValue().get("user");
						}
					}
				}
			}
			if(boo){
				if(moveloc.containsKey(player.getName())){
					if(moveloc.get(player.getName()).equalsIgnoreCase(location)){
						return;
					}
				}
				moveloc.put(player.getName(), location);

				Scoreboard sb = player.getScoreboard();
				Objective object = sb.getObjective("jao" + player.getName());
				if ( object == null ) {
				    object = sb.registerNewObjective("jao" + player.getName(), "dummy");
				}else{
					try{
						object.unregister();
						object = sb.registerNewObjective("jao" + player.getName(), "dummy");
					}catch(java.lang.IllegalArgumentException e){
						return;
					}
				}
				object.setDisplaySlot(DisplaySlot.SIDEBAR);
				object.setDisplayName("jao Minecraft Server");

				object.getScore("Location:").setScore(4);
				object.getScore("    " + location).setScore(3);
				object.getScore("Author:").setScore(1);
				object.getScore("    " + user).setScore(0);
			}else{
				if(moveloc.containsKey(player.getName())){
					if(moveloc.get(player.getName()).equalsIgnoreCase("なし")){
						return;
					}
				}
				moveloc.put(player.getName(), "なし");

				Scoreboard sb = player.getScoreboard();
				Objective object = sb.getObjective("jao" + player.getName());
				if ( object == null ) {
				    object = sb.registerNewObjective("jao" + player.getName(), "dummy");
				}else{
					object.unregister();
					try{
						object = sb.registerNewObjective("jao" + player.getName(), "dummy");
					}catch(java.lang.IllegalArgumentException e){
						return;
					}
				}

				object.setDisplaySlot(DisplaySlot.SIDEBAR);
				object.setDisplayName("jao Minecraft Server");

				object.getScore("Location:").setScore(4);
				object.getScore("    土地情報なし").setScore(3);
				object.getScore("Author:").setScore(1);
				object.getScore("    プレイヤー情報なし").setScore(0);
			}
		}
	}
	*/
}
