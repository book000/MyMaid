package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;
import xyz.jaoafa.mymaid.Method;

public class Cmd_Respawn implements CommandExecutor {
	JavaPlugin plugin;
	public Cmd_Respawn(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 1){
			// Respawn
			Player player = Bukkit.getPlayerExact(args[0]);
			if(player == null){
				Method.SendMessage(sender, cmd, "指定されたプレイヤーは見つかりませんでした。");
				return true;
			}
			if(!player.isOnline()){
				Method.SendMessage(sender, cmd, "指定されたプレイヤーはオンラインではありませんでした。");
				return true;
			}
			Method.SendMessage(sender, cmd, "指定されたプレイヤーをリスポーンさせます…");
			Respawn(player, 1);
			return true;
		}
		Method.SendMessage(sender, cmd, "----- Respawn -----");
		Method.SendMessage(sender, cmd, "/respawn <Player>: 指定したプレイヤーをリスポーンさせます。");
		return true;
	}
	public void Respawn(final Player player, int Time){
		Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {

			@Override
			public void run() {
				((CraftPlayer)player).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
			}
		},Time);
	}
}
