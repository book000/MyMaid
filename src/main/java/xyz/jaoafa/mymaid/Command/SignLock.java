package xyz.jaoafa.mymaid.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Pointjao;

public class SignLock implements CommandExecutor {
	JavaPlugin plugin;
	public SignLock(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		org.bukkit.block.Sign sign;
		try {
			sign = xyz.jaoafa.mymaid.Command.Sign.signlist.get(player.getName());
		}catch(Exception e){
			Method.SendMessage(sender, cmd, "看板が選択されていません。");
			return true;
		}
		int x = sign.getX();
		int y = sign.getY();
		int z = sign.getZ();
		if(args.length == 0){
			String result = Method.url_jaoplugin("signlock", "lock&p="+player.getName()+"&x="+x+"&y="+y+"&z="+z);
			if(result.equalsIgnoreCase("Err")){
				Method.SendMessage(sender, cmd, "ロック出来ませんでした。");
			}else{
				int use = 15;
				if(!Pointjao.hasjao(player, use)){
				 	 Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
				 	 return true;
				}
				Pointjao.usejao(player, use);
				Method.SendMessage(sender, cmd, "ロックしました。");
			}
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("reset")){
				String result = Method.url_jaoplugin("signlock", "reset&p="+player.getName()+"&x="+x+"&y="+y+"&z="+z);
				if(result.equalsIgnoreCase("Err")){
					Method.SendMessage(sender, cmd, "ロック解除出来ませんでした。");
				}else{
					int use = 15;
					if(!Pointjao.hasjao(player, use)){
					 	 Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
					 	 return true;
					}
					Pointjao.usejao(player, use);
					Method.SendMessage(sender, cmd, "ロック解除しました。");
				}
			}else{
				Method.SendMessage(sender, cmd, "resetを指定してください");
				return true;
			}
		}

		return true;
	}
}
