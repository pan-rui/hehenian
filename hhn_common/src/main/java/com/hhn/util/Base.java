package com.hhn.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Created by lenovo on 2014/12/8.
 */
public abstract class Base {
//    @Resource
//    protected JedisPool jedisPool;
    @Resource
    protected MemcachedClient memcachedClient;
    protected Logger logger = Logger.getLogger(this.getClass());
    public static ApplicationContext applicationContext;
    public static String webPath;
    public static ApplicationContext otherApplicationContext;

    @Resource
    protected MessageSource          messageSource;

    /**
     * 获取配置在message里的消息
     * 
     * @param messageKey
     * @return
     * @author: zhangyunhmf
     * @date: 2015年1月15日上午11:09:08
     */
    public String getMessage(String messageKey) {
        return messageSource.getMessage(messageKey, null, Locale.CHINA);
    }

    /**
     * 获取配置在message里的消息
     * 
     * @param messageKey
     * @param parameters
     * @return
     * @author: zhangyunhmf
     * @date: 2015年1月15日上午11:09:37
     */
    public String getMessage(String messageKey, Object[] parameters) {
        return messageSource.getMessage(messageKey, parameters, Locale.CHINA);
    }

//    public static String jsp_classpath;
    public static ApplicationContext getApplicationContext(HttpServletRequest request) {
//        return applicationContext==null?applicationContext=WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()):applicationContext;
        return Base.applicationContext;
    }

    public BaseReturn getFormatError(BindingResult result) {
        List<FieldError> fields = result.getFieldErrors();
        StringBuffer errors = new StringBuffer();
        for (FieldError fieldError : fields) {
            if (StringUtils.isNotEmpty(fieldError.getDefaultMessage()))
                errors.append(MessageFormat.format(fieldError.getDefaultMessage(), fieldError.getField(), fieldError.getRejectedValue()));
        }
        return new BaseReturn(BaseReturn.Err_data_inValid, errors.toString());
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        Base.applicationContext = applicationContext;
    }

    public static String getWebPath() {
        return webPath;
    }

    public static void setWebPath(String webPath) {
        Base.webPath = webPath;
    }

    public static ApplicationContext getOtherApplicationContext() {
        return otherApplicationContext;
    }

    public static void setOtherApplicationContext(ApplicationContext otherApplicationContext) {
        Base.otherApplicationContext = otherApplicationContext;
    }

/*    public static String getJsp_classpath() {
        return jsp_classpath;
    }

    public static void setJsp_classpath(String jsp_classpath) {
        Base.jsp_classpath = jsp_classpath;
    }*/
}
