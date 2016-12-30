package xyz.jaoafa.mymaid;

import java.util.HashMap;
import java.util.Map;

/**
 * 言語コンフィグ
 * @author mine_book000
*/
public class ConfLang {

	/**
	 * 言語コンフィグ初期設定
	 * @param lang プレイヤーのLanguage
	 * @return
	 * @author mine_book000
	*/
	public static String getConfLang(Language lang, String Type){
		if(lang == Language.JAPANESE){
			return LangJPConf(Type);
		}else if(lang == Language.ENGLISH){
			return LangENConf(Type);
		}else{
			return LangJPConf(Type);
		}
	}

	private static String LangJPConf(String Type){
		Map<String, String> lang = new HashMap<String, String>();
		lang.put("DynmapTeleporterToPlayerMessage", "$LOCATION にテレポートされました！");
		lang.put("DynmapTeleporterToALLMessage", "$PLAYERNAME は $LOCATION にワープしました");

		if(lang.containsKey(Type)){
			return lang.get(Type);
		}else{
			throw new NullPointerException("Language Not Found");
		}
	}

	private static String LangENConf(String Type){
		Map<String, String> lang = new HashMap<String, String>();
		lang.put("DynmapTeleporterToPlayerMessage", "You have been teleported to $LOCATION!");
		lang.put("DynmapTeleporterToALLMessage", "Teleported $PLAYERNAME to $LOCATION");

		if(lang.containsKey(Type)){
			return lang.get(Type);
		}else{
			throw new NullPointerException("Language Not Found");
		}
	}
}
