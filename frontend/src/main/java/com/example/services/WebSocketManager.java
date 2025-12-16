package com.example.services;

import com.example.dto.response.SendGroupMsgResDTO;
import com.example.dto.response.SendInboxMsgResDTO;
import com.example.util.HttpClientUtil;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class WebSocketManager {

    private static WebSocketManager instance;

    private WebSocketStompClient client;
    private StompSession session;
    private StompSession.Subscription currentSub;

    private WebSocketManager() {}

    public boolean isConnected() {
        return session != null && session.isConnected();
    }

    private void log(String msg) {
        System.out.println("[WS][" + System.currentTimeMillis() + "] " + msg);
    }

    public static synchronized WebSocketManager getInstance() {
        if (instance == null) instance = new WebSocketManager();
        return instance;
    }

    private String getJSessionId() {
        CookieManager cm =
                (CookieManager) HttpClientUtil
                        .getClient()
                        .cookieHandler()
                        .orElseThrow();

        return cm.getCookieStore()
                .getCookies()
                .stream()
                .filter(c -> c.getName().equals("JSESSIONID"))
                .findFirst()
                .map(c -> c.getName() + "=" + c.getValue())
                .orElse(null);
    }

    public synchronized void connect() throws Exception {
        if (isConnected()) return;

        this.client =
            new WebSocketStompClient(
                new SockJsClient(
                        List.of(new WebSocketTransport(new StandardWebSocketClient()))
                )
            );

        client.setMessageConverter(new MappingJackson2MessageConverter());

        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

        String jsessionId = getJSessionId();
        if (jsessionId != null) {
            headers.add("Cookie", jsessionId);
        }

        CompletableFuture<StompSession> future =
                client.connectAsync(
                        "http://localhost:8080/ws",
                        headers,
                        new StompSessionHandlerAdapter() {
                            @Override
                            public void afterConnected(
                                    StompSession session,
                                    StompHeaders connectedHeaders) {
                                System.out.println("[WS] Connected");
                                log("CONNECTED sessionId=" + session.getSessionId());
                            }

                            @Override
                            public void handleTransportError(
                                    StompSession session,
                                    Throwable exception) {
                                System.err.println("[WS] Transport error");
                                exception.printStackTrace();
                            }
                        }
                );

        this.session = future.get(); // block tại đây (ok cho Swing)
    }

    public synchronized void disconnect() {
        try {
            if (currentSub != null) {
                currentSub.unsubscribe();
                currentSub = null;
            }

            if (session != null) {
                session.disconnect();
                session = null;
            }

            if (client != null) {
                client.stop();
                client = null;
            }

            System.out.println("[WS] Disconnected cleanly");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void unsubscribeCurrent() {
        if (currentSub != null) {
            currentSub.unsubscribe();
            currentSub = null;
        }
    }

    public void subscribeInbox(Long inboxId, Consumer<SendInboxMsgResDTO> handler) {
        unsubscribeCurrent();
        log("SUBSCRIBE inbox -> " );

        currentSub = session.subscribe(
                "/user/queue/inbox." + inboxId,
                new StompFrameHandler() {
                    public Type getPayloadType(StompHeaders headers) {
                        return SendInboxMsgResDTO.class;
                    }

                    public void handleFrame(StompHeaders headers, Object payload) {
                        handler.accept((SendInboxMsgResDTO) payload);
                    }
                }
        );
    }

    public void subscribeGroup(Long groupId, Consumer<SendGroupMsgResDTO> handler) {
        unsubscribeCurrent();

        currentSub = session.subscribe(
                "/topic/group." + groupId,
                new StompFrameHandler() {
                    public Type getPayloadType(StompHeaders headers) {
                        return SendGroupMsgResDTO.class;
                    }

                    public void handleFrame(StompHeaders headers, Object payload) {
                        handler.accept((SendGroupMsgResDTO) payload);
                    }
                }
        );
    }

    public void send(String destination, Object body) {
        session.send(destination, body);
        log("SEND -> " + destination + " payload=" + body);
    }
}
