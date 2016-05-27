package xyz.jaoafa.mymaid.Command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Book implements CommandExecutor {
	JavaPlugin plugin;
	public Book(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			sender.sendMessage("[DT] " + ChatColor.GREEN + "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender; //コマンド実行者を代入
		if(player.getItemInHand().getType() != Material.WRITTEN_BOOK){
			player.sendMessage("[Book] " + ChatColor.GREEN + "本を持って実行してください。");
			return true;
		}
		UUID uuid = player.getUniqueId();
	    BookMeta book = (BookMeta) player.getItemInHand().getItemMeta();
	    String title = "";
	    String author = "";
	    List<String> pages = null;
	    if(book.hasTitle()){
	    	title = book.getTitle();
	    }
	    if(book.hasAuthor()){
	    	author = book.getAuthor();
	    }
	    if(book.hasPages()){
	    	pages = book.getPages();
	    }
	    // /give @p minecraft:written_book 1 0 {display:{Name:"NAME",Lore:[LORE]},title:"TITLE",author:"AUTHOR",generation:0,pages:["{text:\"1TEXT\",color:black,extra:[{text:\"2TEXT\",color:black}]}","{text:\"2PAGE\",color:black}"]}
	    String command = "/give @p minecraft:written_book 1 0 ";


	    command += "{title:\"" + title + "\",author:\"" + author + "\",generation:0,pages:[\"";

	    for(int i = 0; i < pages.size(); i++) {

	    	command += "{text:\\\""+ pages.get(i).replaceAll("\r\n", "\n").replaceAll("[\n|\r]", "\\\\n").replaceAll("\"", "\\\\\\\"") +"\\\",color:black}";
	    	if(i+1 != pages.size()){
	    		command += "\",\"";
	    	}
		}
	    command += "\"]}";

	    try{
	    	  File file = new File(plugin.getDataFolder() + File.separator + uuid + ".txt");
	    	  FileWriter filewriter = new FileWriter(file);

	    	  filewriter.write(command);

	    	  filewriter.close();
	    }catch(IOException e){
	    	player.sendMessage("[Book] " + ChatColor.GREEN + "保存できませんでした。");
	    	System.out.println(e);

	    	  return true;
	    }
	    player.sendMessage("[Book] " + ChatColor.GREEN + "保存しました。ファイル名: 「" + plugin.getDataFolder() + File.separator + uuid + ".txt」");
		return true;
	}
}
