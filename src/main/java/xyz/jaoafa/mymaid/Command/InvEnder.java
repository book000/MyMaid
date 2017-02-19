package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class InvEnder implements CommandExecutor {
	JavaPlugin plugin;
	public InvEnder(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player p = (Player) sender;
		if(args.length == 1){
			for(Player player: Bukkit.getServer().getOnlinePlayers()) {
				if(player.getName().equalsIgnoreCase(args[0])){
					Inventory inv = player.getEnderChest();
					Inventory inventory = Bukkit.getServer().createInventory(null, InventoryType.ENDER_CHEST, player.getName() + "のエンダーチェスト");
					ItemStack[] invdata = inv.getContents();
					for(int n=0; n != invdata.length; n++)
					{
						inventory.setItem(n, inv.getItem(n));
					}
					p.openInventory(inventory);
					return true;
				}
			}
			Method.SendMessage(sender, cmd, "ユーザーが見つかりませんでした。");
			return true;
		}else{
			Method.SendMessage(sender, cmd, "引数が適していません。");
			return true;
		}
	}
}
