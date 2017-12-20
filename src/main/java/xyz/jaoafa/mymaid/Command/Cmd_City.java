package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.EventHandler.CityCornerEditer._OpenGUI;

public class Cmd_City implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_City(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	/*
	 /city addcorner - コーナーを追加
	 /city clearcorner - コーナーを削除
	 /city add <Name> <Color> - 市の範囲を色と共に設定(Dynmapに表示)
	 /city del <Name> - 市の範囲を削除
	 /city setdesc <説明> - 市の説明を設定
	 /city show [市名] - 市の情報を表示。市名を設定しないといまいるところの市情報を表示(できるかどうか)
	 */

	/**
	 * デバックモードか<br>
	 * デバックモードならtrue, そうでなければfalse
	 */
	final boolean DebugMode = true;

	Map<String, Set<Location>> Corner = new HashMap<String, Set<Location>>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Plugin dynmap = plugin.getServer().getPluginManager().getPlugin("dynmap");
		if(dynmap == null || !dynmap.isEnabled()){
			Method.SendMessage(sender, cmd, "Dynmapプラグインが停止中、もしくは存在しないため、このコマンドを利用できません。");
			return true;
		}
		if(!(sender instanceof Player)){
			Method.SendMessage(sender, cmd, "このコマンドはサーバ内から実行可能です。");
			return true;
		}
		Player player = (Player) sender;
		DynmapAPI dynmapapi = (DynmapAPI) dynmap;
		MarkerAPI markerapi = dynmapapi.getMarkerAPI();

		if(markerapi.getMarkerSet("towns") == null){
			Bukkit.getLogger().info("市情報を保管するDynmapSets「towns」が存在しないため、作成します。");
			markerapi.createMarkerSet("towns", "Towns", null, true);
		}

		if(args.length == 1){
			if(args[0].equalsIgnoreCase("addcorner")){
				// /city addcorner - コーナーを追加
				Set<Location> corner = new LinkedHashSet<Location>();
				if(Corner.containsKey(player.getName())){
					corner.addAll(Corner.get(player.getName()));
				}
				Location loc = player.getLocation();
				corner.add(loc);
				Corner.put(player.getName(), corner);
				Method.SendMessage(sender, cmd, "次の地点をコーナーキューに追加しました: #" + corner.size() + " X: " + loc.getBlockX() + " Z: " + loc.getBlockZ());
				return true;
			}else if(args[0].equalsIgnoreCase("clearcorner")){
				// /city clearcorner - コーナーを削除
				if(Corner.containsKey(player.getName())){
					Corner.remove(player.getName());
					Method.SendMessage(sender, cmd, "削除に成功しました。");
					return true;
				}else{
					Method.SendMessage(sender, cmd, "削除に失敗しました。");
					return true;
				}
			}else if(args[0].equalsIgnoreCase("undocorner")){
				// /city undocorner - ひとつ前に追加したコーナーを削除
				if(Corner.containsKey(player.getName())){
					Set<Location> corner = new LinkedHashSet<Location>();
					int now = 0;
					Location loc = null;
					for(Location one : Corner.get(player.getName())){
						if(now == (Corner.get(player.getName()).size() - 1)){
							loc = one;
							break;
						}
						corner.add(one);
						now++;
					}
					if(loc == null){
						Method.SendMessage(sender, cmd, "ひとつ前のコーナーの削除に失敗しました。");
						return true;
					}

					Corner.put(player.getName(), corner);

					Method.SendMessage(sender, cmd, "ひとつ前のコーナー(X: " + loc.getBlockX() + " / Z: " + loc.getBlockZ() + ")の削除に成功しました。");
					return true;
				}else{
					Method.SendMessage(sender, cmd, "ひとつ前のコーナーの削除に失敗しました。");
					return true;
				}
			}else if(args[0].equalsIgnoreCase("show")){
				// /city show [市名] - 市の情報を表示。市名を設定しないといまいるところの市情報を表示(できるかどうか)
				long start = System.currentTimeMillis();

				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					// @param XorZ Xならtrue, Zならfalse
					// @param isMaxMaxならtrue, Minならfalse
					int maxX = getMaxOrMin(areamarker, true, true);
					int maxZ = getMaxOrMin(areamarker, false, true);
					int minX = getMaxOrMin(areamarker, true, false);
					int minZ = getMaxOrMin(areamarker, false, false);
					if(DebugMode) System.out.println("最大最小" + maxX + ", " + maxZ + ", " + minX + ", " + minZ);

					Location playerloc = player.getLocation();
					if(playerloc.getX() < minX || playerloc.getZ() > maxX){
						double x = playerloc.getX();
						double z = playerloc.getZ();
						int count = getCorners(areamarker, (int) x, (int) z);
						if(DebugMode) System.out.println("Count: " + count);

						if(count % 2 != 0){
							// 範囲内
							Method.SendMessage(sender, cmd, "この場所は「" + areamarker.getLabel() + "」という名前のエリアです。");
							Method.SendMessage(sender, cmd, "説明: " + areamarker.getDescription());

							if(DebugMode){
								long end = System.currentTimeMillis();
								System.out.println("処理時間: " + (end - start)  + "ms");
							}
							return true;
						}
					}
				}

				Method.SendMessage(sender, cmd, "この場所はエリア登録されていません。");

				if(DebugMode){
					long end = System.currentTimeMillis();
					System.out.println("処理時間: " + (end - start)  + "ms");
				}
				return true;

				/*
				double minX = 0;
				double maxX = 0;
				boolean bool = true;
				int c = 0;
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					for(int i = 0; i < areamarker.getCornerCount(); i++){
						double x = areamarker.getCornerX(i);
						if(minX > x){
							minX = x;
						}
						if(maxX < x){
							maxX = x;
						}
						c++;
					}
				}
				Method.SendMessage(sender, cmd, "minX: " + minX);
				Method.SendMessage(sender, cmd, "maxX: " + maxX);
				Method.SendMessage(sender, cmd, "Count: " + c);

				if(minX < player.getLocation().getX() && maxX < player.getLocation().getX()){
					Method.SendMessage(sender, cmd, "この場所はエリア登録されていません。");
				}else if(minX > player.getLocation().getX() && maxX > player.getLocation().getX()){
					Method.SendMessage(sender, cmd, "+1");
				}else if(minX < player.getLocation().getX() && maxX > player.getLocation().getX()){
					Method.SendMessage(sender, cmd, "線分より右にいるのか左にいるのかを判定し、結果次第");
				}
				 */
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("del")){
				String cityName = args[1];
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					if(areamarker.getLabel().equals(cityName)){
						areamarker.deleteMarker();
						Method.SendMessage(sender, cmd, "指定された市名のエリアを削除しました。");
						return true;
					}
				}
				Method.SendMessage(sender, cmd, "指定された市名のエリアは見つかりませんでした。");
				return true;
			}else if(args[0].equalsIgnoreCase("show")){
				String cityName = args[1];
				AreaMarker select = null;
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					if(areamarker.getLabel().equals(cityName)){
						select = areamarker;
					}
				}
				if(select == null){
					Method.SendMessage(sender, cmd, "指定された市名のエリアは見つかりませんでした。");
					return true;
				}
				String label = select.getLabel();
				String desc = select.getDescription();
				Method.SendMessage(sender, cmd, "--- " + label + " ---");
				Method.SendMessage(sender, cmd, "説明: " + desc);
				return true;
			}else if(args[0].equalsIgnoreCase("addcorner")){
				String cityName = args[1];
				AreaMarker select = null;
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					if(areamarker.getLabel().equals(cityName)){
						select = areamarker;
					}
				}
				if(select == null){
					Method.SendMessage(sender, cmd, "指定された市名のエリアは見つかりませんでした。");
					return true;
				}
				List<Double> Xs = new LinkedList<Double>();
				List<Double> Zs = new LinkedList<Double>();
				for(int i = 0; i < select.getCornerCount(); i++){
					Xs.add(select.getCornerX(i));
					Zs.add(select.getCornerZ(i));
				}
				Location loc = player.getLocation();
				Xs.add(loc.getBlockX() + 0.5);
				Zs.add(loc.getBlockZ() + 0.5);
				double[] ArrXs = Xs.stream().mapToDouble(Double::doubleValue).toArray();
				double[] ArrZs = Zs.stream().mapToDouble(Double::doubleValue).toArray();
				select.setCornerLocations(ArrXs, ArrZs);
				Method.SendMessage(sender, cmd, "エリア「" + select.getLabel() + "」に新しいコーナーを追加しました。");
				Method.SendMessage(sender, cmd, "順番を変更したりする場合は、「/city editcorner <Name>」コマンドをお使いください。");
				return true;
			}else if(args[0].equalsIgnoreCase("editcorner")){
				String cityName = args[1];
				AreaMarker select = null;
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					if(areamarker.getLabel().equals(cityName)){
						select = areamarker;
					}
				}
				if(select == null){
					Method.SendMessage(sender, cmd, "指定された市名のエリアは見つかりませんでした。");
					return true;
				}
				new _OpenGUI(player, select).runTaskLater(plugin, 1);
				return true;
			}
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("add")){
				// /city add <Name> <Color> - 市の範囲を設定(Dynmapに表示)
				if(!Corner.containsKey(player.getName())){
					// コーナー未登録
					Method.SendMessage(sender, cmd, "コーナーが未登録です。/city addcornerを使用してコーナーを登録してください。");
					return true;
				}
				Set<Location> corner = Corner.get(player.getName());
				if(corner.size() < 2){
					// コーナー数が2つ未満
					Method.SendMessage(sender, cmd, "コーナー数が足りません。/city addcornerを使用してコーナーを登録してください。");
					return true;
				}
				String cityName = args[1];
				String color = args[2];

				for(MarkerSet markerset : markerapi.getMarkerSets()){
					for(AreaMarker areamarker : markerset.getAreaMarkers()){
						if(areamarker.getLabel().equals(cityName)){
							Method.SendMessage(sender, cmd, "登録しようとした市名は既に存在します。再登録する場合は削除してください。");
							return true;
						}
					}
				}

				MarkerSet set = markerapi.getMarkerSet("towns");
				List<Double> Xs = new LinkedList<Double>();
				List<Double> Zs = new LinkedList<Double>();
				for(Location loc : corner){
					Xs.add(new Double(loc.getBlockX()) + 0.5);
					Zs.add(new Double(loc.getBlockZ()) + 0.5);
				}
				double[] ArrXs = Xs.stream().mapToDouble(Double::doubleValue).toArray();
				double[] ArrZs = Zs.stream().mapToDouble(Double::doubleValue).toArray();
				AreaMarker area = set.createAreaMarker(null, cityName, false, player.getWorld().getName(), ArrXs, ArrZs, true);
				if(area == null){
					Method.SendMessage(sender, cmd, "登録に失敗しました。");
					return true;
				}
				area.setFillStyle(area.getFillOpacity(), 0x808080);
				int colorint = Integer.parseInt(color, 16);
				area.setLineStyle(area.getLineWeight(), area.getLineOpacity(), colorint);
				Method.SendMessage(sender, cmd, "登録に成功しました。");
				Corner.remove(player.getName());
				return true;
			}else if(args[0].equalsIgnoreCase("setdesc")){
				String cityName = args[1];
				String desc = "";
				int c = 2;
				while(args.length > c){
					desc += args[c]+" ";
					c++;

				}
				AreaMarker select = null;
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					if(areamarker.getLabel().equals(cityName)){
						select = areamarker;
					}
				}
				if(select == null){
					Method.SendMessage(sender, cmd, "指定された市名のエリアは見つかりませんでした。");
					return true;
				}
				desc = htmlspecialchars(desc);
				select.setDescription("<b>" + select.getLabel() + "</b><br />" +  desc);
				Method.SendMessage(sender, cmd, "指定された市名のエリアへ説明文を追加しました。");
				return true;
			}
		}else if(args.length >= 3){
			if(args[0].equalsIgnoreCase("setdesc")){
				String cityName = args[1];
				String desc = "";
				int c = 2;
				while(args.length > c){
					desc += args[c]+" ";
					c++;

				}
				AreaMarker select = null;
				MarkerSet markerset = markerapi.getMarkerSet("towns");
				for(AreaMarker areamarker : markerset.getAreaMarkers()){
					if(areamarker.getLabel().equals(cityName)){
						select = areamarker;
					}
				}
				if(select == null){
					Method.SendMessage(sender, cmd, "指定された市名のエリアは見つかりませんでした。");
					return true;
				}
				desc = htmlspecialchars(desc);
				select.setDescription("<b>" + select.getLabel() + "</b><br />" +  desc);
				Method.SendMessage(sender, cmd, "指定された市名のエリアへ説明文を追加しました。");
				return true;
			}
		}
		Method.SendMessage(sender, cmd, "----- City -----");
		Method.SendMessage(sender, cmd, "/city addcorner - コーナーを追加");
		Method.SendMessage(sender, cmd, "/city addcorner <Name> - 既存のエリアにコーナーを追加");
		Method.SendMessage(sender, cmd, "/city undocorner - ひとつ前に追加したコーナーを削除");
		Method.SendMessage(sender, cmd, "/city clearcorner - コーナーを削除");
		Method.SendMessage(sender, cmd, "/city show - いまいる地点の情報を表示。(未完成)");
		Method.SendMessage(sender, cmd, "/city add <Name> <Color> - エリアの範囲を色と共に設定(Dynmapに表示)");
		Method.SendMessage(sender, cmd, "/city del <Name> - エリアの範囲を削除");
		Method.SendMessage(sender, cmd, "/city editcorner <Name> - 指定されたエリアのコーナーエディタを使用してコーナーを編集");
		Method.SendMessage(sender, cmd, "/city setdesc <Name> <Description> - 市の説明を設定");
		Method.SendMessage(sender, cmd, "/city show <Name> - エリアの情報を表示。");
		return true;
	}

	String htmlspecialchars(String str){

		String[] escape = {"&", "<", ">", "\"", "\'", "\n", "\t"};
		String[] replace = {"&amp;", "&lt;", "&gt;", "&quot;", "&#39;", "<br>", "&#x0009;"};

		for (int i = 0; i < escape.length; i++ ){
			str = str.replace(escape[i], replace[i]);
		}

		return str;
	}
	/**
	 * Dynmapエリアの各コーナーの最大値もしくは最小値を取得する
	 * @param areamarker AreaMarkerを指定
	 * @param XorZ Xならtrue, Zならfalse
	 * @param isMaxMaxならtrue, Minならfalse
	 * @return 最大値もしくは最小値
	 * @author mine_book000
	 */
	private int getMaxOrMin(AreaMarker areamarker, boolean XorZ, boolean isMax){
		int m;
		if(isMax){
			// Maxなので最小値当てる
			m = Integer.MIN_VALUE;
		}else{
			m = Integer.MAX_VALUE;
		}
		for(int o = 0; o < areamarker.getCornerCount(); o++){
			double XorZInt;
			if(XorZ){
				XorZInt = areamarker.getCornerX(o);
			}else{
				XorZInt = areamarker.getCornerZ(o);
			}
			if(isMax){
				if(XorZInt > m){
					m = (int) XorZInt;
				}
			}else{
				if(XorZInt < m){
					m = (int) XorZInt;
				}
			}
		}
		if(DebugMode) System.out.println("XorZ: " + Boolean.toString(XorZ) + " / 最大・最小: " + Boolean.toString(isMax) + " / 数値: " + m);
		return m;
	}

	/**
	 * コーナー通過数を取得
	 * @param areamarker AreaMarkerを指定
	 * @param width X値
	 * @param height Y値
	 * @return
	 */
	int getCorners(AreaMarker areamarker, int width, int height){
		// 現在地 タテ, ヨコ = height, width
		int passingcount = 0; // NubescoX : 通過数
		for(int o = 0; (o + 1) < areamarker.getCornerCount(); o++){
			int x = (int) areamarker.getCornerX(o);
			int z = (int) areamarker.getCornerZ(o);
			int next_x = (int) areamarker.getCornerX(o + 1);
			int next_z = (int) areamarker.getCornerZ(o + 1);
			// タテ判定 とりあえず挟まってればいい
			if(z > width &&  next_z > width){
				// 下なので何もしない
				if(DebugMode) System.out.println("下(v)");
				if(DebugMode) System.out.println("Z: " + z + " / NEXT_Z: " + next_z);
			}else if(z < width &&  next_z < width){
				// 上なので何もしない
				if(DebugMode) System.out.println("上(^)");
			}else if(z == width &&  next_z == width){
				// 水平なので無視
				if(DebugMode) System.out.println("水平(-)");
			}else{
				// 挟まっている
				// ヨコ判定 0,1 1,2 2,3...
				if(x >= height && next_x >= height){
					// 重なっているもしくは右なので足す
					passingcount++;
				}else if(x < height && next_x < height){
					// 左なので何もしない
					if(DebugMode) System.out.println("<-");
				}else{
					// 挟まっている最悪のパータン 今回何もしない
					if(DebugMode) System.out.println("<>");
				}
			}
			if(DebugMode) System.out.println("Now: " + o);
		}
		if(DebugMode) System.out.println("passingcount: " + passingcount);
		return passingcount;
	}
}
