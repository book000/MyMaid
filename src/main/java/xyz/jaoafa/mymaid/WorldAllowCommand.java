package xyz.jaoafa.mymaid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import xyz.jaoafa.mymaid.Discord.Discord;

public class WorldAllowCommand implements Listener {
	public WorldAllowCommand() {
	}
	static JavaPlugin plugin;
	static File file;
	static Map<String, Map<String, List<String>>> WorldCommand = new HashMap<String, Map<String, List<String>>>();

	/*
	 * 「+*」はすべてのコマンドをAllow(使用可能)
	 *  →Disallowが記載されている場合はそちらを優先
	 * 「-*」はすべてのコマンドをDisallow(使用不可能)
	 *  →Allowが記載されている場合はそちらを優先
	 *
	 * 「+command」はAllow(使用可能)
	 * 「-command」はDisallow(使用不可能)
	 */
	@EventHandler(ignoreCancelled = true)
	public void CommandAllowOrDisallowCheck(PlayerCommandPreprocessEvent event){
		Player player = event.getPlayer();
		String command = event.getMessage();
		String[] args = command.split(" ", 0);
		String Firstarg = StringUtils.stripStart(args[0], "/");
		World world = player.getWorld();
		String group = PermissionsManager.getPermissionMainGroup(player);

		// AdminとModeratorはチェックの例外
		if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
			return;
		}

		// そのワールドに情報がなければ無視
		if(!WorldCommand.containsKey(world.getName())){
			return;
		}

		Map<String, List<String>> TypeList = WorldCommand.get(world.getName());
		String DefaultAllCommand = TypeList.get("DefaultAllCommand").get(0);
		List<String> CommandList = TypeList.get("CommandList");

