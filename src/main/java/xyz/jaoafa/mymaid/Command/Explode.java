package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Explode implements CommandExecutor {
	JavaPlugin plugin;
	public Explode(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<Location,Integer> explode = new HashMap<Location,Integer>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			sender.sendMessage("[Explode] " + ChatColor.GREEN + "----- Explodeoff List -----");
			sender.sendMessage("[Explode] " + ChatColor.GREEN + "World X Y Z R");
			for(Map.Entry<Location, Integer> e : explode.entrySet()) {
				Location location = e.getKey();
				Integer r = e.getValue();
				String world = location.getWorld().getName();
				int x = location.getBlockX();
				int y = location.getBlockY();
				int z = location.getBlockZ();

				sender.sendMessage("[Explode] " + ChatColor.GREEN + world + " " + x + " " + y + " " + z + " " + r);
			}
			sender.sendMessage("[Explode] " + ChatColor.GREEN + "World X Y Z R");
			sender.sendMessage("[Explode] " + ChatColor.GREEN + "---------------------------");
			return true;
		}
		if(args.length > 5 && args.length < 4){
			sender.sendMessage("[Explode] " + ChatColor.GREEN + "引数が多すぎるか足りません");
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage("[Explode] " + ChatColor.GREEN + "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;

		int x;
		if(args[0].equalsIgnoreCase("~")){
			x = player.getLocation().getBlockX();
		}else{
			try{
				x = Integer.parseInt(args[0]);
			} catch (NumberFormatException nfe) {
				sender.sendMessage("[Explode] " + ChatColor.GREEN + "数値を指定してください。(X)");
				return true;
			}
		}
		int y;
		if(args[1].equalsIgnoreCase("~")){
			y = player.getLocation().getBlockY();
		}else{
			try{
				y = Integer.parseInt(args[1]);
			} catch (NumberFormatException nfe) {
				sender.sendMessage("[Explode] " + ChatColor.GREEN + "数値を指定してください。(Y)");
				return true;
			}
		}
		int z;
		if(args[2].equalsIgnoreCase("~")){
			z = player.getLocation().getBlockZ();
		}else{
			try{
				z = Integer.parseInt(args[2]);
			} catch (NumberFormatException nfe) {
				sender.sendMessage("[Explode] " + ChatColor.GREEN + "数値を指定してください。(Z)");
				return true;
			}
		}
		int r = 0;
		if(args.length == 5){
			try{
				r = Integer.parseInt(args[3]);
			} catch (NumberFormatException nfe) {
				sender.sendMessage("[Explode] " + ChatColor.GREEN + "数値を指定してください。(R)");
				return true;
			}
		}
		String onoff = "off";
		if(args.length == 5){
			onoff = args[4];
		}else if(args.length == 4){
			onoff = "off";
		}

		World World = player.getWorld();
		Location tnt = new Location(World, x, y, z);
		if(onoff.equalsIgnoreCase("on")){
			explode.put(tnt, r);
			sender.sendMessage("[Explode] " + ChatColor.GREEN + "爆発を無効化しました。");
			sender.sendMessage("[Explode] " + ChatColor.GREEN + "World:「" + World.getName() + "」");
			sender.sendMessage("[Explode] " + ChatColor.GREEN + "XYZ:「" + x + " " + y + " " + z + "」");
			sender.sendMessage("[Explode] " + ChatColor.GREEN + "R(範囲):「" + r + "block」");
			return true;
		}else if(onoff.equalsIgnoreCase("off")){
			if(explode.containsKey(tnt)){
				explode.remove(tnt);
				sender.sendMessage("[Explode] " + ChatColor.GREEN + "爆発を有効化しました。");
				sender.sendMessage("[Explode] " + ChatColor.GREEN + "World:「" + World.getName() + "」");
				sender.sendMessage("[Explode] " + ChatColor.GREEN + "XYZ:「" + x + " " + y + " " + z + "」");
				sender.sendMessage("[Explode] " + ChatColor.GREEN + "R(範囲):「" + r + "block」");
			}else{
				sender.sendMessage("[Explode] " + ChatColor.GREEN + "爆発を有効化できませんでした。既に有効化されています。");
			}
		}else{
			sender.sendMessage("[Explode] " + ChatColor.GREEN + "onもしくはoffを指定してください。");
			return true;
		}
		return true;
	}
}
