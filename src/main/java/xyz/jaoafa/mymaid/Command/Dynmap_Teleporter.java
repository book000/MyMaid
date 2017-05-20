package xyz.jaoafa.mymaid.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Discord.Discord;

public class Dynmap_Teleporter implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public Dynmap_Teleporter(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Plugin dynmap = plugin.getServer().getPluginManager().getPlugin("dynmap");
		if(dynmap == null || !dynmap.isEnabled()){
			Method.SendMessage(sender, cmd, "Dynmapプラグインが停止中、もしくは存在しないため、このコマンドを利用できません。");
			return true;
		}
		DynmapAPI dynmapapi = (DynmapAPI)dynmap;
		MarkerAPI markerapi = dynmapapi.getMarkerAPI();

		if(args.length == 1){
			if(args[0].equalsIgnoreCase("add")){
				// 一つではaddは使えない
				Method.SendMessage(sender, cmd, "マーカーの追加には/dt add <MarkerName> <MarkerType>を使用します。");
				return true;
			}else if(args[0].equalsIgnoreCase("del")){
				// 一つではdelは使えない
				Method.SendMessage(sender, cmd, "マーカーの削除には/dt add <MarkerName>を使用します。");
				return true;
			}else if(args[0].equalsIgnoreCase("list")){
				Set<Marker> Markers = new HashSet<Marker>();
				for(MarkerSet markerset : markerapi.getMarkerSets()){
					for(Marker marker : markerset.getMarkers()){
						Markers.add(marker);
					}
				}

				int count = 0;
				int page = 1;
				int startcount = (page - 1) * 10;
				int endcount = page * 10;
				int maxpage = Markers.size() / 10;

				Method.SendMessage(sender, cmd, "Marker List: " + page + "page / " + maxpage + "page");
				Method.SendMessage(sender, cmd, "-------------------------");

				for(Marker marker : Markers){
					if(count <= startcount){
						count++;
						continue;
					}
					if(count > endcount){
						break;
					}

					Method.SendMessage(sender, cmd, "[" + count + "|" + marker.getWorld() + "/" + marker.getMarkerSet().getMarkerSetLabel() + "]" + marker.getLabel() + " X:" + marker.getX() + " Y:" + marker.getY() + " Z:" + marker.getY());
					count++;
				}
				Method.SendMessage(sender, cmd, "-------------------------");
				Method.SendMessage(sender, cmd, startcount + " - " + endcount + " / " + Markers.size());
				if(page != maxpage){
					Method.SendMessage(sender, cmd, "次のページを見るには「/dt list " + (page + 1) + "」を実行します。");
				}
				return true;
			}else{
				// addとかdelとか以外 => マーカー名？
				// /dt <MarkerName>
				if(!(sender instanceof Player)){
					Method.SendMessage(sender, cmd, "このコマンドはサーバ内から実行可能です。");
					return true;
				}
				Player player = (Player) sender;
				String markerlabel = args[0];

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
					player.teleport(loc);
					MyMaid.TitleSender.setTime_second(player, 2, 5, 2);
					MyMaid.TitleSender.sendTitle(player, "", ChatColor.AQUA +  "You have been teleported to " + markerlabel + "!");
					Discord.send("*[" + player.getName() + ": " + player.getName() + " to " + markerlabel + "]*");
					Bukkit.broadcastMessage(ChatColor.GRAY + "[" + player.getName() + ": " + player.getName() + " は " + markerlabel + " にワープしました]");
					return true;
				}else{
					// 見つからなかった
					Method.SendMessage(sender, cmd, "指定されたマーカー「" + markerlabel +"」は見つかりませんでした。");
					return true;
				}
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("add")){
				// 二つではaddは使えない
				Method.SendMessage(sender, cmd, "マーカーの追加には/dt add <MarkerName> <MarkerType>を使用します。");
				return true;
			}else if(args[0].equalsIgnoreCase("del")){
				// マーカーの削除
				// /dt del <MarkerName>
				String markerlabel = args[1];
				Map<String, Marker> Markers = new HashMap<String, Marker>();
				for(MarkerSet markerset : markerapi.getMarkerSets()){
					for(Marker marker : markerset.getMarkers()){
						Markers.put(marker.getLabel(), marker);
					}
				}
				if(Markers.containsKey(markerlabel)){
					Marker marker = Markers.get(markerlabel);
					String world = marker.getWorld();
					double x = marker.getX();
					double y = marker.getY();
					double z = marker.getZ();
					marker.deleteMarker();
					Method.SendMessage(sender, cmd, "マーカー「" + markerlabel +"(" + world + ", " + x + ", " + y + ", " + z + ")」を削除しました。");
					return true;
				}else{
					// 見つからなかった
					Method.SendMessage(sender, cmd, "指定されたマーカー「" + markerlabel +"」は見つかりませんでした。");
					return true;
				}
			}else if(args[0].equalsIgnoreCase("list")){
				Set<Marker> Markers = new HashSet<Marker>();
				for(MarkerSet markerset : markerapi.getMarkerSets()){
					for(Marker marker : markerset.getMarkers()){
						Markers.add(marker);
					}
				}

				int count = 0;
				int page = Integer.parseInt(args[1]);
				int startcount = (page - 1) * 10;
				int endcount = page * 10;
				int maxpage = (Markers.size() / 10) + 1;

				if(page < 0){
					Method.SendMessage(sender, cmd, "表示できるマーカーがありません。");
					return true;
				}else if(startcount >= Markers.size()){
					Method.SendMessage(sender, cmd, "表示できるマーカーがありません。");
					return true;
				}

				Method.SendMessage(sender, cmd, "Marker List: " + page + "page / " + maxpage + "page");
				Method.SendMessage(sender, cmd, "-------------------------");

				for(Marker marker : Markers){
					if(count <= startcount){
						count++;
						continue;
					}
					if(count > endcount){
						break;
					}

					Method.SendMessage(sender, cmd, "[" + count + "|" + marker.getWorld() + "/" + marker.getMarkerSet().getMarkerSetLabel() + "]" + marker.getLabel() + " X:" + marker.getX() + " Y:" + marker.getY() + " Z:" + marker.getY());
					count++;
				}
				Method.SendMessage(sender, cmd, "-------------------------");
				Method.SendMessage(sender, cmd, startcount + " - " + endcount + " / " + Markers.size());
				if(page != maxpage){
					Method.SendMessage(sender, cmd, "次のページを見るには「/dt list " + (page + 1) + "」を実行します。");
				}


			}else{
				// addとかdelとか以外 => プレイヤー名？
				// /dt <Player> <MarkerName>
				String playername = args[0];
				Player player = Bukkit.getPlayer(playername);
				if(player == null){
					Method.SendMessage(sender, cmd, "指定されたプレイヤー「" + playername +"」は見つかりませんでした。");
					return true;
				}else if(!player.isOnline()){
					Method.SendMessage(sender, cmd, "指定されたプレイヤー「" + playername +"」はオフラインです。");
					return true;
				}
				String markerlabel = args[1];

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
					player.teleport(loc);
					MyMaid.TitleSender.setTime_second(player, 2, 5, 2);
					MyMaid.TitleSender.sendTitle(player, "", ChatColor.AQUA +  "You have been teleported to " + markerlabel + "!");
					Discord.send("*[" + sender.getName() + ": " + player.getName() + " to " + markerlabel + "]*");
					Bukkit.broadcastMessage(ChatColor.GRAY + "[" + sender.getName() + ": " + player.getName() + " は " + markerlabel + " にワープしました]");
					return true;
				}else{
					// 見つからなかった
					Method.SendMessage(sender, cmd, "指定されたマーカー「" + markerlabel +"」は見つかりませんでした。");
					return true;
				}
			}
		}else if(args.length >= 3){
			if(args[0].equalsIgnoreCase("add")){
				// マーカーの追加
				if(!(sender instanceof Player)){
					Method.SendMessage(sender, cmd, "このコマンドはサーバ内から実行可能です。");
					return true;
				}
				Player player = (Player) sender;
				Location loc = player.getLocation();
				String markerlabel = args[1];
				String markertype = args[2];

				Map<String, MarkerSet> MarkersTypes = new HashMap<String, MarkerSet>();
				for(MarkerSet markerset : markerapi.getMarkerSets()){
					MarkersTypes.put(markerset.getMarkerSetLabel(), markerset);
				}

				if(!MarkersTypes.containsKey(markertype)){
					// マーカータイプが見つからなかった
					Method.SendMessage(sender, cmd, "指定されたマーカータイプ「" + markertype +"」は見つかりませんでした。");
					return true;
				}

				MarkerSet MarkerType = MarkersTypes.get(markertype);

				Map<String, Marker> Markers = new HashMap<String, Marker>();
				for(MarkerSet markerset : markerapi.getMarkerSets()){
					for(Marker marker : markerset.getMarkers()){
						Markers.put(marker.getLabel(), marker);
					}
				}
				String MarkerIconDef = MarkerIcon.DEFAULT;
				MarkerIcon markericondef = null;
				for(MarkerIcon icon : markerapi.getMarkerIcons()){
					if(icon.getMarkerIconID().equals(MarkerIconDef)){
						markericondef = icon;
					}
				}
				if(markericondef == null){
					Method.SendMessage(sender, cmd, "デフォルトマーカーアイコンが存在しませんでした。");
					return true;
				}
				Marker marker = MarkerType.createMarker(null, markerlabel, loc.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(), markericondef, true);

				if(marker == null){
					Method.SendMessage(sender, cmd, "指定されたマーカー「" + markerlabel +"」を作成できませんでした。");
					return true;
				}
				Method.SendMessage(sender, cmd, "指定されたマーカー「" + markerlabel +"」を作成しました。");

				if(Markers.containsKey(markerlabel)){
					Method.SendMessage(sender, cmd, "ヒント: 同じMarkerNameのマーカーが他にもあるようです。");
					Method.SendMessage(sender, cmd, "正常にテレポートできない可能性があります。");
					return true;
				}
				return true;
			}else{
				// addとかdelとか以外 => プレイヤー名？
				// /dt <Player> <MarkerName>
				String playername = args[0];
				Player player = Bukkit.getPlayer(playername);
				if(player == null){
					Method.SendMessage(sender, cmd, "指定されたプレイヤー「" + playername +"」は見つかりませんでした。");
					return true;
				}else if(!player.isOnline()){
					Method.SendMessage(sender, cmd, "指定されたプレイヤー「" + playername +"」はオフラインです。");
					return true;
				}
				String markerlabel = "";
				int c = 1;
				while(args.length > c){
					markerlabel += args[c];
					if(args.length != (c+1)){
						markerlabel += " ";
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
					player.teleport(loc);
					MyMaid.TitleSender.setTime_second(player, 2, 5, 2);
					MyMaid.TitleSender.sendTitle(player, "", ChatColor.AQUA +  "You have been teleported to " + markerlabel + "!");
					Discord.send("*[" + sender.getName() + ": " + player.getName() + " to " + markerlabel + "]*");
					Bukkit.broadcastMessage(ChatColor.GRAY + "[" + sender.getName() + ": " + player.getName() + " は " + markerlabel + " にワープしました]");
					return true;
				}else{
					// 見つからなかった
					Method.SendMessage(sender, cmd, "指定されたマーカー「" + markerlabel +"」は見つかりませんでした。");
					return true;
				}
			}
		}
		Method.SendMessage(sender, cmd, "---- Dynmap Teleporter ----");
		Method.SendMessage(sender, cmd, "/dt [Player] <MarkerName...>: MarkerNameにテレポートします。Playerを指定した場合はそのPlayerをテレポートさせます。");
		Method.SendMessage(sender, cmd, "/dt add <MarkerName> <MarkerType>: Markerを追加します。");
		Method.SendMessage(sender, cmd, "/dt add <MarkerName> <MarkerType>: Markerを削除します。");
		return true;
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (args.length == 1) {
			if (args[0].length() == 0) { // /dtまで
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
			}else{
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
				List<String> tablistFor = new ArrayList<String>();
				tablistFor.addAll(tablist);
				for(String tab : tablistFor){
					if(!tab.toLowerCase().startsWith(args[0].toLowerCase())){
						tablist.remove(tab);
					}
				}
				if(tablist.size() == 0){
					return plugin.onTabComplete(sender, cmd, alias, args);
				}
				return tablist;
			}
		}else if(args.length == 2){
			if (args[1].length() == 0) { // /dt ~まで
				if(args[0].equalsIgnoreCase("del")){
					Plugin dynmap = plugin.getServer().getPluginManager().getPlugin("dynmap");
					if(dynmap == null || !dynmap.isEnabled()){
						Method.SendMessage(sender, cmd, "Dynmapプラグインが停止中、もしくは存在しないため、このコマンドを利用できません。");
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
					return tablist;
				}else{
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
			}else{
				if(args[0].equalsIgnoreCase("del")){
					Plugin dynmap = plugin.getServer().getPluginManager().getPlugin("dynmap");
					if(dynmap == null || !dynmap.isEnabled()){
						Method.SendMessage(sender, cmd, "Dynmapプラグインが停止中、もしくは存在しないため、このコマンドを利用できません。");
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
						if(!tab.startsWith(args[1])){
							tablist.remove(tab);
						}
					}
					if(tablist.size() == 0){
						return plugin.onTabComplete(sender, cmd, alias, args);
					}
					return tablist;
				}else{
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
		}else if(args.length == 3){
			if (args[2].length() == 0) { // /dt ~ ~まで
				if(args[0].equalsIgnoreCase("add")){
					// <MarkerType>を出す
					Plugin dynmap = plugin.getServer().getPluginManager().getPlugin("dynmap");
					if(dynmap == null || !dynmap.isEnabled()){
						Method.SendMessage(sender, cmd, "Dynmapプラグインが停止中、もしくは存在しないため、このコマンドを利用できません。");
						return plugin.onTabComplete(sender, cmd, alias, args);
					}

					List<String> tablist = new ArrayList<String>();
					DynmapAPI dynmapapi = (DynmapAPI)dynmap;
					MarkerAPI markerapi = dynmapapi.getMarkerAPI();

					for(MarkerSet markerset : markerapi.getMarkerSets()){
						tablist.add(markerset.getMarkerSetLabel());
					}
					return tablist;
				}
			}else{
				if(args[0].equalsIgnoreCase("add")){
					Plugin dynmap = plugin.getServer().getPluginManager().getPlugin("dynmap");
					if(dynmap == null || !dynmap.isEnabled()){
						Method.SendMessage(sender, cmd, "Dynmapプラグインが停止中、もしくは存在しないため、このコマンドを利用できません。");
						return plugin.onTabComplete(sender, cmd, alias, args);
					}

					List<String> tablist = new ArrayList<String>();
					DynmapAPI dynmapapi = (DynmapAPI)dynmap;
					MarkerAPI markerapi = dynmapapi.getMarkerAPI();

					for(MarkerSet markerset : markerapi.getMarkerSets()){
						tablist.add(markerset.getMarkerSetLabel());
					}
					List<String> tablistFor = new ArrayList<String>();
					tablistFor.addAll(tablist);
					for(String tab : tablistFor){
						if(!tab.toLowerCase().startsWith(args[2].toLowerCase())){
							tablist.remove(tab);
						}
					}
					if(tablist.size() == 0){
						return plugin.onTabComplete(sender, cmd, alias, args);
					}
					return tablist;
				}
			}
		}
		//JavaPlugin#onTabComplete()を呼び出す
		return plugin.onTabComplete(sender, cmd, alias, args);
	}
}
