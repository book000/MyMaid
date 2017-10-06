package xyz.jaoafa.mymaid.Command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Rider implements CommandExecutor {
	JavaPlugin plugin;
	public Rider(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		if(args.length == 1){
			Player p = Bukkit.getPlayerExact(args[0]);
			if(p != null){
				if(player.getUniqueId().toString().equals(p.getUniqueId().toString())){
					Method.SendMessage(sender, cmd, "処理できません。");
					return true;
				}
				Boolean bool = p.setPassenger(player);
				if(bool){
					Method.SendMessage(sender, cmd, "プレイヤー「" + p.getName() + "」に乗りました。");
				}else{
					Method.SendMessage(sender, cmd, "プレイヤー「" + p.getName() + "」に乗れませんでした。");
				}
			}else{
				Entity e = null;
				List<Entity> NearEntitys = player.getNearbyEntities(5.0, 5.0, 5.0);
				for(Entity near : NearEntitys){
					if(!(near instanceof LivingEntity)){
						continue;
					}
					if(near.getType() == EntityType.PLAYER){
						continue;
					}
					if(near.getName().equalsIgnoreCase(args[0])){
						e = near;
						break;
					}
				}
				if(e == null){
					Method.SendMessage(sender, cmd, "指定されたプレイヤー・エンティティが見つかりませんでした。");
					return true;
				}
				Boolean bool = e.setPassenger(player);
				if(bool){
					Method.SendMessage(sender, cmd, "エンティティ「" + e.getName() + "」に乗りました。");
				}else{
					Method.SendMessage(sender, cmd, "エンティティ「" + e.getName() + "」に乗れませんでした。");
				}
			}
			return true;
		}

		Method.SendMessage(sender, cmd, "----- Rider -----");
		Method.SendMessage(sender, cmd, "/rider <Player>: プレイヤーに乗ります");
		Method.SendMessage(sender, cmd, "/rider <EntityName>: 1辺10マスの立方体内にある指定された名前のエンティティに乗ります");
		return true;
	}
}
