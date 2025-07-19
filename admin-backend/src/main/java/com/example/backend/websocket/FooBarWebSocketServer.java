package com.example.backend.websocket;

import com.alibaba.fastjson2.JSONObject;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/v2/ws/foo-bar/{platform}") // platform 为调用客户端：frontend, ...
public class FooBarWebSocketServer {

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String uuid;
    private String platform;

    // session集合,存放对应的session
    private static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    // concurrent包的线程安全Set,用来存放每个客户端对应的WebSocket对象。
    private static final CopyOnWriteArraySet<FooBarWebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 建立WebSocket连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("platform") String platform) throws IOException {
        // 为该用户创建一个唯一id
        String uuid = UUID.randomUUID().toString();

        // 建立连接
        this.session = session;
        this.uuid = uuid;
        this.platform = platform;

        webSocketSet.add(this);
        sessionPool.put(uuid, session);

        // 打招呼
        session.getBasicRemote().sendText("{\"type\":\"heartbeat\"}");
    }

    /**
     * 发生错误
     *
     * @param throwable e
     */
    @OnError
    public void onError(Throwable throwable) {
        log.error("onError", throwable);
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        sessionPool.remove(this.uuid);
    }

    /**
     * 接收客户端消息
     *
     * @param message 接收的消息
     */
    @OnMessage
    public void onMessage(String message) {
        try {
            if ("ping".equals(message)) { // 心跳
                this.session.getBasicRemote().sendText("{\"type\":\"heartbeat\"}");
                return;
            } else if ("hello".equals(message)) { // 首次连接打招呼
                return;
            }

            log.info("收到 {} 发来的消息：{}", platform, message);
        } catch (Exception error) {
            // 前端传的数据格式不对
            log.info("{} 传的数据格式不对", platform);
        }
    }

    /**
     * 推送消息到当前客户端
     *
     * @param webSocketRes 发送的消息
     */
    public void sendMessage(WebSocketRes webSocketRes) {
        Session session = this.session;
        try {
            session.getBasicRemote().sendText(webSocketRes.toJSON());
        } catch (IOException e) {
            log.error("推送消息到当前 {} 时发生错误", platform, e);
        }
    }

    /**
     * 推送消息到指定客户端
     *
     * @param session      session
     * @param webSocketRes 发送的消息
     */
    public static void sendMessageBySession(Session session, WebSocketRes webSocketRes) {
        try {
            session.getBasicRemote().sendText(webSocketRes.toJSON());
        } catch (IOException e) {
            log.error("通过 Session 推送消息到指定客户端时发生错误", e);
        }
    }

    /**
     * 推送消息到指定客户端
     *
     * @param uuid         uuid
     * @param webSocketRes 发送的消息
     */
    public static void sendMessageByUuid(String uuid, WebSocketRes webSocketRes) {
        Session session = sessionPool.get(uuid);
        try {
            session.getBasicRemote().sendText(webSocketRes.toJSON());
        } catch (IOException e) {
            log.error("通过 uuid 推送消息到指定客户端时发生错误", e);
        }
    }

    /**
     * 群发消息
     *
     * @param webSocketRes 发送的消息
     */
    public static int sendAllMessage(JSONObject webSocketRes) {
        for (FooBarWebSocketServer webSocket : webSocketSet) {
            try {
                webSocket.session.getBasicRemote().sendText(webSocketRes.toString());
            } catch (IOException e) {
                log.error("群发消息发生错误", e);
            }
        }
        return webSocketSet.size();
    }

    /**
     * 群发消息
     *
     * @param content 发送的消息
     */
    public static int sendAllMessage(List<String> platformList, String module, String info, Object content) {
        for (FooBarWebSocketServer webSocket : webSocketSet) {
            if (!platformList.isEmpty() && !platformList.contains(webSocket.platform)) {
                continue;
            }
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("platform", webSocket.platform);
                jsonObject.put("module", module);
                jsonObject.put("type", "json");
                jsonObject.put("info", info);
                jsonObject.put("content", content);
                webSocket.session.getBasicRemote().sendText(jsonObject.toString());
            } catch (IOException e) {
                log.error("群发消息发生错误", e);
            }
        }
        return webSocketSet.size();
    }

    /**
     * @see #sendAllMessage
     */
    public static int sendAllMessage(String platform, String module, String info, Object content) {
        return sendAllMessage(Collections.singletonList(platform), module, info, content);
    }

    /**
     * @see #sendAllMessage
     */
    public static int sendAllMessage(String module, String info, Object content) {
        return sendAllMessage(Collections.emptyList(), module, info, content);
    }

    /**
     * 获取当前在线的客户端数量
     *
     * @return
     */
    public static int getClientCount() {
        return sessionPool.size();
    }
}
