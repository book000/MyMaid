package xyz.jaoafa.mymaid.Command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class ArrowShotter implements CommandExecutor {
	JavaPlugin plugin;
	public ArrowShotter(JavaPlugin plugin){
		this.plugin = plugin;
	}
	/**
	 * 矢の発射回数
	 */
	private int amount;

	/**
	 * 角度の変更速度
	 */
	private int angleInterval;

	/**
	 * 1回ごとの発射数
	 */
	private int shot;

	/**
	 * 矢の発射角度
	 */
	private int angle=0;
	/**
	 * スキル処理
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			sender.sendMessage("[ARROWSHOTTER] " + ChatColor.GREEN + "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender; //コマンド実行者を代入
		int level = 30;
		if(args.length == 1){
			level = Integer.parseInt(args[0]);
		}
		// 角度の変更速度はレベル10以下なら10度、それ以上は20度
		angleInterval=level <= 10?10:20;

		// 一回あたりの発射数はレベル20以下なら5、それ以上は10。
		shot=level <= 20?5:10;

		// 矢の発射回数（本数ではない）は（1+レベル÷20回）×36回
		amount=(1 + level / 20) * 36;

		player.sendMessage("[ARROWSHOTTER] " + ChatColor.GREEN + "ArrowShotterを起動しました。動かないでください。");

		new Shoot(plugin, player).runTaskTimer(plugin, 0L,1L);
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
			this.player=player;
		}

		@Override
		public void run(){
			// 発射済みの矢のリスト
			ArrayList<Entity> arrows=new ArrayList<Entity>();

			// 矢の発射角度を変更する
			angle+=angleInterval;

			// 矢を打つ方向にボスを向ける
			Location loc = player.getLocation();
			loc.setYaw(-angle);
			player.teleport(loc);

			// 矢の発射の基点を計算する
			// モンスターの座標は足元の座標なので、だいたい腕のあたりから矢が出るよう高さを調整する
			loc.add(0,1.3f,0);

			// 矢の発射音を再生
			loc.getWorld().playEffect(loc,Effect.BOW_FIRE,0,angle);

			// 1回あたり複数本の矢を発射する
			for(int n=0;n != shot;n++){

				// 矢の発射方向を計算する
				Vector v=new Vector(Math.sin(Math.toRadians(angle)),n / 10F,Math.cos(Math.toRadians(angle))).normalize();

				// 矢を発射する
				Arrow arrow = player.getWorld().spawnArrow(loc.add(v),v,0.6F,12);

				// 矢の発射者を設定する
				arrow.setShooter(player);

				// 発射した矢を、発射済みの矢のリストに加える
				arrows.add(arrow);
			}

			// 7秒後に、ボスが射った矢をすべて消去する
			new RemoveEntities(arrows).runTaskLater(plugin,7 * 20);

			// 射る回数をひとつ減らす
			amount--;

			// 射る回数がなくなったらスキル終了
			if(amount == 0){
				player.sendMessage("[ARROWSHOTTER] " + ChatColor.GREEN + "ArrowShotterを終了しました。");
				cancel();
			}
		}
	}
	public class RemoveEntities extends BukkitRunnable{

		/**
		 * 消去対象のエンティティ
		 */
		private List<Entity> entities;

		/**
		 * コンストラクタ
		 * @param entities 消去対象のエンティティ
		 */
		public RemoveEntities(List<Entity> entities){
			//消去対象のエンティティを覚えておく
			this.entities=entities;
		}

		@Override
		public void run(){
			//世界から消去対象のエンティティを消去する
			for(Entity entity:entities){
				entity.remove();
			}
		}
	}
}
