package xyz.jaoafa.mymaid.Command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import xyz.jaoafa.mymaid.Pointjao;

@SuppressWarnings("deprecation")
public class DedMsg implements CommandExecutor {
	JavaPlugin plugin;
	public DedMsg(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	/*
	 * pos1が127,67,996でpos2が136,74,987だとして！！！！
	 * とりあえず大小揃えて127,67,987と136,74,996みたいに大きい小さい揃えて！！！
	 * プレイヤーのxは127以上136以下か！！！
	 * プレイヤーのyは67以上74以下か！！！
	 * プレイヤーのzは987以上996以下か！！！
	 * ぜんぶ真なら範囲内！！！
	 */
	public static Map<Location,String> dedmsg = new HashMap<Location,String>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		if(args.length != 1){
			Method.SendMessage(sender, cmd, "引数が適切ではありません。");
			return true;
		}
		LocalSession session = WorldEdit.getInstance().getSession(player.getName());
		RegionSelector regionSelector = session.getRegionSelector(session.getSelectionWorld());
		String message = args[0];

		// セレクタが立方体セレクタか判定
		if (!(session.getRegionSelector(session.getSelectionWorld()) instanceof CuboidRegionSelector)){
			Method.SendMessage(sender, cmd, "WorldEditの選択範囲を立方体にしてください。");
			return true;
		}else{
			try{
				int use = 20;
				if(!Pointjao.hasjao(player, use)){
				 	 Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
				 	 return true;
				}
				new messageset(plugin, sender, cmd, message, regionSelector).runTask(plugin);
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
	private class messageset extends BukkitRunnable{
		JavaPlugin plugin;
		CommandSender sender;
		Command cmd;
		String message;
		RegionSelector regionSelector;
    	public messageset(JavaPlugin plugin, CommandSender sender, Command cmd, String message, RegionSelector regionSelector) {
    		this.plugin = plugin;
    		this.sender = sender;
    		this.cmd = cmd;
    		this.message = message;
    		this.regionSelector = regionSelector;
    	}
		@SuppressWarnings({ "unchecked", "null" })
		@Override
		public void run() {
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
				y1 = regionSelector.getRegion().getMinimumPoint().getBlockY();
				z1 = regionSelector.getRegion().getMinimumPoint().getBlockZ();
				x2 = regionSelector.getRegion().getMaximumPoint().getBlockX();
				y2 = regionSelector.getRegion().getMaximumPoint().getBlockY();
				z2 = regionSelector.getRegion().getMaximumPoint().getBlockZ();
				world = Bukkit.getWorld(worldname);
			} catch (IncompleteRegionException ex) {
				Method.SendMessage(sender, cmd, "範囲を2つ指定してください。");
				return;
			} catch(java.lang.NullPointerException ex){
				Method.SendMessage(sender, cmd, "範囲を指定してください。");
				return;
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
			}catch(FileNotFoundException e){
				System.out.println(e);
			}catch(IOException e){
				System.out.println(e);
			}
			JSONObject obj;
			try {
				obj = (JSONObject) parser.parse(json);
			} catch (ParseException e1) {
				obj = new JSONObject();
			}
			JSONObject loclist = new JSONObject();
			loclist.put("message", message);
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
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			obj.put(sdf1.format(new Date()), loclist);
			try{

		    	File file = new File(plugin.getDataFolder() + File.separator + "DEDMSG.json");
		    	FileWriter filewriter = new FileWriter(file);

		    	filewriter.write(obj.toJSONString());

		    	filewriter.close();
		    }catch(IOException e){
		    	Method.SendMessage(sender, cmd, "設定できませんでした。");
		    	System.out.println(e);
				return;
		    }
			if (sender instanceof Player) {
				Player player = (Player) sender;
				int use = 20;
				Pointjao.usejao(player, use);
			}
			Method.SendMessage(sender, cmd, "設定しました。");
			return;
		}
	}
}
