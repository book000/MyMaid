package xyz.jaoafa.mymaid.Command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class Rider implements CommandExecutor {
	JavaPlugin plugin;
	public Rider(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 1){
			if (!(sender instanceof Player)) {
				Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
				return true;
			}
			Player player = (Player) sender;

			if(args[0].equalsIgnoreCase("leave")){
				if(player.getPassenger() == null){
					Method.SendMessage(sender, cmd, "あなたには誰も乗っていません。");
					return true;
				}
				String rider = player.getPassenger().getName();

				if(player.setPassenger(null)){
					Method.SendMessage(sender, cmd, "プレイヤー・エンティティ「" + rider + "」を下ろしました。");
				}else{
					if(player.getPassenger().getVehicle().eject()){
						Method.SendMessage(sender, cmd, "プレイヤー・エンティティ「" + rider + "」を下ろしました。");
					}else{
						Method.SendMessage(sender, cmd, "プレイヤー・エンティティ「" + rider + "」を下ろせませんでした。");
					}
				}
				return true;
			}

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
				List<Entity> NearEntitys = player.getNearbyEntities(15.0, 15.0, 15.0);
				for(Entity near : NearEntitys){
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
		}else if(args.length == 2){
			// 乗せる
			Entity rider = null;
			Entity riding = null;
			Player p_rider = Bukkit.getPlayerExact(args[0]); // 乗る人
			Player p_riding = Bukkit.getPlayerExact(args[1]); // 乗られる人
			if(p_rider != null){
				rider = p_rider;
			}else{
				if (sender instanceof Player) {
					Player player = (Player) sender;
					List<Entity> WorldEntitys = player.getWorld().getEntities();
					double d = Double.MAX_VALUE;
					for(Entity e : WorldEntitys){
						if(e.getType() == EntityType.PLAYER){
							continue;
						}
						if(!e.getName().equalsIgnoreCase(args[0])){
							continue;
						}
						double distance = e.getLocation().distance(player.getLocation());
						if(d > distance){
							rider = e;
							d = distance;
						}
					}
					if(rider == null){
						Method.SendMessage(sender, cmd, "指定されたプレイヤー・エンティティが見つかりませんでした。");
						return true;
					}
				}else if (sender instanceof BlockCommandSender) {
					BlockCommandSender cmdb = (BlockCommandSender) sender;
					List<Entity> WorldEntitys = cmdb.getBlock().getWorld().getEntities();
					double d = Double.MAX_VALUE;
					for(Entity e : WorldEntitys){
						if(e.getType() == EntityType.PLAYER){
							continue;
						}
						if(!e.getName().equalsIgnoreCase(args[0])){
							continue;
						}
						double distance = e.getLocation().distance(cmdb.getBlock().getLocation());
						if(d > distance){
							rider = e;
							d = distance;
						}
					}
					if(rider == null){
						Method.SendMessage(sender, cmd, "指定されたプレイヤー・エンティティが見つかりませんでした。");
						return true;
					}
				}else{
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}

			}

			if(p_riding != null){
				riding = p_riding;
			}else{
				if (sender instanceof Player) {
					Player player = (Player) sender;
					List<Entity> WorldEntitys = player.getWorld().getEntities();
					double d = Double.MAX_VALUE;
					for(Entity e : WorldEntitys){
						if(e.getType() == EntityType.PLAYER){
							continue;
						}
						if(!e.getName().equalsIgnoreCase(args[1])){
							continue;
						}
						double distance = e.getLocation().distance(player.getLocation());
						if(d > distance){
							riding = e;
							d = distance;
						}
					}
					if(riding == null){
						Method.SendMessage(sender, cmd, "指定されたプレイヤー・エンティティが見つかりませんでした。");
						return true;
					}
				}else if (sender instanceof BlockCommandSender) {
					BlockCommandSender cmdb = (BlockCommandSender) sender;
					List<Entity> WorldEntitys = cmdb.getBlock().getWorld().getEntities();
					double d = Double.MAX_VALUE;
					for(Entity e : WorldEntitys){
						if(e.getType() == EntityType.PLAYER){
							continue;
						}
						if(!e.getName().equalsIgnoreCase(args[1])){
							continue;
						}
						double distance = e.getLocation().distance(cmdb.getBlock().getLocation());
						if(d > distance){
							riding = e;
							d = distance;
						}
					}
					if(riding == null){
						Method.SendMessage(sender, cmd, "指定されたプレイヤー・エンティティが見つかりませんでした。");
						return true;
					}
				}else{
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}
			}
			String rider_type = "エンティティ", riding_type = "エンティティ";
			if(rider.getType() == EntityType.PLAYER){
				rider_type = "プレイヤー";
			}
			if(riding.getType() == EntityType.PLAYER){
				riding_type = "プレイヤー";
			}

			if(riding.getUniqueId().toString().equalsIgnoreCase(rider.getUniqueId().toString())){
				Method.SendMessage(sender, cmd, "処理できません。");
				return true;
			}

			Boolean bool = riding.setPassenger(rider);
			if(bool){
				Method.SendMessage(sender, cmd, rider_type + "「" + rider.getName() + "」を" + riding_type + "「" + riding.getName() + "」に乗せました。");
			}else{
				Method.SendMessage(sender, cmd, rider_type + "「" + rider.getName() + "」を" + riding_type + "「" + riding.getName() + "」に乗せられませんでした。");
			}
			return true;
		}

		Method.SendMessage(sender, cmd, "----- Rider -----");
		Method.SendMessage(sender, cmd, "/rider <Player>: プレイヤーに乗ります");
		Method.SendMessage(sender, cmd, "/rider <EntityName>: 1辺30ブロックの立方体内にある指定された名前のエンティティに乗ります");
		Method.SendMessage(sender, cmd, "/rider <Rider Player/EntityName> <Riding Player/EntityName>: プレイヤーもしくはエンティティ(Rider)をプレイヤーもしくはエンティティ(Riding)に乗せます");
		Method.SendMessage(sender, cmd, "/rider leave: 乗っているプレイヤー・エンティティを下ろします");
		return true;
	}
}
