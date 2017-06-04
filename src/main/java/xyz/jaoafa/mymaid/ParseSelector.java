package xyz.jaoafa.mymaid;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.EntityType;

/**
 * killコマンド等で可変するプレイヤーを指定するための仕組み、セレクターを判定するクラス
 * @author mine_book000
 */
public class ParseSelector {
	boolean valid = true;
	String selector;
	Map<String, String> args = new HashMap<String, String>();
	/**
	 * ParseSelectorクラスの作成
	 * @param SelectorText セレクター
	 * @return ParseSelectorクラス
	 * @throws IllegalArgumentException 指定されたセレクターが適切でなかった場合に発生します。!
	 * @author mine_book000
	 */
	public ParseSelector(String SelectorText) throws IllegalArgumentException{
		Pattern p = Pattern.compile("^@(.)(.*)$");
		Matcher m = p.matcher(SelectorText);
		if(!m.find()){
			valid = false;
			throw new IllegalArgumentException("セレクターテキストがセレクターとして認識できません。");
		}

		selector = m.group(1);

		if(m.group(2).equals("[]")){
			throw new IllegalArgumentException("セレクターの引数が認識できません。");
		}

		p = Pattern.compile("^\\[(.+)\\]$");
		m = p.matcher(m.group(2));
		if(!m.find()){
			return;
		}

		if(m.group(1).equals("")){
			throw new IllegalArgumentException("セレクターの引数が認識できません。");
		}

		String[] specialargs = {"x", "y", "z", "r"};

		if(!m.group(1).contains(",")){
			String arg = m.group(1);
			if(arg.contains("=")){
				String[] key_value = arg.split("=");
				String key = key_value[0];
				String value = key_value[1];
				this.args.put(key, value);
			}else{
				throw new IllegalArgumentException("セレクターの1番目の引数が認識できません。");
			}
			return;
		}

		String[] args = m.group(1).split(",");
		int i = 0;
		for(String arg : args){
			if(arg.contains("=")){
				String[] key_value = arg.split("=");
				String key = key_value[0];
				String value = key_value[1];
				this.args.put(key, value);
			}else{
				if(specialargs.length <= i){
					valid = false;
					throw new IllegalArgumentException("セレクターの" + (i + 1) +"番目の引数が認識できません。");
				}
				if(!isNumber(arg)){
					valid = false;
					throw new IllegalArgumentException("セレクターの" + (i + 1) +"番目の引数がint型ではありません。");
				}
				String key = specialargs[i];
				String value = arg;
				this.args.put(key, value);
				i++;
			}
		}
	}
	private boolean isNumber(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	@SuppressWarnings("deprecation")
	public boolean isValidValues(){
		if(valid == false){
			return false;
		}
		if(args.containsKey("x")){
			if(!isNumber(args.get("x"))){
				return false;
			}
		}
		if(args.containsKey("y")){
			if(!isNumber(args.get("y"))){
				return false;
			}
		}
		if(args.containsKey("z")){
			if(!isNumber(args.get("y"))){
				return false;
			}
		}
		if(args.containsKey("r")){
			if(!isNumber(args.get("r"))){
				return false;
			}
		}
		if(args.containsKey("type")){
			for(EntityType type : EntityType.values()){
				if(!"Player".equals(args.get("type"))){
					if(!type.getName().equals(args.get("type"))){
						return false;
					}
				}
			}
		}
		return true;
	}
	public String getSelector(){
		return "@" + selector;
	}
	public Map<String, String> getArgs(){
		return args;
	}
}
