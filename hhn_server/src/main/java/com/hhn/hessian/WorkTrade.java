package com.hhn.hessian;

import com.hhn.dao.*;
import com.hhn.pojo.*;
import com.hhn.util.BaseProxy;
import com.hhn.util.BaseReturn;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by lenovo on 2014/12/11.
 */
@Aspect
@Component
public class WorkTrade extends BaseProxy<LoanDetail> {
@Resource
private IFundUserAccountDao fundUserAccountDao;
    @Resource
    private ILoanDetailDao loanDetailDao;
    @Resource
    private IFundPreRepaymentDao fundPreRepaymentDao;
    @Resource
    private IFundTradeDao fundTradeDao;
    @Resource
    private IFundAccountLogDao fundAccountLogDao;


    @Pointcut("execution(* com.hhn.service.*.Loan*.*(..)) && @annotation(VerifyAfter)")
    public void loanVerify() {
    }

    @Pointcut("execution(* com.hhn.service.*.Invest*.*(..)) && @annotation(VerifyAfter)")
    public void investProcess(){}

    @Around("loanVerify()")//TODO:满标审核放款
    public BaseReturn trade(ProceedingJoinPoint pjp) throws Throwable {
        BaseReturn baseReturn = (BaseReturn) pjp.proceed();
        if (baseReturn.getReturnCode() == 0) {
//            String kPrifex=baseReturn.getData().getClass().getSimpleName();
//            Class clazz=baseReturn.getData().getClass();
//            Class clazz= (Class) ((ParameterizedType)pjp.getTarget().getClass().getGenericSuperclass()).getActualTypeArguments()[0];
//            System.out.println("*****AoP********\t"+clazz.getName());
//            System.out.println(Class.forName(getClassName(baseReturn.getData())));
//           BaseDao dao= (BaseDao) applicationContext.getBean(Class.forName(getClassName(baseReturn.getData())));
           LoanDetail loanDetail=getById(loanDetailDao,(Integer)pjp.getArgs()[0]);
            FundUserAccount fundUserAccount = new FundUserAccount();
            Trade trade = (Trade) baseReturn.getData();
            FundUserAccount fundUserAccount1 = fundUserAccountDao.queryUserAccount(loanDetail.getUser_id());
            fundUserAccount.setUser_account_id(fundUserAccount1.getUser_account_id());
            fundUserAccount.setBalance_amount(fundUserAccount1.getBalance_amount()==null?new BigDecimal(0).add(trade.getAmount()):fundUserAccount1.getBalance_amount().add(trade.getAmount()));
            trade.getDao().update(fundUserAccount);
            Calendar calendar=Calendar.getInstance();
//交易记录
            FundTrade fundTrade = new FundTrade();
            fundTrade.setProduct_id(loanDetail.getLoan_id());
            fundTrade.setUser_id(fundUserAccount1.getUser_id());
            fundTrade.setUser_account_id(loanDetail.getUser_id());
            fundTrade.setTrade_amount(loanDetail.getLoan_amount());
            fundTrade.setTrade_type(IFundTradeDao.TradeType.LOAN.toString());
            fundTrade.setTrade_status(IFundTradeDao.TradeStatus.TRANSFEREDMONEY.toString());
            fundTrade.setTrade_time(calendar.getTime());
            fundTrade.setUpdate_time(calendar.getTime());
            calendar.add(Calendar.MONTH,loanDetail.getLoan_period());
            fundTrade.setExpect_trade_time(calendar.getTime());
            fundTradeDao.save(fundTrade);
//流水记录
/*            FundAccountLog fundAccountLog = new FundAccountLog();
            fundAccountLog.setTrade_detail_id(fundTrade.getTrade_id());
            fundAccountLog.setTrade_amount(loanDetail.getLoan_amount());
            fundAccountLog.setTo_account(loanDetail.getUser_id().toString());
            fundAccountLog.setTo_user_id(fundUserAccount1.getUser_id());
            fundAccountLog.setFrom_account("000000");
            fundAccountLog.setFrom_user_id(000000);
            fundAccountLog.setTrade_time(fundTrade.getTrade_time());
            fundAccountLogDao.save(fundAccountLog);*/
//            TransactionItem transactionItem=new TransactionItem(loanDetail.getLoan_id(),fundUserAccountl2.getUser_id(),loanDetail.getUser_id(), IFundTradeDao.TradeType.LOAN,loanDetail.getLoan_amount(), IFundTradeDao.TradeStatus.TRANSFEREDMONEY);

           baseReturn=preRepayment(loanDetail);
        }
        return baseReturn;
    }

