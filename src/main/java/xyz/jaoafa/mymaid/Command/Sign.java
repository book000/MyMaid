package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.MyMaid;

public class Sign implements CommandExecutor {
	JavaPlugin plugin;
	public Sign(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String,org.bukkit.block.Sign> signlist = new HashMap<String,org.bukkit.block.Sign>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			sender.sendMessage("[Sign] " + ChatColor.GREEN + "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		if(args.length == 0 || args.length == 1){
			sender.sendMessage("[Sign] " + ChatColor.GREEN + "引数は3つ必要です。");
			return true;
		}
		Player player = (Player) sender;
		org.bukkit.block.Sign sign;
		try {
			sign = signlist.get(player.getName());
		}catch(Exception e){
			sender.sendMessage("[Sign] " + ChatColor.GREEN + "看板が選択されていません。");
			return true;
		}
		int x = sign.getX();
		int y = sign.getY();
		int z = sign.getZ();
		String name = MyMaid.url_access("http://toma.webcrow.jp/jao.php?file=signlock.php&x="+x+"&y="+y+"&z="+z);
		if(!name.equalsIgnoreCase("NOTFOUND")){
			if(!name.equalsIgnoreCase(player.getName())){
				sender.sendMessage("[Sign] " + ChatColor.GREEN + "この看板は" + name + "によってロックされています。");
				return true;
			}
		}
		if(args[0].equalsIgnoreCase("set")){
			String regex = "^[1-4]$"; //正規表現
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(args[1]);
			if (!m.find()){
				sender.sendMessage("[Sign] " + ChatColor.GREEN + "エラーが発生しました。看板の行番号:1～4を入力してください。");
				return true;
			}

			String text = "";
			int c = 2;
			while(args.length > c){
				text += args[c]+" ";
				c++;
			}
	        sign.setLine(Integer.parseInt(args[1])-1, text);
	        sign.update();
	        sender.sendMessage("[Sign] " + ChatColor.GREEN + "書き換えを行いました。");
		}else{
			sender.sendMessage("[Sign] " + ChatColor.GREEN + "setを指定してください");
			return true;
		}
		return true;

	}
}
