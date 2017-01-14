package xyz.jaoafa.mymaid.Command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Prison implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public Prison(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String,Boolean> prison = new HashMap<String,Boolean>();
	public static Map<String,Boolean> prison_block = new HashMap<String,Boolean>();
	public static Map<String,String> jail_lasttext = new HashMap<String,String>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 2){

			if(args[0].equalsIgnoreCase("lasttext")){
				if (!(sender instanceof Player)) {
					Method.SendMessage(sender, cmd, "プレイヤーから実行する必要があります。");
				}
				if(!prison.containsKey(sender.getName())){
					Method.SendMessage(sender, cmd, "あなたは牢獄にいません。");
					return true;
				}
				if(jail_lasttext.containsKey(sender.getName())){
					Method.SendMessage(sender, cmd, "すでに残しています。");
					return true;
				}
				String text = "";
				String lasttext = "";
				int c = 1;
				while(args.length > c){
					text += args[c];
					lasttext += args[c];
					if(args.length != (c+1)){
						text+="_";
						lasttext += " ";
					}
					c++;
				}
				Method.url_jaoplugin("eban", "p=" + sender.getName() + "&lasttext=" + text);
				jail_lasttext.put(sender.getName(), lasttext);
				Bukkit.broadcastMessage("[JAIL] " + ChatColor.GREEN + "プレイヤー「" + sender.getName() +"」が遺言を残しました。遺言:「" + lasttext +"」");
				JailBackupSaveTxt(sender.getName(), JailType.LASTTEXT, "", lasttext);
				return true;
			}

		}
		if(args.length == 2){
			if(args[0].equalsIgnoreCase("add")){
				String sendername = sender.getName();
				if (sender instanceof ConsoleCommandSender) {
					if(prison.containsKey(args[1])){
						Method.SendMessage(sender, cmd, "すでに牢獄にいます。");
						return true;
					}
					prison.put(args[1], false);
					prison_block.put(args[1], false);
					for(Player p: Bukkit.getServer().getOnlinePlayers()) {
						if(!p.getName().equalsIgnoreCase(args[1])) {
							Method.SendMessage(p, cmd, "プレイヤー:「" + args[1] + "」を牢獄リストに追加しました。");
						}
					}
					Method.url_jaoplugin("eban", "p="+args[1]+"&u=&b="+sendername+"&r=");
					Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + args[1] + "」を牢獄リストに追加しました。");
					JailBackupSaveTxt(args[1], JailType.ADD, sendername, "");
					return true;
				}

				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					if(player.getName().equalsIgnoreCase(args[1])) {
						if(prison.containsKey(args[1])){
							Method.SendMessage(sender, cmd, "すでに牢獄にいます。");
							return true;
						}
						prison.put(player.getName(), false);
						prison_block.put(player.getName(), false);
						World World = Bukkit.getServer().getWorld("Jao_Afa");
						Location prison = new Location(World, 1767, 70, 1767);
						player.teleport(prison);
						Date Date = new Date();
						SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
						player.sendMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "やあ。" + player.getName() + "クン。どうも君はなにかをして南の楽園に来てしまったみたいなんだ");
						player.sendMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "なにをしてしまったのは知らないけどなにかをしたからここに来たんだと思うんだ。");
						player.sendMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "ちょっとやったことを反省してみるのもいいかもしれないね");
						player.sendMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "あ、そうだ、今の君に人権はないよ。");
						for(Player p: Bukkit.getServer().getOnlinePlayers()) {
							if(!p.getName().equalsIgnoreCase(args[1])) {
								Method.SendMessage(p, cmd, "プレイヤー:「" + player.getName() + "」を牢獄リストに追加しました。");
							}
						}
						Method.url_jaoplugin("eban", "p="+args[1]+"&u="+player.getUniqueId()+"&b="+sendername+"&r=");
						Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を牢獄リストに追加しました。");
						JailBackupSaveTxt(player.getName(), JailType.ADD, sendername, "");
						return true;
					}
				}
				Method.SendMessage(sender, cmd, "「" + args[1] + "」はみつかりません。");
			}else if(args[0].equalsIgnoreCase("remove")){
				if(!prison.containsKey(args[1])){
					Method.SendMessage(sender, cmd, "「" + args[1] + "」は牢獄リストにありません。");
					return true;
				}
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(!p.getName().equalsIgnoreCase(args[1])) {
						Method.SendMessage(p, cmd, "プレイヤー:「" + args[1] + "」を牢獄リストから削除しました。");
					}
				}
				prison.remove(args[1]);
				prison_block.remove(args[1]);
				jail_lasttext.remove(args[1]);
				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					if(player.getName().equalsIgnoreCase(args[1])) {
						Date Date = new Date();
						SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

						player.sendMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "じゃあな");
						Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + args[1] + "」を牢獄リストから削除しました。");
						JailBackupSaveTxt(args[1], JailType.REMOVE, sender.getName(), "");

					}
				}
				return true;
			}else{
				Method.SendMessage(sender, cmd, "第1引数を確認してください");
			}

		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("area")){
				if(args[2].equalsIgnoreCase("true")){
					if (sender instanceof ConsoleCommandSender) {
						if(!prison.containsKey(args[1])){
							Method.SendMessage(sender, cmd, "「" + args[1] + "」は牢獄リストにありません。");
							return true;
						}
						prison.put(args[1], true);
						for(Player p: Bukkit.getServer().getOnlinePlayers()) {
							if(!p.getName().equalsIgnoreCase(args[1])) {
								Method.SendMessage(p, cmd, "プレイヤー:「" + args[1] + "」を範囲外に移動できるよう設定しました。");
							}
						}
						Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" +args[1] + "」を範囲外に移動できるよう設定しました。");
						JailBackupSaveTxt(args[1], JailType.AREATRUE, sender.getName(), "");
						return true;
					}
					for(Player player: Bukkit.getServer().getOnlinePlayers()) {
						if(player.getName().equalsIgnoreCase(args[1])) {
							if(!prison.containsKey(player.getName())){
								Method.SendMessage(sender, cmd, "「" + args[1] + "」は牢獄リストにありません。");
								return true;
							}
							prison.put(player.getName(), true);
							for(Player p: Bukkit.getServer().getOnlinePlayers()) {
								if(!p.getName().equalsIgnoreCase(args[1])) {
									Method.SendMessage(p, cmd, "プレイヤー:「" + player.getName() + "」を範囲外に移動できるよう設定しました。");
								}
							}
							Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を範囲外に移動できるよう設定しました。");
							JailBackupSaveTxt(player.getName(), JailType.AREATRUE, sender.getName(), "");
							return true;
						}
					}
				}else if(args[2].equalsIgnoreCase("false")){
					if (sender instanceof ConsoleCommandSender) {
						if(!prison.containsKey(args[1])){
							Method.SendMessage(sender, cmd, "「" + args[1] + "」は牢獄リストにありません。");
							return true;
						}
						prison.put(args[1], false);
						for(Player p: Bukkit.getServer().getOnlinePlayers()) {
							if(!p.getName().equalsIgnoreCase(args[1])) {
								Method.SendMessage(p, cmd, "プレイヤー:「" + args[1] + "」を範囲外に移動できないよう設定しました。");
							}
						}
						Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + args[1] + "」を範囲外に移動できないよう設定しました。");
						JailBackupSaveTxt(args[1], JailType.AREAFALSE, sender.getName(), "");
						return true;
					}
					for(Player player: Bukkit.getServer().getOnlinePlayers()) {
						if(player.getName().equalsIgnoreCase(args[1])) {
							if(!prison.containsKey(player.getName())){
								Method.SendMessage(sender, cmd, "「" + args[1] + "」は牢獄リストにありません。");
								return true;
							}
							prison.put(player.getName(), false);
							for(Player p: Bukkit.getServer().getOnlinePlayers()) {
								if(!p.getName().equalsIgnoreCase(args[1])) {
									Method.SendMessage(p, cmd, "プレイヤー:「" + player.getName() + "」を範囲外に移動できないよう設定しました。");
								}
							}
							Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を範囲外に移動できないよう設定しました。");
							JailBackupSaveTxt(player.getName(), JailType.AREAFALSE, sender.getName(), "");
							return true;
						}
					}
				}
			}else if(args[0].equalsIgnoreCase("block")){
				if(args[2].equalsIgnoreCase("true")){
					if (sender instanceof ConsoleCommandSender) {
						prison_block.put(args[1], true);
						for(Player p: Bukkit.getServer().getOnlinePlayers()) {
							if(!p.getName().equalsIgnoreCase(args[1])) {
								Method.SendMessage(p, cmd, "プレイヤー:「" + args[1] + "」がブロックを設置破壊できるよう設定しました。");
							}
						}
						Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + args[1] + "」がブロックを設置破壊できるよう設定しました。");
						JailBackupSaveTxt(args[1], JailType.BLOCKTRUE, sender.getName(), "");
						return true;
					}
					for(Player player: Bukkit.getServer().getOnlinePlayers()) {
						if(player.getName().equalsIgnoreCase(args[1])) {
							if(!prison.containsKey(player.getName())){
								Method.SendMessage(sender, cmd, "「" + args[1] + "」は牢獄リストにありません。");
								return true;
							}
							prison_block.put(player.getName(), true);
							for(Player p: Bukkit.getServer().getOnlinePlayers()) {
								if(!p.getName().equalsIgnoreCase(args[1])) {
									Method.SendMessage(p, cmd, "プレイヤー:「" + player.getName() + "」がブロックを設置破壊できるよう設定しました。");
								}
							}
							Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」がブロックを設置破壊できるよう設定しました。");
							JailBackupSaveTxt(player.getName(), JailType.BLOCKTRUE, sender.getName(), "");
							return true;
						}
					}
				}else if(args[2].equalsIgnoreCase("false")){
					if (sender instanceof ConsoleCommandSender) {
						prison_block.put(args[1], false);
						for(Player p: Bukkit.getServer().getOnlinePlayers()) {
							if(!p.getName().equalsIgnoreCase(args[1])) {
								Method.SendMessage(p, cmd, "プレイヤー:「" + args[1] + "」がブロックを設置破壊できないよう設定しました。");
							}
						}
						Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + args[1] + "」がブロックを設置破壊できないよう設定しました。");
						JailBackupSaveTxt(args[1], JailType.BLOCKFALSE, sender.getName(), "");
						return true;
					}
					for(Player player: Bukkit.getServer().getOnlinePlayers()) {
						if(player.getName().equalsIgnoreCase(args[1])) {
							if(!prison.containsKey(player.getName())){
								Method.SendMessage(sender, cmd, "「" + args[1] + "」は牢獄リストにありません。");
								return true;
							}
							prison_block.put(player.getName(), false);
							for(Player p: Bukkit.getServer().getOnlinePlayers()) {
								if(!p.getName().equalsIgnoreCase(args[1])) {
									Method.SendMessage(p, cmd, "プレイヤー:「" + player.getName() + "」がブロックを設置破壊できないよう設定しました。");
								}
							}
							Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」がブロックを設置破壊できないよう設定しました。");
							JailBackupSaveTxt(player.getName(), JailType.BLOCKFALSE, sender.getName(), "");
							return true;
						}
					}
				}
			}else if(args[0].equalsIgnoreCase("add")){
				String sendername = sender.getName();
				if (sender instanceof ConsoleCommandSender) {
					if(prison.containsKey(args[1])){
						Method.SendMessage(sender, cmd, "すでに牢獄にいます。");
						return true;
					}
					prison.put(args[1], false);
					prison_block.put(args[1], false);
					for(Player p: Bukkit.getServer().getOnlinePlayers()) {
						if(!p.getName().equalsIgnoreCase(args[1])) {
							Method.SendMessage(p, cmd, "プレイヤー:「" + args[1] + "」を「" + args[2] + "」という理由で牢獄リストに追加しました。");
						}
					}
					Method.url_jaoplugin("eban", "p="+args[1]+"&u=&b="+sendername+"&r=" + args[2]);
					Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + args[1] + "」を「" + args[2] + "」という理由で牢獄リストに追加しました。");
					JailBackupSaveTxt(args[1], JailType.ADD, sendername, args[2]);
					return true;
				}
				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					if(player.getName().equalsIgnoreCase(args[1])) {
						if(prison.containsKey(args[1])){
							Method.SendMessage(sender, cmd, "すでに牢獄にいます。");
							return true;
						}
						prison.put(player.getName(), false);
						prison_block.put(player.getName(), false);
						World World = Bukkit.getServer().getWorld("Jao_Afa");
						Location prison = new Location(World, 1767, 70, 1767);
						player.teleport(prison);
						Date Date = new Date();
						SimpleDateFormat H = new SimpleDateFormat("H");
						SimpleDateFormat m = new SimpleDateFormat("m");
						SimpleDateFormat s = new SimpleDateFormat("s");
						String Hs = H.format(Date);
						String ms = m.format(Date);
						String ss = s.format(Date);
						String date = String.format("%02d", Integer.parseInt(Hs)) + ":" + String.format("%02d", Integer.parseInt(ms)) + ":" + String.format("%02d", Integer.parseInt(ss));
						player.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "やあ。" + player.getName() + "クン。どうも君はなにかをして南の楽園に来てしまったみたいなんだ");
						player.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "話を聞けば、「" + args[2] + "」という理由でここにきたみたいだね。");
						player.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "なにをしてしまったのか詳しい話は知らないけどさっき言ったような理由でここに来たんだと思うんだ。");
						player.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "ちょっとやったことを反省してみるのもいいかもしれないね");
						player.sendMessage(ChatColor.GRAY + "["+ date + "]" + ChatColor.GOLD + "jaotan" + ChatColor.WHITE +  ": " + "あ、そうだ、今の君に人権はないよ。");
						for(Player p: Bukkit.getServer().getOnlinePlayers()) {
							if(!p.getName().equalsIgnoreCase(args[1])) {
								Method.SendMessage(p, cmd, "プレイヤー:「" + player.getName() + "」を「" + args[2] + "」という理由で牢獄リストに追加しました。");
							}
						}
						Method.url_jaoplugin("eban", "p="+args[1]+"&u="+player.getUniqueId()+"&b="+sendername+"&r="+args[2]);
						Bukkit.getLogger().info("[JAIL] " + ChatColor.GREEN + "プレイヤー:「" + player.getName() + "」を「" + args[2] + "」という理由で牢獄リストに追加しました。");
						JailBackupSaveTxt(player.getName(), JailType.ADD, sendername, args[2]);
						return true;
					}
				}
			}
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("list")){
				Method.SendMessage(sender, cmd, "------ 牢獄リスト ------");
				for(Map.Entry<String, Boolean> data : prison.entrySet()){
					String text;
					if(data.getValue()){
						text = "過度移動許可";
					}else{
						text = "過度移動不許可";
					}
					text += " ";
					if(prison_block.get(data.getKey())){
						text += "設置破壊許可";
					}else{
						text += "設置破壊不許可";
					}
					Method.SendMessage(sender, cmd, data.getKey() + " " + text);
				}
				Method.SendMessage(sender, cmd, "------------------------");
			}else{
				Method.SendMessage(sender, cmd, "引数が足りないか多すぎます。");
			}
		}
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			if (args[0].length() == 0) {
				return Arrays.asList("add", "remove", "list", "area", "block");
			} else {
				//入力されている文字列と先頭一致
				if ("add".startsWith(args[0])) {
					return Collections.singletonList("add");
				} else if ("remove".startsWith(args[0])) {
					return Collections.singletonList("remove");
				} else if ("list".startsWith(args[0])) {
					return Collections.singletonList("list");
				} else if ("area".startsWith(args[0])) {
					return Collections.singletonList("area");
				} else if ("block".startsWith(args[0])) {
					return Collections.singletonList("block");
				}
			}
		}
		//JavaPlugin#onTabComplete()を呼び出す
		return plugin.onTabComplete(sender, command, alias, args);
	}
	public enum JailType {
		ADD("追加"),
		REMOVE("削除"),
		LASTTEXT("遺言"),
		AREATRUE("移動可"),
		AREAFALSE("移動不可"),
		BLOCKTRUE("ブロック設置破壊可"),
		BLOCKFALSE("ブロック設置破壊不可");



		private String name;

	    JailType(String name) {
	        this.name = name;
	    }
	}
	private void JailBackupSaveTxt(String player, JailType type, String by, String reason){
		try{
			File file = new File(plugin.getDataFolder() + File.separator + "jaillog.txt");

			if(file.exists()){
				file.createNewFile();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String text = "["+ sdf.format(new Date()) + "|" + type.name + "] ";
			StringBuffer TextBuf = new StringBuffer();
			TextBuf.append(text);

			if(type == JailType.ADD){
				TextBuf.append(player + "が" + by + "によって追加されました。(理由: " + reason + ")");
			}else if(type == JailType.REMOVE){
				TextBuf.append(player + "が" + by + "によって解除されました。");
			}else if(type == JailType.LASTTEXT){
				TextBuf.append(player + "が遺言を記載しました。(" + reason +")");
			}else if(type == JailType.AREATRUE){
				TextBuf.append(by + "が" + player + "の牢獄外移動を許可しました。");
			}else if(type == JailType.AREAFALSE){
				TextBuf.append(by + "が" + player + "の牢獄外移動を禁止しました。");
			}else if(type == JailType.BLOCKTRUE){
				TextBuf.append(by + "が" + player + "のブロック設置破壊を許可しました。");
			}else if(type == JailType.BLOCKFALSE){
				TextBuf.append(by + "が" + player + "のブロック設置破壊を禁止しました。");
			}

			text = TextBuf.toString();

			FileWriter filewriter = new FileWriter(file, true);

			filewriter.write(text + System.getProperty("line.separator"));

			filewriter.close();
		}catch(IOException e){
			System.out.println(e);
		}
	}
}