		if(DefaultAllCommand.equalsIgnoreCase("Allow")){
			// すべてのコマンドをデフォルトAllow
			// Disallowが設定されている場合のみDisallow処理
			if(CommandList.contains("-" + Firstarg)){
				player.sendMessage("[" + ChatColor.RED + "CommandCheck" + ChatColor.WHITE + "] " + ChatColor.GREEN + "そのコマンド「" + Firstarg + "」はこのワールドで実行が制限されているため使用できません。");
				player.sendMessage("[" + ChatColor.RED + "CommandCheck" + ChatColor.WHITE + "] " + ChatColor.GREEN + "使用したい場合は管理部にお問い合わせください。");
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
						p.sendMessage("[" + ChatColor.RED + "CommandCheck" + ChatColor.WHITE + "] " + ChatColor.GREEN + player.getName() + "がワールド「" + world.getName() + "」で使用禁止されているコマンド「" + Firstarg + "」を使用しようとしました。");
					}
				}
				Discord.send("293856671799967744", "[***CommandCheck***]" + player.getName() + "がワールド「" + world.getName() + "」で使用禁止されているコマンド「" + Firstarg + "」を使用しようとしました。");
				event.setCancelled(true);
				return;
			}
		}else if(DefaultAllCommand.equalsIgnoreCase("Disallow")){
			// すべてのコマンドをデフォルトDisallow
			// Allowが設定されている場合のみAllow処理
			if(!CommandList.contains("+" + Firstarg)){
				player.sendMessage("[" + ChatColor.RED + "CommandCheck" + ChatColor.WHITE + "] " + ChatColor.GREEN + "そのコマンド「" + Firstarg + "」はこのワールドで実行が制限されているため使用できません。");
				player.sendMessage("[" + ChatColor.RED + "CommandCheck" + ChatColor.WHITE + "] " + ChatColor.GREEN + "使用したい場合は管理部にお問い合わせください。");
				for(Player p: Bukkit.getServer().getOnlinePlayers()) {
					if(group.equalsIgnoreCase("Admin") || group.equalsIgnoreCase("Moderator")) {
						p.sendMessage("[" + ChatColor.RED + "CommandCheck" + ChatColor.WHITE + "] " + ChatColor.GREEN + player.getName() + "がワールド「" + world.getName() + "」で使用禁止されているコマンド「" + Firstarg + "」を使用しようとしました。");
					}
				}
				Discord.send("293856671799967744", "[***CommandCheck***]" + player.getName() + "がワールド「" + world.getName() + "」で使用禁止されているコマンド「" + Firstarg + "」を使用しようとしました。");
				event.setCancelled(true);
				return;
			}
		}
	}

	/**
	 * 使用できるコマンド情報をロードしたりデータを保存したり初期設定をします。
	 * @param plugin プラグインのJavaPluginを指定
	 * @return 初期設定を完了したかどうか
	 * @author mine_book000
	 */
	public static boolean first(JavaPlugin plugin){
		WorldAllowCommand.plugin = plugin;
		// 設定ファイルがなければ作成
		File file = new File(plugin.getDataFolder(), "WorldCommand.yml");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				BugReport.report(e);
				return false;
			}
			WorldAllowCommand.file = file;
			Save();
		}else{
			WorldAllowCommand.file = file;
			Load();
		}
		return true;
	}

	/**
	 * コマンドの使用可否情報をセーブします。
	 * @return 完了したかどうか
	 * @author mine_book000
	 */
	public static boolean Save(){
		FileConfiguration data = YamlConfiguration.loadConfiguration(file);
		for(World world : Bukkit.getWorlds()){
			if(WorldCommand.containsKey(world.getName())){
				// 設定がメモリ上に存在する
				Map<String, List<String>> TypeList = WorldCommand.get(world.getName());
				if(TypeList.containsKey("DefaultAllCommand") && TypeList.containsKey("CommandList")){
					// ワールド設定欄があってその中の設定があるとき
					List<String> DefaultAllCommand = TypeList.get("DefaultAllCommand");
					List<String> CommandList = TypeList.get("CommandList");

					data.set(world.getName() + "." + "DefaultAllCommand", DefaultAllCommand.get(0)); // 保存時はString
					data.set(world.getName() + "." + "CommandList", CommandList); // 保存時もList
				}else{
					// ワールド設定欄があるのにその中の設定が存在しないとき
					List<String> DefaultAllCommand = new ArrayList<String>();
					// 本当はこんな管理したくないんだけどObject管理もそれはそれでアレなので.get(0)が判定ということで
					DefaultAllCommand.add("Allow"); // DefaultAllCommandのDefault値はAllow
					TypeList.put("DefaultAllCommand", DefaultAllCommand);

					List<String> CommandList = new ArrayList<String>();
					TypeList.put("CommandList", CommandList);

					data.set(world.getName() + "." + "DefaultAllCommand", DefaultAllCommand.get(0)); // 保存時はString
					data.set(world.getName() + "." + "CommandList", CommandList); // 保存時もList
				}
			}else{
				// ワールド設定欄さえないとき
				Map<String, List<String>> TypeList = new HashMap<String, List<String>>();
				List<String> DefaultAllCommand = new ArrayList<String>();
				// 本当はこんな管理したくないんだけどObject管理もそれはそれでアレなので.get(0)が判定ということで
				DefaultAllCommand.add("Allow"); // DefaultAllCommandのDefault値はAllow
				TypeList.put("DefaultAllCommand", DefaultAllCommand);

				List<String> CommandList = new ArrayList<String>();
				TypeList.put("CommandList", CommandList);

				WorldCommand.put(world.getName(), TypeList);

				data.set(world.getName() + "." + "DefaultAllCommand", DefaultAllCommand.get(0)); // 保存時はString
				data.set(world.getName() + "." + "CommandList", CommandList); // 保存時もList
			}
		}
		try {
			data.save(file);
			return true;
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			BugReport.report(e);
			return false;
		}
	}

	/**
	 * コマンドの使用可否情報をロードします。
	 * @return 完了したかどうか
	 * @author mine_book000
	 */
	public static boolean Load(){
		FileConfiguration data = YamlConfiguration.loadConfiguration(file);

		for(World world : Bukkit.getWorlds()){
			if(data.contains(world.getName() + "." + "DefaultAllCommand") && data.contains(world.getName() + "." + "CommandList")){
				Map<String, List<String>> TypeList = new HashMap<String, List<String>>();

				List<String> DefaultAllCommand = new ArrayList<String>();
				// 本当はこんな管理したくないんだけどObject管理もそれはそれでアレなので.get(0)が判定ということで
				DefaultAllCommand.add(data.getString(world.getName() + "." + "DefaultAllCommand"));
				TypeList.put("DefaultAllCommand", DefaultAllCommand);

				List<String> CommandList = data.getStringList(world.getName() + "." + "CommandList");
				TypeList.put("CommandList", CommandList);

				WorldCommand.put(world.getName(), TypeList);
			}
		}
		return true;
	}
}
