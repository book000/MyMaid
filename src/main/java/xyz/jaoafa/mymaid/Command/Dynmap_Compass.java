package xyz.jaoafa.mymaid.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Pointjao;

public class Dynmap_Compass implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public Dynmap_Compass(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String, String> dcdata = new HashMap<String, String>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Plugin dynmap = plugin.getServer().getPluginManager().getPlugin("dynmap");
		if(dynmap == null || !dynmap.isEnabled()){
			Method.SendMessage(sender, cmd, "Dynmapプラグインが停止中、もしくは存在しないため、このコマンドを利用できません。");
			return true;
		}
		DynmapAPI dynmapapi = (DynmapAPI)dynmap;
		MarkerAPI markerapi = dynmapapi.getMarkerAPI();

		if(args.length >= 2){
			if(args[0].equalsIgnoreCase("set")){
				if (!(sender instanceof Player)) {
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
					return true;
				}
				Player player = (Player) sender;

				int c = 1;
				String markerlabel = "";
				while(args.length > c){
					markerlabel += args[c];
					if(args.length != (c+1)){
						markerlabel+=" ";
					}
					c++;
				}
				Map<String, Marker> Markers = new HashMap<String, Marker>();
				for(MarkerSet markerset : markerapi.getMarkerSets()){
					for(Marker marker : markerset.getMarkers()){
						Markers.put(marker.getLabel(), marker);
					}
				}
				if(Markers.containsKey(markerlabel)){
					Marker marker = Markers.get(markerlabel);
					World world = Bukkit.getWorld(marker.getWorld());
					double x = marker.getX();
					double y = marker.getY();
					double z = marker.getZ();
					Location loc = new Location(world, x, y, z);
					loc.add(0.5f, 0f, 0.5f);
					int use = 10;
					if(!Pointjao.hasjao(player, use)){
						Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
						return true;
					}
					Pointjao.usejao(player, use, "dcコマンド実行の為");
					dcdata.put(player.getName(), markerlabel);
					player.setCompassTarget(loc);
					Method.SendMessage(sender, cmd, "コンパスの方向をDynmapのマーカー地点「" + markerlabel + "」にセットしました。");
					Method.SendMessage(sender, cmd, "リセットするには/dc clearを実行してください。");
					return true;
				}else{
					// 見つからなかった
					Method.SendMessage(sender, cmd, "指定されたマーカー「" + markerlabel +"」は見つかりませんでした。");
					return true;
				}
			}
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("show")){
				if (!(sender instanceof Player)) {
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}
				Player player = (Player) sender;
				if(!dcdata.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "DCで指定された方向設定は行われていません。");
					return true;
				}
				String location = dcdata.get(player.getName());
				Method.SendMessage(sender, cmd, "コンパスの方向は、Dynmapのマーカー地点「" + location + "」にセットされています。");
				return true;
			}else if(args[0].equalsIgnoreCase("clear")){
				if (!(sender instanceof Player)) {
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}
				Player player = (Player) sender;
				if(!dcdata.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "DCで指定された方向設定は行われていません。");
					return true;
				}
				dcdata.remove(player.getName());
				player.setCompassTarget(player.getWorld().getSpawnLocation());
				Method.SendMessage(sender, cmd, "コンパスの方向をリセットしました。");
				return true;
			}
		}
		Method.SendMessage(sender, cmd, "--- Dynmap Compass Help ---");
		Method.SendMessage(sender, cmd, "/dc set <DynmapMarkerName>: コンパスの方向をDynmapのマーカー地点に設定します。");
		Method.SendMessage(sender, cmd, "/dc show: コンパスの方向が設定されているDynmapのマーカー地点名を表示します。");
		Method.SendMessage(sender, cmd, "/dc clear: コンパスの方向をリセットします。");
		return true;
	}
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("set") && args[1].length() == 0) { // /testまで
				Plugin dynmap = plugin.getServer().getPluginManager().getPlugin("dynmap");
				if(dynmap == null || !dynmap.isEnabled()){
					Method.SendMessage(sender, cmd, "Dynmapプラグインが停止中、もしくは存在しないため、このコマンドを利用できません。");
					return plugin.onTabComplete(sender, cmd, alias, args);
				}

				List<String> tablist = new ArrayList<String>();
				tablist.add("add");
				tablist.add("del");
				tablist.add("list");

				DynmapAPI dynmapapi = (DynmapAPI)dynmap;
				MarkerAPI markerapi = dynmapapi.getMarkerAPI();
				for(MarkerSet markerset : markerapi.getMarkerSets()){
					for(Marker marker : markerset.getMarkers()){
						tablist.add(marker.getLabel());
					}
				}
				return tablist;
			}
		}else if (args.length == 3) {
			if (args[0].equalsIgnoreCase("set") && args[1].length() != 0) { // /testまで
				Plugin dynmap = plugin.getServer().getPluginManager().getPlugin("dynmap");
				if(dynmap == null || !dynmap.isEnabled()){
					Method.SendMessage(sender, cmd, "Dynmapプラグインが停止中、もしくは存在しないため、このコマンドを利用できません。");
					return plugin.onTabComplete(sender, cmd, alias, args);
				}

				Player player = Bukkit.getPlayer(args[0]);
				if(player == null){
					return plugin.onTabComplete(sender, cmd, alias, args);
				}else if(!player.isOnline()){
					return plugin.onTabComplete(sender, cmd, alias, args);
				}


				List<String> tablist = new ArrayList<String>();
				DynmapAPI dynmapapi = (DynmapAPI)dynmap;
				MarkerAPI markerapi = dynmapapi.getMarkerAPI();

				for(MarkerSet markerset : markerapi.getMarkerSets()){
					for(Marker marker : markerset.getMarkers()){
						tablist.add(marker.getLabel());
					}
				}
				List<String> tablistFor = new ArrayList<String>();
				tablistFor.addAll(tablist);
				for(String tab : tablistFor){
					if(!tab.toLowerCase().startsWith(args[1].toLowerCase())){
						tablist.remove(tab);
					}
				}
				if(tablist.size() == 0){
					return plugin.onTabComplete(sender, cmd, alias, args);
				}
				return tablist;
			}
		}
		//JavaPlugin#onTabComplete()を呼び出す
		return plugin.onTabComplete(sender, cmd, alias, args);
	}
}
