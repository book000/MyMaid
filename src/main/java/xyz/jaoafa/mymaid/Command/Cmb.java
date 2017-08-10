package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Cmb implements Listener, CommandExecutor {
	JavaPlugin plugin;
	public Cmb(JavaPlugin plugin){
		this.plugin = plugin;
	}
/*
	private static boolean UseCheckCommand(String command){
		List<String> commands = new ArrayList<String>();
		commands.add("tellraw");
		commands.add("tp");
		commands.add("spawnpoint");
		commands.add("spreadplayers");
		commands.add("clear");
		commands.add("gamemode");
		commands.add("msg");
		commands.add("testfor");
		commands.add("testforblock");
		commands.add("testforblocks");
		commands.add("title");
		commands.add("invsave");
		commands.add("invload");
		commands.add("dt");
		commands.add("effect");
		commands.add("give");
		commands.add("me");
		commands.add("playsound");
		commands.add("say");
		commands.add("scoreboard");
		commands.add("tell");
		commands.add("time");
		if(commands.contains(command)){
			return true;
		}else{
			return false;
		}
	}
*/
	public static Map<String, Location> SelectCMB = new HashMap<String, Location>();

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Method.SendMessage(sender, cmd, "このコマンドは一時的にサポートを停止しています。");
		return true;
		/*
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;

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
			Method.SendMessage(sender, cmd, BugReport.report(e));
			return true;
		}

		statement = MySQL.check(statement);

		if(args.length == 0){
			if(!SelectCMB.containsKey(player.getName())){
				Method.SendMessage(sender, cmd, "選択しているコマンドブロックが見つかりません。");
				return true;
			}
			Location loc = SelectCMB.get(player.getName());

			Block block = loc.getWorld().getBlockAt(loc);

			if(block.getType() != Material.COMMAND){
				Method.SendMessage(sender, cmd, "選択しているブロックはコマンドブロックではないようです。");
				return true;
			}

			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();

			Method.SendMessage(sender, cmd, "選択しているコマンドブロック: ");
			Method.SendMessage(sender, cmd, x + " " + y + " " + z);
			return true;
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("i")){
				if(!SelectCMB.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "選択しているコマンドブロックが見つかりません。");
					return true;
				}
				Location loc = SelectCMB.get(player.getName());

				Block block = loc.getWorld().getBlockAt(loc);

				if(block.getType() != Material.COMMAND){
					Method.SendMessage(sender, cmd, "選択しているブロックはコマンドブロックではないようです。");
					return true;
				}

				try {
					ResultSet res = statement.executeQuery("SELECT * FROM cmb_history WHERE world = '" + loc.getWorld().getName() + "' AND x = " + loc.getBlockX() + " AND y = " + loc.getBlockY() + " AND z = " + loc.getBlockZ() + " LIMIT 0, 5;");
					Method.SendMessage(sender, cmd, "----- CommandBlock(" + loc.getWorld().getName() + " - " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + ") -----");
					while(res.next()){
						if(res.getString("type").equalsIgnoreCase("setcommand")){
							Method.SendMessage(sender, cmd, res.getString("player") + " setcommand \"" + res.getString("new_command") + "\"(OLD: " + res.getString("old_command") + ")");
						}else if(res.getString("type").equalsIgnoreCase("run")){
							Method.SendMessage(sender, cmd, "runned \"" + res.getString("new_command") + "\"");
						}
					}
					return true;
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					Method.SendMessage(sender, cmd, BugReport.report(e));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("clear")){
				if(!SelectCMB.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "選択しているコマンドブロックが見つかりません。");
					return true;
				}
				SelectCMB.remove(player.getName());
				Method.SendMessage(sender, cmd, "コマンドブロックの選択を解除しました。");
				return true;
			}
		}else if(args.length >= 2){
			if(args[0].equalsIgnoreCase("i")){
				if(!SelectCMB.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "選択しているコマンドブロックが見つかりません。");
					return true;
				}

				Location loc = SelectCMB.get(player.getName());

				Block block = loc.getWorld().getBlockAt(loc);

				if(block.getType() != Material.COMMAND){
					Method.SendMessage(sender, cmd, "選択しているブロックはコマンドブロックではないようです。");
					return true;
				}

				int page = Integer.parseInt(args[1]);
				int min = (page * 5) - 5;

				try {
					ResultSet res = statement.executeQuery("SELECT * FROM cmb_history WHERE world = '" + loc.getWorld().getName() + "' AND x = " + loc.getBlockX() + " AND y = " + loc.getBlockY() + " AND z = " + loc.getBlockZ() + " LIMIT " + min + ", 5;");
					Method.SendMessage(sender, cmd, "----- CommandBlock(" + loc.getWorld().getName() + " - " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + ") -----");
					while(res.next()){
						if(res.getString("type").equalsIgnoreCase("setcommand")){
							Method.SendMessage(sender, cmd, res.getString("player") + " setcommand \"" + res.getString("new_command") + "\"(OLD: " + res.getString("old_command") + ")");
						}else if(res.getString("type").equalsIgnoreCase("run")){
							Method.SendMessage(sender, cmd, "runned \"" + res.getString("new_command") + "\"");
						}
					}
					return true;
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
					Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					return true;
				}
			}else if(args[0].equalsIgnoreCase("set")){
				if(!SelectCMB.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "選択しているコマンドブロックが見つかりません。");
					return true;
				}

				Location loc = SelectCMB.get(player.getName());

				Block block = loc.getWorld().getBlockAt(loc);

				if(block.getType() != Material.COMMAND){
					Method.SendMessage(sender, cmd, "選択しているブロックはコマンドブロックではないようです。");
					return true;
				}
				CommandBlock cb = (CommandBlock) block.getState();
				String old_command = cb.getCommand();

				String command = "";
				int c = 1;
				while(args.length > c){
					command += args[c];
					if(args.length != (c+1)){
						command += " ";
					}
					c++;
				}

				String noslashcommand = StringUtils.stripStart(args[1], "/");
				if(!UseCheckCommand(noslashcommand)){
					Method.SendMessage(sender, cmd, "操作に失敗しました。");
					return true;
				}

				try {
					statement.executeUpdate("INSERT INTO cmb_history (id, player, uuid, world, x, y, z, type, old_command, new_command, created_at) VALUES (NULL, '" + player.getName() + "', '" + player.getUniqueId().toString() + "', '" + loc.getWorld().getName() + "', " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ", 'setcommand', '" + old_command + "', '" + command + "', '20127454785174');");
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
					Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
					return true;
				}

				cb.setCommand(command);
				cb.update();
				Method.SendMessage(sender, cmd, "操作に成功しました。");
				return true;
			}
		}
		Method.SendMessage(sender, cmd, "----- CMB Help -----");
		Method.SendMessage(sender, cmd, "/cmb: 選択したコマンドブロックの情報を表示します");
		Method.SendMessage(sender, cmd, "/cmb i: /co iと同じようにクリックしたコマンドブロックのログを表示します");
		Method.SendMessage(sender, cmd, "/cmb clear: コマンドブロックの選択を解除します");
		Method.SendMessage(sender, cmd, "/cmb set <Command>: Commandを選択したコマンドブロックに書き込みます");
		return true;

		*/
	}
	/*
	@EventHandler
	public void onCMBClick(PlayerInteractEvent event) {
	    if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
	    if(event.getPlayer().getItemInHand().getType() != Material.BLAZE_ROD) return;
	    Block block = event.getClickedBlock();
	    Material material = block.getType();
	    if (material == Material.COMMAND) {
	        Player player = event.getPlayer();
	        SelectCMB.put(player.getName(), block.getLocation());
	        player.sendMessage("[CMB] " + ChatColor.GREEN + "コマンドブロックを選択しました。書き込むには/cmbコマンドを使用してください。");
	        event.setCancelled(true);
	    }
	}
	*/
}
