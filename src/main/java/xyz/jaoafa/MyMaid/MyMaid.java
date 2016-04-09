package xyz.jaoafa.mymaid;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MyMaid extends JavaPlugin {
	BukkitTask task = null;//あとで自分で自分を止めるためのもの
	@Override
    public void onEnable() {
    	getLogger().info("(c) jao Minecraft Server MyMaid Project.");
    	getLogger().info("Product by tomachi.");

    }

    @Override
    public void onDisable() {
        // TODO ここに、プラグインが無効化された時の処理を実装してください。
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        // プレイヤーが「/basic」コマンドを投入した際の処理...
    	if(cmd.getName().equalsIgnoreCase("jf")){
    		Player player = (Player) sender;
    		Date date1 = new Date();
    		SimpleDateFormat date = new SimpleDateFormat("H:m:s");
    		Bukkit.broadcastMessage(ChatColor.GRAY + "[" + date.format(date1) +"]"  + ChatColor.WHITE + player.getName() +  ": jao");
    		task = this.getServer().getScheduler().runTaskTimer(this, new Timer(this, player.getName()), 60L, 60L);
    		return true;
		}
    	return false;
        // コマンドが実行されなかった場合は、falseを返して当メソッドを抜ける。
    }

    private class Timer extends BukkitRunnable{
		String user;//秒数
		JavaPlugin plugin;//BukkitのAPIにアクセスするためのJavaPlugin
		public Timer(JavaPlugin plugin, String user) {
			this.plugin = plugin;
			this.user = user;
		}
		@Override
		public void run() {
			Date date1 = new Date();
			SimpleDateFormat date = new SimpleDateFormat("H:m:s");
			Bukkit.broadcastMessage(ChatColor.GRAY + "["+ date.format(date1) + "]" + ChatColor.WHITE + user +  ": afa");
			task.cancel();
		}
	}

}
