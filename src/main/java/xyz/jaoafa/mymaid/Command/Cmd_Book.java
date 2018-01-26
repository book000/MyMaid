package xyz.jaoafa.mymaid.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.BugReport;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.MySQL;
import xyz.jaoafa.mymaid.Pointjao;
import xyz.jaoafa.mymaid.Command.MyMaidBookHistory.MyMaidBookHistoryType;
import xyz.jaoafa.mymaid.Discord.Discord;

public class Cmd_Book implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Book(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
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
				Method.SendMessage(sender, cmd, BugReport.report(e1));
				return true;
			}
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			Method.SendMessage(sender, cmd, BugReport.report(e));
			return true;
		}

		statement = MySQL.check(statement);

		if(args.length == 1){
			if(args[0].equalsIgnoreCase("list")){
				// /book list : 本のリストを表示
				try {
					ResultSet res = statement.executeQuery("SELECT COUNT(*) FROM book WHERE status = 'now'");
					if(!res.next()){
						Method.SendMessage(sender, cmd, "処理に失敗しました。");
						return true;
					}
					int size = res.getInt("COUNT(*)");

					res = statement.executeQuery("SELECT * FROM book WHERE status = 'now'");

					int count = 0;
					int page = 1;
					int startcount = (page - 1) * 10;
					int endcount = page * 10;
					int maxpage = size / 10;
					if(size % 10 != 0) maxpage += 1;

					Method.SendMessage(sender, cmd, "Book List: " + page + "page / " + maxpage + "page");
					Method.SendMessage(sender, cmd, "-------------------------");

					while(res.next()){
						if(count < startcount){
							count++;
							continue;
						}
						if(count > endcount){
							break;
						}

						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");

						MyMaidBookData bookdata;
						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, pages_str, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReport.report(e);
								Method.SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
						Method.SendMessage(sender, cmd, "[" + bookdata.getID() + "] " + bookdata.getTitle() + " - " + bookdata.getAuthorName() + "(" + bookdata.getRequiredjao() + "jao)");
						count++;
					}

					Method.SendMessage(sender, cmd, "-------------------------");
					Method.SendMessage(sender, cmd, startcount + " - " + endcount + " / " + size);
					if(page != maxpage){
						Method.SendMessage(sender, cmd, "次のページを見るには「/book list " + (page + 1) + "」を実行します。");
					}
					return true;
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					Method.SendMessage(sender, cmd, BugReport.report(e));
					return true;
				}
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("get")){
				// /book get <Name|ID>
				if(!(sender instanceof Player)){
					Method.SendMessage(sender, cmd, "このコマンドはサーバ内から実行可能です。");
					return true;
				}
				Player player = (Player) sender;
				try {
					MyMaidBookData bookdata = null;
					if(isNumber(args[1])){
						// ID
						ResultSet res = statement.executeQuery("SELECT * FROM book WHERE id = " + args[1]);
						if(!res.next()){
							Method.SendMessage(sender, cmd, "指定された本は見つかりませんでした。");
							return true;
						}

						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");


						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, pages_str, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReport.report(e);
								Method.SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
					}else{
						PreparedStatement ps = MyMaid.c.prepareStatement("SELECT * FROM book WHERE title = ?");
						ps.setString(1, args[1]);
						ResultSet res = ps.executeQuery();

						if(!res.next()){
							Method.SendMessage(sender, cmd, "指定された本は見つかりませんでした。");
							return true;
						}

						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");

						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, pages_str, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReport.report(e);
								Method.SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
					}
					if(!Pointjao.hasjao(player, bookdata.getRequiredjao())){
						// ポイントが足りない
						Method.SendMessage(sender, cmd, "指定された本を購入するには、" + bookdata.getRequiredjao() + "jaoが必要です。");
						return true;
					}
					Pointjao.usejao(player, bookdata.getRequiredjao(), "本「" + bookdata.getTitle() + "」の購入のため");
					if(bookdata.getAuthor() != null){
						Pointjao.addjao(bookdata.getAuthor(), bookdata.getRequiredjao(), "本「" + bookdata.getTitle() + "」を" + player.getName() + "が購入したため");
					}

					ItemStack is = bookdata.getBook();
					if(player.getInventory().firstEmpty() == -1){
						player.getLocation().getWorld().dropItem(player.getLocation(), is);
						Method.SendMessage(sender, cmd, "購入された本をインベントリに追加しようとしましたが、インベントリが一杯だったのであなたの足元にドロップしました。");
					}else{
						player.getInventory().addItem(is);
						Method.SendMessage(sender, cmd, "購入された本をインベントリに追加しました。");
					}

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					String date = sdf.format(new Date());
					bookdata.addHistory(player, MyMaidBookHistoryType.BUY, date);
					statement.execute("UPDATE book SET history = '" + bookdata.getRawHistory() + "', count = count + 1 WHERE id = " + bookdata.getID() + ";");
					return true;
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					Method.SendMessage(sender, cmd, BugReport.report(e));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("delete")){
				// /book delete <Name|ID>
				if(!(sender instanceof Player)){
					Method.SendMessage(sender, cmd, "このコマンドはサーバ内から実行可能です。");
					return true;
				}
				//Player player = (Player) sender;
				Method.SendMessage(sender, cmd, "未実装です。実装を希望される場合はDiscord#debeloperで希望すると実装が早くなるかもしれません。");
			}else if(args[0].equalsIgnoreCase("history")){
				// /book history <Name|ID>
				try{
					MyMaidBookData bookdata = null;
					if(isNumber(args[1])){
						// ID
						ResultSet res = statement.executeQuery("SELECT * FROM book WHERE id = " + args[1]);

						if(!res.next()){
							Method.SendMessage(sender, cmd, "指定された本は見つかりませんでした。");
							return true;
						}


						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");


						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, status, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReport.report(e);
								Method.SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
					}else{
						PreparedStatement ps = MyMaid.c.prepareStatement("SELECT * FROM book WHERE title = ?");
						ps.setString(1, args[1]);
						ResultSet res = ps.executeQuery();

						if(!res.next()){
							Method.SendMessage(sender, cmd, "指定された本は見つかりませんでした。");
							return true;
						}

						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");

						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, status, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReport.report(e);
								Method.SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
					}
					Method.SendMessage(sender, cmd, "----- 本「" + bookdata.getTitle() + "」のログデータ -----");
					List<MyMaidBookHistory> historys = bookdata.getHistory();
					for(MyMaidBookHistory history : historys){
						Method.SendMessage(sender, cmd, history.getName() + "が" + history.getDate() + "に" + history.getType().getName() + "しました。");
					}
					return true;
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					Method.SendMessage(sender, cmd, BugReport.report(e));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("sell")){
				// /book save <Point>
				if(!(sender instanceof Player)){
					Method.SendMessage(sender, cmd, "このコマンドはサーバ内から実行可能です。");
					return true;
				}
				Player player = (Player) sender;
				if(!isNumber(args[1])){ // 販売に必要なjaoポイント
					Method.SendMessage(sender, cmd, "必要jaoポイント数には数値を指定してください。");
					return true;
				}
				int requiredjao = Integer.parseInt(args[1]);
				try {
					if(player.getItemInHand().getType() == Material.AIR){
						Method.SendMessage(sender, cmd, "アイテムを持っていません。");
						return true;
					}
					Material handtype = player.getItemInHand().getType();
					if(handtype != Material.WRITTEN_BOOK){
						Method.SendMessage(sender, cmd, "このコマンドを使用するには、販売する本を手に持ってください。");
						return true;
					}
					BookMeta book = (BookMeta) player.getItemInHand().getItemMeta();

					String title = book.getTitle();

					ResultSet res = statement.executeQuery("SELECT * FROM book WHERE title = '" + title + "'");
					if(res.next()){
						// 被りを防ぐため、同名の本は販売できない
						Method.SendMessage(sender, cmd, "指定された本の題名と同じ題名の本が発売されています。");
						Method.SendMessage(sender, cmd, "システムの障害を引き起こしたり、無駄な発売のし過ぎを未然に防ぐため、同名の本を複数販売することはできません。");
						return true;
					}

					String pages_data = implode(book.getPages(), "§j");

					String author = player.getName();
					String author_uuid = player.getUniqueId().toString();
					long unixtime = System.currentTimeMillis() / 1000L;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					String date = sdf.format(new Date());
					statement.execute("INSERT INTO book (title, author, author_uuid, data, requiredjao, count, history, status, createdate) VALUES ('" + title + "', '" + author + "', '" + author_uuid + "', '" + pages_data + "', '" + requiredjao + "', '0', '" + author + ",create," + date + "', 'now', '" + unixtime + "');");
					Method.SendMessage(sender, cmd, "指定された本の販売を開始しました。詳しくは/book listをお使いください。");
					return true;
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					Method.SendMessage(sender, cmd, BugReport.report(e));
					return true;
				}
			}else if(args[0].equalsIgnoreCase("list")){
				// /book list <Page> : 本のリストを表示
				try {
					ResultSet res = statement.executeQuery("SELECT COUNT(*) FROM book WHERE status = 'now'");
					if(!res.next()){
						Method.SendMessage(sender, cmd, "処理に失敗しました。");
						return true;
					}
					int size = res.getInt("COUNT(*)");

					res = statement.executeQuery("SELECT * FROM book WHERE status = 'now'");

					if(!isNumber(args[1])){
						Method.SendMessage(sender, cmd, "指定されたページは数値ではありません。");
						return true;
					}

					int count = 0;
					int page = Integer.parseInt(args[1]);
					int startcount = (page - 1) * 10;
					int endcount = page * 10;
					int maxpage = size / 10;
					if(size % 10 != 0) maxpage += 1;

					Method.SendMessage(sender, cmd, "Book List: " + page + "page / " + maxpage + "page");
					Method.SendMessage(sender, cmd, "-------------------------");

					while(res.next()){
						if(count < startcount){
							count++;
							continue;
						}
						if(count > endcount){
							break;
						}

						int id = res.getInt("id");
						String title = res.getString("title");
						String author = res.getString("author");
						String author_uuid = res.getString("author_uuid");
						String pages_str = res.getString("data");
						int requiredjao = res.getInt("requiredjao");
						int book_count = res.getInt("count");
						String history = res.getString("history");
						String status = res.getString("status");
						int createdate = res.getInt("createdate");

						MyMaidBookData bookdata;
						if(author_uuid.equalsIgnoreCase("null") || author_uuid.equalsIgnoreCase("")){
							bookdata = new MyMaidBookData(id, title, author, pages_str, requiredjao, book_count, history, status, createdate);
						}else{
							UUID uuid = UUID.fromString(author_uuid);

							try{
								bookdata = new MyMaidBookData(id, title, uuid, pages_str, requiredjao, book_count, history, status, createdate);
							}catch(IllegalArgumentException e){
								BugReport.report(e);
								Method.SendMessage(sender, cmd, "データの解析に失敗しました。");
								return true;
							}
						}
						Method.SendMessage(sender, cmd, "[" + bookdata.getID() + "] " + bookdata.getTitle() + " - " + bookdata.getAuthorName() + "(" + bookdata.getRequiredjao() + "jao)");
						count++;
					}

					Method.SendMessage(sender, cmd, "-------------------------");
					Method.SendMessage(sender, cmd, startcount + " - " + endcount + " / " + size);
					if(page != maxpage){
						Method.SendMessage(sender, cmd, "次のページを見るには「/book list " + (page + 1) + "」を実行します。");
					}
					return true;
				} catch (SQLException e) {
					// TODO 自動生成された catch ブロック
					Method.SendMessage(sender, cmd, BugReport.report(e));
					return true;
				}
			}
		}
		Method.SendMessage(sender, cmd, "----- Book -----");
		Method.SendMessage(sender, cmd, "/book list: 本リストを表示します。");
		Method.SendMessage(sender, cmd, "/book list <Page>: 指定されたページの本リストを表示します。");
		Method.SendMessage(sender, cmd, "/book sell <jaoPoint>: 手に持っている本を指定したjaoPoint数で販売します。");
		Method.SendMessage(sender, cmd, "/book get <Name|ID>: NameもしくはIDに合う本を購入します。");
		Method.SendMessage(sender, cmd, "/book delete <Name|ID>: NameもしくはIDに合う本を販売終了します。販売者のみ実行できます。");
		Method.SendMessage(sender, cmd, "/book history <Name|ID>: NameもしくはIDに合う本のログリストを表示します。");
		return true;
	}
	boolean isNumber(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	<T> String implode(List<T> list, String glue) {
		StringBuilder sb = new StringBuilder();
		for (T e : list) {
			sb.append(glue).append(e);
		}
		return sb.substring(glue.length());
	}
}
class MyMaidBookData {
	private int id; // 本のID
	private String title; // 本のタイトル
	private OfflinePlayer author = null; // 著者。存在しない場合nullもしくは空文字になるケース
	private String author_name; // 著者名
	private UUID author_uuid = null; // 著者UUID。UUIDが存在しない場合nullになるケースあり
	private List<String> pages = new ArrayList<String>(); // ページデータ。
	private int requiredjao; // 必要jaoポイント数
	private int count; // 購入数
	private String history; // 本のログデータ(購入など)
	private String status; // 本のステータス。販売中/販売終了など
	private Date createdate;
	private int createdate_unixtime;

	MyMaidBookData(int id, String title, String author_name, String pages_str, int requiredjao, int count, String history, String status, int createdate_unixtime){
		this.id = id;
		this.title = title;
		this.author_name = author_name;
		@SuppressWarnings("deprecation")
		OfflinePlayer offplayer = Bukkit.getOfflinePlayer(author_name);
		if(offplayer != null){
			author_uuid = offplayer.getUniqueId();
		}
		pages = Arrays.asList(pages_str.split("§j"));
		this.requiredjao = requiredjao;
		this.history = history;
		this.status = status;
		createdate = new Date(createdate_unixtime);
		this.createdate_unixtime = createdate_unixtime;
	}

	MyMaidBookData(int id, String title, UUID author_uuid, String pages_str, int requiredjao, int count, String history, String status, int createdate_unixtime) throws IllegalArgumentException{
		this.id = id;
		this.title = title;
		this.author_uuid = author_uuid;
		OfflinePlayer offplayer = Bukkit.getOfflinePlayer(author_uuid);
		if(offplayer != null){
			author_name = offplayer.getName();
		}else{
			throw new IllegalArgumentException("データの解析に失敗しました。(This UUID Player is not found. / UUID: " + author_uuid.toString() + ")");
		}
		pages = Arrays.asList(pages_str.split("§j"));
		this.requiredjao = requiredjao;
		this.history = history;
		this.status = status;
		createdate = new Date(createdate_unixtime);
		this.createdate_unixtime = createdate_unixtime;
	}

	int getID(){
		return id;
	}

	String getTitle(){
		return title;
	}

	OfflinePlayer getAuthor(){
		return author;
	}

	String getAuthorName(){
		return author_name;
	}

	UUID getAuthorUUID(){
		return author_uuid;
	}

	List<String> getPages(){
		return pages;
	}

	String getPagesToString(){
		return implode(getPages(), "§j");
	}

	int getRequiredjao(){
		return requiredjao;
	}

	int getCount(){
		return count;
	}

	List<MyMaidBookHistory> getHistory(){
		// 一行ごとに「mine_book000|create|1514732400」など。
		List<String> lines = Arrays.asList(getRawHistory().split("\n"));
		List<MyMaidBookHistory> historyList = new ArrayList<MyMaidBookHistory>();
		for(String line : lines){
			String[] line_data = line.split(",");
			if(line_data.length == 3){
				MyMaidBookHistory mbh = new MyMaidBookHistory(line_data[0], line_data[1], line_data[2]);
				historyList.add(mbh);
			}else{
				// データ形式が不適合
				Discord.send("293856671799967744", ":octagonal_sign:MyMaidのBookコマンドにてMyMaidBookHistoryのデータ形式不適合が発生しました。\nデータ: ```" + line + "```\n行数: " + lines.size() + "行\n列数: " + line_data.length + "列");
				Bukkit.getLogger().warning("MyMaidのBookコマンドにてMyMaidBookHistoryのデータ形式不適合が発生しました。\nデータ: ```" + line + "```\n行数: " + lines.size() + "行\n列数: " + line_data.length + "列");
			}
		}
		return historyList;
	}

	String getRawHistory(){
		return history;
	}

	void addHistory(Player player, MyMaidBookHistoryType type, String date){
		String rawhistory = getRawHistory();
		history = rawhistory + "\n" + player.getName() + "," + type.getRawName() + "," + date;
	}

	String getStatus(){
		return status;
	}

	Date getCreateDate(){
		return createdate;
	}

	int getCreateDateUnixTime(){
		return createdate_unixtime;
	}

	ItemStack getBook(){
		ItemStack item = new ItemStack(Material.WRITTEN_BOOK, 1);
		BookMeta bm = (BookMeta) item.getItemMeta();
		bm.setAuthor(getAuthorName());
		bm.setTitle(getTitle());
		bm.setPages(getPages());
		List<String> lore = new ArrayList<String>();
		lore.add("BookID: " + getID());
		lore.add("必要jaoポイント数: " + getRequiredjao() + "jao");
		bm.setLore(lore);
		item.setItemMeta(bm);
		return item;
	}

	<T> String implode(List<T> list, String glue) {
		StringBuilder sb = new StringBuilder();
		for (T e : list) {
			sb.append(glue).append(e);
		}
		return sb.substring(glue.length());
	}
}
class MyMaidBookHistory {
	private String name;
	private String type;
	private String date;

	MyMaidBookHistory(String name, String type, String date){
		this.name = name;
		this.type = type;
		this.date = date;
	}

	String getName(){
		return name;
	}

	MyMaidBookHistoryType getType(){
		if(type.equalsIgnoreCase("create")){
			return MyMaidBookHistoryType.CREATE;
		}else if(type.equalsIgnoreCase("buy")){
			return MyMaidBookHistoryType.BUY;
		}else if(type.equalsIgnoreCase("end")){
			return MyMaidBookHistoryType.END;
		}else{
			return MyMaidBookHistoryType.UNKNOWN;
		}
	}

	String getRawType(){
		return type;
	}

	String getDate(){
		return date;
	}

	public enum MyMaidBookHistoryType {
		CREATE("発売開始", "create"),
		BUY("購入", "buy"),
		END("発売終了", "end"),
		UNKNOWN("未定義", "unknown");

		private String name;
		private String rawname;
		MyMaidBookHistoryType(String name, String rawname) {
			this.name = name;
			this.rawname = rawname;
		}

		String getName(){
			return name;
		}

		String getRawName(){
			return rawname;
		}
	}
}
