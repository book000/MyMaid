package xyz.jaoafa.mymaid.Command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.MyMaid;

public class Vote implements CommandExecutor {
	JavaPlugin plugin;
	public Vote(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/* onCommand jf
	 * jao afaします。
	 * /jf */
	public static FileConfiguration conf;
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		// 変数定義
		if (!(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage("[Vote] " + ChatColor.GREEN + "You do not have permission to perform this command.");
			return true;
		}
		if(args.length != 1){
			return true;
		}
		for(Player p: Bukkit.getServer().getOnlinePlayers()) {
			if(p.getName().equalsIgnoreCase(args[0])) {
				String name = p.getName();
				UUID uuid = p.getUniqueId();
				String i = MyMaid.url_access("http://toma.webcrow.jp/jaovote.php?p="+name+"&u="+uuid);
				MyMaid.chatcolor.put(name, i);
				if(i.equalsIgnoreCase("3")){
					if(p.hasPermission("mymaid.pex.default")){
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user "+p.getName()+" group set R");
						p.setOp(true);
						Bukkit.broadcastMessage("[Vote] " + ChatColor.GREEN + "当鯖への投票を3回していただきましたので、ユーザー「"+p.getName()+"」が常連になりました。");
						return true;
					}
				}
				if(i.equalsIgnoreCase("10")){
					if(p.getName().equalsIgnoreCase(args[0])) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give "+p.getName()+" minecraft:written_book 1 0 {title:\"ティッシュマン\",author:\"Hirotaisou2012\",generation:0,pages:[\"{text:\\\"「うわぁっ、きたない。鼻水なんかつけるなぁっ」。\nティッシュマンは、人間につかまれて鼻にあてられて、鼻水をつけられていた。ようするに、普通のティッシュと同じように。\nそして、ティッシュマンは、ゴミ箱へ入れられた。\\\",color:black}\",\"{text:\\\"「いたいなぁ、全く、人間ってどうしてこんなにひどいんだろう」。\nティッシュマンは、そう言って辺りを見回した。\n暗くて、あまり中が見えない。少し歩くと、壁にぶつかった。\\\",color:black}\",\"{text:\\\"ティッシュマンは、壁を登ってみた。でも、すべってしまい、落ちて体をぶつけてしまった。\n「あー、いたい。もう、なんでおれさまがこんな目にあわなきゃいけないんだ。捨てた野郎のばかやろう」。\\\",color:black}\",\"{text:\\\"ティッシュマンは、さけんだ。\nでも、だぁれも助けにはこなかった。\nティッシュマンは、ここから脱出することを考えた。そして、\n｢体をきたえて、ここを登ろう｣。\nと、考えた。\\\",color:black}\",\"{text:\\\"早速ティッシュマンは体をきたえた。すると、おどろくことに、ポコンッという音がして、手が出てきた。もっと体をきたえると、足も出てきた。\n「おおおおおーっ、手が出た、足が出た、最強のティッシュになったぁっ」。\\\",color:black}\",\"{text:\\\"ティッシュマンが喜んでいると、急にゴミ箱の上から声がした。\n「うわぁーっ」。\nグシャッ！\nティッシュマンは、びっくりしてしりもちをついてしまった。そしてまわりを見ると、見慣れない、変なやつがいた。\\\",color:black}\",\"{text:\\\"そいつは正方形でうすっぺら。片方に色がついている。\n「おまえは、だれだ」。\n　すると、そいつは答えた。\n「おりがみ・・・・・、ぼく、おりがみ」。\n「おりがみ、なんだそれは」。\\\",color:black}\",\"{text:\\\"「おりがみは、からだをこうやっておってね・・・・・」。\n　おりがみくんは半分にからだをまげた。\n「こうやって、こうやって、いろんなものになる紙だよ」。\n「なるほどーっ。じゃあ、ひこうきになって上まで運んでくれるかい」。\n「いいよ」。\\\",color:black}\",\"{text:\\\"おりがみくんは、そう答えると、あっという間にひこうきになった。\n「さあ、乗って」。\n「おうよっ」。\n　ティッシュマンはおりがみくんに飛び乗った。\nと、そのとたん・・・・・\\\",color:black}\",\"{text:\\\"グシャッ！\n「お、重い・・・・・、いったい何㎏あるんだい」。\n「おどろくなよ。体重はなぁ・・・・・、４５．３㎏だ。ハーッハッハッ」。\n「それじゃあ、持ち上がんないよう」。\n「そうか、上がらないかぁ・・・・・、うわぁっ」。\\\",color:black}\",\"{text:\\\"ガコンッ！\n　急にゴミ箱がたおれて、ティッシュマンとおりがみくんは外へ放り出された。そしてそのまま風に飛ばされて、飛んでいった。\n　落ちたところには、ゴミをあさりに来ていたのらねこがいた。\\\",color:black}\",\"{text:\\\"「ニャーッ」。\n　ねこたちは、ひらひらと動いたティッシュマンとおりがみくんを見つけると、おそいかかってきた。\n「にげろーっ」。\nティッシュマンとおりがみくんは、なんとかねこたちからにげきった。ところが水たまりにふみこんでしまい、体が少しとけてしまった。\n「うわぁ、きたないっ」。\\\",color:black}\",\"{text:\\\"しばらくして、夜になった。\n「夜は暗いから、動きやすいなぁ。見つかりにくいから」。\n「うん」。\n二人は公園ににげることにした。公園に着くと、石の上にすわった。\nと、そのとき！\n　バチバチバチッ！\\\",color:black}\",\"{text:\\\"近くで子供たちが、花火を始めたのです。花火からは、火花が飛び散っている。ティッシュマンは、おりがみくんと手をつないでにげた。\n　ところが、もう、手遅れだったのです。二人は、一瞬にして燃えて灰になってしまいました。\n　二人のたましいは、空へと消えていきました。\\\",color:black}\",\"{text:\\\"\n\n\n\n\n\n\n\n\nおしまい\\\",color:black}\",\"{text:\\\"筆者あとがき\n\nこれは僕が小学6年のときに5分で書き上げた雑すぎて日本語がおかしいキモい物語です。小学館主催の12歳の文学賞にて、石川県一位をなぜか取ってしまったゴミ小説です。読み終わったら処分しましょう。\\\",color:black}\"]}");
						return true;
					}
				}
			}
		}
		return true;
	}
}
