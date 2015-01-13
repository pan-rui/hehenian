package com.hhn.test;

import com.hhn.dao.ILoanDetailDao;
import com.hhn.service.impl.LoanDetailServiceImpl;
import com.hhn.service.pay.MessageInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by lenovo on 2014/12/6.
 */
@Component("redis")
public class Redis {
private @Value("#{config['mysql.host']}") String operate;
//    private @Value("${mysql.host}") String rootLog;

/*    public String getRootLog() {
        return rootLog;
    }

    public void setRootLog(String rootLog) {
        this.rootLog = rootLog;
    }*/

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public static void main(String[] args) {
        String key = "bbb";
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
/*        T1 t1 = (T1) ctx.getBean("t1");
        JedisPool jedisPool = (JedisPool) ctx.getBean("jedisPool");
        System.out.println("Hello "+t1.t2(key));
        System.out.println("Hello " + t1.t2(key));
        System.out.println(jedisPool.getResource().exists("abd"));
        System.out.println(t1.t3(key));*/
//        System.out.println(t1.t2(key));

//        ILoanPersonDao loanPersonDao = (ILoanPersonDao) ctx.getBean("iLoanPersonDao");
//        ILoanPersonDao loanPersonDao2 = (ILoanPersonDao) ctx.getBean(ILoanPersonDao.class);
        ILoanDetailDao loanDetailDao = (ILoanDetailDao) ctx.getBean(ILoanDetailDao.class);
        System.out.println("loanDetailDao is getBean\t"+(loanDetailDao==null));
//        System.out.println(loanPersonDao==null);
//        System.out.println(loanPersonDao2 instanceof BaseDao);
//        WorkTrade workTrade = (WorkTrade) ctx.getBean("workTrade");
        LoanDetailServiceImpl loanPersonService = (LoanDetailServiceImpl) ctx.getBean("loanDetailServiceImpl");
        System.out.println("loanPerson is null\t"+(loanPersonService==null));
//        loanPersonService.verify(12);
        System.out.println("===============================");
//        new BaseService<LoanPerson>();
    }

    public static void mn(String[] args) {
        new BigDecimal(0).divide(new BigDecimal(23), 2);
        System.out.println(MessageInfo.getMessage("pfxPath"));
        File file = new File("/E:/workspace/hhn/hhn_server/target/hhn_server/WEB-INF/classes/"+ MessageInfo.getMessage("pfxPath"));
        System.out.println(ClassLoader.getSystemClassLoader().getResource(""));
    Redis redis=new Redis();
        System.out.println("new Redis()====>"+redis.operate);
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        Redis redis1= (Redis) ctx.getBean("redis");
        System.out.println("redis1=========>"+redis1.operate);
//        System.out.println("rootLog=========>"+redis1.rootLog);
    }
}
