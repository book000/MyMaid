package xyz.jaoafa.mymaid;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MyMaid extends JavaPlugin {

	BukkitTask task = null;//あとで自分で自分を止めるためのもの
	@Override
    public void onEnable() {
    	getLogger().info("(c) jao Minecraft Server MyMaid Project.");
    	getLogger().info("Product by tomachi.");

    }

    @Override
    public void onDisable() {
        // TODO ここに、プラグインが無効化された時の処理を実装してください。
    }



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        // プレイヤーが「/basic」コマンドを投入した際の処理...
    	if(cmd.getName().equalsIgnoreCase("jf")){
    		final Player player = (Player) sender;
    		Date date1 = new Date();
    		SimpleDateFormat date = new SimpleDateFormat("H:m:s");
    		Bukkit.broadcastMessage(ChatColor.GRAY + "[" + date.format(date1) +"]"  + ChatColor.WHITE + player.getName() +  ": jao");
    		task = new BukkitRunnable() {

                @Override
                public void run() {
                	Date date1 = new Date();
        			SimpleDateFormat date = new SimpleDateFormat("H:m:s");
        			Bukkit.broadcastMessage(ChatColor.GRAY + "["+ date.format(date1) + "]" + ChatColor.WHITE + player.getName() +  ": afa");
                    task.cancel();
                }
            }.runTaskLater(this, 60);
            // ↑そのままスケジュールします。
    		return true;
		}
    	if(cmd.getName().equalsIgnoreCase("ct")) {
			if(args.length == 0){
				sender.sendMessage("[CT] " + ChatColor.GREEN + "このコマンドは1つまたは2つの引数が必要です。");
				return false;
			}else if(args.length == 1){
				// コマンド実行者がプレイヤーかどうか
				if (!(sender instanceof Player)) {
					sender.sendMessage("[CT] " + ChatColor.GREEN + "このコマンドはゲーム内から実行してください。");
					getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
					return true;
				}
				String location = args[0];

				try{
					URL url=new URL("http://toma.webcrow.jp/jaoget.php?location=" + location);
					// URL接続
					HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
					connect.setRequestMethod("GET");//プロトコルの設定
					InputStream in=connect.getInputStream();//ファイルを開く
					String data;//ネットから読んだデータを保管する変数を宣言
					if(location.equalsIgnoreCase("list")){
						sender.sendMessage("[CT] " + ChatColor.GREEN + "----- Location List -----");
						data = readString(in);//1行読み取り
						while (data != null) {//読み取りが成功していれば
							sender.sendMessage("[CT] " + ChatColor.GREEN + data);
							data = readString(in);//次を読み込む
						}
						sender.sendMessage("[CT] " + ChatColor.GREEN + "-------------------------");
						return true;
					}else{
						data = readString(in);
						if(data.equalsIgnoreCase("NOLOCATION")){
							sender.sendMessage("[CT] " + ChatColor.GREEN + "その名前の場所は登録されていません。/ct listで場所を確認してください。");
							return true;
						}else{
							String[] datas = data.split(",", 0);
							String x = datas[0];
							String y = datas[1];
							String z = datas[2];
							String world = datas[3];
							Bukkit.dispatchCommand(sender, "mvtp " + world);
							Bukkit.dispatchCommand(sender, "tp " + x + " " + y + " " + z);
							return true;
						}
					}

				}catch(Exception e){
					//例外処理が発生したら、表示する
					System.out.println(e);
					sender.sendMessage("エラーが発生しました。詳しくはサーバーログを確認してください。");
				}
			}else if(args.length == 2){
				String p;
				p = args[1];
				for(Player player: getServer().getOnlinePlayers()) {
					if(player.getName().equalsIgnoreCase(p)) {
						if(Bukkit.dispatchCommand(player, "ct " + args[0])){
							sender.sendMessage("[CT] " + ChatColor.GREEN + "正常に処理を実行しました。");
						}else{
							sender.sendMessage("[CT] " + ChatColor.GREEN + "エラーが発生しました。");
						}
						return true;
					}
				}
				sender.sendMessage("[CT] " + ChatColor.GREEN + "ユーザーが見つかりません");
			}


		}
    	return false;
        // コマンドが実行されなかった場合は、falseを返して当メソッドを抜ける。
    }
    String[] datas;
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("ct")) return super.onTabComplete(sender, command, alias, args);
        if (args.length == 1) {
            if (args[0].length() == 0) { // /testまで
            	try{
					URL url=new URL("http://toma.webcrow.jp/jaoget.php?tab=all");
					// URL接続
					HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
					connect.setRequestMethod("GET");//プロトコルの設定
					InputStream in=connect.getInputStream();//ファイルを開く

					String data;//ネットから読んだデータを保管する変数を宣言
					data = readString(in);
					if(data == null){
						return null;
					}
					if(!data.contains(",")){
						return Collections.singletonList(data);
					}
					datas = data.split(",", 0);
					return Arrays.asList(datas);
				}catch(Exception e){
					//例外処理が発生したら、表示する
					System.out.println(e);
					sender.sendMessage("エラーが発生しました。詳しくはサーバーログを確認してください。");
				}
            } else {
            	try{
					URL url=new URL("http://toma.webcrow.jp/jaoget.php?tab=" + args[0]);
					// URL接続
					HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
					connect.setRequestMethod("GET");//プロトコルの設定
					connect.setRequestProperty("Accept-Language", "jp");
					connect.setRequestProperty("Content-Type","text/html;charset=utf-8");
					InputStream in=connect.getInputStream();//ファイルを開く
					String data;//ネットから読んだデータを保管する変数を宣言
					data = readString(in);
					if(data == null){
						return null;
					}
					if(!data.contains(",")){
						return Collections.singletonList(data);
					}
					datas = data.split(",", 0);
					return Arrays.asList(datas);
				}catch(Exception e){
					//例外処理が発生したら、表示する
					System.out.println(e);
					sender.sendMessage("エラーが発生しました。詳しくはサーバーログを確認してください。");
				}
            }
        }
        //JavaPlugin#onTabComplete()を呼び出す
        return super.onTabComplete(sender, command, alias, args);
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
