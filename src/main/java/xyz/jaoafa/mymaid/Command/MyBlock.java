package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;

public class MyBlock implements CommandExecutor {
	JavaPlugin plugin;
	public MyBlock(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String, Material> myblock = new HashMap<String, Material>();
	FallingBlock fb;
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(!(sender instanceof org.bukkit.entity.Player)){
			Method.SendMessage(sender, cmd, "サーバ内で実行してください。");
			return true;
		}
		org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
		if(args.length == 0){
			myblock.get(null);
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("on")){
				if(myblock.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "既にあなたはブロックです。");
					return true;
				}
				myblock.put(player.getName(), Material.STONE);
				if(myblock.size() == 1){
					new MyBlockRunCheck().runTaskLaterAsynchronously(plugin, 1);
				}
				for(Player p: Bukkit.getServer().getOnlinePlayers()){
					if(!p.getName().equalsIgnoreCase(player.getName())){
						p.hidePlayer(player);
					}
				}
				Method.SendMessage(sender, cmd, "あなたをブロックに変更しました。");
				Method.SendMessage(sender, cmd, "自分からはブロックには見えませんが、まわりにはブロックに見えるはずです。");
				Method.SendMessage(sender, cmd, "初期設定は/myblock set [Block]で行えます。");
				return true;
			}else if(args[0].equalsIgnoreCase("off")){
				if(!myblock.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "あなたはブロックではないようです。");
					return true;
				}
				myblock.remove(player.getName());
				if(myblock.size() == 0){
					new MyBlockRunCheck().cancel();
				}
				for(Player p: Bukkit.getServer().getOnlinePlayers()){
					if(!p.getName().equalsIgnoreCase(player.getName())){
						p.showPlayer(player);
					}
				}
				Method.SendMessage(sender, cmd, "あなたはブロックでは無くなりました。");
				return true;
			}
		}else if(args.length == 2){
			if(args[1].equalsIgnoreCase("set")){
				if(!myblock.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "あなたはブロックではないようです。");
					return true;
				}
				Material material = null;
				try{
					int i = Integer.parseInt(args[0]);
					material = Material.getMaterial(i);
				}catch(NumberFormatException e){
					material = Material.getMaterial(args[0]);
				}
				myblock.put(player.getName(), material);
				Method.SendMessage(sender, cmd, "あなたは「" + material + "」になりました。");
			}
		}
		Method.SendMessage(sender, cmd, "MyBlock HELP");
		Method.SendMessage(sender, cmd, "/myblock on: あなたをプロックに変更します。");
		Method.SendMessage(sender, cmd, "/myblock off: あなたがブロックから戻します。");
		Method.SendMessage(sender, cmd, "/myblock set [Block]: あなたのプロックを変えます。");
		return true;
	}
	private class MyBlockRunCheck extends BukkitRunnable {
		@Override
		public void run() {
			if(MyMaid.nextbakrender){
				if(myblock.size() == 0){
					new MyBlockRunCheck().cancel();
				}
				for(Entry<String, Material> data : myblock.entrySet()) {
				    Player player = Bukkit.getPlayer(data.getKey());
				    if(!player.isOnline()){
				    	for(Player p: Bukkit.getServer().getOnlinePlayers()){
							p.showPlayer(player);
						}
				    	myblock.remove(data.getKey());
				    }
					Material material = data.getValue();
					for(Player p: Bukkit.getServer().getOnlinePlayers()){
						p.hidePlayer(player);
					}
					CraftPlayer cp = ((CraftPlayer) player);
					CraftWorld cw = ((CraftWorld) player.getWorld());
					if(!fb.isDead()){
						fb.remove();
					}
					fb = cw.spawnFallingBlock(cp.getLocation(), material, (byte) 0);
				}
			}
		}
	}
}
