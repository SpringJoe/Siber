package ccc.cj.siber.config;

import ccc.cj.siber.util.SiberFileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;

/**
 * 该类只有在用tomcat启动项目的时候才会用到，如果用jetty启动，则不会用到。
 */
public class SiberInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    private static final Logger logger = LogManager.getLogger(SiberInitializer.class);

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebMvcConfigurer.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        String jarPath = SiberInitializer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (jarPath.substring(jarPath.length() - 1).equals("/")) {
            jarPath = jarPath.substring(0, jarPath.length() - 2);
        }
//        String tempPath = jarPath.substring(0, jarPath.lastIndexOf("/"));
//        tempPath = tempPath.substring(0, tempPath.lastIndexOf("/"));
//        tempPath = tempPath.substring(0, tempPath.lastIndexOf("/"));
//        tempPath = tempPath.substring(0, tempPath.lastIndexOf("/"));

        String tempPath = System.getProperty("user.home");

        String dataPath = tempPath + File.separator + "siber_data";
        logger.debug("dataPath is " + dataPath);
        //如果dataPath不存在，则创建
        SiberFileUtils.mkdirIfNotExist(dataPath);
        System.setProperty("dataPath", dataPath);
    }


}