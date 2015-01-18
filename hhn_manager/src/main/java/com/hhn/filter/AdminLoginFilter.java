package com.hhn.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2015/1/4.
 */
public class AdminLoginFilter implements Filter {
    protected String adminLoginView;

    public String getAdminLoginView() {
        return adminLoginView;
    }

    public void setAdminLoginView(String adminLoginView) {
        this.adminLoginView = adminLoginView;
    }

    public AdminLoginFilter() {}

    public void destroy() {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //String fromUrl =req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+req.getRequestURI();
        String fromUrl = req.getRequestURI();
        Object obj=req.getSession().getAttribute("admin");
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
        if (obj == null || StringUtils.isEmpty(jsonObject.get("id"))) {
            resp.sendRedirect(adminLoginView + "?fromUrl=" + fromUrl);
        } else {
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig fConfig) throws ServletException {
    }

}
