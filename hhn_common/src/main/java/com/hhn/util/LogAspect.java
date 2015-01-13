package com.hhn.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hhn.dao.ISystemLogDao;
import com.hhn.pojo.AccountUserDo;
import com.hhn.pojo.SystemLog;
import com.hhn.util.annotaion.ControllerLog;
import com.hhn.util.annotaion.ServiceLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/18.
 */
@Component
@Aspect
public class LogAspect extends BaseService {
@Resource
private ISystemLogDao systemLogDao;

    @Pointcut("@annotation(com.hhn.util.annotaion.ServiceLog)")
    public void serviceAspect(){}

    @Pointcut("@annotation(com.hhn.util.annotaion.ControllerLog)")
    public void controllerAspect(){}

    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        Object user = request.getSession().getAttribute("user");
        Integer userId=null;
        if(user==null) {
            Object[] obj = joinPoint.getArgs();
            if (obj.length != 1 || !(obj[0] instanceof Map)) return;
            Map params = (Map) obj[0];
            userId= Integer.parseInt(String.valueOf(params.get("userId")==null?params.get("user_id"):params.get("userId")));//获取当前用户ID
        }else userId=Integer.parseInt(String.valueOf(((AccountUserDo) user).getId()));
        String ip = request.getRemoteAddr();
        try {
            SystemLog log = new SystemLog();
            log.setDescription(getMethodDescription(joinPoint,0));
            log.setMethod(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
            log.setLog_type(Byte.valueOf("0"));
            log.setRequest_ip(ip);
            log.setException_code(null);
            log.setException_detail(null);
            log.setUser_id(userId);
            log.setParams(JSON.toJSONString(joinPoint.getArgs()));
            log.setCtime(Calendar.getInstance().getTime());
            systemLogDao.save(log);
        } catch (Exception ex) {
            logger.error("前置通知异常......");
            logger.error(MessageFormat.format("异常信息:{}", ex.getMessage().toString()));
        }
    }

    @AfterThrowing(pointcut = "serviceAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        Object user = request.getSession().getAttribute("user");
        Integer userId=null;
        if(user==null) {
            Object[] obj = joinPoint.getArgs();
            if (obj.length != 1 || !(obj[0] instanceof Map)) return;
            Map params = (Map) obj[0];
            userId= Integer.parseInt(String.valueOf(params.get("userId")==null?params.get("user_id"):params.get("userId")));//获取当前用户ID
        }else userId=Integer.parseInt(String.valueOf(((AccountUserDo) user).getId()));
        String ip = request.getRemoteAddr();
        String params= JSONArray.toJSONString(joinPoint.getArgs());
        try{
            SystemLog log = new SystemLog();
            log.setDescription(getMethodDescription(joinPoint,1));
            log.setMethod(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
            log.setLog_type(Byte.valueOf("1"));
            log.setRequest_ip(ip);
            log.setException_code(e.getClass().getName());
            log.setException_detail(e.getMessage());
            log.setUser_id(userId);
            log.setParams(JSON.toJSONString(joinPoint.getArgs()));
            log.setCtime(Calendar.getInstance().getTime());
            systemLogDao.save(log);
        }catch (Exception ex) {
            logger.error("异常通知异常......");
            logger.error(MessageFormat.format("异常信息:{}", ex.getMessage().toString()));
        }
        logger.error(MessageFormat.format("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params));
    }

    @AfterReturning(pointcut = "serviceAspect()")
    public void doAfterReturn(JoinPoint joinPoint) {
        HttpServletRequest request= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        Object user = request.getSession().getAttribute("user");
        Integer userId=null;
        if(user==null) {
            Object[] obj = joinPoint.getArgs();
            if (obj.length != 1 || !(obj[0] instanceof Map)) return;
            Map params = (Map) obj[0];
            userId= Integer.parseInt(String.valueOf(params.get("userId")==null?params.get("user_id"):params.get("userId")));//获取当前用户ID
        }else userId=Integer.parseInt(String.valueOf(((AccountUserDo) user).getId()));
        String ip = request.getRemoteAddr();
        String params= JSONArray.toJSONString(joinPoint.getArgs());
        try{
            SystemLog log = new SystemLog();
            log.setDescription(getMethodDescription(joinPoint,1));
            log.setMethod(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
            log.setLog_type(Byte.valueOf("1"));
            log.setRequest_ip(ip);
            log.setException_code(null);
            log.setException_detail(null);
            log.setUser_id(userId);
            log.setParams(JSON.toJSONString(joinPoint.getArgs()));
            log.setCtime(Calendar.getInstance().getTime());
            systemLogDao.save(log);
        }catch (Exception ex) {
            logger.error("通知异常......");
            logger.error(MessageFormat.format("异常信息:{}", ex.getMessage().toString()));
        }
    }

    public static String getMethodDescription(JoinPoint joinPoint,int type) throws Exception {
        Object[] objs=joinPoint.getArgs();
        Class[] clazzs = new Class[objs.length];
        for (int i=0;i<clazzs.length;i++)
            clazzs[i]=objs[i].getClass();
          Method method= joinPoint.getTarget().getClass().getMethod(joinPoint.getSignature().getName(), clazzs);
        if (type==1)
           return method.getAnnotation(ServiceLog.class).description();
        else return method.getAnnotation(ControllerLog.class).description();
    }
}
