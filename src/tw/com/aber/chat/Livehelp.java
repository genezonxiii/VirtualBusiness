
package tw.com.aber.chat;

import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

public class Livehelp {

	private static final Log log = LogFactory.getLog(Livehelp.class);

	private static final AtomicInteger connectionIds = new AtomicInteger(0);
	private static final Set<Livehelp> connections = new CopyOnWriteArraySet<>();
	private static String livehelper = "DemoUser";

	private String color;
	private String nickname;
	private Session session;

	public Livehelp() {
	}

	public String getName() {
		return nickname;
	}

	@OnOpen
	public void start(@PathParam("Username") String username, Session session) {
		this.session = session;
		this.nickname = username;
		connectionIds.incrementAndGet();
		connections.add(this);

		int online = 0;
		for (Livehelp client : connections) {
			if (livehelper.equals(client.getName())) {
				online = 1;
			}
		}
		broadcast("System(>_<):" + (online == 1 ? "online" : "offline"), this.getName());
		broadcast("System(>_<):Total:" + connectionIds, livehelper);
	}

	@OnClose
	public void end() {
		connectionIds.decrementAndGet();
		connections.remove(this);
		int online = 0;
		for (Livehelp client : connections) {
			if (livehelper.equals(client.getName())) {
				online = 1;
			}
		}
		broadcast("System(>_<):Leave:" + this.getName(), livehelper);
		broadcast("System(>_<):" + (online == 1 ? "online" : "offline"), this.getName());
		broadcast("System(>_<):Total:" + connectionIds, livehelper);
	}

	@OnMessage
	public void incoming(String message) {
		// Never trust the client
		if (livehelper.equals(this.getName())) {
			String[] msg = message.split(":");
			if (msg.length < 2) {
				return;
			}
			String filteredMessage = String.format("<font class='tag'>%s</font> <b>%s</b> : %s",
					(new SimpleDateFormat("[HH:mm]").format(new Date())),
					"<font color='" + "blue" + "' name='name'>" + nickname + "</font>", filter(msg[1].toString()));
			broadcast(filteredMessage, msg[0]);
		} else {
			String filteredMessage = String.format("<font class='tag'>%s</font> <b>%s</b> : %s",
					(new SimpleDateFormat("[HH:mm]").format(new Date())),
					"<font color='" + "#e600c3" + "' name='name'>" + nickname + "</font>", filter(message.toString()));
			broadcast(filteredMessage, this.getName());
		}
	}

	@OnError
	public void onError(Throwable t) throws Throwable {
		log.error("Chat Error: " + t.toString(), t);
	}

	private static void broadcast(String msg, String withwho) {
		for (Livehelp client : connections) {
			try {
				synchronized (client) {
					if (withwho.equals(client.getName()) || livehelper.equals(client.getName())
							|| msg.contains("System(>_<):")) {
						client.session.getBasicRemote().sendText(msg);
					}
				}
			} catch (IOException e) {
				log.debug("Chat Error: Failed to send message to client", e);
				connections.remove(client);
				try {
					client.session.close();
				} catch (IOException e1) {
				}
				String message = String.format("* %s %s", client.nickname, "has been disconnected.");
				broadcast(message, withwho);
			}
		}
	}

	public static String filter(String message) {

		if (message == null)
			return (null);

		char content[] = new char[message.length()];
		message.getChars(0, message.length(), content, 0);
		StringBuilder result = new StringBuilder(content.length + 50);
		for (int i = 0; i < content.length; i++) {
			switch (content[i]) {
			case '<':
				result.append("&lt;");
				break;
			case '>':
				result.append("&gt;");
				break;
			case '&':
				result.append("&amp;");
				break;
			case '"':
				result.append("&quot;");
				break;
			default:
				result.append(content[i]);
			}
		}
		return (result.toString());

	}
}
