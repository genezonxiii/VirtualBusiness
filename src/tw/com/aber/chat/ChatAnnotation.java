/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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

@ServerEndpoint(value = "/websocket/chat/{Username}")
public class ChatAnnotation {

    private static final Log log = LogFactory.getLog(ChatAnnotation.class);

   // private static final String GUEST_PREFIX = "Guest";
    private static final AtomicInteger connectionIds = new AtomicInteger(0);
    private static final Set<ChatAnnotation> connections =
            new CopyOnWriteArraySet<>();
    private String color; 
    private String nickname;
    private Session session;
    //private final String[] default_color={"#e9e744","#666699","#92d5ea","#ee8310","#8d10ee","#5a3b16","#26a4ed","#f45a90","#be1e2d"};
    public ChatAnnotation() {
    	//connectionIds.getAndIncrement();
        //nickname = GUEST_PREFIX + connectionIds.getAndIncrement();
        //nickname = username;
    }
    
    public String getName(){
    	return nickname;
    }

    @OnOpen
    public void start(@PathParam("Username") String username, Session session) {
        this.session = session;
        this.nickname = username;
        //this.color = "#"+Integer.toHexString((int) (Math.random()*4000000+7600000));//Math.random()*700000+300000;
        Color myRGBColor = Color.getHSBColor((float)(Math.random()*360),(float) 1,(float) 0.9);
        
        //String.format("%02d",)
        //String.format("%02d",)
        //String.format("%02d",)
        //myRGBColor.getRed()>10?"":"0";
        this.color=("#"+(myRGBColor.getRed()>10?"":"0")+Integer.toHexString(myRGBColor.getRed())+(myRGBColor.getGreen()>10?"":"0")+Integer.toHexString(myRGBColor.getGreen())+(myRGBColor.getBlue()>10?"":"0")+Integer.toHexString(myRGBColor.getBlue()));
        //if(Integer.parseInt(""+connectionIds)<8){this.color=default_color[Integer.parseInt(""+connectionIds)];}
        //System.out.println(Integer.toHexString(myRGBColor.getRed())+" "+Integer.toHexString(myRGBColor.getGreen())+" "+Integer.toHexString(myRGBColor.getBlue()));
        //System.out.println(this.color);
        connectionIds.incrementAndGet();
        connections.add(this);
        String message = String.format("[<font color=red>系統</font>] %s %s", "<font color='"+color+"'>"+nickname+"</font>", "進入了討論區");
        //message = " * "+username+" has joined.";
        
        
        broadcast(message);
        //connections.forEach(action);
        
        String tmp=""+connectionIds;
        for (ChatAnnotation client : connections) {
        	tmp+=","+"<font color='"+client.color+"'>"+client.getName()+"</font>";
        }
        
        broadcast("System(>_<):"+tmp);
        //broadcast("system(>_<):線上人數共:"+connectionIds+"位 "+tmp);
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
        // Never trust the client
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
//                	if("list".equals(msg)){client.session.getBasicRemote().sendText("thanks");}
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                log.debug("Chat Error: Failed to send message to client", e);
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // Ignore
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
