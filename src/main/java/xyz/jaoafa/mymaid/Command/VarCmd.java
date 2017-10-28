package xyz.jaoafa.mymaid.Command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import xyz.jaoafa.mymaid.Method;

public class VarCmd implements CommandExecutor {
	JavaPlugin plugin;
	public VarCmd(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			Method.SendMessage(sender, cmd, "引数が適切ではありません。");
			return true;
		}
		String text = "";
		int c = 0;
		while(args.length > c){
			text += args[c];
			if(args.length != (c+1)){
				text += " ";
			}
			c++;
		}
		text = StringUtils.stripStart(text, "/");

		// 「@p」のみ置き換える動作をする
		if(sender instanceof Player){
			Player player = (Player) sender;
			NearestPlayer npr = new NearestPlayer(player.getLocation());
			if(npr.getStatus()){
				text = text.replaceAll("@" + "p" + "", npr.getPlayer().getName());
			}
		}else if(sender instanceof BlockCommandSender){
			BlockCommandSender cmdb = (BlockCommandSender) sender;
			NearestPlayer npr = new NearestPlayer(cmdb.getBlock().getLocation());
			if(npr.getStatus()){
				text = text.replaceAll("@" + "p" + "", npr.getPlayer().getName());
			}
		}

		// ----- 事前定義(予約済み変数) ----- //

		SimpleDateFormat sdf_Year = new SimpleDateFormat("yyyy");
		text = text.replaceAll("\\$" + "DateTime_Year" + "\\$", sdf_Year.format(new Date()));
		SimpleDateFormat sdf_Month = new SimpleDateFormat("MM");
		text = text.replaceAll("\\$" + "DateTime_Month" + "\\$", sdf_Month.format(new Date()));
		SimpleDateFormat sdf_Day = new SimpleDateFormat("dd");
		text = text.replaceAll("\\$" + "DateTime_Day" + "\\$", sdf_Day.format(new Date()));

		SimpleDateFormat sdf_Hour = new SimpleDateFormat("HH");
		text = text.replaceAll("\\$" + "DateTime_Hour" + "\\$", sdf_Hour.format(new Date()));
		SimpleDateFormat sdf_Minute = new SimpleDateFormat("mm");
		text = text.replaceAll("\\$" + "DateTime_Minute" + "\\$", sdf_Minute.format(new Date()));
		SimpleDateFormat sdf_Second = new SimpleDateFormat("ss");
		text = text.replaceAll("\\$" + "DateTime_Second" + "\\$", sdf_Second.format(new Date()));

		text = text.replaceAll("\\$" + "PlayerCount" + "\\$", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()));


		for(Player p : Bukkit.getOnlinePlayers()){
			if(!text.contains("$" + "Damager_" + p.getName() + "$")){
				continue;
			}
			EntityDamageEvent ede = p.getLastDamageCause();
			Entity e = ede.getEntity();
			if(e == null){
				continue;
			}
			String name = e.getName();
			text = text.replaceAll("\\$" +  "Damager_" + p.getName() + "\\$", name);
		}

		ScoreboardManager sbm = Bukkit.getScoreboardManager();
		Scoreboard sb = sbm.getMainScoreboard();
		for(Objective obj : sb.getObjectives()){
		}

		// ----- 事前定義(予約済み変数) ----- //

		for(Map.Entry<String, String> e : Var.var.entrySet()) {
			text = text.replaceAll("\\$" + e.getKey() + "\\$", e.getValue());
		}

		Bukkit.dispatchCommand(sender, text);
		Method.SendMessage(sender, cmd, "コマンド「" + text + "」を実行しました。");
		return true;
	}
}
class NearestPlayer {
	Boolean status;
	Player player = null;
	double closest = -1;
	public NearestPlayer(Location loc){
		double closest = Double.MAX_VALUE;
		Player closestp = null;
		for(Player p : Bukkit.getOnlinePlayers()){
			if(!p.getWorld().equals(loc.getWorld())){
				continue;
			}
			double dist = p.getLocation().distance(loc);
			if(closest == Double.MAX_VALUE || dist < closest){
				closest = dist;
				closestp = p;
			}
		}
		if(closestp == null){
			this.status = false;
		}else{
			this.status = true;
			this.player = closestp;
			this.closest = closest;
		}
	}
	public Boolean getStatus(){
		return status;
	}
	public Player getPlayer(){
		return player;
	}
	public Double getClosest(){
		return closest;
	}
}
