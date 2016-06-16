package com.playtech.qatools.storage.endpoints;

import com.jayway.jsonpath.JsonPath;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ClientEndpoint
@ServerEndpoint(value = "/put/")
public class PutBaseline {

    private StringBuilder deviceDescription;
    private boolean loggedIn;
    private StringBuilder filename = new StringBuilder("_loggedIn_");
    private StringBuilder status;
    private StringBuilder url;
    private StringBuilder snapshot;
    private String part[];

    @OnOpen
    public void onWebSocketConnect(Session session) {
        session.setMaxTextMessageBufferSize(20 * 1024 * 1024);
    }

    @OnMessage
    public void onWebSocketText(String message) {
        System.out.println("Endpoint [PutBaseline], received TEXT message length: " + message.length());
        deviceDescription = new StringBuilder(JsonPath.parse(message).read("$.device", String.class)).append("_");
        deviceDescription.append(JsonPath.parse(message).read("$.OS", String.class)).append("_");
        deviceDescription.append(JsonPath.parse(message).read("$.browser", String.class));

        status = new StringBuilder(JsonPath.parse(message).read("$.status", String.class));
        url = new StringBuilder(JsonPath.parse(message).read("$.url", String.class).replaceAll("^(http|https)://", "").replace("?ui", "").replaceAll(":8080","").replaceAll("[\\?|&](?<name>.*?)=[^&]*&?", ""));
        url.append("/");
        //System.out.println(url);
        loggedIn = JsonPath.parse(message).read("$.loggedIn", Boolean.class);
        filename.append(loggedIn).append(".json");
        deviceDescription.append(filename);
        if (status.toString().equals("ok")) {
            part = message.split("\"snapshot\":");
            snapshot = new StringBuilder(part[1].replaceAll(",\"status\":\"ok\"}", ""));
        } else {
            snapshot = new StringBuilder("{\"status\":\"not ok\"}");
        }
        writeMsgToFile("./target/baseline/" + url, deviceDescription.toString(), snapshot.toString());
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason) {
    }

    @OnError
    public void onWebSocketError(Throwable cause) {
        cause.printStackTrace(System.err);
    }

    private boolean writeMsgToFile(String path, String filename, String message) {
        new File(path).mkdirs();
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
