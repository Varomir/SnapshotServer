package com.playtech.qatools.storage.endpoints;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        writeMsgToFile("./target/baseline/", "_guest.json", message);
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }

    private boolean writeMsgToFile(String path, String filename, String message) {
        Path newDirectoryPath = Paths.get(path);
        if (!Files.exists(newDirectoryPath)) {
            try {
                Files.createDirectory(newDirectoryPath);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
                return false;
            }
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path + filename))) {
            writer.write(message);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            return false;
        }
        return true;
    }
}
