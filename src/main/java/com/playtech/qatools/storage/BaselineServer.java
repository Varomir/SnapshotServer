package com.playtech.qatools.storage;

import com.playtech.qatools.storage.endpoints.GetBaseline;
import com.playtech.qatools.storage.endpoints.PutBaseline;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import javax.websocket.server.ServerContainer;

public class BaselineServer {

    public static void main(String[] args) {

        Integer sslPort = Integer.parseInt(System.getProperty("port", "8081"));
        Server server = new Server();

        SslContextFactory contextFactory = new SslContextFactory();
        contextFactory.setKeyStorePath("./keystore");
        contextFactory.setKeyStorePassword("changeit");
        SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(contextFactory, org.eclipse.jetty.http.HttpVersion.HTTP_1_1.toString());
        HttpConfiguration config = new HttpConfiguration();
        config.setSecureScheme("https");
        config.setSecurePort(sslPort);
        config.setOutputBufferSize(32786);
        config.setRequestHeaderSize(8192);
        config.setResponseHeaderSize(8192);
        HttpConfiguration sslConfiguration = new HttpConfiguration(config);
        sslConfiguration.addCustomizer(new SecureRequestCustomizer());
        HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(sslConfiguration);

        ServerConnector connector = new ServerConnector(server, sslConnectionFactory, httpConnectionFactory);
        connector.setPort(sslPort);
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