package com.hhn.util;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2014/12/23.
 */
@Controller
public class BaseLoginAction extends BaseAction {
    //判断用户登录，并取userId
    public String getUserId(HttpServletRequest request) {
        Object user = getSessionObj(request);
        return getObjVal(user, "getId");
    }
    //取用户姓名
    public String getUserName(HttpServletRequest request) {
        Object user = getSessionObj(request);
        return getObjVal(user, "getUsername");
    }
    //获取对象
    public Object getSessionObj(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        if (user != null) {
            return user;
        }else {
            return null;
        }
    }
    //获取对象属性的值
    public String getObjVal(Object o, String name){
        try {
            if (o != null) {
                Class clazz = o.getClass();
                Object obj = clazz.getMethod(name).invoke(o);
                if (obj != null && !"".equals(obj.toString())) {
                    System.out.println("session "+name+" info:=======>" + obj.toString());
                    return obj.toString();
                }
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
