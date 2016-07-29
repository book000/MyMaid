package xyz.jaoafa.mymaid.Command;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;

public class MakeCmd implements CommandExecutor {
	JavaPlugin plugin;
	public MakeCmd(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender; //コマンド実行者を代入
		if(player.getItemInHand().getType() == Material.AIR){
			Method.SendMessage(sender, cmd, "アイテムを持っていません。");
			return true;
		}

		UUID uuid = player.getUniqueId();
		Material handtype = player.getItemInHand().getType();
		String command = "";
		if(handtype == Material.WRITTEN_BOOK){
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

			command = "/give @p minecraft:written_book 1 0 ";
			command += "{title:\"" + title + "\", author:\"" + author + "\", generation:0, pages:[\"";

			for(int i = 0; i < pages.size(); i++) {
		    	command += "{text:\\\""+ pages.get(i).replaceAll("\r\n", "\n").replaceAll("[\n|\r]", "\\\\n") + "\\\",color:black}";
		    	if(i+1 != pages.size()){
		    		command += "\",\"";
		    	}
			}
		    command += "\"]}";
		}else if(handtype == Material.WOOD_SWORD){
			ItemMeta sword = player.getItemInHand().getItemMeta();
			command = "/give @p minecraft:wooden_sword 1 0 ";
			command += "{display:{";

			String name = "";
			List<String> lore = null;
			if(sword.hasDisplayName()){
				name = sword.getDisplayName();
				command += "Name:\"" + name + "\"";
			}
			if(sword.hasLore()){
				if(sword.hasDisplayName()) command += ", ";
				lore = sword.getLore();
				command += "Lore:[\"";
				for(int i = 0; i < lore.size(); i++){
					command += lore.get(i);
					if(i+1 < lore.size()) command += "\", \"";
				}
				command += "\"]";
			}
			command += "}";

			if(sword.hasEnchants()){
				command += ",ench:[";
				int i = 0;
				for(Entry<Enchantment, Integer> data : sword.getEnchants().entrySet()){
					command += "{id:" + data.getKey().getId() + "s,lvl:" + data.getValue() + "s}";
					if(i+1 < sword.getEnchants().size()) command += ", ";
					i++;
				}
				command += "]";
			}
			command += "}";
		}else if(handtype == Material.STONE_SWORD){
			ItemMeta sword = player.getItemInHand().getItemMeta();
			command = "/give @p minecraft:stone_sword 1 0 ";
			command += "{display:{";

			String name = "";
			List<String> lore = null;
			if(sword.hasDisplayName()){
				name = sword.getDisplayName();
				command += "Name:\"" + name + "\"";
			}
			if(sword.hasLore()){
				if(sword.hasDisplayName()) command += ", ";
				lore = sword.getLore();
				command += "Lore:[\"";
				for(int i = 0; i < lore.size(); i++){
					command += lore.get(i);
					if(i+1 < lore.size()) command += "\", \"";
				}
				command += "\"]";
			}
			command += "}";

			if(sword.hasEnchants()){
				command += ",ench:[";
				int i = 0;
				for(Entry<Enchantment, Integer> data : sword.getEnchants().entrySet()){
					command += "{id:" + data.getKey().getId() + "s,lvl:" + data.getValue() + "s}";
					if(i+1 < sword.getEnchants().size()) command += ", ";
					i++;
				}
				command += "]";
			}
			command += "}";
		}else if(handtype == Material.IRON_SWORD){
			ItemMeta sword = player.getItemInHand().getItemMeta();
			command = "/give @p minecraft:iron_sword 1 0 ";
			command += "{display:{";

			String name = "";
			List<String> lore = null;
			if(sword.hasDisplayName()){
				name = sword.getDisplayName();
				command += "Name:\"" + name + "\"";
			}
			if(sword.hasLore()){
				if(sword.hasDisplayName()) command += ", ";
				lore = sword.getLore();
				command += "Lore:[\"";
				for(int i = 0; i < lore.size(); i++){
					command += lore.get(i);
					if(i+1 < lore.size()) command += "\", \"";
				}
				command += "\"]";
			}
			command += "}";

			if(sword.hasEnchants()){
				command += ",ench:[";
				int i = 0;
				for(Entry<Enchantment, Integer> data : sword.getEnchants().entrySet()){
					command += "{id:" + data.getKey().getId() + "s,lvl:" + data.getValue() + "s}";
					if(i+1 < sword.getEnchants().size()) command += ", ";
					i++;
				}
				command += "]";
			}
			command += "}";
		}else if(handtype == Material.GOLD_SWORD){
			ItemMeta sword = player.getItemInHand().getItemMeta();
			command = "/give @p minecraft:golden_sword 1 0 ";
			command += "{display:{";

			String name = "";
			List<String> lore = null;
			if(sword.hasDisplayName()){
				name = sword.getDisplayName();
				command += "Name:\"" + name + "\"";
			}
			if(sword.hasLore()){
				if(sword.hasDisplayName()) command += ", ";
				lore = sword.getLore();
				command += "Lore:[\"";
				for(int i = 0; i < lore.size(); i++){
					command += lore.get(i);
					if(i+1 < lore.size()) command += "\", \"";
				}
				command += "\"]";
			}
			command += "}";

			if(sword.hasEnchants()){
				command += ",ench:[";
				int i = 0;
				for(Entry<Enchantment, Integer> data : sword.getEnchants().entrySet()){
					command += "{id:" + data.getKey().getId() + "s,lvl:" + data.getValue() + "s}";
					if(i+1 < sword.getEnchants().size()) command += ", ";
					i++;
				}
				command += "]";
			}
			command += "}";
		}else if(handtype == Material.DIAMOND_SWORD){
			ItemMeta sword = player.getItemInHand().getItemMeta();
			command = "/give @p minecraft:diamond_sword 1 0 ";
			command += "{display:{";

			String name = "";
			List<String> lore = null;
			if(sword.hasDisplayName()){
				name = sword.getDisplayName();
				command += "Name:\"" + name + "\"";
			}
			if(sword.hasLore()){
				if(sword.hasDisplayName()) command += ", ";
				lore = sword.getLore();
				command += "Lore:[\"";
				for(int i = 0; i < lore.size(); i++){
					command += lore.get(i);
					if(i+1 < lore.size()) command += "\", \"";
				}
				command += "\"]";
			}
			command += "}";

			if(sword.hasEnchants()){
				command += ",ench:[";
				int i = 0;
				for(Entry<Enchantment, Integer> data : sword.getEnchants().entrySet()){
					command += "{id:" + data.getKey().getId() + "s,lvl:" + data.getValue() + "s}";
					if(i+1 < sword.getEnchants().size()) command += ", ";
					i++;
				}
				command += "]";
			}
			command += "}";
		}else{
			Method.SendMessage(sender, cmd, "そのアイテムは現在対応していません。");
			Method.SendMessage(sender, cmd, "必要であれば以下のデバックデータを撮影し、プラグイン管理者までお問い合わせください。");
			Method.SendMessage(sender, cmd, "Debug HandMaterial:「" + handtype + "」");
			return true;
		}



		try{
	    	  File file = new File(plugin.getDataFolder() + File.separator + uuid + "-" + handtype + ".txt");
	    	  FileWriter filewriter = new FileWriter(file);

	    	  filewriter.write(command);

	    	  filewriter.close();
	    }catch(IOException e){
	    	Method.SendMessage(sender, cmd, "保存できませんでした。");
	    	System.out.println(e);

	    	  return true;
	    }
		Method.SendMessage(sender, cmd, "保存しました。ファイル名: 「" + plugin.getDataFolder() + File.separator + uuid + "-" + handtype + ".txt」");
		return true;
	}
}
