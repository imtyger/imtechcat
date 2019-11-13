package com.website.imtechcat.common.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer {

    private static int onlineCount = 0;

    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    private Session session;

    private String sid="";

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid){
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        log.info("有新窗口开始监听："+ sid + ",当前在线人数：" + getOnlineCount());
        this.sid = sid;
        try {
            sendMessage("连接成功");
        }catch (IOException e){
            log.error("websocket IO 异常");
        }
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有一连接关闭，当前在线人数:" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session){
        log.info("收到来自窗口：" + sid + "的信息：" + message);
        //群发消息
        for(WebSocketServer item : webSocketSet){
            try{
                item.sendMessage(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(String session, Throwable error){
        log.error("发生错误");
        error.printStackTrace();
    }


    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message,@PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口"+sid+"，推送内容:"+message);
        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(sid==null) {
                    item.sendMessage(message);
                }else if(item.sid.equals(sid)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