    /**
     * 预还款记录
     * @param loanDetail
     * @return
     */
    public BaseReturn preRepayment(LoanDetail loanDetail) {
      BigDecimal interest=loanDetail.getLoan_rate().multiply(BigDecimal.valueOf(loanDetail.getLoan_period() / 12)); //利息
        BigDecimal repayment = loanDetail.getLoan_amount().multiply(interest.add(BigDecimal.valueOf(1)));
        int size=loanDetail.getLoan_period() / loanDetail.getRepay_period();
        if(loanDetail.getLoan_period() % loanDetail.getRepay_period()>0) size+=1;
        BigDecimal amount = repayment.divide(BigDecimal.valueOf(size), 2);
//        Date date = new Date();
//        FundUserAccount fundUserAccountl2 = fundUserAccountDao.queryUserAccount(loanDetail.getUser_id());
        Calendar calendar=Calendar.getInstance();
        Calendar calendar1=Calendar.getInstance();
        calendar1.setTime(loanDetail.getUpdate_time());
        calendar1.add(Calendar.MONTH,loanDetail.getLoan_period());
        for(;size>0;size--){
            FundPreRepayment fundPreRepayment = new FundPreRepayment();
            fundPreRepayment.setLoan_id(loanDetail.getLoan_id());
            fundPreRepayment.setUser_id(loanDetail.getUser_id()); //TODO:用户资金账户ID
            fundPreRepayment.setRepay_amount(amount);
            fundPreRepayment.setRepay_status("NO_REPAYMENT");  //TODO:enum
            fundPreRepayment.setRepay_type("PRINCIPAL+");
            fundPreRepayment.setRepay_amount(amount);
            fundPreRepayment.setCreate_time(calendar.getTime());
            fundPreRepayment.setUpdate_time(calendar.getTime());
            calendar.add(Calendar.MONTH, loanDetail.getRepay_period());
            fundPreRepayment.setRepay_date(calendar.getTime().getTime() > calendar1.getTime().getTime() ? calendar1.getTime() : calendar.getTime());
            fundPreRepaymentDao.save(fundPreRepayment);
        }
        return new BaseReturn(0,loanDetail.getLoan_id());
    }

/*    @Around("investProcess()")
    public BaseReturn invest(ProceedingJoinPoint proceedingJoinPoint) {
        return null;
    }*/

}

//    @Before("execution(* com.hhn.hessian..abc(..)) && @annotation(VerifyAfter)")
//    @Before("loanVerify()")
//    public BaseReturn trade(JoinPoint joinPoint) {
//        System.out.println("...............................-==================");
//        return null;
//    }

//    public String getClassName(Object obj) {
//        Class cla = obj.getClass();
//        String claName = cla.getName().substring(0, cla.getName().lastIndexOf(".")+1);
//        return claName + "I" + cla.getSimpleName() + "Dao";
//    }

    /**
     * 交易记录
     * @param item
     */
/*    public void writeTransaction(TransactionItem item) {
        Calendar calendar=Calendar.getInstance();
        FundTrade fundTrade = new FundTrade();
        fundTrade.setProduct_id(item.getProduct_id());
        fundTrade.setUser_id(item.getUser_id());
        fundTrade.setUser_account_id(item.getUser_account_id());
        fundTrade.setTrade_amount(item.getTrade_amount());
        fundTrade.setTrade_type(item.getTrade_type().toString());
        fundTrade.setTrade_status(item.getTrade_status().toString());
        fundTrade.setTrade_time(calendar.getTime());
        fundTrade.setUpdate_time(calendar.getTime());

    }*/
