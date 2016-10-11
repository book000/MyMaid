package xyz.jaoafa.mymaid.EventHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Command.Prison;

public class OnPlayerCommand implements Listener {
	JavaPlugin plugin;
	public OnPlayerCommand(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e){
    	String command = e.getMessage();
    	Player player = e.getPlayer();
    	String[] args = command.split(" ", 0);
    	if(args.length == 1){
    		if(args[0].equalsIgnoreCase("/god")){
    			ChatColor color = ChatColor.RESET;
				if(player.hasPermission("mymaid.pex.limited")){
					color = ChatColor.BLACK;
    			}else if(Prison.prison.containsKey(player.getName())){
    				color = ChatColor.DARK_GRAY;
    			}else if(MyMaid.chatcolor.containsKey(player.getName())){
    	  			int i = Integer.parseInt(MyMaid.chatcolor.get(player.getName()));
    				if(i >= 0 && i <= 5){
    					color = ChatColor.WHITE;
    				}else if(i >= 6 && i <= 19){
    					color = ChatColor.DARK_BLUE;
    				}else if(i >= 20 && i <= 33){
    					color = ChatColor.BLUE;
    				}else if(i >= 34 && i <= 47){
    					color = ChatColor.AQUA;
    				}else if(i >= 48 && i <= 61){
    					color = ChatColor.DARK_AQUA;
    				}else if(i >= 62 && i <= 76){
    					color = ChatColor.DARK_GREEN;
    				}else if(i >= 77 && i <= 89){
    					color = ChatColor.GREEN;
    				}else if(i >= 90 && i <= 103){
    					color = ChatColor.YELLOW;
    				}else if(i >= 104 && i <= 117){
    					color = ChatColor.GOLD;
    				}else if(i >= 118 && i <= 131){
    					color = ChatColor.RED;
    				}else if(i >= 132 && i <= 145){
    					color = ChatColor.DARK_RED;
    				}else if(i >= 146 && i <= 159){
    					color = ChatColor.DARK_PURPLE;
    				}else if(i >= 160){
    					color = ChatColor.LIGHT_PURPLE;
    				}
    			}else{
    				color = ChatColor.GRAY;
    			}
    			Date Date = new Date();
    			SimpleDateFormat H = new SimpleDateFormat("H");
    			SimpleDateFormat m = new SimpleDateFormat("m");
    			SimpleDateFormat s = new SimpleDateFormat("s");
    			String Hs = H.format(Date);
    			String ms = m.format(Date);
    			String ss = s.format(Date);
    			String date = String.format("%02d", Integer.parseInt(Hs)) + ":" + String.format("%02d", Integer.parseInt(ms)) + ":" + String.format("%02d", Integer.parseInt(ss));
    			String text = "オ、オオwwwwwwwwオレアルファwwwwwwww最近めっちょふぁぼられてんねんオレwwwwwwwwエゴサとかかけるとめっちょ人気やねんwwwwァァァァァァァwwwクソアルファを見下しながら食べるエビフィレオは一段とウメェなァァァァwwwwwwww";
    			OnMyMaidJoinLeftChatCmdLogs.log(plugin, "chat", player.getName(), text);
    			Bukkit.broadcastMessage(ChatColor.GRAY + "["+ date + "]" + color + "■" + ChatColor.WHITE + player.getName() +  ": " + text);
    			e.setCancelled(true);
    		}
    	}else if(args.length == 2){
    		if(args[0].equalsIgnoreCase("/kill")){
        		if(args[1].equalsIgnoreCase("@e")){
        			player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @eはサーバー内のすべてのエンティティが削除されてしまうので使用できません");
        			e.setCancelled(true);
        			return;
        		}
        		if(args[1].equalsIgnoreCase("@a")){
        			player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @aはサーバー内のすべてのプレイヤーが殺害されてしまうので使用できません");
        			e.setCancelled(true);
        			return;
        		}
        		if(args[1].startsWith("@e")){
        			if(player.hasPermission("mymaid.pex.provisional") || player.hasPermission("mymaid.pex.default")){
        				player.sendMessage("[COMMAND] " + ChatColor.GREEN + "└( ・з・)┘");
            			e.setCancelled(true);
            			return;
        			}
        		}
        	}
    		if(args[0].equalsIgnoreCase("/minecraft:kill")){
        		if(args[1].equalsIgnoreCase("@e")){
        			player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @eはサーバー内のすべてのエンティティが削除されてしまうので使用できません");
        			e.setCancelled(true);
        			return;
        		}
        		if(args[1].equalsIgnoreCase("@a")){
        			player.sendMessage("[COMMAND] " + ChatColor.GREEN + "kill @aはサーバー内のすべてのプレイヤーが殺害されてしまうので使用できません");
        			e.setCancelled(true);
        			return;
        		}
        		if(args[1].startsWith("@e")){
        			if(player.hasPermission("mymaid.pex.provisional") || player.hasPermission("mymaid.pex.default")){
        				player.sendMessage("[COMMAND] " + ChatColor.GREEN + "└( ・з・)┘");
            			e.setCancelled(true);
            			return;
        			}
        		}
        	}
    	}
	}
}
