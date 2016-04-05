package xyz.jaoafa.mymaid;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.bukkit.plugin.java.JavaPlugin;

public class MyMaid extends JavaPlugin {
<<<<<<< HEAD
	@Override
    public void onEnable() {
        // TODO ここに、プラグインが有効化された時の処理を実装してください。
=======
	/** 設定ファイルの文字コード（UTF-8の場合） */
	static final private Charset CONFIG_CHAREST=StandardCharsets.UTF_8;

	@Override
    public void onEnable() {
    	getLogger().info("(c) jao Minecraft Server MyMaid Project.");
    	getLogger().info("Product by tomachi.");
>>>>>>> refs/remotes/origin/tomachi
    }

    @Override
    public void onDisable() {
        // TODO ここに、プラグインが無効化された時の処理を実装してください。
    }

    /*
     *
     * // プラグインフォルダ上の設定ファイルのパスを作る
		String confFilePath = "./plugins/dynmap/markers.yml";

		// 設定ファイルを開く
		try(Reader reader=new InputStreamReader(new FileInputStream(confFilePath),CONFIG_CHAREST)){

			// 設定データ入出力クラスを作る
			FileConfiguration conf=new YamlConfiguration();

			// 設定を読み込む
			conf.load(reader);
		}catch(Exception e){
			// エラーが起きた場合はその旨を表示する
			System.out.println(e);

			// 起動失敗のためこのプラグインを無効にする
			onDisable();
		}
     */
}
