package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class InvSave implements CommandExecutor {
	JavaPlugin plugin;
	public InvSave(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String,PlayerInventory> Saveinv = new HashMap<String,PlayerInventory>();
	public static Map<String,ItemStack[]> Savearmor = new HashMap<String,ItemStack[]>();
	public static Map<String,Location> Savebed = new HashMap<String,Location>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length != 1){
			Method.SendMessage(sender, cmd, "引数が不正です。");
			return true;
		}
		for(Player player: Bukkit.getServer().getOnlinePlayers()) {
			if(player.getName().equalsIgnoreCase(args[0])){
				PlayerInventory pi = player.getInventory();
				ItemStack[] armor = player.getInventory().getArmorContents();
				Location bed = player.getBedSpawnLocation();

				Saveinv.put(player.getName(), pi);
				Savearmor.put(player.getName(), armor);
				Savebed.put(player.getName(), bed);
				Method.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」を保存しました。");
				return true;
			}
		}
		Method.SendMessage(sender, cmd, "プレイヤーが見つかりません。");
		return true;
	}
}
