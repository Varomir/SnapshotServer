package com.playtech.qatools.storage.endpoints;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
@ServerEndpoint(value = "/put/")
public class PutBaseline {

    @OnOpen
    public void onWebSocketConnect(Session session) {
        session.setMaxTextMessageBufferSize(2 * 1024 * 1024);
    }

    @OnMessage
    public void onWebSocketText(String message) {
        System.out.println("Endpoint [PutBaseline], received TEXT message length: " + message.length());
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }
}
