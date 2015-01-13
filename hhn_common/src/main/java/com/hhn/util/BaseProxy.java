package com.hhn.util;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by lenovo on 2014/12/8.
 */
public abstract class BaseProxy<T> extends BaseService<T>{
    @Pointcut("execution(* com.hhn.hessian..*(..))")
    public void anyMethod(){}



/*    @Before("anyMethod()")
    public void beforeExcute(JoinPoint joinPoint) {
        Object[] objs = joinPoint.getArgs();
        for(Object obj:objs)
            System.out.println(obj);
        System.out.println("===============================method before excute.........");
    }

    @After("anyMethod()")
    public void afterExcute(){
        System.out.println("=================================method after excute..........");
    }
    @Around("anyMethod()")
    public void aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("================================进入环绕通知........");
        System.out.println(pjp.getArgs().length);
        pjp.proceed();
        System.out.println("=========================离开环绕通知....");
    }*/

    //    @After("within(* com.hhn.hessian..*) && @args(rl)")

}
