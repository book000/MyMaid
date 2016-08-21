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
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.CuboidRegionSelector;
import com.sk89q.worldedit.regions.RegionSelector;

import xyz.jaoafa.mymaid.Method;

@SuppressWarnings("deprecation")
public class DedMsg implements CommandExecutor {
	JavaPlugin plugin;
	public DedMsg(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<Location,String> dedmsg = new HashMap<Location,String>();
	@SuppressWarnings( "unchecked" )
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
		if (session != null) {
			// セレクタが立方体セレクタか判定
			if (!(session.getRegionSelector() instanceof CuboidRegionSelector)){
				Method.SendMessage(sender, cmd, "WorldEditの選択範囲を立方体にしてください。");
			}
			try {
				RegionSelector regionSelector = session.getRegionSelector(session.getSelectionWorld());
				String worldname = regionSelector.getRegion().getWorld().getName();
				int x1 = regionSelector.getRegion().getMinimumPoint().getBlockX();
				int y1 = regionSelector.getRegion().getMinimumPoint().getBlockY();
				int z1 = regionSelector.getRegion().getMinimumPoint().getBlockZ();
				int x2 = regionSelector.getRegion().getMaximumPoint().getBlockX();
				int y2 = regionSelector.getRegion().getMaximumPoint().getBlockY();
				int z2 = regionSelector.getRegion().getMaximumPoint().getBlockZ();
				World world = Bukkit.getWorld(worldname);
				JSONObject loclist = new JSONObject();
				int i = 0;
				for(int x = x1; x <= x2; x++){
					for(int y = y1; y <= y2; y++){
						for(int z = z1; z <= z2; z++){
							JSONObject xyz = new JSONObject();
							xyz.put("world", world.getName());
							xyz.put("x", x);
							xyz.put("y", y);
							xyz.put("z", z);
							loclist.put(i, xyz);
							i++;
						}
					}
				}
				loclist.put("message", args[0]);
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
				obj.put(sdf1.format(new Date()), loclist);

				//Method.SendMessage(sender, cmd, obj.toJSONString());

			} catch (IncompleteRegionException ex) {
				Method.SendMessage(sender, cmd, "範囲を2つ指定してください。");
				return true;
			} catch(java.lang.NullPointerException ex){
				Method.SendMessage(sender, cmd, "範囲を指定してください。");
				return true;
			}
		}else{
			Method.SendMessage(sender, cmd, "範囲を指定してください。");
			return true;
		}
		try{

	    	File file = new File(plugin.getDataFolder() + File.separator + "DEDMSG.json");
	    	FileWriter filewriter = new FileWriter(file);

	    	filewriter.write(obj.toJSONString());

	    	filewriter.close();
	    }catch(IOException e){
	    	Method.SendMessage(sender, cmd, "設定できませんでした。");
	    	System.out.println(e);
	    	return true;
	    }
		Method.SendMessage(sender, cmd, "設定しました。");
		return true;
	}
}
