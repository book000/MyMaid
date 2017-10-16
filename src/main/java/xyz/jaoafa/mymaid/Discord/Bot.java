package xyz.jaoafa.mymaid.Discord;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.plugin.java.JavaPlugin;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import xyz.jaoafa.mymaid.BugReport;

public class Bot {
	JavaPlugin plugin;
	public Bot(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/*
	 * ヘルプコマンドは/jaohelp
	 *
	 */

	@EventSubscriber
	public void ChatEvent(MessageReceivedEvent event) {
		String message = event.getMessage().getContent();
		if(message.contains(" ")){
			String[] args = message.split(" ");
			if(!args[0].startsWith("/")){
				return;
			}
			//String cmdName = args[0];
			String cmdName = StringUtils.stripStart(args[0], "/");
			Command command = EnumUtils.getEnum(Command.class, cmdName);
			if(command == null){
				return;
			}
			args[0] = cmdName; // [0]をスラッシュ無しに
			command.onCommand(event, args);
		}else{
			if(!message.startsWith("/")){
				return;
			}
			//String cmdName = args[0];
			String cmdName = StringUtils.stripStart(message, "/");
			Command command = EnumUtils.getEnum(Command.class, cmdName);
			if(command == null){
				return;
			}
			String[] args = {cmdName};
			command.onCommand(event, args);
		}
	}
	public static enum Command {
		changegame {
			@Override
			public void onCommand(final MessageReceivedEvent event, final String[] args) {
				if(args.length == 1){
					reply(event, "引数が足りません");
					return;
				}
				String game = StringUtils.join(args, " ", 1, args.length);
				Discord.setGame(game);
				reply(event, "Game Changed.");
			}
			@Override
			public String getHelpMessage(){
				return "jaotanの実行中ゲームを変更します。";
			}
		},
		jaohelp {
			@Override
			public void onCommand(MessageReceivedEvent event, String[] args) {
				String commands = Arrays.stream(Command.values()).filter(command -> command != Command.jaohelp).map(Command::name).collect(Collectors.joining(" "));
				reply(event, "```" + commands + "```");
			}
			@Override
			public String getHelpMessage(){
				return "MyMaidで実装されているjaotanコマンドのヘルプを表示します。";
			}
		};

		public abstract void onCommand(MessageReceivedEvent event, String[] args);
		public abstract String getHelpMessage();
	}
	private static void reply(MessageReceivedEvent event, String message){
		try {
			event.getMessage().reply(message);
		} catch (MissingPermissionsException e) {
			// TODO 自動生成された catch ブロック
			BugReport.report(e);
		} catch (RateLimitException e) {
			// TODO 自動生成された catch ブロック
			BugReport.report(e);
		} catch (DiscordException e) {
			// TODO 自動生成された catch ブロック
			BugReport.report(e);
		}
	}
}
