package com.jaoafa.mymaid;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Location（座標）をシリアライズ・デシリアライズするためのラッパークラス
 * @author 石橋(<a href="http://www.jias.jp/blog/?78">シリアライズ用のクラスを作ってデータを保存する</a>)
 */
public class SerializableLocation implements ConfigurationSerializable{

	/** ワールド名を保存するキー名 */
	static final private String WORLD = "world";

	/** X座標の値を保存するキー名 */
	static final private String X = "x";

	/** Y座標の値を保存するキー名 */
	static final private String Y = "y";

	/** Z座標の値を保存するキー名 */
	static final private String Z = "z";

	/** ヨーの値（向いている方角。東西南北どちらを向いているかの角度）を保存するキー名 */
	static final private String YAW = "yaw";

	/** ピッチの値（向いている仰俯角。上下どちらを向いているかの角度）を保存するキー名 */
	static final private String PITCH = "pitch";

	/** 座標 */
	private Location loc = null;

	/**
	 * コンストラクタ
	 *
	 * @param loc
	 *            座標
	 */
	public SerializableLocation(Location loc){
		this.loc = loc;
	}

	/**
	 * 座標を返す
	 *
	 * @return 座標
	 */
	public Location getLocation(){
		return loc;
	}

	/**
	 * 座標をシリアライズする
	 *
	 * @return シリアライズされた座標
	 */
	public Map<String,Object> serialize(){

		// マップを作る
		Map<String,Object> map = new HashMap<String,Object>();

		// この座標のワールド名をマップに保存
		map.put(WORLD,loc.getWorld().getName());

		// この座標のX,Y,Z軸の値をマップに保存
		map.put(X,loc.getX());
		map.put(Y,loc.getY());
		map.put(Z,loc.getZ());

		// この座標の角度の値をマップに保存
		map.put(YAW,loc.getYaw());
		map.put(PITCH,loc.getPitch());

		// データを保存したマップを返す（ここで返したマップの中身がconfig.ymlなどに保存される）
		return map;
	}

	/**
	 * 座標をデシリアライズする
	 *
	 * @param map
	 *            シリアライズされた座標
	 * @return デシリアライズされた座標
	 */
	public static SerializableLocation deserialize(Map<String,Object> map){

		// config.ymlなどから読み込まれたマップが引数に渡されるので、このマップを元に座標を作成（復元）する

		// 保存されているワールド名を元にワールドを取得
		World world = Bukkit.getWorld(map.get(WORLD).toString());

		// 保存されているX,Y,Z軸の値を少数として読み込む
		double x = Double.parseDouble(map.get(X).toString());
		double y = Double.parseDouble(map.get(Y).toString());
		double z = Double.parseDouble(map.get(Z).toString());

		// 保存されている角度の値を少数として読み込む
		float yaw = Float.parseFloat(map.get(YAW).toString());
		float pitch = Float.parseFloat(map.get(PITCH).toString());

		// Locationクラスのコンストラクタに読み込んだ6つの引数を渡してオブジェクトを作成
		Location loc = new Location(world,x,y,z,yaw,pitch);

		// 作成したLocationオブジェクトをSerializableLocationでラップして返す
		return new SerializableLocation(loc);
	}
}