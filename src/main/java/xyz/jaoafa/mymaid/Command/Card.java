package xyz.jaoafa.mymaid.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Card implements CommandExecutor {
	JavaPlugin plugin;
	public Card(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Method.SendMessage(sender, cmd, "このコマンドは話し合いにより廃止されました。");
		return true;
		/*
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:MM:dd");
		long now = System.currentTimeMillis() / 1000L;

		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

		cal.set(Calendar.HOUR_OF_DAY, 10);
		long today10 = cal.getTimeInMillis() / 1000L;

		cal.set(Calendar.HOUR_OF_DAY, 12);
		long today12 = cal.getTimeInMillis() / 1000L;

		cal.set(Calendar.HOUR_OF_DAY, 18);
		long today18 = cal.getTimeInMillis() / 1000L;

		cal.set(Calendar.HOUR_OF_DAY, 22);
		long today22 = cal.getTimeInMillis() / 1000L;

		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.HOUR_OF_DAY, 22);
		long yestday22 = cal.getTimeInMillis() / 1000L;

		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 10);
		//long tomorrow10 = cal.getTimeInMillis() / 1000L;

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
				Method.SendMessage(sender, cmd, BugReport.report(e1));
				return true;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			Method.SendMessage(sender, cmd, BugReport.report(e));
			return true;
		}

		int flag = 5;

		if(yestday22 <= now && now < today10){
			// 昨日22時～今日10時まで
			flag = 1;
		}

		if(today10 <= now && now < today12){
			// 今日10時～今日12時まで
			flag = 2;
		}

		if(today12 <= now && now < today18){
			// 今日12時～今日18時まで
			flag = 3;
		}

		if(today18 <= now && now < today22){
			// 今日18時～今日22時まで
			flag = 4;
		}


		//Bukkit.broadcastMessage(""+flag);

		statement = MySQL.check(statement);
		UUID uuid = player.getUniqueId();

		if(player.getInventory().firstEmpty() == -1){
			Method.SendMessage(sender, cmd, "インベントリに空きがないため、配布できません。");
			return true;
		}

		ItemStack is = new ItemStack(Material.PAPER);
		is.setAmount(1);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName("ガチャ券");
		ArrayList<String> lore = new ArrayList<String>(){
			// 「空のブロックにはコードまたはコメントを記述する必要があります」
		};
		lore.add("jaoカード場でガチャができるぞ！");
		meta.setLore(lore);
		is.setItemMeta(meta);

		try {
			ResultSet res = statement.executeQuery("SELECT * FROM card WHERE uuid = '" + uuid.toString() +"'");
			if(res.next()){

				String lasttime = res.getString("lasttime");
				Long last = Long.parseLong(lasttime);
				if(flag == 1){
					if(yestday22 >= last){
						// 取得可能
						Method.SendMessage(sender, cmd, "「22時～10時」のカードを配布しました。");
						Method.SendMessage(sender, cmd, "次は「10時～12時」です。");
						player.getInventory().addItem(is);
					}else{
						// 取得済み
						Method.SendMessage(sender, cmd, "既に「22時～10時」のカードを配布済みです！");
						Method.SendMessage(sender, cmd, "次は「10時～12時」です。");
						return true;
					}
				}else if(flag == 2){
					if(today10 >= last){
						// 取得可能
						Method.SendMessage(sender, cmd, "「10時～12時」のカードを配布しました。");
						Method.SendMessage(sender, cmd, "次は「12時～18時」です。");
						player.getInventory().addItem(is);
					}else{
						// 取得済み
						Method.SendMessage(sender, cmd, "既に「10時～12時」のカードを配布済みです！");
						Method.SendMessage(sender, cmd, "次は「12時～18時」です。");
						return true;
					}
				}else if(flag == 3){
					if(today12 >= last){
						// 取得可能
						Method.SendMessage(sender, cmd, "「12時～18時」のカードを配布しました。");
						Method.SendMessage(sender, cmd, "次は「18時～22時」です。");
						player.getInventory().addItem(is);
					}else{
						// 取得済み
						Method.SendMessage(sender, cmd, "既に「12時～18時」のカードを配布済みです！");
						Method.SendMessage(sender, cmd, "次は「18時～22時」です。");
						return true;
					}
				}else if(flag == 4){
					if(today18 >= last){
						// 取得可能
						Method.SendMessage(sender, cmd, "「18時～22時」のカードを配布しました。");
						Method.SendMessage(sender, cmd, "次は「22時～10時」です。");
						player.getInventory().addItem(is);
					}else{
						// 取得済み
						Method.SendMessage(sender, cmd, "既に「18時～22時」のカードを配布済みです！");
						Method.SendMessage(sender, cmd, "次は「22時～10時」です。");
						return true;
					}
				}else if(flag == 5){
					if(today22 >= last){
						// 取得可能
						Method.SendMessage(sender, cmd, "「22時～10時」のカードを配布しました。");
						Method.SendMessage(sender, cmd, "次は「22時～10時」です。");
						player.getInventory().addItem(is);
					}else{
						// 取得済み
						Method.SendMessage(sender, cmd, "既に「22時～10時」のカードを配布済みです！");
						Method.SendMessage(sender, cmd, "次は「10時～12時」です。");
						return true;
					}
				}
				player.updateInventory();
				statement.executeUpdate("UPDATE card SET last = '" + sdf.format(new Date()) + "', lasttime = '" + now + "' WHERE uuid = '" + player.getUniqueId().toString() + "'");
			}else{
				if(flag == 1){
					// 取得可能
					Method.SendMessage(sender, cmd, "「22時～10時」のカードを配布しました。");
					Method.SendMessage(sender, cmd, "次は「10時～12時」です。");
					player.getInventory().addItem(is);
				}else if(flag == 2){
					// 取得可能
					Method.SendMessage(sender, cmd, "「10時～12時」のカードを配布しました。");
					Method.SendMessage(sender, cmd, "次は「12時～18時」です。");
					player.getInventory().addItem(is);
				}else if(flag == 3){
					// 取得可能
					Method.SendMessage(sender, cmd, "「12時～18時」のカードを配布しました。");
					Method.SendMessage(sender, cmd, "次は「18時～22時」です。");
					player.getInventory().addItem(is);
				}else if(flag == 4){
					// 取得可能
					Method.SendMessage(sender, cmd, "「18時～22時」のカードを配布しました。");
					Method.SendMessage(sender, cmd, "次は「22時～10時」です。");
					player.getInventory().addItem(is);
				}else if(flag == 5){
					// 取得可能
					Method.SendMessage(sender, cmd, "「22時～10時」のカードを配布しました。");
					Method.SendMessage(sender, cmd, "次は「22時～10時」です。");
					player.getInventory().addItem(is);
				}
				player.updateInventory();

				statement.executeUpdate("INSERT INTO card (player, uuid, last, lasttime) VALUES ('" + player.getName() + "', '" + player.getUniqueId().toString() + "', '" + sdf.format(new Date()) + "', '" + now + "');");
				return true;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			Method.SendMessage(sender, cmd, "操作に失敗しました。(SQLException)");
			Method.SendMessage(sender, cmd, "詳しくはサーバコンソールをご確認ください");
			return true;
		}
		return false;
		*/
	}
}
