package xyz.jaoafa.mymaid.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.CuboidRegionSelector;
import com.sk89q.worldedit.regions.RegionSelector;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Method;

@SuppressWarnings("deprecation")
public class Where implements CommandExecutor {
	JavaPlugin plugin;
	public Where(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("unchecked")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		Location loc = player.getLocation();
		if(args.length == 0){
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
			}catch(IOException e1){
				System.out.println(e1);
			}
			JSONObject obj;
			try {
				obj = (JSONObject) parser.parse(json);
			} catch (ParseException e1) {
				obj = new JSONObject();
			}
			String location = "";
			String user = "";
			String discription = "";
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
							if(one.getValue().containsKey("discription")){
								discription = (String) one.getValue().get("discription");
							}
						}
					}
				}
			}
			if(boo){
				Method.SendMessage(sender, cmd, "この土地は「" + location + "」という名で「" + user + "」の所有地です。" + discription);
			}else{
				Method.SendMessage(sender, cmd, "この土地は登録されていません。");
			}
			return true;
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("add")){
				LocalSession session;
				RegionSelector regionSelector;
				try {
					session = WorldEdit.getInstance().getSession(player.getName());
					regionSelector = session.getRegionSelector(session.getSelectionWorld());
				} catch(java.lang.NullPointerException ex){
					Method.SendMessage(sender, cmd, "範囲を指定してください。");
					return true;
				}
				String location = args[1];

				// セレクタが立方体セレクタか判定
				if (!(session.getRegionSelector() instanceof CuboidRegionSelector)){
					Method.SendMessage(sender, cmd, "WorldEditの選択範囲を立方体にしてください。");
					return true;
				}else{
					try{
						new messageset(plugin, sender, cmd, location, regionSelector).runTask(plugin);
					}catch(java.lang.NoClassDefFoundError e){
						for(Player p: Bukkit.getServer().getOnlinePlayers()) {
							if(PermissionsEx.getUser(p).inGroup("Admin")) {
								p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。通常は再起動で直りますが直らない場合は開発者に連絡を行ってください。");
								p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
							}
						}
					}

					return true;
				}
			}else if(args[0].equalsIgnoreCase("delete")){
				try{
					new messagedel(plugin, sender, cmd, args[1]).runTask(plugin);
				}catch(java.lang.NoClassDefFoundError e){
					for(Player p: Bukkit.getServer().getOnlinePlayers()) {
						if(PermissionsEx.getUser(p).inGroup("Admin")) {
							p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。通常は再起動で直りますが直らない場合は開発者に連絡を行ってください。");
							p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
						}
					}
				}

				return true;
			}
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("add")){
				LocalSession session;
				RegionSelector regionSelector;
				try {
					session = WorldEdit.getInstance().getSession(player.getName());
					regionSelector = session.getRegionSelector(session.getSelectionWorld());
				} catch(java.lang.NullPointerException ex){
					Method.SendMessage(sender, cmd, "範囲を指定してください。");
					return true;
				}
				String location = args[1];
				String discription = args[2];
				// セレクタが立方体セレクタか判定
				if (!(session.getRegionSelector() instanceof CuboidRegionSelector)){
					Method.SendMessage(sender, cmd, "WorldEditの選択範囲を立方体にしてください。");
					return true;
				}else{
					try{
						new messagesetdisc(plugin, sender, cmd, location, regionSelector, discription).runTask(plugin);
					}catch(java.lang.NoClassDefFoundError e){
						for(Player p: Bukkit.getServer().getOnlinePlayers()) {
							if(PermissionsEx.getUser(p).inGroup("Admin")) {
								p.sendMessage("[MyMaid] " + ChatColor.GREEN + "MyMaidのシステム障害が発生しました。通常は再起動で直りますが直らない場合は開発者に連絡を行ってください。");
								p.sendMessage("[MyMaid] " + ChatColor.GREEN + "エラー: " + e.getMessage());
							}
						}
					}

					return true;
				}
			}
		}
		Method.SendMessage(sender, cmd, "第1引数にはadd, flag, deleteが使用できます。");
		return true;
	}

	private class messageset extends BukkitRunnable{
		JavaPlugin plugin;
		CommandSender sender;
		Command cmd;
		String location;
		RegionSelector regionSelector;
    	public messageset(JavaPlugin plugin, CommandSender sender, Command cmd, String location, RegionSelector regionSelector) {
    		this.plugin = plugin;
    		this.sender = sender;
    		this.cmd = cmd;
    		this.location = location;
    		this.regionSelector = regionSelector;
    	}
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			Player player = (Player) sender;
			Location loc = player.getLocation();
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
			}catch(IOException e1){
				System.out.println(e1);
			}
			JSONObject obj;
			try {
				obj = (JSONObject) parser.parse(json);
			} catch (ParseException e1) {
				obj = new JSONObject();
			}
			Boolean boo = false;
			Boolean boo_name = false;
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
							if(location.equals((String) one.getValue().get("location"))){
								boo_name = true;
							}
						}
					}
				}
			}
			if(boo){
				Method.SendMessage(sender, cmd, "この土地は他のユーザーに既に登録されています。");
				return;
			}
			if(boo_name){
				Method.SendMessage(sender, cmd, "この土地名は他のユーザーに既に登録されています。");
				return;
			}

			int x1;
			int y1;
			int z1;
			int x2;
			int y2;
			int z2;
			World world;
			try {
				String worldname = regionSelector.getRegion().getWorld().getName();
				x1 = regionSelector.getRegion().getMinimumPoint().getBlockX();
				y1 = 0;
				z1 = regionSelector.getRegion().getMinimumPoint().getBlockZ();
				x2 = regionSelector.getRegion().getMaximumPoint().getBlockX();
				y2 = 255;
				z2 = regionSelector.getRegion().getMaximumPoint().getBlockZ();
				world = Bukkit.getWorld(worldname);
			} catch (IncompleteRegionException ex) {
				Method.SendMessage(sender, cmd, "範囲を2つ指定してください。");
				return;
			} catch(java.lang.NullPointerException ex){
				Method.SendMessage(sender, cmd, "範囲を指定してください。");
				return;
			}

			parser = new JSONParser();
			json = "";
			try{
				File file = new File(plugin.getDataFolder() + File.separator + "where.json");
				BufferedReader br = new BufferedReader(new FileReader(file));

				String brseparator = System.getProperty("line.separator");

				String str;
				while((str = br.readLine()) != null){
					json += str + brseparator;
				}

				br.close();
			}catch(FileNotFoundException e){
				System.out.println(e);
			}catch(IOException e){
				System.out.println(e);
			}
			try {
				obj = (JSONObject) parser.parse(json);
			} catch (ParseException e1) {
				obj = new JSONObject();
			}
			JSONObject loclist = new JSONObject();
			loclist.put("location", location);
			loclist.put("user", sender.getName());
			loclist.put("world", world.getName());
			int a, b;
			if(x1 < x2){
			    a = x1;
			    b = x2;
			}else{
			    a = x2;
			    b = x1;
			}
			loclist.put("x1", a);
			loclist.put("x2", b);

			if(y1 < y2){
			    a = y1;
			    b = y2;
			}else{
			    a = y2;
			    b = y1;
			}
			loclist.put("y1", a);
			loclist.put("y2", b);

			if(z1 < z2){
			    a = z1;
			    b = z2;
			}else{
			    a = z2;
			    b = z1;
			}
			loclist.put("z1", a);
			loclist.put("z2", b);

			JSONObject flaglist = new JSONObject();
			// 他の人ができること(true), できないこと(false)
			flaglist.put("edit", true); //ブロックの変更はデフォルトでできるように
			flaglist.put("tnt", true); //TNTの爆発破壊はデフォルトでできるように
			flaglist.put("move", true); //場所に入ることをデフォルトでできるように
			flaglist.put("message", false); //入ったときにメッセージはデフォルトで出ないように
			loclist.put("flag", flaglist);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			obj.put(sdf1.format(new Date()), loclist);

			try{

		    	File file = new File(plugin.getDataFolder() + File.separator + "where.json");
		    	FileWriter filewriter = new FileWriter(file);

		    	filewriter.write(obj.toJSONString());

		    	filewriter.close();
		    }catch(IOException e){
		    	Method.SendMessage(sender, cmd, "設定できませんでした。");
		    	System.out.println(e);
				return;
		    }
			Method.SendMessage(sender, cmd, "設定しました。");
			return;
		}
	}
	private class messagesetdisc extends BukkitRunnable{
		JavaPlugin plugin;
		CommandSender sender;
		Command cmd;
		String location;
		RegionSelector regionSelector;
		String discription;
    	public messagesetdisc(JavaPlugin plugin, CommandSender sender, Command cmd, String location, RegionSelector regionSelector, String discription) {
    		this.plugin = plugin;
    		this.sender = sender;
    		this.cmd = cmd;
    		this.location = location;
    		this.regionSelector = regionSelector;
    		this.discription = discription;
    	}
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			Player player = (Player) sender;
			Location loc = player.getLocation();
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
			}catch(IOException e1){
				System.out.println(e1);
			}
			JSONObject obj;
			try {
				obj = (JSONObject) parser.parse(json);
			} catch (ParseException e1) {
				obj = new JSONObject();
			}
			Boolean boo = false;
			Boolean boo_name = false;
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
				if(location.equals((String) one.getValue().get("location"))){
					boo_name = true;
				}
			}
			if(boo){
				Method.SendMessage(sender, cmd, "この土地は他のユーザーに既に登録されています。");
				return;
			}
			if(boo_name){
				Method.SendMessage(sender, cmd, "この土地名は他のユーザーに既に登録されています。");
				return;
			}

			int x1;
			int y1;
			int z1;
			int x2;
			int y2;
			int z2;
			World world;
			try {
				String worldname = regionSelector.getRegion().getWorld().getName();
				x1 = regionSelector.getRegion().getMinimumPoint().getBlockX();
				y1 = 0;
				z1 = regionSelector.getRegion().getMinimumPoint().getBlockZ();
				x2 = regionSelector.getRegion().getMaximumPoint().getBlockX();
				y2 = 255;
				z2 = regionSelector.getRegion().getMaximumPoint().getBlockZ();
				world = Bukkit.getWorld(worldname);
			} catch (IncompleteRegionException ex) {
				Method.SendMessage(sender, cmd, "範囲を2つ指定してください。");
				return;
			} catch(java.lang.NullPointerException ex){
				Method.SendMessage(sender, cmd, "範囲を指定してください。");
				return;
			}

			parser = new JSONParser();
			json = "";
			try{
				File file = new File(plugin.getDataFolder() + File.separator + "where.json");
				BufferedReader br = new BufferedReader(new FileReader(file));

				String brseparator = System.getProperty("line.separator");

				String str;
				while((str = br.readLine()) != null){
					json += str + brseparator;
				}

				br.close();
			}catch(FileNotFoundException e){
				System.out.println(e);
			}catch(IOException e){
				System.out.println(e);
			}
			try {
				obj = (JSONObject) parser.parse(json);
			} catch (ParseException e1) {
				obj = new JSONObject();
			}
			JSONObject loclist = new JSONObject();
			loclist.put("location", location);
			loclist.put("user", sender.getName());
			loclist.put("discription", discription);
			loclist.put("world", world.getName());
			int a, b;
			if(x1 < x2){
			    a = x1;
			    b = x2;
			}else{
			    a = x2;
			    b = x1;
			}
			loclist.put("x1", a);
			loclist.put("x2", b);

			if(y1 < y2){
			    a = y1;
			    b = y2;
			}else{
			    a = y2;
			    b = y1;
			}
			loclist.put("y1", a);
			loclist.put("y2", b);

			if(z1 < z2){
			    a = z1;
			    b = z2;
			}else{
			    a = z2;
			    b = z1;
			}
			loclist.put("z1", a);
			loclist.put("z2", b);

			JSONObject flaglist = new JSONObject();
			// 他の人ができること(true), できないこと(false)
			flaglist.put("edit", true); //ブロックの変更はデフォルトでできるように
			flaglist.put("tnt", true); //TNTの爆発破壊はデフォルトでできるように
			flaglist.put("move", true); //場所に入ることをデフォルトでできるように
			flaglist.put("message", false); //入ったときにメッセージはデフォルトで出ないように
			loclist.put("flag", flaglist);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			obj.put(sdf1.format(new Date()), loclist);

			try{

		    	File file = new File(plugin.getDataFolder() + File.separator + "where.json");
		    	FileWriter filewriter = new FileWriter(file);

		    	filewriter.write(obj.toJSONString());

		    	filewriter.close();
		    }catch(IOException e){
		    	Method.SendMessage(sender, cmd, "設定できませんでした。");
		    	System.out.println(e);
				return;
		    }
			Method.SendMessage(sender, cmd, "設定しました。");
			return;
		}
	}
	private class messagedel extends BukkitRunnable{
		JavaPlugin plugin;
		CommandSender sender;
		Command cmd;
		String location;
    	public messagedel(JavaPlugin plugin, CommandSender sender, Command cmd, String location) {
    		this.plugin = plugin;
    		this.sender = sender;
    		this.cmd = cmd;
    		this.location = location;
    	}
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			Player player = (Player) sender;
			Location loc = player.getLocation();
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
			}catch(IOException e1){
				System.out.println(e1);
			}
			JSONObject obj;
			try {
				obj = (JSONObject) parser.parse(json);
			} catch (ParseException e1) {
				obj = new JSONObject();
			}
			Boolean boo = true;
			String user = "";
			JSONObject newdata = new JSONObject();
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
							if(location.equals((String) one.getValue().get("location"))){
								boo = false;
								user = (String) one.getValue().get("user");
							}else{
								newdata.put(one.getKey(), one.getValue());
							}
						}else{
							newdata.put(one.getKey(), one.getValue());
						}
					}else{
						newdata.put(one.getKey(), one.getValue());
					}
				}else{
					newdata.put(one.getKey(), one.getValue());
				}


			}
			if(boo){
				Method.SendMessage(sender, cmd, "指定された土地名の土地はありません。");
				return;
			}
			if(!user.equalsIgnoreCase(player.getName())){
				Method.SendMessage(sender, cmd, "指定された土地名の土地はあなたの所有地ではありません。");
				return;
			}
			try{

		    	File file = new File(plugin.getDataFolder() + File.separator + "where.json");
		    	FileWriter filewriter = new FileWriter(file);

		    	filewriter.write(newdata.toJSONString());

		    	filewriter.close();
		    }catch(IOException e){
		    	Method.SendMessage(sender, cmd, "削除できませんでした。");
		    	System.out.println(e);
				return;
		    }
			Method.SendMessage(sender, cmd, "削除しました。");
			return;
		}
	}
}
