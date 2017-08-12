package xyz.jaoafa.mymaid.EventHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.ParseSelector;
import xyz.jaoafa.mymaid.Discord.Discord;
import xyz.jaoafa.mymaid.SKKColors.SKKColors;

public class OnPlayerCommand implements Listener {
	JavaPlugin plugin;
	public OnPlayerCommand(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event){
		String command = event.getMessage();
		Player player = event.getPlayer();
		String[] args = command.split(" ", 0);
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("/god")){
				ChatColor color = SKKColors.getPlayerSKKChatColor(player);
				Date Date = new Date();
				SimpleDateFormat H = new SimpleDateFormat("H");
				SimpleDateFormat m = new SimpleDateFormat("m");
				SimpleDateFormat s = new SimpleDateFormat("s");
				String Hs = H.format(Date);
				String ms = m.format(Date);
				String ss = s.format(Date);
				String date = String.format("%02d", Integer.parseInt(Hs)) + ":" + String.format("%02d", Integer.parseInt(ms)) + ":" + String.format("%02d", Integer.parseInt(ss));
				String text = "オ、オオwwwwwwwwオレアルファwwwwwwww最近めっちょふぁぼられてんねんオレwwwwwwwwエゴサとかかけるとめっちょ人気やねんwwwwァァァァァァァwwwクソアルファを見下しながら食べるエビフィレオは一段とウメェなァァァァwwwwwwww";
				OnMyMaidJoinLeftChatCmdLogs.log(plugin, "chat", player, text);
				Bukkit.broadcastMessage(ChatColor.GRAY + "["+ date + "]" + color + "■" + ChatColor.WHITE + player.getName() +  ": " + text);
				Discord.send("**" + player.getName() + "**: オ、オオwwwwwwwwオレアルファwwwwwwww最近めっちょふぁぼられてんねんオレwwwwwwwwエゴサとかかけるとめっちょ人気やねんwwwwァァァァァァァwwwクソアルファを見下しながら食べるエビフィレオは一段とウメェなァァァァwwwwwwww");
				event.setCancelled(true);
				return;
			}
		}else if(args.length >= 2){

			List<String> LeastOne = new ArrayList<String>();
			LeastOne.add("r");
			LeastOne.add("type");
			LeastOne.add("team");
			LeastOne.add("name");

			if(args[0].equalsIgnoreCase("/kill")){
				boolean killflag = false;
				if(PermissionsEx.getUser(player).inGroup("Limited")){
					killflag = true;
				}else if(PermissionsEx.getUser(player).inGroup("QPPE")){
					killflag = true;
				}else if(PermissionsEx.getUser(player).inGroup("Default")){
					killflag = true;
				}
				if(killflag){
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(new Date()) + "]" + ChatColor.GOLD + "■" + ChatColor.WHITE + "jaotan: " + player.getName() + "さんが" + args[1] + "を殺すとか調子に乗ってると思うので" + player.getName() + "さんを殺しておきますね^^");
					Discord.send("**jaotan**: " + player.getName() + "さんが" + args[1] + "を殺すとか調子に乗ってると思うので" + player.getName() + "さんを殺しておきますね^^");
					player.setHealth(0);
					event.setCancelled(true);
					return;
				}
				if(args[1].equalsIgnoreCase("@e")){
					player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @eはサーバー内のすべてのエンティティが削除されてしまうので使用できません");
					event.setCancelled(true);
					return;
				}
				if(args[1].equalsIgnoreCase("@a")){
					player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @aはサーバー内のすべてのプレイヤーが殺害されてしまうので使用できません");
					event.setCancelled(true);
					return;
				}
				if(args[1].startsWith("@e")){
					if(player.hasPermission("mymaid.pex.provisional") || player.hasPermission("mymaid.pex.default")){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "└( ・з・)┘");
						event.setCancelled(true);
						return;
					}
				}
				if(args[1].startsWith("@e")){
					try {
						ParseSelector parser = new ParseSelector(args[1]);
						if(!parser.isValidValues()){
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							event.setCancelled(true);
							return;
						}
						if(!parser.getArgs().containsKey("r")){
							Boolean exist = false;
							for(String one : LeastOne){
								if(parser.getArgs().containsKey(one)){
									Bukkit.broadcastMessage(one + ": true");
									exist = true;
								}
							}
							if(!exist){
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」、「type」、「team」、「name」のいずれかを指定せずに実行することはできません。");
								event.setCancelled(true);
								return;
							}
						}
						if(parser.getArgs().containsKey("r")){
							if(Integer.parseInt(parser.getArgs().get("r")) >= 300){
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」に300以上の値を指定することはできません。");
								event.setCancelled(true);
								return;
							}
						}else{
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」を指定せずに実行することはできません。");
							event.setCancelled(true);
							return;
						}
					}catch(IllegalArgumentException e){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: " + e.getMessage());
						event.setCancelled(true);
						return;
					}
				}
				if(args[1].startsWith("@a")){
					try {
						ParseSelector parser = new ParseSelector(args[1]);
						if(!parser.isValidValues()){
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							event.setCancelled(true);
							return;
						}
					}catch(IllegalArgumentException e){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: " + e.getMessage());
						event.setCancelled(true);
						return;
					}
				}
			}
			if(args[0].equalsIgnoreCase("/minecraft:kill")){
				boolean killflag = false;
				if(PermissionsEx.getUser(player).inGroup("Limited")){
					killflag = true;
				}else if(PermissionsEx.getUser(player).inGroup("QPPE")){
					killflag = true;
				}else if(PermissionsEx.getUser(player).inGroup("Default")){
					killflag = true;
				}
				if(killflag){
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(new Date()) + "]" + ChatColor.GOLD + "■" + ChatColor.WHITE + "jaotan: " + player.getName() + "ごときが" + args[1] + "を殺そうだなんて図が高いわ！ " + player.getName() + "が死ね！");
					Discord.send("**jaotan**: " + player.getName() + "ごときが" + args[1] + "を殺そうだなんて図が高いわ！ " + player.getName() + "が死ね！");
					player.setHealth(0);
					event.setCancelled(true);
					return;
				}
				if(args[1].equalsIgnoreCase("@e")){
					player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @eはサーバー内のすべてのエンティティが削除されてしまうので使用できません");
					event.setCancelled(true);
					return;
				}
				if(args[1].equalsIgnoreCase("@a")){
					player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @aはサーバー内のすべてのプレイヤーが殺害されてしまうので使用できません");
					event.setCancelled(true);
					return;
				}
				if(args[1].startsWith("@e")){
					if(player.hasPermission("mymaid.pex.provisional") || player.hasPermission("mymaid.pex.default")){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "└( ・з・)┘");
						event.setCancelled(true);
						return;
					}
				}

				if(args[1].startsWith("@e")){
					try {
						ParseSelector parser = new ParseSelector(args[1]);
						if(!parser.isValidValues()){
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							event.setCancelled(true);
							return;
						}
						if(!parser.getArgs().containsKey("r")){
							Boolean exist = false;
							for(String one : LeastOne){
								if(parser.getArgs().containsKey(one)){
									Bukkit.broadcastMessage(one + ": true");
									exist = true;
								}
							}
							if(!exist){
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」、「type」、「team」、「name」のいずれかを指定せずに実行することはできません。");
								event.setCancelled(true);
								return;
							}
						}
						if(parser.getArgs().containsKey("r")){
							if(Integer.parseInt(parser.getArgs().get("r")) >= 300){
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
								player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」に300以上の値を指定することはできません。");
								event.setCancelled(true);
								return;
							}
						}else{
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: @eセレクターで引数「r」を指定せずに実行することはできません。");
							event.setCancelled(true);
							return;
						}
					}catch(IllegalArgumentException e){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: " + e.getMessage());
						event.setCancelled(true);
						return;
					}
				}
				if(args[1].startsWith("@a")){
					try {
						ParseSelector parser = new ParseSelector(args[1]);
						if(!parser.isValidValues()){
							player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
							event.setCancelled(true);
							return;
						}
					}catch(IllegalArgumentException e){
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "指定されたセレクターは適切でありません。");
						player.sendMessage("[COMMAND] " + ChatColor.GREEN + "理由: " + e.getMessage());
						event.setCancelled(true);
						return;
					}
				}
			}
			if(args[0].equalsIgnoreCase("//calc") || args[0].equalsIgnoreCase("/worldedit:/calc")){
				ChatColor color = SKKColors.getPlayerSKKChatColor(player);
				Date Date = new Date();
				SimpleDateFormat H = new SimpleDateFormat("H");
				SimpleDateFormat m = new SimpleDateFormat("m");
				SimpleDateFormat s = new SimpleDateFormat("s");
				String Hs = H.format(Date);
				String ms = m.format(Date);
				String ss = s.format(Date);
				String date = String.format("%02d", Integer.parseInt(Hs)) + ":" + String.format("%02d", Integer.parseInt(ms)) + ":" + String.format("%02d", Integer.parseInt(ss));
				String text = "オ、オオwwwwwwwwオレアタマ良いwwwwwwww最近めっちょ成績あがってんねんオレwwwwwwwwエゴサとかかけるとめっちょ人気やねんwwwwァァァァァァァwwwクソハゲアタマを見下しながら食べるフライドチキンは一段とウメェなァァァァwwwwwwww";
				OnMyMaidJoinLeftChatCmdLogs.log(plugin, "chat", player.getName(), text);
				Bukkit.broadcastMessage(ChatColor.GRAY + "["+ date + "]" + color + "■" + ChatColor.WHITE + player.getName() +  ": " + text);
				event.setCancelled(true);
				return;
			}
			if(args[0].equalsIgnoreCase("/tp") && args[1].equalsIgnoreCase("jaotan")){
				Bukkit.broadcastMessage(ChatColor.GRAY + "[" + player.getName() + ": " + player.getName() + " は jaotan にワープしました]");
				player.teleport(new Location(Bukkit.getWorld("Jao_Afa"), 0, 77, 0));
			}
		}
	}
}
