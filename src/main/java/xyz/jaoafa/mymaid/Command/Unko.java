package xyz.jaoafa.mymaid.Command;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.MyMaid;


public class Unko implements CommandExecutor {
	JavaPlugin plugin;
	public Unko(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		int count = 1;
		if (!(sender instanceof Player)) {
			Method.SendMessage(sender, cmd, "このコマンドはゲーム内から実行してください。");
			Bukkit.getLogger().info("ERROR! コマンドがゲーム内から実行されませんでした。");
			return true;
		}
		Player player = (Player) sender;
		Random rnd = new Random();
		List<String> chattext = new ArrayList<String>();
		chattext.add("{1} “垣根”は相手が作っているのではなく、自分が作っている。 - アリストテレス");
		chattext.add("{2} “足して２で割る”案は最悪になる。 - 加賀見俊夫");
		chattext.add("{3} 「ああ、ここにおれの進むべき道があった！ようやく掘り当てた！」こういう感投詞を心の底から叫び出される時、貴方がたははじめて心を安んずる事ができるのだろう。 - 夏目漱石");
		chattext.add("{4} 「それも、いいじゃないか」は、おもしろい人生のスローガン。 - メーソン・クーリー");
		chattext.add("{5} 「一喜一憂」をのぞいて我々の人生にいったい何が残るというのか？ - 野中日文");
		chattext.add("{6} 「運」ってやつは、たえず変わる。いま後頭部にがんと一撃くらわせたかと思うと、次の瞬間には砂糖をほおばらせてくれたりする。問題はただひとつ、へこたれてしまわないことだ。 - Ａ・シリトー");
		chattext.add("{7} 「己の長を以って人の短をあらわすなかれ」人は往々にして自分の長所を標準として他人の短所を責めがちなんだ。 - 久保田美文");
		chattext.add("{8} 「私は～しなければならない」と私たちが言う時はいつも、実際にそれをやる場合より、すっと多くのエネルギーを消耗しているのだ。 - ギタ・ベリン");
		chattext.add("{9} 「真面目になる」ということは、しばしば「憂鬱になる」ということの外の、何のいい意味でもありはしない。 - 萩原朔太郎");
		chattext.add("{10} １つのドアが閉まれば、もう１つのドアが必ず開きます。それはバランスをとるための、自然の法則なのだ。 - ブライアン・アダムス");
		chattext.add("{11} １試合にわたって集中力を維持するためには、適度にリラックスすることが絶対に必要だと思う。 - ジミー・コーナーズ");
		chattext.add("{12} ３０分ぐらいでは何もできないと考えているより、世の中の一番つまらぬことでもする方がまさっている。 - ゲーテ");
		chattext.add("{13} アイデアの秘訣は執念である。 - 湯川秀樹");
		chattext.add("{14} アイデアは、それを一心に求めてさえいれば必ず生まれる。 - チャップリン");
		chattext.add("{15} あきらめてしまうと、癒しようのない不幸も和らげる。 - ホラティウス");
		chattext.add("{16} あくる朝起きたら、また違う風が吹いているからね。 - 河合隼雄");
		chattext.add("{17} 貴方がたとえ氷のように潔癖で雪のように潔白であろうとも、世の悪口はまぬがれまい。 - シェイクスピア");
		chattext.add("{18} 貴方がたの人間性を心にとどめ、そして他のことを忘れよ。 - アインシュタイン");
		chattext.add("{19} 貴方が今、夢中になっているものを大切にしなさい。それは貴方が真に求めているものだから。 - エマーソン");
		chattext.add("{20} 貴方が持ち合わせた力に余る強さなど、人生は要求しない。貴方にたて得るただひとつの手柄は、そこから逃げないこと。 - ダグ・マハーショルド");
		chattext.add("{21} 貴方が成すべきことを成さねばならないのなら、したいことをするための賢明さも持たなくてはならない。 - マルコム・フォーブズ");
		chattext.add("{22} 貴方と世の中との戦いなら、世の中のほうに賭けなさい。 - カフカ");
		chattext.add("{23} 貴方に配られたトランプのカードは不利ではない。貴方の考えや感情が不利にも有利にも作用するのだ。 - マーフィー");
		chattext.add("{24} 貴方の考えが積極的で、建設的で愛に満ちていれば正しい結論は必然的に得られます。 - マーフィー");
		chattext.add("{25} 貴方の周りを変えようとしてもほとんど意味がない。まず最初に、自分の信念を変えなさい。そうすれば、貴方の周りのあらゆることがそれに応じて、変わる。 - ブライアン・アダムス");
		chattext.add("{26} 貴方の心からくるものは、人の心を動かす。 - ドン・シベット");
		chattext.add("{27} 貴方の心が正しいと感じることを行いなさい。行なえば非難されるだろうが、行なわなければ、やはり非難されるのだから。 - エリノア・ルーズベルト");
		chattext.add("{28} 貴方の進歩を妨げているのは、貴方が何であるかではなく、貴方が自分を何だと思っているかである。 - デニス・ウェイトリー");
		chattext.add("{29} 貴方の人生は貴方の思いどおりに変えられる。なぜなら貴方自身によってデザインされるのが貴方の人生だからだ。 - マーフィー");
		chattext.add("{30} 貴方の知る最善をなせ。もし貴方がランナーであれば走れ、鐘であれば鳴れ。 - イグナス・バーンスタイン");
		chattext.add("{31} 貴方は他人の責任をとる必要はない。貴方が他人に対して負っていることといえば、それは愛と善意だ。 - マーフィー");
		chattext.add("{32} 貴方は無心になろうと努めている。つまり貴方は故意に無心なのである。それではこれ以上進むはずがない。 - ヘリゲル");
		chattext.add("{33} あまり人を理解できるとは思わない。わかるのは、好きか嫌いかだけだ。 - Ｅ・Ｍ・フォースター");
		chattext.add("{34} あまり多くを求めないことだ。とくに他人に対しては。 - レオ・Ｃ・ローステン");
		chattext.add("{35} あめつちの初めは今日より始まる。 - 北畠親房");
		chattext.add("{36} あらあらしい毒づいた言葉は、根拠の弱いものであることが多い。 - ビクトル・ユーゴー");
		chattext.add("{37} ありあまるほど自由な時間のある人間は、たいてい悪いことを考えるものである。 - スピノザ");
		chattext.add("{38} 過去はもはや関係がなく、未来はまだ来ぬ。 - セネカ");
		chattext.add("{39} ある選択をするということは、その選択によって生まれるはずのマイナスをすべて背負うぞ、ということでやんしょ。 - 井上ひさし");
		chattext.add("{40} あれもいい、これもいいという生き方はどこにもねえや。あっちがよけりゃこっちが悪いに決まっているのだから、これだと思ったときに盲滅法に進まなけりゃ嘘だよ。 - 尾崎士郎");
		chattext.add("{41} 貴方の遊びに言い訳はいらない。言い訳を決して言うな。 - シェイクスピア");
		chattext.add("{42} いかなるものも変化しつつある。これは真理だ。だから貴方がいまどんな苦境にあろうとも、その状態を保持する努力をしない限り、永久に続くはずはないのだ。 - マーフィー");
		chattext.add("{43} いかなる時でも、お辞儀はし足りないよりも、し過ぎたほうがいい。 - トルストイ");
		chattext.add("{44} いかに弱き人といえども、その全力を単一の目的に集中すれば必ずその事を成し得べし。 - 春日潜庵");
		chattext.add("{45} いかんものは、いくら考えてもよくならん。 - 高野泰明");
		chattext.add("{46} いくら粉飾したところで、自分の生地は誤魔化し切れない。正直こそが、処世の一番安全な道。 - 松下幸之助");
		chattext.add("{47} いくら儲けたいの、いくら儲けねばならんのと、そんな横着な考えでは人間生きてゆけるものではない。 - 豊田佐吉");
		chattext.add("{48} いさめてくれる部下は、一番槍をする勇士より値打ちがある。 - 徳川家康");
		chattext.add("{49} いずかたをも捨てじと心にとり持ちては、一事もなるべからず。 - 吉田兼好");
		chattext.add("{50} いたずらに過ごす月日の多けれど、道を求める時ぞ少なき。 - 道元");
		chattext.add("{51} いつまでも人を恨んでいてはならない。貴方が恨んでいる人は、人生を楽しんでいる。 - ハケット");
		chattext.add("{52} いつまで一緒にいられるか分からないということをしっかり心にとめてお互いを大切にしよう。 - ジョシュア・リーブマン");
		chattext.add("{53} いつも楽しく暮らすよう心がければ、外的環境から完全にあるいはほとんど解放される。 - ロバート・ルイス・スティーブンソン（英 小説家）");
		chattext.add("{54} いまの世の中、人間が人間を見捨てているのよね。親が子を、子が親を、兄が弟を、友が友を、隣人が隣人を。 - マザー・テレサ");
		chattext.add("{55} いや、人生は気合だね。 - 二葉亭四迷");
		chattext.add("{56} いわゆる十分に力を出す者に限って、おのれに十二分の力があり、十二分の力を出した者がおのれに十五分の力あることがわかってくる。 - 新渡戸稲造");
		chattext.add("{57} うぬぼれというものがついぞなかったら、人生はてんで楽しくあるまい。 - ラ・ロシュフーコー");
		chattext.add("{58} うまくいかなかった日は、寝る前に自問する。今ここで何かできることがあるのか、と。なければぐっすり寝る。 - Ｌ・Ｌ・コルベルト");
		chattext.add("{59} うわべになにか「徳」のしるしをつけないような素直な「悪」はない。 - シェイクスピア");
		chattext.add("{60} おお 人情に通じた人よ！彼は子どもたちの相手をするときは子どもっぽいしぐさをする。だが、樹木と子どもは、頭上のものを求めるものなのだ。 - フリードリヒ・ヘルデルリーン");
		chattext.add("{61} おこないはおれのもの、批判は他人のもの、おれの知ったことじゃない。 - 勝海舟");
		chattext.add("{62} おだやかな心は問題を解決します。怒りにふるえ、悲しみに打ちひしがれ、嫉妬に狂った心は問題をますます混乱させます。問題の解決は心のおだやかな時にしなさい。 - マーフィー");
		chattext.add("{63} おのれの職分を守り黙々として勤めることは、中傷に対する最上の答えである。 - ワシントン");
		chattext.add("{64} おのれの得るところを軽んずるなかれ。 - 釈迦");
		chattext.add("{65} お前は、他人のなかにある自分と同じ欠点をむち打とうとするのか。 - シェイクスピア");
		chattext.add("{66} おもしろきこともなき世をおもしろく　住みなすものは心なりけり。 - 高杉晋作");
		chattext.add("{67} およそ人を扱う場合には、相手を論理の動物だと思ってはならない。相手は感情の動物であり、しかも偏見に満ち、自尊心と虚栄心によって行動するということを、よく心得ておかねばならない。 - Ｄ・カーネギー");
		chattext.add("{68} おれは、かつて、おれ自身に惚れこんだことがなかった。自分に惚れこみ、自分の才を信じて事を行えば、人の世に不運などはあるまい。 - 司馬遼太郎");
		chattext.add("{69} お互いに手をつなぐ時にも間をあけよう。 - カーリル・ギブラン");
		chattext.add("{70} お前がそれを抑えつければ抑えつけるほど、ますます燃え上がるよ。静かにささやくように流れていく流れも、せき止められればカンシャクを起こしたように暴れ出すわね。だけどそのさわやかな流れが阻まれなければ、エナメルをかけた石に触れて快い音を奏でるわね。 - シェイクスピア");
		chattext.add("{71} お前の分別に一粒の愚かさを混ぜておきたまえ。時をみてばかなことをするのもよいことだ。 - ホラティウス");
		chattext.add("{72} お前は熊から、のがれようとしている。しかし、その途中で荒れ狂う大海に出会って、もう一度、獣の口のほうへ引きかえすのか？ - シェイクスピア");
		chattext.add("{73} かぎりを行うのが人の道にして、そのことの成ると成らざるとは人の力におよばざるところぞ。 - 本居宣長");
		chattext.add("{74} こういう無批判な愛は嬉しかった。それなら、こちらも惜しみなく愛してやれたから。 - マーガレット・ロセッティ");
		chattext.add("{75} こういう曖昧なものを理屈できっぱりさせようなんてぇのは、理づめで気違いになろうてなもんだよ。 - テレンティウス");
		chattext.add("{76} こうしようか、ああしようか迷った時は、必ず積極的な方へいく。 - ジェームス三木");
		chattext.add("{77} ここで今これ以上骨を折っても無駄だ！バラならば、花咲くだろう。 - ゲーテ");
		chattext.add("{78} ことごとくの雲が嵐をなすというわけではない。 - シェイクスピア");
		chattext.add("{79} この世にて慈悲も悪事もせぬ人は、さぞや閻魔も困りたまはん - 一休");
		chattext.add("{80} この世は、考える者にとっては喜劇であり、感じるものにとっては悲劇である。 - ウォルポール");
		chattext.add("{81} この世は絶え間のないシーソーだ。 - モンテーニュ");
		chattext.add("{82} 人生というものは、あらゆる規範からはなれ、もっと底抜けに自由であっていいはずだ。 - 八尋舜右");
		chattext.add("{83} この道を行けばどうなるものか、危ぶむなかれ、危ぶめば道はなし、踏み出せばその一歩が道となる、迷わずゆけよ、ゆけばわかる。 - 一休");
		chattext.add("{84} この年になると、じっとしているだけで、うれしいことが向こうからやってくる。 - 加藤楸邨");
		chattext.add("{85} しかし、所詮は人間、いかに優れた者でも時には我を忘れます。 - シェイクスピア");
		chattext.add("{86} しなくちゃいけない仕事には、何か楽しめる要素があるもの。 - ディズニー");
		chattext.add("{87} しばしの別離は再会をいっそう快いものにする。 - ミルトン");
		chattext.add("{88} しゃべったことより、しゃべらずにいたことのほうが力がある。 - ジャーミー");
		chattext.add("{89} ずいぶん敵も持ったけど、妻よ、お前のようなヤツは初めてだ。 - バイロン");
		chattext.add("{90} すぐれたジョークは、すぐれたアイデアに通じる。 - 本田宗一郎");
		chattext.add("{91} すべての日がそれぞれの贈り物をもっている。 - マルティアリス");
		chattext.add("{92} すべての不幸は未来への踏み台にすぎない。 - ソロー");
		chattext.add("{93} すべては、待っている間に頑張った人のもの。 - トーマス・エジソン");
		chattext.add("{94} すべては原因、結果の法則によります。運命論者のいう運・不運は、貴方の思考や行動と無縁ではない。 - マーフィー");
		chattext.add("{95} すべてをいますぐに知ろうとは無理なこと。雪が解ければ見えてくる。 - ゲーテ");
		chattext.add("{96} すべてを納得すれば、心はきわめて寛大になる。 - スタール夫人");
		chattext.add("{97} すべて軍陣などに臨みて面白しとだに思えば、恐ろしきとこも失せて、自ら計策も出てくるなれ。 - 徳川秀忠");
		chattext.add("{98} すべて人の不仕合せの時、別けて立ち入り、見舞い、付け届けつかまつるべきなり。 - 山本常朝");
		chattext.add("{99} ぜがひでも欲しいと思うものは何でも得られる。それには皮膚から噴き上げ、世界を創造したエネルギーと合流する、あふれんばかりの熱情でそれを望まなければならない。 - シーラ・グレアム");
		chattext.add("{100} そうあるべきことは、やがてそうなっていくだろう。 - アイスキュロス");
		int ran = rnd.nextInt(chattext.size());
		String text = chattext.get(ran-1);

		String Msg = "";
		if(player.hasPermission("mymaid.pex.limited")){
			Msg = player.getName().replaceFirst(player.getName(), ChatColor.BLACK + "■" + ChatColor.WHITE + player.getName());

		}else if(Prison.prison.containsKey(player.getName())){
			Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_GRAY + "■" + ChatColor.WHITE + player.getName());
		}else if(MyMaid.chatcolor.containsKey(player.getName())){
			int i = Integer.parseInt(MyMaid.chatcolor.get(player.getName()));
			if(i >= 0 && i <= 5){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.WHITE + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 6 && i <= 19){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_BLUE + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 20 && i <= 33){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.BLUE + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 34 && i <= 47){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.AQUA + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 48 && i <= 61){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_AQUA + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 62 && i <= 76){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_GREEN + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 77 && i <= 89){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.GREEN + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 90 && i <= 103){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.YELLOW + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 104 && i <= 117){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.GOLD + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 118 && i <= 131){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.RED + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 132 && i <= 145){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_RED + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 146 && i <= 159){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.DARK_PURPLE + "■" + ChatColor.WHITE + player.getName());
			}else if(i >= 160){
				Msg = player.getName().replaceFirst(player.getName(), ChatColor.LIGHT_PURPLE + "■" + ChatColor.WHITE + player.getName());
			}
		}else{
			Msg = player.getName().replaceFirst(player.getName(), ChatColor.GRAY + "■" + ChatColor.WHITE + player.getName());
		}
		Date Date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Bukkit.broadcastMessage(ChatColor.GRAY + "["+ timeFormat.format(Date) + "]" + Msg + ": "+text);

		for(Player to: Bukkit.getServer().getOnlinePlayers()) {
			new Unko_Unko(plugin, player, to).runTaskLater(plugin, (20 * count));
			count++;
		}
		return true;
	}
	private class Unko_Unko extends BukkitRunnable{
    	Player player;
    	Player to;
    	public Unko_Unko(JavaPlugin plugin, Player player, Player to) {
    		this.player = player;
    		this.to = to;
    	}
		@Override
		public void run() {
			player.teleport(to);
			player.getWorld().playSound(to.getLocation().add(0, 2, 0), Sound.GHAST_SCREAM,10,1);
			player.getWorld().playEffect(to.getLocation().add(0, 2, 0), Effect.HEART, 0);
		}
	}
}
