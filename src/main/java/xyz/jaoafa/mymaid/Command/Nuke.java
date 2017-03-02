package xyz.jaoafa.mymaid.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Pointjao;

public class Nuke implements CommandExecutor {
	JavaPlugin plugin;
	public Nuke(JavaPlugin plugin){
		this.plugin = plugin;
	}
	private int amount;
	private int angleInterval;
	private int shot;
	private int angle=0;
	private int used=0;
	private boolean now = false;
	private BukkitTask task = null;
	private List<Integer> list;
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if ((sender instanceof Player)) {
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("stop")){
					if(task == null){
						Method.SendMessage(sender, cmd, "止まってました。");
						now = false;
						task = null;
						return true;
					}
					task.cancel();
					task = null;
					now = false;
					Method.SendMessage(sender, cmd, "停止しました。");
					return true;
				}
			}
			if(now){
				Method.SendMessage(sender, cmd, "現在他のユーザーが実行しています。");
				return true;
			}
			Player player = (Player) sender; //コマンド実行者を代入
			PlayerInventory inv = player.getInventory();
			ItemStack[] invdata = inv.getContents();
			list = new ArrayList<Integer>();
			for(int n=0; n != invdata.length; n++){
				ItemStack is = inv.getItem(n);
				if(is == null){
					continue;
				}
				if(is.getType() == Material.AIR){
					continue;
				}
				list.add(n);
			}

			// 角度の変更速度はレベル10以下なら10度、それ以上は20度
			angleInterval = 5;

			// 一回あたりの発射数はレベル20以下なら5、それ以上は10。
			shot = 1;

			// 矢の発射回数（本数ではない）は（1+レベル÷20回）×36回
			amount = list.size();

			int use = 20;
			if(!Pointjao.hasjao(player, use)){
				Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
				return true;
			}
			Pointjao.usejao(player, use, "Nuke実行の為");

			Method.SendMessage(sender, cmd, "Nukeを起動しました。");
			now = true;
			used=0;
			task = new Shoot(plugin, player).runTaskTimer(plugin, 0L,1L);
			return true;
		}

		Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
		Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
		return true;
	}
	public class Shoot extends BukkitRunnable{
		Player player;
		JavaPlugin plugin;
		/**
		 * コンストラクタ
		 * @param entities 消去対象のエンティティ
		 */
		public Shoot(JavaPlugin plugin, Player player){
			this.plugin = plugin;
			this.player = player;
		}

		@Override
		public void run(){
			angle+=angleInterval;
			Location loc = player.getLocation();
			loc.setYaw(-angle);
			player.teleport(loc);
			loc.add(0,1.3f,0);
			for(int n=0;n != shot;n++){
				Vector v=new Vector(Math.sin(Math.toRadians(angle)),n / 5F,Math.cos(Math.toRadians(angle))).normalize();
				Collections.shuffle(list);
				int i = list.get(0);
				ItemStack item = player.getInventory().getItem(i);
				if(item == null){
					continue;
				}

				Item ITEM = player.getWorld().dropItem(loc.add(v), item);
				ITEM.setVelocity(v);

				try{
					list.remove(i-used);
				}catch(IndexOutOfBoundsException e){
					Bukkit.broadcastMessage(i + ": IndexOutOfBoundsException(" + e.getLocalizedMessage() +")");
					break;
				}
				Bukkit.broadcastMessage(item.getType() + " -1 (" + list.size() + ")");
				used++;
			}
			// 射る回数をひとつ減らす
			amount--;


			// 射る回数がなくなったらスキル終了
			if(amount == 0){
				player.sendMessage("[NUKE] " + ChatColor.GREEN + "Nukeを終了しました。");
				player.getInventory().clear();
				now = false;
				cancel();
				task = null;
			}
		}
	}
}