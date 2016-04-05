package xyz.jaoafa.mymaid;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.bukkit.plugin.java.JavaPlugin;

public class MyMaid extends JavaPlugin {
	/** 設定ファイルの文字コード（UTF-8の場合） */
	static final private Charset CONFIG_CHAREST=StandardCharsets.UTF_8;

	@Override
    public void onEnable() {
    	getLogger().info("(c) jao Minecraft Server MyMaid Project.");
    	getLogger().info("Product by tomachi.");
    }

    @Override
    public void onDisable() {

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
