package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import xyz.jaoafa.mymaid.Method;

public class Actionbar implements CommandExecutor {
	JavaPlugin plugin;
	public Actionbar(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 1){
			// 自分
			if (!(sender instanceof Player)) {
				Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				return true;
			}
			Player player = (Player) sender;
			String text = args[0];
			PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
	        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	        Method.SendMessage(sender, cmd, "表示しました。");
	        return true;
		}else if(args.length == 2){
			// 他
			Player player = Bukkit.getPlayerExact(args[0]);
			if(player == null){
				Method.SendMessage(sender, cmd, "指定されたプレイヤーが存在しません。");
				return true;
			}
			String text = args[1];
			PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
	        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	        Method.SendMessage(sender, cmd, "表示しました。");
	        return true;
		}
		Method.SendMessage(sender, cmd, "----- ActionBar -----");
		Method.SendMessage(sender, cmd, "/actionbar <Text>: Textをアクションバーに表示します。");
		Method.SendMessage(sender, cmd, "/actionbar <Player> <Text>: PlayerにTextをアクションバーに表示します。");
		return true;
	}
}
