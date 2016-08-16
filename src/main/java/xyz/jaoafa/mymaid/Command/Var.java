package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Var {
	JavaPlugin plugin;
	public Var(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String, String> var = new HashMap<String, String>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length < 2){
			Method.SendMessage(sender, cmd, "引数が適切ではありません。");
		}
		if(args[0].equalsIgnoreCase("text")){
			//Text(/var text var text)
			if(args.length != 3){
				Method.SendMessage(sender, cmd, "引数が適切ではありません。");
			}
			Pattern p = Pattern.compile("^[0-9a-zA-Z]+$");
	        Matcher m = p.matcher(args[1]);
	        if(!m.find()){
	        	Method.SendMessage(sender, cmd, "変数は英数字のみ許可しています。");
	        	return true;
	        }
			var.put(args[1], args[2]);
			Method.SendMessage(sender, cmd, "変数「" + args[1] + "」に「" + args[2] + "」を入力しました。");
			return true;
		}else if(args[0].equalsIgnoreCase("block")){
			//Block(/var block var x y z)
			Method.SendMessage(sender, cmd, "未実装");
			return true;
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

			*/
		}
		Method.SendMessage(sender, cmd, "未実装(?)");
		return true;
	}
}
