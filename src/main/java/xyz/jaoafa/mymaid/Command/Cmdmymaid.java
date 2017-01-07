package xyz.jaoafa.mymaid.Command;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.TileEntitySkull;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;
import xyz.jaoafa.mymaid.Pointjao;

public class Cmdmymaid implements CommandExecutor {
	JavaPlugin plugin;
	public Cmdmymaid(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	private static final Random random = new Random();
	private static final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length == 3){
			if(args[0].equalsIgnoreCase("jaoadd")){
				if (!(sender instanceof org.bukkit.entity.Player)) {
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}
				Player p = (org.bukkit.entity.Player) sender;
				if(!PermissionsEx.getUser(p).inGroup("Admin")){
					Method.SendMessage(sender, cmd, "このコマンドは管理部のみ使用可能です。");
					return true;
				}
				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					if(player.getName().equalsIgnoreCase(args[1])){
						int add;
						try{
							add = Integer.parseInt(args[2]);
						} catch (NumberFormatException nfe) {
							Method.SendMessage(sender, cmd, "ポイントには数値を指定してください。");
							return true;
						}
						Pointjao.addjao(player, add);
						Method.SendMessage(sender, cmd, player.getName() + "に" + add + "ポイントを追加しました。");
						return true;
					}
				}
				Method.SendMessage(sender, cmd, "プレイヤーが見つかりません。");
				return true;
			}else if(args[0].equalsIgnoreCase("jaouse")){
				if (!(sender instanceof org.bukkit.entity.Player)) {
					Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
					return true;
				}
				Player p = (org.bukkit.entity.Player) sender;
				if(!PermissionsEx.getUser(p).inGroup("Admin")){
					Method.SendMessage(sender, cmd, "このコマンドは管理部のみ使用可能です。");
					return true;
				}
				for(Player player: Bukkit.getServer().getOnlinePlayers()) {
					if(player.getName().equalsIgnoreCase(args[1])){
						int add;
						try{
							add = Integer.parseInt(args[2]);
						} catch (NumberFormatException nfe) {
							Method.SendMessage(sender, cmd, "ポイントには数値を指定してください。");
							return true;
						}
						Pointjao.addjao(player, add);
						Method.SendMessage(sender, cmd, player.getName() + "から" + add + "ポイントを減らしました。");
						return true;
					}
				}
				Method.SendMessage(sender, cmd, "プレイヤーが見つかりません。");
				return true;
			}else{
				Method.SendMessage(sender, cmd, "Usage: \"/mymaid jaoadd jao\" OR \"/mymaid load\" OR \"/mymaid save\"");
				return true;
			}
		}else if(args.length == 1){
			if(args[0].equalsIgnoreCase("load")){
				FileConfiguration conf = new YamlConfiguration();

				Reader reader;
				try {
					reader = new InputStreamReader(new FileInputStream(plugin.getDataFolder() + File.separator + "config.yml"));
					conf.load(reader);
				} catch (IOException | InvalidConfigurationException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
					return true;
				}
				MyMaid.conf = conf;
				if(conf.contains("prison")){
					//Prison.prison = (Map<String,Boolean>) conf.getConfigurationSection("prison").getKeys(false);
					Map<String, Object> pl = conf.getConfigurationSection("prison").getValues(true);
					if(pl.size() != 0){
						for(Entry<String, Object> p: pl.entrySet()){
							Prison.prison.put(p.getKey(), (Boolean) p.getValue());
						}
					}
				}else{
					Prison.prison = new HashMap<String,Boolean>();
					conf.set("prison",Prison.prison);
				}
				if(conf.contains("prison_block")){
					//Prison.prison_block = (Map<String,Boolean>) conf.getConfigurationSection("prison_block").getKeys(false);
					Map<String, Object> pl = conf.getConfigurationSection("prison_block").getValues(true);
					if(pl.size() != 0){
						for(Entry<String, Object> p: pl.entrySet()){
							Prison.prison_block.put(p.getKey(), (Boolean) p.getValue());
						}
					}
				}else{
					Prison.prison_block = new HashMap<String,Boolean>();
					conf.set("prison_block",Prison.prison_block);
				}
				if(conf.contains("prison_lasttext")){
					Map<String, Object> pl = conf.getConfigurationSection("prison_lasttext").getValues(true);
					if(pl.size() != 0){
						for(Entry<String, Object> p: pl.entrySet()){
							Prison.jail_lasttext.put(p.getKey(), p.getValue().toString());
						}
					}
				}else{
					Prison.jail_lasttext = new HashMap<String,String>();
					conf.set("prison_lasttext",Prison.jail_lasttext);
				}
				if(conf.contains("var")){
					//Prison.prison_block = (Map<String,Boolean>) conf.getConfigurationSection("prison_block").getKeys(false);
					Map<String, Object> var = conf.getConfigurationSection("var").getValues(true);
					if(var.size() != 0){
						for(Entry<String, Object> p: var.entrySet()){
							Var.var.put(p.getKey(), (String) p.getValue());
						}
					}
				}else{
					Var.var = new HashMap<String, String>();
					conf.set("var",Var.var);
				}
				if(conf.contains("jao")){
					//Prison.prison_block = (Map<String,Boolean>) conf.getConfigurationSection("prison_block").getKeys(false);
					Map<String, Object> jao = conf.getConfigurationSection("jao").getValues(true);
					if(jao.size() != 0){
						for(Entry<String, Object> p: jao.entrySet()){
							Pointjao.jao.put(p.getKey(), (Integer) p.getValue());
						}
					}
				}else{
					Pointjao.jao = new HashMap<String, Integer>();
					conf.set("jao",Pointjao.jao);
				}
				if(conf.contains("color")){
					//Prison.prison_block = (Map<String,Boolean>) conf.getConfigurationSection("prison_block").getKeys(false);
					Map<String, Object> color = conf.getConfigurationSection("color").getValues(true);
					if(color.size() != 0){
						for(Entry<String, Object> p: color.entrySet()){
							String colorstring = (String) p.getValue();
							if(!colorstring.equalsIgnoreCase("null")){
								ChatColor chatcolor;
								if(colorstring.equalsIgnoreCase("AQUA")){
									chatcolor = ChatColor.AQUA;
								}else if(colorstring.equalsIgnoreCase("BLACK")){
									chatcolor = ChatColor.BLACK;
								}else if(colorstring.equalsIgnoreCase("BLUE")){
									chatcolor = ChatColor.BLUE;
								}else if(colorstring.equalsIgnoreCase("DARK_AQUA")){
									chatcolor = ChatColor.DARK_AQUA;
								}else if(colorstring.equalsIgnoreCase("DARK_BLUE")){
									chatcolor = ChatColor.DARK_BLUE;
								}else if(colorstring.equalsIgnoreCase("DARK_GRAY")){
									chatcolor = ChatColor.DARK_GRAY;
								}else if(colorstring.equalsIgnoreCase("DARK_GREEN")){
									chatcolor = ChatColor.DARK_GREEN;
								}else if(colorstring.equalsIgnoreCase("DARK_PURPLE")){
									chatcolor = ChatColor.DARK_PURPLE;
								}else if(colorstring.equalsIgnoreCase("DARK_RED")){
									chatcolor = ChatColor.DARK_RED;
								}else if(colorstring.equalsIgnoreCase("GOLD")){
									chatcolor = ChatColor.GOLD;
								}else if(colorstring.equalsIgnoreCase("GREEN")){
									chatcolor = ChatColor.GREEN;
								}else if(colorstring.equalsIgnoreCase("LIGHT_PURPLE")){
									chatcolor = ChatColor.LIGHT_PURPLE;
								}else if(colorstring.equalsIgnoreCase("RED")){
									chatcolor = ChatColor.RED;
								}else if(colorstring.equalsIgnoreCase("WHITE")){
									chatcolor = ChatColor.WHITE;
								}else if(colorstring.equalsIgnoreCase("YELLOW")){
									chatcolor = ChatColor.YELLOW;
								}else if(colorstring.equalsIgnoreCase("GRAY")){
									chatcolor = ChatColor.GRAY;
								}else{
									chatcolor = null;
								}
								Color.color.put(p.getKey(), chatcolor);
							}
						}
					}
				}else{
					Color.color = new HashMap<String, ChatColor>();
					conf.set("color",Color.color);
				}
				if(conf.contains("maxplayer")){
					MyMaid.maxplayer = conf.getInt("maxplayer");
				}else{
					MyMaid.maxplayer = 0;
				}
				if(conf.contains("maxplayertime")){
					MyMaid.maxplayertime = conf.getString("maxplayertime");
				}else{
					MyMaid.maxplayertime = "無し";
				}
				Method.SendMessage(sender, cmd, "MyMaid Config Loaded!");
				return true;
			}else if(args[0].equalsIgnoreCase("save")){
				FileConfiguration conf = plugin.getConfig();
				conf.set("prison",Prison.prison);
				conf.set("prison_block",Prison.prison_block);
				conf.set("prison_lasttext",Prison.jail_lasttext);
				conf.set("var",Var.var);
				Map<String,String> colorstr = new HashMap<String,String>();
				for(Entry<String, ChatColor> p: Color.color.entrySet()){
					ChatColor color = p.getValue();
					String chatcolor;
					if(color.equals(ChatColor.AQUA)){
						chatcolor = "AQUA";
					}else if(color.equals(ChatColor.BLACK)){
						chatcolor = "BLACK";
					}else if(color.equals(ChatColor.BLUE)){
						chatcolor = "BLUE";
					}else if(color.equals(ChatColor.DARK_AQUA)){
						chatcolor = "DARK_AQUA";
					}else if(color.equals(ChatColor.DARK_BLUE)){
						chatcolor = "DARK_BLUE";
					}else if(color.equals(ChatColor.DARK_GRAY)){
						chatcolor = "DARK_GRAY";
					}else if(color.equals(ChatColor.DARK_GREEN)){
						chatcolor = "DARK_GREEN";
					}else if(color.equals(ChatColor.DARK_PURPLE)){
						chatcolor = "DARK_PURPLE";
					}else if(color.equals(ChatColor.DARK_RED)){
						chatcolor = "DARK_RED";
					}else if(color.equals(ChatColor.GOLD)){
						chatcolor = "GOLD";
					}else if(color.equals(ChatColor.GREEN)){
						chatcolor = "GREEN";
					}else if(color.equals(ChatColor.LIGHT_PURPLE)){
						chatcolor = "LIGHT_PURPLE";
					}else if(color.equals(ChatColor.RED)){
						chatcolor = "RED";
					}else if(color.equals(ChatColor.WHITE)){
						chatcolor = "WHITE";
					}else if(color.equals(ChatColor.YELLOW)){
						chatcolor = "YELLOW";
					}else if(color.equals(ChatColor.GRAY)){
						chatcolor = "GRAY";
					}else{
						chatcolor = null;
					}
					colorstr.put(p.getKey(), chatcolor);
				}
				conf.set("color", colorstr);
				conf.set("jao",Pointjao.jao);
				conf.set("maxplayer",MyMaid.maxplayer);
				conf.set("maxplayertime",MyMaid.maxplayertime);
				plugin.saveConfig();
				Method.SendMessage(sender, cmd, "MyMaid Config Saved!");
				return true;
			}else{
				Method.SendMessage(sender, cmd, "Usage: \"/mymaid jaoadd jao\" OR \"/mymaid load\" OR \"/mymaid save\"");
				return true;
			}
		}else{
			Method.SendMessage(sender, cmd, "Usage: \"/mymaid jaoadd jao\" OR \"/mymaid load\" OR \"/mymaid save\"");
			return true;
		}
	}
	// Real Method
	public static GameProfile getNonPlayerProfile(String skinURL, boolean randomName) {
		GameProfile newSkinProfile = new GameProfile(UUID.randomUUID(), randomName ? getRandomString(16) : null);
		newSkinProfile.getProperties().put("textures", new Property("textures", Base64Coder.encodeString("{textures:{SKIN:{url:\"" + skinURL + "\"}}}")));
		return newSkinProfile;
	}

	// Example Usage
	@SuppressWarnings("deprecation")
	public static void setSkullWithNonPlayerProfile(String skinURL, boolean randomName, Block skull) {
		if(skull.getType() != Material.SKULL) throw new IllegalArgumentException("Block must be a skull." + skull.getType());
		BlockPosition bp = new BlockPosition(skull.getX(), skull.getY(), skull.getZ());
		TileEntitySkull skullTile = (TileEntitySkull)((CraftWorld)skull.getWorld()).getHandle().getTileEntity(bp);
		skullTile.setGameProfile(getNonPlayerProfile(skinURL, randomName));
		skull.getWorld().refreshChunk(skull.getChunk().getX(), skull.getChunk().getZ());
	}

	// Util
	public static String getRandomString(int length) {
		StringBuilder b = new StringBuilder(length);
		for(int j = 0; j < length; j++)
			b.append(chars.charAt(random.nextInt(chars.length())));
		return b.toString();
	}
}
