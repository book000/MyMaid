package xyz.jaoafa.mymaid.Command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Jail.Jail;

public class SSK implements CommandExecutor {
	JavaPlugin plugin;
	public SSK(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 0){
			Player player = (Player) sender;
			UUID uuid = player.getUniqueId();
	  		String data = Method.url_jaoplugin("joinvote", "u="+uuid);
	  		String[] arr = data.split("###", 0);
	  		MyMaid.chatcolor.put(player.getName(), arr[1]);
			if(player.hasPermission("mymaid.pex.limited")){
				player.setPlayerListName(ChatColor.BLACK + "■" + ChatColor.WHITE + player.getName());
			}else if(Jail.isJail(player)){
				player.setPlayerListName(ChatColor.DARK_GRAY + "■" + ChatColor.WHITE + player.getName());
			}else if(MyMaid.chatcolor.containsKey(player.getName())){
				int i = Integer.parseInt(MyMaid.chatcolor.get(player.getName()));
				if(i >= 0 && i <= 5){
					player.setPlayerListName(ChatColor.WHITE + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 6 && i <= 19){
					player.setPlayerListName(ChatColor.DARK_BLUE + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 20 && i <= 33){
					player.setPlayerListName(ChatColor.BLUE + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 34 && i <= 47){
					player.setPlayerListName(ChatColor.AQUA + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 48 && i <= 61){
					player.setPlayerListName(ChatColor.DARK_AQUA  + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 62 && i <= 76){
					player.setPlayerListName(ChatColor.DARK_GREEN + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 77 && i <= 89){
					player.setPlayerListName(ChatColor.GREEN + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 90 && i <= 103){
					player.setPlayerListName(ChatColor.YELLOW + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 104 && i <= 117){
					player.setPlayerListName(ChatColor.GOLD + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 118 && i <= 131){
					player.setPlayerListName(ChatColor.RED + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 132 && i <= 145){
					player.setPlayerListName(ChatColor.DARK_RED + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 146 && i <= 159){
					player.setPlayerListName(ChatColor.DARK_PURPLE + "■" + ChatColor.WHITE + player.getName());
				}else if(i >= 160){
					player.setPlayerListName(ChatColor.LIGHT_PURPLE + "■" + ChatColor.WHITE + player.getName());
				}
			}else{
				player.setPlayerListName(ChatColor.GRAY + "■" + ChatColor.WHITE + player.getName());
			}
	  		Method.SendMessage(sender, cmd, player.getName()+"'s SKK -> "+arr[1]);
		}else if(args.length == 1){
			for(Player player: Bukkit.getServer().getOnlinePlayers()) {
				if(player.getName().equalsIgnoreCase(args[0])){
					UUID uuid = player.getUniqueId();
			  		String data = Method.url_jaoplugin("joinvote", "u="+uuid);
			  		String[] arr = data.split("###", 0);
			  		MyMaid.chatcolor.put(player.getName(), arr[1]);
					if(player.hasPermission("mymaid.pex.limited")){
						player.setPlayerListName(ChatColor.BLACK + "■" + ChatColor.WHITE + player.getName());
					}else if(Jail.isJail(player)){
						player.setPlayerListName(ChatColor.DARK_GRAY + "■" + ChatColor.WHITE + player.getName());
					}else if(MyMaid.chatcolor.containsKey(player.getName())){
						int i = Integer.parseInt(MyMaid.chatcolor.get(player.getName()));
						if(i >= 0 && i <= 5){
							player.setPlayerListName(ChatColor.WHITE + "■" + ChatColor.WHITE + player.getName());
						}else if(i >= 6 && i <= 19){
							player.setPlayerListName(ChatColor.DARK_BLUE + "■" + ChatColor.WHITE + player.getName());
						}else if(i >= 20 && i <= 33){
							player.setPlayerListName(ChatColor.BLUE + "■" + ChatColor.WHITE + player.getName());
						}else if(i >= 34 && i <= 47){
							player.setPlayerListName(ChatColor.AQUA + "■" + ChatColor.WHITE + player.getName());
						}else if(i >= 48 && i <= 61){
							player.setPlayerListName(ChatColor.DARK_AQUA  + "■" + ChatColor.WHITE + player.getName());
						}else if(i >= 62 && i <= 76){
							player.setPlayerListName(ChatColor.DARK_GREEN + "■" + ChatColor.WHITE + player.getName());
						}else if(i >= 77 && i <= 89){
							player.setPlayerListName(ChatColor.GREEN + "■" + ChatColor.WHITE + player.getName());
						}else if(i >= 90 && i <= 103){
							player.setPlayerListName(ChatColor.YELLOW + "■" + ChatColor.WHITE + player.getName());
						}else if(i >= 104 && i <= 117){
							player.setPlayerListName(ChatColor.GOLD + "■" + ChatColor.WHITE + player.getName());
						}else if(i >= 118 && i <= 131){
							player.setPlayerListName(ChatColor.RED + "■" + ChatColor.WHITE + player.getName());
						}else if(i >= 132 && i <= 145){
							player.setPlayerListName(ChatColor.DARK_RED + "■" + ChatColor.WHITE + player.getName());
						}else if(i >= 146 && i <= 159){
							player.setPlayerListName(ChatColor.DARK_PURPLE + "■" + ChatColor.WHITE + player.getName());
						}else if(i >= 160){
							player.setPlayerListName(ChatColor.LIGHT_PURPLE + "■" + ChatColor.WHITE + player.getName());
						}
					}else{
						player.setPlayerListName(ChatColor.GRAY + "■" + ChatColor.WHITE + player.getName());
					}
			  		Method.SendMessage(sender, cmd, player.getName()+"'s SKK -> "+arr[1]);
				}
    		}
		}
		return true;
	}
}
