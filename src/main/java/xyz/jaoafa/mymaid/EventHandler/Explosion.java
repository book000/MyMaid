package xyz.jaoafa.mymaid.EventHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Explosion {
	JavaPlugin plugin;
	public Explosion(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	Boolean tntexplode = true;
	BukkitTask task = null;
	@EventHandler
    public void onExplosion(EntityExplodeEvent e){
    	try {
    		if(tntexplode){
        		BlockState states = null;
            	for(Block block : e.blockList()){
            		states = block.getState();
            		break;
            	}
            	org.bukkit.Location location = states.getLocation();
            	int x = location.getBlockX();
            	int y = location.getBlockY();
            	int z = location.getBlockZ();
            	double min = 1.79769313486231570E+308;
            	Player min_player = null;
            	for(Player player: Bukkit.getServer().getOnlinePlayers()){
            		org.bukkit.Location location_p = player.getLocation();
                	double distance = location.distance(location_p);
                	if(distance < min){
                		min = distance;
                		min_player = player;
                	}
            	}
            	if(min < 20 && min_player.hasPermission("pin_code_auth.joinmsg")){
            		// 無視
            	}else{
            		Boolean tntuser = true;
            		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
            			if(p.hasPermission("pin_code_auth.joinmsg")) {
            				p.sendMessage("[" + ChatColor.RED + "TNT" + ChatColor.WHITE + "] " + ChatColor.GREEN + min_player.getName() + "の近くの" + x + " " + y + " " + z + "地点にてTNTが爆発し、ブロックが破壊されました。確認して下さい。");
            				Bukkit.getLogger().info(min_player.getName() + " near [" + x + " " + y + " " + z + "] TNTExploded.");
            				tntuser = false;
            			}
            		}
            		String name = min_player.getName();
        			UUID uuid = min_player.getUniqueId();
        			url_access("http://toma.webcrow.jp/jaotnt.php?p="+name+"&u="+uuid+"&x="+x+"&y="+y+"&z="+z);

            		task = new BukkitRunnable() {
                        @Override
                        public void run() {
                        	tntexplode = true;
                        	Bukkit.getLogger().info("TNT Exploded notice on");
                            task.cancel();
                        }
                    }.runTaskLater(plugin, 1200);
                    tntexplode = false;
                    Bukkit.getLogger().info("TNT Exploded notice off");
            	}
        	}
    	}catch(Exception e1) {
    		tntexplode = false;
    		Bukkit.getLogger().info(e1.getMessage());
    	}

    	return;
    }
	static String url_access(String address){
		System.out.println("[PCA] URLConnect Start:"+address);
		try{
			URL url=new URL(address);
			// URL接続
			HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
			connect.setRequestMethod("GET");//プロトコルの設定
			InputStream in=connect.getInputStream();//ファイルを開く

			// ネットからデータの読み込み
			String data=readString(in);//1行読み取り
			// URL切断
			in.close();//InputStreamを閉じる
			connect.disconnect();//サイトの接続を切断
			System.out.println("[MyMaid] URLConnect End:"+address);
			return data;
		}catch(Exception e){
			//例外処理が発生したら、表示する
			System.out.println(e);
			System.out.println("[MyMaid] URLConnect Err:"+address);
			return "";
		}
	}
	//InputStreamより１行だけ読む（読めなければnullを返す）
	static String readString(InputStream in){
		try{
			int l;//呼んだ長さを記録
			int a;//読んだ一文字の記録に使う
			byte b[]=new byte[2048];//呼んだデータを格納
			a=in.read();//１文字読む
			if (a<0) return null;//ファイルを読みっていたら、nullを返す
			l=0;
			while(a>10){//行の終わりまで読む
				if (a>=' '){//何かの文字であれば、バイトに追加
					b[l]=(byte)a;
					l++;
				}
				a=in.read();//次を読む
			}
			return new String(b,0,l);//文字列に変換
		}catch(IOException e){
			//Errが出たら、表示してnull値を返す
			System.out.println("Err="+e);
			return null;
		}
	}
}
