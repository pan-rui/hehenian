package com.hhn.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2014/12/31.
 */
public class UserLoginFilter implements Filter {
    protected String loginView;
    protected String adminLoginView;
    protected String topView;
    protected String footerView;

    public String getLoginView() {
        return loginView;
    }

    public void setLoginView(String loginView) {
        this.loginView = loginView;
    }

    public String getAdminLoginView() {
        return adminLoginView;
    }

    public void setAdminLoginView(String adminLoginView) {
        this.adminLoginView = adminLoginView;
    }

    public String getTopView() {
        return topView;
    }

    public void setTopView(String topView) {
        this.topView = topView;
    }

    public String getFooterView() {
        return footerView;
    }

    public void setFooterView(String footerView) {
        this.footerView = footerView;
    }

    public UserLoginFilter() {}

    public void destroy() {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String fromUrl =req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+req.getRequestURI();
        HttpSession session = req.getSession();
        setParameter(session, req.getParameterMap());
        Object obj=session.getAttribute("user");
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
        if (obj == null || StringUtils.isEmpty(jsonObject.get("id"))) {
            resp.sendRedirect(loginView + "?fromUrl=" + fromUrl);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void setParameter(HttpSession session, Map map){
        if (map!=null && map.size()>0){
            session.setAttribute("parameterMap", map);
        }
    }

    public void init(FilterConfig fConfig) throws ServletException {
       ServletContext sc=fConfig.getServletContext();
        sc.setAttribute("topView",topView);
        sc.setAttribute("footerView",footerView);
    }

}
