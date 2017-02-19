package com.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.jaoafa.mymaid.Method;
import com.jaoafa.mymaid.MyMaid;

import net.minecraft.server.v1_10_R1.EntityArmorStand;
import net.minecraft.server.v1_10_R1.EntityBat;
import net.minecraft.server.v1_10_R1.EntityBlaze;
import net.minecraft.server.v1_10_R1.EntityCaveSpider;
import net.minecraft.server.v1_10_R1.EntityChicken;
import net.minecraft.server.v1_10_R1.EntityCow;
import net.minecraft.server.v1_10_R1.EntityCreeper;
import net.minecraft.server.v1_10_R1.EntityEnderman;
import net.minecraft.server.v1_10_R1.EntityEndermite;
import net.minecraft.server.v1_10_R1.EntityGhast;
import net.minecraft.server.v1_10_R1.EntityGiantZombie;
import net.minecraft.server.v1_10_R1.EntityGuardian;
import net.minecraft.server.v1_10_R1.EntityHorse;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.EntityMushroomCow;
import net.minecraft.server.v1_10_R1.EntityOcelot;
import net.minecraft.server.v1_10_R1.EntityPig;
import net.minecraft.server.v1_10_R1.EntityPigZombie;
import net.minecraft.server.v1_10_R1.EntityRabbit;
import net.minecraft.server.v1_10_R1.EntitySheep;
import net.minecraft.server.v1_10_R1.EntitySilverfish;
import net.minecraft.server.v1_10_R1.EntitySkeleton;
import net.minecraft.server.v1_10_R1.EntitySlime;
import net.minecraft.server.v1_10_R1.EntitySpider;
import net.minecraft.server.v1_10_R1.EntitySquid;
import net.minecraft.server.v1_10_R1.EntityVillager;
import net.minecraft.server.v1_10_R1.EntityWitch;
import net.minecraft.server.v1_10_R1.EntityWolf;
import net.minecraft.server.v1_10_R1.EntityZombie;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_10_R1.PacketPlayOutSpawnEntityLiving;

public class MyMob implements CommandExecutor {

