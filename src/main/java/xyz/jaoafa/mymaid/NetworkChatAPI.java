package xyz.jaoafa.mymaid;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class NetworkChatAPI {
	JavaPlugin plugin;
	static String NETWORKCHATAPIACCESSTOKEN = null;
	// https://stackoverflow.com/questions/29048932/communication-java-javascript-with-http-requests

	public NetworkChatAPI(JavaPlugin plugin, String NETWORKCHATAPIACCESSTOKEN) throws IOException {
		this.plugin = plugin;
		NetworkChatAPI.NETWORKCHATAPIACCESSTOKEN = NETWORKCHATAPIACCESSTOKEN;
		plugin.getLogger().info("CREATED HTTP PROCESSOR...!");
		HttpServer server = HttpServer.create(new InetSocketAddress(4096), 0);
		server.createContext("/message", new MessageSendHandler());
		plugin.getLogger().info("STARTED HTTP SERVER!");
		server.setExecutor(null); // creates a default executor
		server.start();
	}

	static class MessageSendHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange httpExchange) throws IOException {
			// TODO Auto-generated method stub
			Map<String,String> parms = NetworkChatAPI.queryToMap(httpExchange.getRequestURI().getQuery());
			StringBuilder response = new StringBuilder();

			String token = parms.get("token");

			if(!token.equals(NETWORKCHATAPIACCESSTOKEN)){
				response.append("ERROR! : ACCESS TOKEN is invalid!");
				return;
			}

			String author = parms.get("author");
			String content = parms.get("content");
			Bukkit.broadcastMessage(ChatColor.AQUA + "(Discord) " + ChatColor.RESET + author + ": " + content);
			response.append("OK!");
			NetworkChatAPI.writeResponse(httpExchange, response.toString());
		}
	}

	public static void writeResponse(HttpExchange httpExchange, String response) throws IOException {
		httpExchange.sendResponseHeaders(200, response.length());
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}

	public static Map<String, String> queryToMap(String query){
		Map<String, String> result = new HashMap<String, String>();
		for (String param : query.split("&")) {
			String pair[] = param.split("=");
			if (pair.length>1) {
				result.put(pair[0], pair[1]);
			}else{
				result.put(pair[0], "");
			}
		}
		return result;
	}

}
