package xyz.jaoafa.mymaid.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Var implements CommandExecutor, TabCompleter {
	JavaPlugin plugin;
	public Var(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String, String> var = new HashMap<String, String>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length < 1){
			Method.SendMessage(sender, cmd, "引数が適切ではありません。");
			return true;
		}
		if(args[0].equalsIgnoreCase("text")){
			//Text(/var text var text)
			if(args.length != 3){
				Method.SendMessage(sender, cmd, "引数が適切ではありません。");
				return true;
			}
			Pattern p = Pattern.compile("^[a-zA-Z0-9_]+$");
	        Matcher m = p.matcher(args[1]);
	        if(!m.find()){
	        	Method.SendMessage(sender, cmd, "変数は英数字記号のみ許可しています。");
	        	return true;
	        }
	        String text = "";
			int c = 2;
			while(args.length > c){
				text += args[c];
				if(args.length != (c+1)){
					text += " ";
				}
				c++;
			}
			var.put(args[1], text);
			Method.SendMessage(sender, cmd, "変数「" + args[1] + "」に「" + args[2] + "」を入力しました。");
			return true;
		}else if(args[0].equalsIgnoreCase("plus")){
			//Plus(/var plus var var)
			if(args.length < 3){
				Method.SendMessage(sender, cmd, "引数が適切ではありません。");
			}
			if(!var.containsKey(args[1])){
				Method.SendMessage(sender, cmd, "変数「" + args[1] + "」は定義されていません。");
				return true;
			}
			if(!var.containsKey(args[2])){
				Method.SendMessage(sender, cmd, "変数「" + args[2] + "」は定義されていません。");
				return true;
			}
			int var1;
			try{
				var1 = Integer.parseInt(var.get(args[1]));
			} catch (NumberFormatException nfe) {
				Method.SendMessage(sender, cmd, "変数「" + args[1] + "」は数字ではないため加算先にできません。");
				return true;
			}
			int var2;
			try{
				var2 = Integer.parseInt(var.get(args[2]));
			} catch (NumberFormatException nfe) {
				Method.SendMessage(sender, cmd, "変数「" + args[2] + "」は数字ではないため加算元にできません。");
				return true;
			}
			int newvar = var1 + var2;
			var.put(args[1], newvar+"");
			Method.SendMessage(sender, cmd, "変数「" + args[1] + "」と変数「" + args[2] + "」を加算し、変数「" + args[1] + "」に入力しました。(回答:" + newvar +")");
			return true;
		}else if(args[0].equalsIgnoreCase("minus")){
			//Plus(/var plus var var)
			if(args.length < 3){
				Method.SendMessage(sender, cmd, "引数が適切ではありません。");
				return true;
			}
			if(!var.containsKey(args[1])){
				Method.SendMessage(sender, cmd, "変数「" + args[1] + "」は定義されていません。");
				return true;
			}
			if(!var.containsKey(args[2])){
				Method.SendMessage(sender, cmd, "変数「" + args[2] + "」は定義されていません。");
				return true;
			}
			int var1;
			try{
				var1 = Integer.parseInt(var.get(args[1]));
			} catch (NumberFormatException nfe) {
				Method.SendMessage(sender, cmd, "変数「" + args[1] + "」は数字ではないため減算元にできません。");
				return true;
			}
			int var2;
			try{
				var2 = Integer.parseInt(var.get(args[2]));
			} catch (NumberFormatException nfe) {
				Method.SendMessage(sender, cmd, "変数「" + args[2] + "」は数字ではないため減算値にできません。");
				return true;
			}
			int newvar = var1 - var2;
			var.put(args[1], newvar+"");
			Method.SendMessage(sender, cmd, "変数「" + args[1] + "」から変数「" + args[2] + "」を減算し、変数「" + args[1] + "」に入力しました。(回答:" + newvar +")");
			return true;
		}else if(args[0].equalsIgnoreCase("block")){
			//Block(/var block var x y z)
			Method.SendMessage(sender, cmd, "未実装");
/*
			if(args.length != 5){
				Method.SendMessage(sender, cmd, "引数が適切ではありません。");
			}
			int x;
			try{
				x = Integer.parseInt(args[2]);
			} catch (NumberFormatException nfe) {
				Method.SendMessage(sender, cmd, "数値を指定してください。(X)");
				return true;
			}

			int y;
			try{
				y = Integer.parseInt(args[3]);
			} catch (NumberFormatException nfe) {
				Method.SendMessage(sender, cmd, "数値を指定してください。(X)");
				return true;
			}

			int z;
			try{
				z = Integer.parseInt(args[4]);
			} catch (NumberFormatException nfe) {
				Method.SendMessage(sender, cmd, "数値を指定してください。(X)");
				return true;
			}
			World world;
			if (sender instanceof Player) {
				world = ((Player) sender).getWorld();
			}else if (sender instanceof BlockCommandSender) {
				CommandBlock cb = (CommandBlock) ((BlockCommandSender)sender).getBlock().getState();
				world = cb.getWorld();
			}else{
				Method.SendMessage(sender, cmd, "プレイヤーかコマンドブロックから実行してください。");
				return true;
			}
			Location cmdb_loc = new Location(world, x, y, z);
			if(cmdb_loc.getBlock().getType() != Material.COMMAND){
				Method.SendMessage(sender, cmd, "指定するブロックはコマンドブロックにしてください。");
				return true;
			}
			CommandBlock cmdb = (CommandBlock) cmdb_loc.getBlock().getState();
			// Todo コマンドブロックの実行結果取得方法
			List<MetadataValue> meta = cmdb.getMetadata("LastOutput");
			Method.SendMessage(sender, cmd, ""+meta);
*/
		}else if(args[0].equalsIgnoreCase("equal")){
			//Equal(/var equal var var)
			if(args.length < 3){
				Method.SendMessage(sender, cmd, "引数が適切ではありません。");
			}
			if(!var.containsKey(args[1])){
				Method.SendMessage(sender, cmd, "変数「" + args[1] + "」は定義されていません。");
				return true;
			}
			if(!var.containsKey(args[2])){
				Method.SendMessage(sender, cmd, "変数「" + args[2] + "」は定義されていません。");
				return true;
			}
			if(var.get(args[1]).equals(var.get(args[2]))){
				Method.SendMessage(sender, cmd, "結果は「True」です。");
				return true;
			}else{
				Method.SendMessage(sender, cmd, "結果は「False」です。");
				return true;
			}
		}else if(args[0].equalsIgnoreCase("list")){
			Method.SendMessage(sender, cmd, "現在定義されている変数");
			Method.SendMessage(sender, cmd, "-------------------------");
			for(Map.Entry<String, String> e : Var.var.entrySet()) {
				Method.SendMessage(sender, cmd, "$" + e.getKey() + "=>" + e.getValue());
			}
			Method.SendMessage(sender, cmd, "-------------------------");
			return true;
		}else{
			Method.SendMessage(sender, cmd, "未実装(?)");
			return true;
		}
		return false;

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            if (args[0].length() == 0) {
                return Arrays.asList("text", "plus", "minus", "block", "equal", "list");
            } else {
                //入力されている文字列と先頭一致
                if ("text".startsWith(args[0])) {
                    return Collections.singletonList("text");
                } else if ("plus".startsWith(args[0])) {
                    return Collections.singletonList("plus");
                } else if ("minus".startsWith(args[0])) {
                    return Collections.singletonList("minus");
                } else if ("block".startsWith(args[0])) {
                    return Collections.singletonList("block");
                } else if ("equal".startsWith(args[0])) {
                    return Collections.singletonList("equal");
                } else if ("list".startsWith(args[0])) {
                    return Collections.singletonList("list");
                }
            }
        }
      //JavaPlugin#onTabComplete()を呼び出す
        return plugin.onTabComplete(sender, command, alias, args);
	}
}
