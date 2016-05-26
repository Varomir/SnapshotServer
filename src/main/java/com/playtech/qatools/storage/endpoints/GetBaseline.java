package com.playtech.qatools.storage.endpoints;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ClientEndpoint
@ServerEndpoint(value = "/get/")
public class GetBaseline {

    @OnOpen
    public void onWebSocketConnect(Session session) {
        session.setMaxTextMessageBufferSize(2 * 1024 * 1024);
    }

    @OnMessage
    public void onWebSocketText(String message, Session session) {
        System.out.println("Endpoint [GetBaseline], received TEXT message length: " + message.length());
        try {
            session.getBasicRemote().sendText(message.toUpperCase());
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    @OnClose
    public void onWebSocketClose() {
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }
}
