package xyz.jaoafa.mymaid.Command;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class PPAP {
	JavaPlugin plugin;
	ArrayList<String> text = new ArrayList<String>();
	public PPAP(JavaPlugin plugin) {
		this.plugin = plugin;

		text.add("");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){

		return true;
	}
}
