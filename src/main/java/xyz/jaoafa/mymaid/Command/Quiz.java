package xyz.jaoafa.mymaid.Command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import xyz.jaoafa.mymaid.Method;
import xyz.jaoafa.mymaid.Pointjao;

public class Quiz implements CommandExecutor {
	JavaPlugin plugin;
	public Quiz(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public static Map<Integer, Map<String, String>> quiz = new HashMap<Integer, Map<String, String>>();
	public static Map<Integer, Map<Integer, Integer>> answer = new HashMap<Integer, Map<Integer, Integer>>();
	public static Map<Integer, Map<String, Boolean>> answered = new HashMap<Integer, Map<String, Boolean>>();
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(args.length >= 4){
			if(args[0].equalsIgnoreCase("q")){
				// Question
				int quizcount = args.length - 2;
				Map<String, String> data = new HashMap<String, String>();
				Map<Integer, Integer> answers = new HashMap<Integer, Integer>();
				data.put("player", sender.getName());
				data.put("question", args[1]);
				String questions = "";
				int c = 2;
				while(args.length > c){
					if((args.length-1) == c){
						data.put((c-1)+"", args[c]);
						answers.put((c-1), 0);
						questions += (c-1) + ": 「" + args[c] + "」";
						break;
					}
					data.put((c-1)+"", args[c]);
					answers.put((c-1), 0);
					questions += (c-1) + ": 「" + args[c] + "」";
					c++;
					if(args.length != c){
						questions += ", ";
					}
				}
				int qid;
				while(true){
					qid = (int)(Math.random() * 99999 + 10000);
					if(!quiz.containsKey(qid)){
						break;
					}
				}
				if (sender instanceof Player) {
					Player player = (Player) sender;
					int use = 20;
					if(!Pointjao.hasjao(player, use)){
					 	 Method.SendMessage(sender, cmd, "このコマンドを使用するためのjaoPointが足りません。");
					 	 return true;
					}
					Pointjao.usejao(player, use);
				}

				quiz.put(qid, data);
				answer.put(qid, answers);
				answered.put(qid, new HashMap<String, Boolean>());
				Bukkit.broadcastMessage("[QUIZ] " + ChatColor.GREEN + sender.getName() + "がクイズを開始しました。");
				Bukkit.broadcastMessage("[QUIZ] " + ChatColor.GREEN + "質問: 「" + args[1] + "」");
				Bukkit.broadcastMessage("[QUIZ] " + ChatColor.GREEN + "回答選択: " + questions);
				Bukkit.broadcastMessage("[QUIZ] " + ChatColor.GREEN + "回答形式:「/quiz " + qid + " [1-" + quizcount + "]」");
				return true;
			}else{
				Method.SendMessage(sender, cmd, "--- Quiz Help ---");
				Method.SendMessage(sender, cmd, "/quiz q <Question> <Answer...>: クイズを作成します。");
				Method.SendMessage(sender, cmd, "/quiz a <QuestionID> <AnswerID>: クイズの回答を発表します。");
				Method.SendMessage(sender, cmd, "/quiz <QuestionID> <AnswerID>: クイズに答えます。");
				return true;
			}
		}else if(args.length == 3){
			if(args[0].equalsIgnoreCase("a")){
				// Answer
				/*
				String regex = "^[0-9][0-9][0-9][0-9][0-9]$"; //正規表現
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(args[1]);
				if (!m.find()){
					sender.sendMessage("[QUIZ] " + ChatColor.GREEN + "クイズID(5桁の半角数字)を入力してください。");
					return true;
				}
				*/
				int qid;
				int aid;
				try{
					qid = Integer.parseInt(args[1]);
				}catch(java.lang.NumberFormatException e){
					sender.sendMessage("[QUIZ] " + ChatColor.GREEN + "クイズID(5桁の半角数字)を入力してください。");
					return true;
				}
				try{
					aid = Integer.parseInt(args[2]);
				}catch(java.lang.NumberFormatException e){
					sender.sendMessage("[QUIZ] " + ChatColor.GREEN + "回答ID(半角数字)を入力してください。");
					return true;
				}
				if(!quiz.containsKey(qid)){
					sender.sendMessage("[QUIZ] " + ChatColor.GREEN + "指定されたクイズIDのクイズは見つかりません。");
					return true;
				}
				if(!quiz.get(qid).containsKey(aid+"")){
					sender.sendMessage("[QUIZ] " + ChatColor.GREEN + "指定された回答IDの回答は見つかりません。");
					return true;
				}
				String player = quiz.get(qid).get("player");
				String question = quiz.get(qid).get("question");
				String answer = quiz.get(qid).get(aid+"");
				if(!player.equals(sender.getName())){
					sender.sendMessage("[QUIZ] " + ChatColor.GREEN + "クイズの正解回答設定は出題者のみです。");
					return true;
				}
				int c = 1;
				String answers = "";
				while(true){
					if(!Quiz.answer.get(qid).containsKey(c)){
						break;
					}
					answers += c + ": " + Quiz.answer.get(qid).get(c);
					if(Quiz.answer.get(qid).containsKey((c+1))){
						answers += " | ";
					}
					c++;
				}
				quiz.remove(qid);
				Quiz.answer.remove(qid);
				answered.remove(qid);
				Bukkit.broadcastMessage("[QUIZ] " + ChatColor.GREEN + "クイズ「" + question + "(クイズID:" + qid + ")」の解答が発表されました。");
				Bukkit.broadcastMessage("[QUIZ] " + ChatColor.GREEN + "答えは" + aid + "の「" + answer + "」でした。");
				Bukkit.broadcastMessage("[QUIZ] " + ChatColor.GREEN + "回答 " + answers);
				return true;
			}else{
				Method.SendMessage(sender, cmd, "--- Quiz Help ---");
				Method.SendMessage(sender, cmd, "/quiz q <Question> <Answer...>: クイズを作成します。");
				Method.SendMessage(sender, cmd, "/quiz a <QuestionID> <AnswerID>: クイズの回答を発表します。");
				Method.SendMessage(sender, cmd, "/quiz <QuestionID> <AnswerID>: クイズに答えます。");
				return true;
			}
		}else if(args.length == 2){
			/*
			String regex = "^[0-9][0-9][0-9][0-9][0-9]$"; //正規表現
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(args[0]);
			if (!m.find()){
				sender.sendMessage("[QUIZ] " + ChatColor.GREEN + "クイズID(5桁の半角数字)を入力してください。");
				return true;
			}
			*/
			int qid;
			int aid;
			try{
				qid = Integer.parseInt(args[0]);
			}catch(java.lang.NumberFormatException e){
				sender.sendMessage("[QUIZ] " + ChatColor.GREEN + "クイズID(5桁の半角数字)を入力してください。");
				return true;
			}
			try{
				aid = Integer.parseInt(args[1]);
			}catch(java.lang.NumberFormatException e){
				sender.sendMessage("[QUIZ] " + ChatColor.GREEN + "回答ID(半角数字)を入力してください。");
				return true;
			}
			if(!quiz.containsKey(qid)){
				sender.sendMessage("[QUIZ] " + ChatColor.GREEN + "指定されたクイズIDのクイズは見つかりません。");
				return true;
			}
			if(!quiz.get(qid).containsKey(aid+"")){
				sender.sendMessage("[QUIZ] " + ChatColor.GREEN + "指定された回答IDの回答は見つかりません。");
				return true;
			}
			if(answered.get(qid).containsKey(sender.getName())){
				sender.sendMessage("[QUIZ] " + ChatColor.GREEN + "指定されたクイズIDのクイズに回答済みです。");
				return true;
			}
			String question = quiz.get(qid).get("question");
			String answer = quiz.get(qid).get(aid+"");

			Quiz.answer.get(qid).put(aid, Quiz.answer.get(qid).get(aid) + 1);
			answered.get(qid).put(sender.getName(), true);

			sender.sendMessage("[QUIZ] " + ChatColor.GREEN + "「" + question + "」に「" + answer + "」と回答しました。");
			return true;
		}else{
			Method.SendMessage(sender, cmd, "--- Quiz Help ---");
			Method.SendMessage(sender, cmd, "/quiz q <Question> <Answer...>: クイズを作成します。");
			Method.SendMessage(sender, cmd, "/quiz a <QuestionID> <AnswerID>: クイズの回答を発表します。");
			Method.SendMessage(sender, cmd, "/quiz <QuestionID> <AnswerID>: クイズに答えます。");
			return true;
		}
	}
}