	JavaPlugin plugin;
	public MyMob(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	public static Map<String, String> mymob = new HashMap<String, String>();
	public static Map<String, Integer> myint = new HashMap<String, Integer>();
	BukkitTask bt;

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(!(sender instanceof org.bukkit.entity.Player)){
			Method.SendMessage(sender, cmd, "サーバ内で実行してください。");
			return true;
		}
		org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
		if(args.length == 1){
			if(args[0].equalsIgnoreCase("on")){
				if(mymob.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "既にあなたは他Mobです。");
					return true;
				}
				mymob.put(player.getName(), "Zombie");
				if(mymob.size() == 1){
					bt = new MyBlockRunCheck().runTaskTimer(plugin, 0, 1);
				}
				for(Player p: Bukkit.getServer().getOnlinePlayers()){
					if(!p.getName().equalsIgnoreCase(player.getName())){
						p.hidePlayer(player);
					}
				}
				Method.SendMessage(sender, cmd, "あなたを他Mobに変更しました。");
				Method.SendMessage(sender, cmd, "自分からは他Mobには見えませんが、まわりには他Mobに見えるはずです。");
				Method.SendMessage(sender, cmd, "初期設定は/mymob set [Mob]で行えます。");
				return true;
			}else if(args[0].equalsIgnoreCase("off")){
				if(!mymob.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "あなたは他Mobではないようです。");
					return true;
				}
				mymob.remove(player.getName());
				if(mymob.size() == 0){
					bt.cancel();
				}
				for(Player p: Bukkit.getServer().getOnlinePlayers()){
					if(!p.getName().equalsIgnoreCase(player.getName())){
						p.showPlayer(player);
					}
				}
				Method.SendMessage(sender, cmd, "あなたは他Mobでは無くなりました。");
				return true;
			}
		}else if(args.length == 2){
			if(args[0].equalsIgnoreCase("set")){
				if(!mymob.containsKey(player.getName())){
					Method.SendMessage(sender, cmd, "あなたは他Mobではないようです。");
					return true;
				}
				String entityname = args[1];
				Boolean boo = false;
				if("Zombie".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Creeper".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Skeleton".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Spider".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Slime".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Ghast".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("PigZombie".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Enderman".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("CaveSpider".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Silverfish".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Blaze".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Bat".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Witch".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Endermite".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Guardian".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Pig".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Sheep".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Cow".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Chicken".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Squid".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Wolf".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Mooshroom".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Ocelot".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Horse".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Rabbit".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Villager".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("ArmorStand".equalsIgnoreCase(entityname)){
					boo = true;
				}else if("Giant".equalsIgnoreCase(entityname)){
					boo = true;
				}
				if(!boo){
					mymob.put(player.getName(), entityname);
					Method.SendMessage(sender, cmd, "選択されたMobにはなれません。");

					return true;
				}
				mymob.put(player.getName(), entityname);
				Method.SendMessage(sender, cmd, "あなたは「" + entityname + "」になりました。");

				return true;
			}
		}
		Method.SendMessage(sender, cmd, "MyMob HELP");
		Method.SendMessage(sender, cmd, "/mymob on: あなたを他Mobに変更します。");
		Method.SendMessage(sender, cmd, "/mymob off: あなたが他Mobから戻します。");
		Method.SendMessage(sender, cmd, "/mymob set [Mob]: あなたの他Mobを変えます。");
		return true;
	}
	private class MyBlockRunCheck extends BukkitRunnable {
		@Override
		public void run() {
			if(MyMaid.nextbakrender){
				if(mymob.size() == 0){
					bt.cancel();
				}
				for(Entry<String, String> data : mymob.entrySet()) {
				    Player player = Bukkit.getPlayer(data.getKey());
				    if(!player.isOnline()){
				    	for(Player p: Bukkit.getServer().getOnlinePlayers()){
							p.showPlayer(player);
						}
				    	mymob.remove(data.getKey());
				    }
					for(Player p: Bukkit.getServer().getOnlinePlayers()){
						p.hidePlayer(player);
					}
					CraftPlayer cp = ((CraftPlayer) player);
					//CraftWorld cw = ((CraftWorld) player.getWorld());

					String entityname = data.getValue();
					EntityLiving e = null;

					if("Zombie".equalsIgnoreCase(entityname)){
						e = new EntityZombie(cp.getHandle().world);
					}else if("Creeper".equalsIgnoreCase(entityname)){
						e = new EntityCreeper(cp.getHandle().world);
					}else if("Skeleton".equalsIgnoreCase(entityname)){
						e = new EntitySkeleton(cp.getHandle().world);
					}else if("Spider".equalsIgnoreCase(entityname)){
						e = new EntitySpider(cp.getHandle().world);
					}else if("Slime".equalsIgnoreCase(entityname)){
						e = new EntitySlime(cp.getHandle().world);
					}else if("Ghast".equalsIgnoreCase(entityname)){
						e = new EntityGhast(cp.getHandle().world);
					}else if("PigZombie".equalsIgnoreCase(entityname)){
						e = new EntityPigZombie(cp.getHandle().world);
					}else if("Enderman".equalsIgnoreCase(entityname)){
						e = new EntityEnderman(cp.getHandle().world);
					}else if("CaveSpider".equalsIgnoreCase(entityname)){
						e = new EntityCaveSpider(cp.getHandle().world);
					}else if("Silverfish".equalsIgnoreCase(entityname)){
						e = new EntitySilverfish(cp.getHandle().world);
					}else if("Blaze".equalsIgnoreCase(entityname)){
						e = new EntityBlaze(cp.getHandle().world);
					}else if("Bat".equalsIgnoreCase(entityname)){
						e = new EntityBat(cp.getHandle().world);
					}else if("Witch".equalsIgnoreCase(entityname)){
						e = new EntityWitch(cp.getHandle().world);
					}else if("Endermite".equalsIgnoreCase(entityname)){
						e = new EntityEndermite(cp.getHandle().world);
					}else if("Guardian".equalsIgnoreCase(entityname)){
						e = new EntityGuardian(cp.getHandle().world);
					}else if("Pig".equalsIgnoreCase(entityname)){
						e = new EntityPig(cp.getHandle().world);
					}else if("Sheep".equalsIgnoreCase(entityname)){
						e = new EntitySheep(cp.getHandle().world);
					}else if("Cow".equalsIgnoreCase(entityname)){
						e = new EntityCow(cp.getHandle().world);
					}else if("Chicken".equalsIgnoreCase(entityname)){
						e = new EntityChicken(cp.getHandle().world);
					}else if("Squid".equalsIgnoreCase(entityname)){
						e = new EntitySquid(cp.getHandle().world);
					}else if("Wolf".equalsIgnoreCase(entityname)){
						e = new EntityWolf(cp.getHandle().world);
					}else if("Mooshroom".equalsIgnoreCase(entityname)){
						e = new EntityMushroomCow(cp.getHandle().world);
					}else if("Ocelot".equalsIgnoreCase(entityname)){
						e = new EntityOcelot(cp.getHandle().world);
					}else if("Horse".equalsIgnoreCase(entityname)){
						e = new EntityHorse(cp.getHandle().world);
					}else if("Rabbit".equalsIgnoreCase(entityname)){
						e = new EntityRabbit(cp.getHandle().world);
					}else if("Villager".equalsIgnoreCase(entityname)){
						e = new EntityVillager(cp.getHandle().world);
					}else if("ArmorStand".equalsIgnoreCase(entityname)){
						e = new EntityArmorStand(cp.getHandle().world);
					}else if("Giant".equalsIgnoreCase(entityname)){
						e = new EntityGiantZombie(cp.getHandle().world);
					}

					if(myint.containsKey(player.getName())){
						PacketPlayOutEntityDestroy oldpa = new PacketPlayOutEntityDestroy(new int[]{myint.get(player.getName())});
						for(org.bukkit.entity.Player o : Bukkit.getOnlinePlayers()){
							if(!o.getName().equalsIgnoreCase(player.getName())){
								((CraftPlayer)o).getHandle().playerConnection.sendPacket(oldpa);
							}
						}
					}

					PacketPlayOutEntityDestroy p29 = new PacketPlayOutEntityDestroy(new int[]{player.getEntityId()});

					if(e != null){
						Location loc = cp.getLocation();
						e.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
						e.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

						PacketPlayOutSpawnEntityLiving p24 = new PacketPlayOutSpawnEntityLiving(e);

						for(org.bukkit.entity.Player o : Bukkit.getOnlinePlayers()){
							if(!o.getName().equalsIgnoreCase(player.getName())){
								((CraftPlayer)o).getHandle().playerConnection.sendPacket(p29);
								((CraftPlayer)o).getHandle().playerConnection.sendPacket(p24);
							}
						}
						myint.put(player.getName(), e.getId());
					}

					PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + "あなたは「" + entityname + "」になっています。" + "\"}"), (byte) 2);
			        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);

				}
			}
		}
	}
}
