package ccc.cj.siber.main;


import ccc.cj.siber.util.SiberFileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.io.IOException;

public class EmbeddedJetty {

    private static final Logger logger = LogManager.getLogger(EmbeddedJetty.class);

    private static final int PORT = 8088;

    private static final String CONTEXT_PATH = "/";
    private static final String CONFIG_LOCATION_PACKAGE = "ccc.cj.siber.config";
    private static final String MAPPING_URL = "/";
//    private static final String WEBAPP_DIRECTORY = "webapp";

    public static void main(String[] args) throws Exception {
        new EmbeddedJetty().startJetty(PORT);
    }

    private void startJetty(int port) throws Exception {
        try {
            logger.debug("Starting server at port {}", port);
            Server server = new Server(port);

            server.setHandler(getServletContextHandler());

            addRuntimeShutdownHook(server);

            server.start();
            logger.info("Server started at port {}", port);
            server.join();
        } catch (Throwable t) {
            logger.error("", t);
        }
    }

    private static ServletContextHandler getServletContextHandler() throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS); // SESSIONS requerido para JSP 
        contextHandler.setErrorHandler(null);

        String jarPath = EmbeddedJetty.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (jarPath.substring(jarPath.length() - 1).equals("/")) {
            jarPath = jarPath.substring(0, jarPath.length() - 2);
        }
//        String parentPath = jarPath.substring(0, jarPath.lastIndexOf("/"));
//        String dataPath = parentPath.substring(0, parentPath.lastIndexOf("/")) + File.separator + "siber_data";
        String dataPath = System.getProperty("user.home") + File.separator + "siber_data";
        logger.debug("dataPath is " + dataPath);
        //如果dataPath不存在，则创建
        SiberFileUtils.mkdirIfNotExist(dataPath);
        System.setProperty("dataPath", dataPath);
        contextHandler.setResourceBase(dataPath);
        contextHandler.setContextPath(CONTEXT_PATH);

        // Spring
        WebApplicationContext webAppContext = getWebApplicationContext();
        DispatcherServlet dispatcherServlet = new DispatcherServlet(webAppContext);
        ServletHolder springServletHolder = new ServletHolder("default", dispatcherServlet);
        contextHandler.addServlet(springServletHolder, MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(webAppContext));


        return contextHandler;
    }

    private static WebApplicationContext getWebApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION_PACKAGE);
        return context;
    }

    private static void addRuntimeShutdownHook(final Server server) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (server.isStarted()) {
                    server.setStopAtShutdown(true);
                    try {
                        logger.debug("stop the server");
                        server.stop();
                    } catch (Exception e) {
                        System.out.println("Error while stopping jetty server: " + e.getMessage());
                        logger.error("Error while stopping jetty server: " + e.getMessage(), e);
                    }
                }
            }
        }));
    }

}
