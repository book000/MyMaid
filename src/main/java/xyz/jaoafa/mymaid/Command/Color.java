package xyz.jaoafa.mymaid.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.SKKColors.SKKColors;

public class Color implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public Color(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String,ChatColor> color = new HashMap<String,ChatColor>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
  		if(SKKColors.getPlayerVoteCount(player) < 200){
  			Method.SendMessage(sender, cmd, "投票数が200回を超えていないため、四角色を変更する権限がありません。");
			return true;
  		}
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("show")){
				if(color.get(player.getName()) == null){
					Method.SendMessage(sender, cmd, "デフォルト色のLIGHT_PURPLEに指定されています。");
					return true;
				}else{
					Method.SendMessage(sender, cmd, "「" + color.get(player.getName()) + "■" + ChatColor.GREEN + "」に指定されています。");
					return true;
				}
			}else{
				Method.SendMessage(sender, cmd, "--- Color Help ---");
				Method.SendMessage(sender, cmd, "/color show: 現在指定している四角色を表示します。");
				Method.SendMessage(sender, cmd, "/color set <Color>: 四角色を変更します。");
				return true;
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("set")){
				ChatColor color;
				if(args[1].equalsIgnoreCase("AQUA")){
					color = ChatColor.AQUA;
				}else if(args[1].equalsIgnoreCase("BLACK")){
					color = ChatColor.BLACK;
				}else if(args[1].equalsIgnoreCase("BLUE")){
					color = ChatColor.BLUE;
				}else if(args[1].equalsIgnoreCase("DARK_AQUA")){
					color = ChatColor.DARK_AQUA;
				}else if(args[1].equalsIgnoreCase("DARK_BLUE")){
					color = ChatColor.DARK_BLUE;
				}else if(args[1].equalsIgnoreCase("DARK_GRAY")){
					color = ChatColor.DARK_GRAY;
				}else if(args[1].equalsIgnoreCase("DARK_GREEN")){
					color = ChatColor.DARK_GREEN;
				}else if(args[1].equalsIgnoreCase("DARK_PURPLE")){
					color = ChatColor.DARK_PURPLE;
				}else if(args[1].equalsIgnoreCase("DARK_RED")){
					color = ChatColor.DARK_RED;
				}else if(args[1].equalsIgnoreCase("GOLD")){
					color = ChatColor.GOLD;
				}else if(args[1].equalsIgnoreCase("GREEN")){
					color = ChatColor.GREEN;
				}else if(args[1].equalsIgnoreCase("LIGHT_PURPLE")){
					color = ChatColor.LIGHT_PURPLE;
				}else if(args[1].equalsIgnoreCase("RED")){
					color = ChatColor.RED;
				}else if(args[1].equalsIgnoreCase("WHITE")){
					color = ChatColor.WHITE;
				}else if(args[1].equalsIgnoreCase("YELLOW")){
					color = ChatColor.YELLOW;
				}else if(args[1].equalsIgnoreCase("GRAY")){
					color = ChatColor.GRAY;
				}else{
					Method.SendMessage(sender, cmd, "指定された色は選択できませんでした。");
					return true;
				}
				Color.color.put(player.getName(), color);
				player.setPlayerListName(Color.color.get(player.getName()) + "■" + ChatColor.WHITE + player.getName());
				Method.SendMessage(sender, cmd, "四角色を「" + color + "■" + ChatColor.GREEN + "」に変更しました。");
				return true;
			}else{
				Method.SendMessage(sender, cmd, "--- Color Help ---");
				Method.SendMessage(sender, cmd, "/color show: 現在指定している四角色を表示します。");
				Method.SendMessage(sender, cmd, "/color set <Color>: 四角色を変更します。");
				return true;
			}
		}else{
			Method.SendMessage(sender, cmd, "--- Color Help ---");
			Method.SendMessage(sender, cmd, "/color show: 現在指定している四角色を表示します。");
			Method.SendMessage(sender, cmd, "/color set <Color>: 四角色を変更します。");
			return true;
		}
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
        	if(!args[0].equalsIgnoreCase("set")) return plugin.onTabComplete(sender, command, alias, args);
            if (args[1].length() == 0) {
                return Arrays.asList("AQUA", "BLACK", "BLUE", "DARK_AQUA", "DARK_BLUE", "DARK_GRAY", "DARK_GREEN", "DARK_PURPLE", "DARK_RED", "GOLD", "GREEN", "LIGHT_PURPLE", "RED", "WHITE", "YELLOW", "GRAY");
            } else {
            	String colorstring = args[1];
            	List<String> chatcolor = new ArrayList<String>();
            	if(colorstring.startsWith("AQUA")){
            		chatcolor.add("AQUA");
				}else if(colorstring.startsWith("BLACK")){
					chatcolor.add("BLACK");
				}else if(colorstring.startsWith("BLUE")){
					chatcolor.add("BLUE");
				}else if(colorstring.startsWith("DARK_AQUA")){
					chatcolor.add("DARK_AQUA");
				}else if(colorstring.startsWith("DARK_BLUE")){
					chatcolor.add("DARK_BLUE");
				}else if(colorstring.startsWith("DARK_GRAY")){
					chatcolor.add("DARK_GRAY");
				}else if(colorstring.startsWith("DARK_GREEN")){
					chatcolor.add("DARK_GREEN");
				}else if(colorstring.startsWith("DARK_PURPLE")){
					chatcolor.add("DARK_PURPLE");
				}else if(colorstring.startsWith("DARK_RED")){
					chatcolor.add("DARK_RED");
				}else if(colorstring.startsWith("GOLD")){
					chatcolor.add("GOLD");
				}else if(colorstring.startsWith("GREEN")){
					chatcolor.add("GREEN");
				}else if(colorstring.startsWith("LIGHT_PURPLE")){
					chatcolor.add("LIGHT_PURPLE");
				}else if(colorstring.startsWith("RED")){
					chatcolor.add("RED");
				}else if(colorstring.startsWith("WHITE")){
					chatcolor.add("WHITE");
				}else if(colorstring.startsWith("YELLOW")){
					chatcolor.add("YELLOW");
				}else if(colorstring.startsWith("GRAY")){
					chatcolor.add("GRAY");
				}
                return chatcolor;
            }
        }
      //JavaPlugin#onTabComplete()を呼び出す
        return plugin.onTabComplete(sender, command, alias, args);
	}
}
