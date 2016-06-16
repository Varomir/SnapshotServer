package com.playtech.qatools.storage;

import com.playtech.qatools.storage.endpoints.GetBaseline;
import com.playtech.qatools.storage.endpoints.PutBaseline;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import javax.websocket.server.ServerContainer;

public class BaselineServer {

    public static void main(String[] args) {

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8081);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        try {
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
            wscontainer.addEndpoint(PutBaseline.class);
            wscontainer.addEndpoint(GetBaseline.class);

            server.start();
            server.dump(System.err);
            server.join();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
}