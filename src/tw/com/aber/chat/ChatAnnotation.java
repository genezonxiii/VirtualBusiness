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

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

public class ChatAnnotation {

    private static final Log log = LogFactory.getLog(ChatAnnotation.class);

    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<ChatAnnotation> connections =
            new CopyOnWriteArraySet<>();
    private String color; 
    private String nickname;
    private Session session;
    public ChatAnnotation() {
    }
    
    public String getName(){
    	return nickname;
    }

    @OnOpen
    public void start(@PathParam("Username") String username, Session session) {
        this.session = session;
        this.nickname = username;
        Color myRGBColor = Color.getHSBColor((float)(Math.random()*360),(float) 1,(float) 0.9);
        
        this.color=("#"+(myRGBColor.getRed()>10?"":"0")+Integer.toHexString(myRGBColor.getRed())+(myRGBColor.getGreen()>10?"":"0")+Integer.toHexString(myRGBColor.getGreen())+(myRGBColor.getBlue()>10?"":"0")+Integer.toHexString(myRGBColor.getBlue()));
        connectionIds.incrementAndGet();
        connections.add(this);
        String message = String.format("[<font color=red>系統</font>] %s %s", "<font color='"+color+"'>"+nickname+"</font>", "進入了討論區");
        
        broadcast(message);
       
        String tmp=""+connectionIds;
        for (ChatAnnotation client : connections) {
        	tmp+=","+"<font color='"+client.color+"'>"+client.getName()+"</font>";
        }
        
        broadcast("System(>_<):"+tmp);
    }


    @OnClose
    public void end() {
    	connectionIds.decrementAndGet();
        connections.remove(this);
        String message = String.format("[<font color=red>系統</font>] %s %s",
        		"<font color='"+color+"'>"+nickname+"</font>", "離開了討論區.");
        broadcast(message);
        
        String tmp=""+connectionIds;
        for (ChatAnnotation client : connections) {
        	tmp+=","+"<font color='"+client.color+"'>"+client.getName()+"</font>";
        }
        
        broadcast("System(>_<):"+tmp);
        
    }


    @OnMessage
    public void incoming(String message) {
        String filteredMessage = String.format("%s <b>%s</b> : %s",
        		(new SimpleDateFormat("[ HH:mm ]").format(new Date())),"<font color='"+color+"'>"+nickname+"</font>", filter(message.toString()));
        broadcast(filteredMessage);
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        log.error("Chat Error: " + t.toString(), t);
    }


    private static void broadcast(String msg) {
        for (ChatAnnotation client : connections) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                log.debug("Chat Error: Failed to send message to client", e);
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                }
                String message = String.format("* %s %s",
                        client.nickname, "has been disconnected.");
                broadcast(message);
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
