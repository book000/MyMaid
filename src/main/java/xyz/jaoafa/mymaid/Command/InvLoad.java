package xyz.jaoafa.mymaid.Command;

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

public class InvLoad implements CommandExecutor {
	JavaPlugin plugin;
	public InvLoad(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length != 1){
			Method.SendMessage(sender, cmd, "引数が不正です。");
			return true;
		}
		for(Player player: Bukkit.getServer().getOnlinePlayers()) {
			if(player.getName().equalsIgnoreCase(args[0])){
				if(!InvSave.Saveinv.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」を復旧できません。");
					return true;
				}
				PlayerInventory pi = InvSave.Saveinv.get(player.getName());
				ItemStack[] armor = InvSave.Savearmor.get(player.getName());
				Location bed = InvSave.Savebed.get(player.getName());

				player.getInventory().setContents(pi.getContents());
				player.getInventory().setArmorContents(armor);
				player.setBedSpawnLocation(bed, true);
				player.updateInventory();

				Method.SendMessage(sender, cmd, "プレイヤー「" + player.getName() + "」を復旧しました。");
				return true;
			}
		}
		Method.SendMessage(sender, cmd, "プレイヤーが見つかりません。");
		return true;
	}
}
