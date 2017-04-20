package xyz.jaoafa.mymaid.Command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Pointjao;

public class BON implements CommandExecutor {
	JavaPlugin plugin;
	ArrayList<String> text = new ArrayList<String>();
	public static boolean nowbon = false;
	public static boolean stopbon = false;
	public BON(JavaPlugin plugin) {
		this.plugin = plugin;

		text.add("( ◠‿◠ )ﾎﾞﾝ ﾊﾞｯ ﾎﾞｯﾎﾞｯｯﾎﾞｯ ｯﾎﾞﾝﾊﾞｯ ﾎﾞｯﾎﾞｯｯﾎﾞｯ ﾎﾞﾝ ﾊﾞｯ ﾎﾞｯﾎﾞｯｯﾎﾞｯ ｯﾎﾞﾝﾊﾞｯ ﾎﾞｯﾎﾞｯｯﾎﾞｯ");
		text.add("( ◠‿◠ )ﾃﾞﾊﾞﾊﾞ ﾊﾙｻﾒ ﾎｳﾕﾖ ﾊﾞｧ! ﾃﾞﾏﾞｶﾝﾒ ﾊﾙｻﾒ ﾎﾕﾖ ﾊﾞｧ!");
		text.add("( ◠‿◠ )ﾃﾞｷﾞｬﾝﾏ ｽﾕﾊﾟｼﾝﾇ ﾒﾆｭﾊﾞﾊﾞﾆｼﾞｬｽ! ﾅ ﾆ ｼｭｯｶ ｼﾞｬｯ ﾋﾟ ｵﾝ ｶ!");
		text.add("( ◠‿◠ )( ◠‿◠ )ﾃﾞﾊﾞﾊﾞ ﾊﾙｻﾒ ﾎｳﾕﾖ ﾊﾞｧ! ﾃﾞﾏﾞｶﾝﾒ ﾊﾙｻﾒ ﾎﾕﾖ ﾊﾞｧ!!");
		text.add("( ◠‿◠ )( ◠‿◠ )ﾃﾞｷﾞｬｷﾞｬ ｽﾕﾊﾟｼﾝﾇ ﾒﾆｭﾊﾞﾊﾞﾆｼﾞｬｽ! ﾅ ﾆ ｼｭｯｶ ｼﾞｬｯ ｼﾞｬｼﾞｬｯｼﾞｬｯｼﾞｬｯ!!");
		text.add("( ◠‿◠ )∠( ◠‿◠ )／おーう！！！");
		text.add("( ◠‿◠ )(「◠‿◠ )」ぼん ばっ ぼっぼっっぼっ っぼっぼっ ぼっぼっっぼっ L( ◠‿◠ )┘ぼっぼっぼっ ぼっぼっっぼっ っぼっぼぼっ ぼっぼっ");
		text.add("( ◠‿◠ )L(◠‿◠ 「)ぼん ばっ ぼっぼっっぼっ っぼっぼっ ぼっぼっっぼっ └( ◠‿◠ )」ぼっぼっぼっ ぼっぼっっぼっ っぼっぼぼっ ぼっぼっ");
		text.add("( ◠‿◠ ):((; ◠‿◠ ;)): ねばば はるさめ ほうゆよ ばぁ! でまかんめ はるさめ ほゆよ ばぁ！");
		text.add("( ◠‿◠ ):((; ◠‿◠ ;)): でぎゃんま すゆぱしんぬ めにゅばばにじゃす! な に しゅっか じゃっ ぴ おん か!");
		text.add("( ◠‿◠ ):((; ◠‿◠ ;)): ねばば はるさめ ほうゆよ ばぁ! でまかんめ はるさめ ほゆよ ばぁ！！！");
		text.add("( ◠‿◠ ):((; ◠‿◠ ;)): でぎゃんま すゆぱしんぬ めにゅばばにじゃす! な に しゅっか じゃっ じゃじゃっじゃっじゃっ！！！！");
		text.add("( ◠‿◠ )(「◠‿◠ )」ぼん ばっ ぼっぼっっぼっ っぼっぼっ ぼっぼっっぼっ L( ◠‿◠ )┘ぼっぼっぼっ ぼっぼっっぼっ っぼっぼぼっ ぼっぼっ…");
		text.add("( ◠‿◠ )L(◠‿◠ 「)ぼん ばっ ぼっぼっっぼっ っぼっぼっ ぼっぼっっぼっ └( ◠‿◠ )」ぼっぼっぼっ ぼっぼっっぼっ っぼっぼぼっ ぼっぼっ…");
		text.add("( ◠‿◠ ) ＜ を");

	}


	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			return true;
		}
		Player player = (Player) sender;
		int use = 10;
		if(!Pointjao.hasjao(player, use)){
		 	 Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
		 	 return true;
		}
		Pointjao.usejao(player, use, "bonコマンド実行の為");
		String Msg = "";
		if(player.hasPermission("mymaid.pex.limited")){
			Msg = player.getName().replaceFirst(player.getName(), ChatColor.BLACK + "■" + ChatColor.WHITE + player.getName());

		}else if(Prison.prison.containsKey(player.getName())){
			Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_GRAY + "■" + ChatColor.WHITE + player.getName());
		}else if(Color.color.containsKey(player.getName())){
			Msg = player.getName().replaceFirst(player.getName(), Color.color.get(player.getName()) + "■" + ChatColor.WHITE + player.getName());
		}else if(MyMaid.chatcolor.containsKey(player.getName())){
			int i = Integer.parseInt(MyMaid.chatcolor.get(player.getName()));
			if(i >= 0 && i <= 5){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.WHITE + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 6 && i <= 19){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_BLUE + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 20 && i <= 33){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.BLUE + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 34 && i <= 47){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.AQUA + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 48 && i <= 61){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_AQUA + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 62 && i <= 76){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_GREEN + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 77 && i <= 89){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.GREEN + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 90 && i <= 103){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.YELLOW + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 104 && i <= 117){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.GOLD + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 118 && i <= 131){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.RED + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 132 && i <= 145){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_RED + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 146 && i <= 159){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_PURPLE + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 160){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.LIGHT_PURPLE + "■" + ChatColor.WHITE + player.getName());
			}
		}else{
			Msg = player.getName().replaceFirst(player.getName(), ChatColor.GRAY + "■" + ChatColor.WHITE + player.getName());
		}
		Msg = Msg + ": ";
		new Message(Msg, player).runTaskTimer(plugin, 0, 20);
		return true;
	}
	private class Message extends BukkitRunnable{
		int i;
		String Msg;
		Player player;
		public Message(String Msg, Player player){
			i = 0;
			this.Msg = Msg;
		}
		@Override
		public void run() {
			if(i == 0){
				nowbon = true;
			}
			if(i >= text.size()){
				nowbon = false;
				player.sendMessage("[BON] " + ChatColor.GREEN + "bonコマンドを途中で止められずに終えることができたため、ポイントを返却します！");
				Pointjao.addjao(player, 10, "bonコマンドのポイント返却のため");
				cancel();
			}else{
				if(stopbon){
					Date Date = new Date();
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + Msg + text.get(text.size()-1));
					nowbon = false;
					stopbon = false;
					cancel();
				}
				if(nowbon){
					Date Date = new Date();
					SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
					Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + Msg + text.get(i));
					i++;
				}
			}
		}
	}
}
