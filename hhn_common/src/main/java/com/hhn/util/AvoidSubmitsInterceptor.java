package com.hhn.util;

import com.hhn.util.annotaion.AvoidSubmits;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by hynpublic on 2015/1/5.
 */
public class AvoidSubmitsInterceptor extends HandlerInterceptorAdapter {
    public AvoidSubmitsInterceptor(){}

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod) {
            AvoidSubmits avoidSubmits = ((HandlerMethod) handler).getMethod().getAnnotation(AvoidSubmits.class);
            TokenProcessor tokenProcessor=TokenProcessor.getInstance();
            if(avoidSubmits!=null&&avoidSubmits.saveToken())
                tokenProcessor.saveToken(request);
            if(avoidSubmits!=null&&avoidSubmits.removeToken())
                return tokenProcessor.isTokenValid(request);
        }
        return true;
    }
}
