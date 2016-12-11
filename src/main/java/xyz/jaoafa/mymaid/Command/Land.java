package xyz.jaoafa.mymaid.Command;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.CuboidRegionSelector;
import com.sk89q.worldedit.regions.RegionSelector;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.MySQL;

@SuppressWarnings("deprecation")
public class Land implements CommandExecutor, Listener {
	JavaPlugin plugin;
	public Land(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		if(!player.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
			Method.SendMessage(sender, cmd, "このコマンドが使用できるワールドではありまぜん。");
			return true;
		}
		if(args.length == 0){
			Statement statement;
			try {
				statement = MyMaid.c.createStatement();
			} catch (NullPointerException e) {
				MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
				try {
					MyMaid.c = MySQL.openConnection();
					statement = MyMaid.c.createStatement();
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
					Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
					Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					return true;
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
				return true;
			}

			try {

				ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + player.getLocation().getBlockX() + " AND y1 >= " + player.getLocation().getBlockY() + " AND z1 >= " + player.getLocation().getBlockZ() + " AND x2 <= " + player.getLocation().getBlockX() + " AND y2 <= " + player.getLocation().getBlockY() + " AND z2 <= " + player.getLocation().getBlockZ() + ";");
				if(!res.next()){
					Method.SendMessage(sender, cmd, "この土地は登録されていません。");
					return true;
				}else{
					int id = res.getInt("id");
					if(!res.getBoolean("isplayerland")){
						Method.SendMessage(sender, cmd, "この土地は土地ID「" + id + "」で登録されていますが、まだ取得されていません。");
					}else{
						Method.SendMessage(sender, cmd, "この土地は土地ID「" + id + "」で登録されています。既に" + res.getString("player") + "が取得しています。");
					}
					return true;
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
				Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			}

		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("new")){
				if(!PermissionsEx.getUser(player).inGroup("Admin")){
					Method.SendMessage(sender, cmd, "このコマンドは管理部のみ使用可能です。");
					return true;
				}
				LocalSession session = WorldEdit.getInstance().getSession(player.getName());
				RegionSelector regionSelector;
				try {
					regionSelector = session.getRegionSelector(session.getSelectionWorld());
				}catch(java.lang.NullPointerException e){
					Method.SendMessage(sender, cmd, "範囲を指定してください。");
					return true;
				}
				// セレクタが立方体セレクタか判定
				if (!(session.getRegionSelector() instanceof CuboidRegionSelector)){
					Method.SendMessage(sender, cmd, "WorldEditの選択範囲を立方体にしてください。");
					return true;
				}else{
					Statement statement;
					try {
						statement = MyMaid.c.createStatement();
					} catch (NullPointerException e) {
						MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
						try {
							MyMaid.c = MySQL.openConnection();
							statement = MyMaid.c.createStatement();
						} catch (ClassNotFoundException | SQLException e1) {
							// TODO 自動生成された catch ブロック
							e1.printStackTrace();
							Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
							Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
							return true;
						}
					} catch (SQLException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
						Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
						Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
						return true;
					}
					try {
						ResultSet res = statement.executeQuery("SELECT * FROM land WHERE uuid = '" + player.getUniqueId() + "';");
						res.next();
						res.next();
						if(res.next()){
							Method.SendMessage(sender, cmd, "あなたは既に土地を3つ取得しています。");
							return true;
						}
						String worldname = regionSelector.getRegion().getWorld().getName();
						if(!worldname.equalsIgnoreCase("ReJao_Afa")){
							Method.SendMessage(sender, cmd, "このコマンドが使用できるワールドではありまぜん。");
							return true;
						}
						//int i = regionSelector.getRegion().getArea();
						int x1 = regionSelector.getRegion().getMinimumPoint().getBlockX();
						int z1 = regionSelector.getRegion().getMinimumPoint().getBlockZ();
						int x2 = regionSelector.getRegion().getMaximumPoint().getBlockX();
						int z2 = regionSelector.getRegion().getMaximumPoint().getBlockZ();

						if(x1 < x2){
							int x2_ = x1;
						    x1 = x2;
						    x2 = x2_;
						}

						int y1 = 255;
						int y2 = 0;

						if(z1 < z2){
							int z2_ = z1;
						    z1 = z2;
						    z2 = z2_;
						}

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

						statement.executeUpdate("INSERT INTO land (`isplayerland`, `x1`, `y1`, `z1`, `x2`, `y2`, `z2`, `createdate`) VALUES ('false', " + x1 + ", " + y1 + ", " + z1 + ", " + x2 + ", " + y2 + ", " + z2 + ", '" + sdf.format(new Date()) + "');");
						Method.SendMessage(sender, cmd, "操作に成功しました。");
						return true;
					} catch (SQLException e) {
						// TODO 自動生成された catch ブロック
						Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
						Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
						e.printStackTrace();
						return true;
					} catch (IncompleteRegionException e){
						Method.SendMessage(sender, cmd, "範囲を2つ指定してください。");
						return true;
					}catch(java.lang.NullPointerException e){
						Method.SendMessage(sender, cmd, "範囲を指定してください。");
						return true;
					}
				}
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("get")){
				int i;
				try{
					i = Integer.parseInt(args[1]);
				}catch (NumberFormatException e){
					Method.SendMessage(sender, cmd, "土地IDは数値で指定してください。");
					return true;
				}
				Statement statement;
				try {
					statement = MyMaid.c.createStatement();
				} catch (NullPointerException e) {
					MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
					try {
						MyMaid.c = MySQL.openConnection();
						statement = MyMaid.c.createStatement();
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
						Method.SendMessage(sender, cmd, "操作に失敗しました。(ClassNotFoundException/SQLException)");
						Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
						return true;
					}
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
					Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					return true;
				}
				try {
					ResultSet res = statement.executeQuery("SELECT * FROM land WHERE id = " + i + ";");
					if(!res.next()){
						Method.SendMessage(sender, cmd, "指定された土地はありません。");
						return true;
					}else{
						int id = res.getInt("id");
						if(res.getBoolean("isplayerland")){
							Method.SendMessage(sender, cmd, "指定された土地は既に" + res.getString("player") + "が取得しています。");
						}
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						statement.executeUpdate("UPDATE land SET player = '" + player.getName() + "', uuid = '" + player.getUniqueId() + "', isplayerland = true, date = '" + sdf.format(new Date()) + "' WHERE id = " + id + ";");
						Method.SendMessage(sender, cmd, "土地を取得しました。");
						return true;
					}
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
					Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					e.printStackTrace();
					return true;
				}
			}
		}
		Method.SendMessage(sender, cmd, "--- Land Help ---");
		Method.SendMessage(sender, cmd, "/land: いま居る場所の土地所有者を確認します。");
		Method.SendMessage(sender, cmd, "/land new: 土地を登録します。(管理部のみ)");
		Method.SendMessage(sender, cmd, "/land get [LandID]: 土地IDの土地を取得します。(1人につき3つの土地が取得可能)");
		return true;
	}
	@EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event){
    	Player player = event.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!player.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
    		return;
    	}
    	if(PermissionsEx.getUser(player).inGroup("Admin")){
    		return;
    	}
    	Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(ClassNotFoundException/SQLException)");
				player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			return;
		}
    	try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getBlock().getLocation().getBlockX() + " AND y1 >= " + event.getBlock().getLocation().getBlockY() + " AND z1 >= " + event.getBlock().getLocation().getBlockZ() + " AND x2 <= " + event.getBlock().getLocation().getBlockX() + " AND y2 <= " + event.getBlock().getLocation().getBlockY() + " AND z2 <= " + event.getBlock().getLocation().getBlockZ() + ";");
			if(res.next()){
				if(res.getBoolean("isplayerland")){
					if(!res.getString("uuid").equalsIgnoreCase(""+player.getUniqueId())){
						player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されており、権限が無いため設置行為をすることはできません。");
						event.setCancelled(true);
						return;
					}
				}
			}else{
				player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いため設置行為をすることはできません。");
				event.setCancelled(true);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			event.setCancelled(true);
			return;
		}
    }
	@EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event){
    	Player player = event.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!player.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
    		return;
    	}
    	if(PermissionsEx.getUser(player).inGroup("Admin")){
    		return;
    	}
    	Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(ClassNotFoundException/SQLException)");
				player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			return;
		}
    	try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getBlock().getLocation().getBlockX() + " AND y1 >= " + event.getBlock().getLocation().getBlockY() + " AND z1 >= " + event.getBlock().getLocation().getBlockZ() + " AND x2 <= " + event.getBlock().getLocation().getBlockX() + " AND y2 <= " + event.getBlock().getLocation().getBlockY() + " AND z2 <= " + event.getBlock().getLocation().getBlockZ() + ";");
			if(res.next()){
				if(res.getBoolean("isplayerland")){
					if(!res.getString("uuid").equalsIgnoreCase(""+player.getUniqueId())){
						player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されており、権限が無いため破壊行為をすることはできません。");
						event.setCancelled(true);
						return;
					}
				}
			}else{
				player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いため破壊行為をすることはできません。");
				event.setCancelled(true);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			event.setCancelled(true);
			return;
		}
    }
	@EventHandler
    public void onBlockIgniteEvent(BlockIgniteEvent event){
		Player player = event.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!player.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
    		return;
    	}
    	if(PermissionsEx.getUser(player).inGroup("Admin")){
    		return;
    	}
    	Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(ClassNotFoundException/SQLException)");
				player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			return;
		}
    	try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getBlock().getLocation().getBlockX() + " AND y1 >= " + event.getBlock().getLocation().getBlockY() + " AND z1 >= " + event.getBlock().getLocation().getBlockZ() + " AND x2 <= " + event.getBlock().getLocation().getBlockX() + " AND y2 <= " + event.getBlock().getLocation().getBlockY() + " AND z2 <= " + event.getBlock().getLocation().getBlockZ() + ";");
			if(res.next()){
				if(res.getBoolean("isplayerland")){
					if(!res.getString("uuid").equalsIgnoreCase(""+player.getUniqueId())){
						player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されており、権限が無いため着火等行為をすることはできません。");
						event.setCancelled(true);
						return;
					}
				}
			}else{
				player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いため着火等行為をすることはできません。");
				event.setCancelled(true);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			event.setCancelled(true);
			return;
		}
    }
	@EventHandler
    public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event){
    	Player player = event.getPlayer();
    	if (!(player instanceof Player)) {
			return;
		}
    	if(!player.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
    		return;
    	}
    	if(PermissionsEx.getUser(player).inGroup("Admin")){
    		return;
    	}
    	Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(ClassNotFoundException/SQLException)");
				player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			return;
		}
    	try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getBlockClicked().getLocation().getBlockX() + " AND y1 >= " + event.getBlockClicked().getLocation().getBlockY() + " AND z1 >= " + event.getBlockClicked().getLocation().getBlockZ() + " AND x2 <= " + event.getBlockClicked().getLocation().getBlockX() + " AND y2 <= " + event.getBlockClicked().getLocation().getBlockY() + " AND z2 <= " + event.getBlockClicked().getLocation().getBlockZ() + ";");
			if(res.next()){
				if(res.getBoolean("isplayerland")){
					if(!res.getString("uuid").equalsIgnoreCase(""+player.getUniqueId())){
						player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されており、権限が無いため着火等行為をすることはできません。");
						event.setCancelled(true);
						return;
					}
				}
			}else{
				player.sendMessage("[LAND] " + ChatColor.GREEN + "この土地は購入されておらず、権限が無いため着火等行為をすることはできません。");
				event.setCancelled(true);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			player.sendMessage("[LAND] " + ChatColor.GREEN + "操作に失敗しました。(SQLException)");
			player.sendMessage("[LAND] " + ChatColor.GREEN + "詳しくはサーバコンソールをご確認ください");
			event.setCancelled(true);
			return;
		}
    }
	@EventHandler(ignoreCancelled = true)
	public void onEntityExplodeEvent(EntityExplodeEvent event){
		for(Block block : event.blockList()){
			if(!block.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
	    		return;
	    	}
			event.setCancelled(true);
		}
	}
	@EventHandler(ignoreCancelled = true)
	public void onBlockFromToEvent(BlockFromToEvent event){
		if(!event.getBlock().getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
    		return;
    	}
		Statement statement;
		try {
			statement = MyMaid.c.createStatement();
		} catch (NullPointerException e) {
			MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
			try {
				MyMaid.c = MySQL.openConnection();
				statement = MyMaid.c.createStatement();
				event.setCancelled(true);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
				event.setCancelled(true);
				return;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return;
		}
		try {
			ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getBlock().getLocation().getBlockX() + " AND y1 >= " + event.getBlock().getLocation().getBlockY() + " AND z1 >= " + event.getBlock().getLocation().getBlockZ() + " AND x2 <= " + event.getBlock().getLocation().getBlockX() + " AND y2 <= " + event.getBlock().getLocation().getBlockY() + " AND z2 <= " + event.getBlock().getLocation().getBlockZ() + ";");
			if(res.next()){
				int resi = res.getInt("id");
				ResultSet res_to = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getToBlock().getLocation().getBlockX() + " AND y1 >= " + event.getToBlock().getLocation().getBlockY() + " AND z1 >= " + event.getToBlock().getLocation().getBlockZ() + " AND x2 <= " + event.getToBlock().getLocation().getBlockX() + " AND y2 <= " + event.getToBlock().getLocation().getBlockY() + " AND z2 <= " + event.getToBlock().getLocation().getBlockZ() + ";");
				if(res_to.next()){
					if(resi != res_to.getInt("id")){
						event.setCancelled(true);
					}
				}else{
					event.setCancelled(true);
				}
			}else{
				event.setCancelled(true);
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler(ignoreCancelled = true)
	public void onBlockPistonExtendEvent(BlockPistonExtendEvent event){
		for(Block block : event.getBlocks()){
			if(!block.getLocation().getWorld().getName().equalsIgnoreCase("ReJao_Afa")){
	    		return;
	    	}
			Statement statement;
			try {
				statement = MyMaid.c.createStatement();
			} catch (NullPointerException e) {
				MySQL MySQL = new MySQL("jaoafa.com", "3306", "jaoafa", MyMaid.sqluser, MyMaid.sqlpassword);
				try {
					MyMaid.c = MySQL.openConnection();
					statement = MyMaid.c.createStatement();
					event.setCancelled(true);
				} catch (ClassNotFoundException | SQLException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
					event.setCancelled(true);
					return;
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				return;
			}
			try {
				ResultSet res = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + event.getBlock().getLocation().getBlockX() + " AND y1 >= " + event.getBlock().getLocation().getBlockY() + " AND z1 >= " + event.getBlock().getLocation().getBlockZ() + " AND x2 <= " + event.getBlock().getLocation().getBlockX() + " AND y2 <= " + event.getBlock().getLocation().getBlockY() + " AND z2 <= " + event.getBlock().getLocation().getBlockZ() + ";");

				if(res.next()){
					int id = res.getInt("id");
					ResultSet res_to = statement.executeQuery("SELECT * FROM land WHERE x1 >= " + block.getLocation().getBlockX() + " AND y1 >= " + block.getLocation().getBlockY() + " AND z1 >= " + block.getLocation().getBlockZ() + " AND x2 <= " + block.getLocation().getBlockX() + " AND y2 <= " + block.getLocation().getBlockY() + " AND z2 <= " + block.getLocation().getBlockZ() + ";");
					if(res_to.next()){
						if(id != res_to.getInt("id")){
							event.setCancelled(true);
						}
					}else{
						event.setCancelled(true);
					}
				}else{
					event.setCancelled(true);
				}
			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				event.setCancelled(true);
				return;
			}
		}
	}
}
