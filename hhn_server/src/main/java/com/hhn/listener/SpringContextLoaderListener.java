package com.hhn.listener;

import com.hhn.util.Base;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import java.util.Enumeration;

/**
 * Created by lenovo on 2014/12/10.
 */
public class SpringContextLoaderListener extends ContextLoaderListener {
    private String webPath;
    @Override
    public void contextInitialized(ServletContextEvent event) {
        super.contextInitialized(event);
        Base.setApplicationContext(WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext()));
//        Base.setApplicationContext(createWebApplicationContext(event.getServletContext()));
        Base.setWebPath(event.getServletContext().getRealPath("/"));
/*        String classpath=event.getServletContext().getAttribute("org.apache.catalina.jsp_classpath").toString();
        Base.setJsp_classpath(classpath.substring(0, classpath.indexOf(";")));*/
/*        System.out.println(Base.getWebPath());
//        System.out.println(Base.getJsp_classpath());
        System.out.println(event.getServletContext().getAttribute("org.apache.catalina.jsp_classpath"));
        System.out.println("=====================AttributeNames===============");
        Enumeration<String> enumeration=event.getServletContext().getAttributeNames();
        while (enumeration.hasMoreElements()){
            String key=enumeration.nextElement();
            System.out.println(key+"====>"+event.getServletContext().getAttribute(key));
        }*/

    }

    public String getWebPath() {
        return webPath;
    }

    public void setWebPath(String webPath) {
        this.webPath = webPath;
    }
}
