package com.hhn.service.impl;

import com.hhn.dao.*;
import com.hhn.hessian.VerifyAfter;
import com.hhn.pojo.*;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import com.hhn.util.DqlcConfig;
import com.hhn.util.FundUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 购买产品处理
 * Created by lenovo on 2014/12/12.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InvestServiceImpl extends BaseService<FundInvestmentDetail> {
    @Resource
    private IFundProductDao fundProductDao;
    @Resource
    private IFundUserAccountDao fundUserAccountDao;
    @Resource
    private IFundInvestmentDetailDao fundInvestmentDetailDao;
    @Resource
    private IFundTradeDao fundTradeDao;
    @Resource
    private FundUtil fundUtil;
    @Resource
    private IProductRateDao productRateDao;
    @Resource
    private DqlcConfig processInfo;
    @Resource
    private ILoanDetailDao loanDetailDao;
    @Resource
    private IFundProductAuditDao fundProductAuditDao;
    @Resource
    private IFundPaymentDao fundPaymentDao;
    @Resource
    IFundTransferDao fundTransferDao;
    private final ConcurrentHashMap<Integer, CountDownLatch> fundProductMap = new ConcurrentHashMap();

    @VerifyAfter
    public BaseReturn investment(final Invest invest) throws InterruptedException {
        BaseReturn baseReturn = null;
        if ((baseReturn = validAndReturn(invest)).getReturnCode() == 1)
            return baseReturn;
        BigDecimal money = invest.getMoney();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, Integer.valueOf(processInfo.LOAN_AHEAD));
        BigDecimal surplusMoney = fundTradeDao.queryPay(now.getTime());
        if (surplusMoney == null || surplusMoney.compareTo(money) < 0)
            return new BaseReturn(1, null, processInfo.NOT_PRODUCT); //判断可投金额
        FundUserAccount fundUserAccount = fundUserAccountDao.queryUserAccount(invest.getUser_id());//查询资金账户
        FundUserAccount fundUserAccount1 = new FundUserAccount();
        fundUserAccount1.setUser_id(fundUserAccount.getUser_id());
        BigDecimal bigDecimal = fundUserAccount.getBalance_amount().subtract(invest.getMoney());
        if (bigDecimal.compareTo(BigDecimal.ZERO) < 0) return new BaseReturn(1, processInfo.USER_BALANCE_LACK);
        fundUserAccount1.setBalance_amount(bigDecimal);
        fundUserAccount1.setFreeze_amount(fundUserAccount.getFreeze_amount().add(invest.getMoney()));
        fundUserAccount1.setInvest_amount(invest.getMoney());
        fundUserAccount1.setTotal_invest_amount(fundUserAccount.getTotal_invest_amount().add(invest.getMoney()));
        fundUserAccount1.setUpdate_time(Calendar.getInstance().getTime());
        fundUserAccount1.setUser_account_id(fundUserAccount.getUser_account_id());
        fundUserAccountDao.update(fundUserAccount1); // success
        //log...........
        invest.setUser_account_id(fundUserAccount.getUser_account_id());
        invest.setTradeType(IFundTradeDao.TradeType.INVESTMENT);
        invest.setTradeStatus(IFundTradeDao.TradeStatus.PROCESSING);
        invest.setBalance(invest.getMoney());
        invest.setLogType(IFundAccountLogDao.LogType.BUY);
        FundTrade trade = fundUtil.Tradelog(invest);
        logger.info("当前日期："+now.getTime()+"=================购买"+invest.getMonth()+"个月的理财产品，购买金额为：\t"+invest.getMoney()+"元。");
        return new BaseReturn(0, trade, processInfo.OPERATE_SUCCESS);
    }

    public void autoInvest() throws InterruptedException {

        Calendar calendar = Calendar.getInstance();
        logger.info("当前日期："+calendar.getTime()+"自动投标开始=======================================");
        List<FundInvestmentDetail> fundInvestmentDetails = fundInvestmentDetailDao.queryByNow(calendar.getTime());
        // TODO
        for (FundInvestmentDetail fundInvestmentDetail : fundInvestmentDetails) {
            FundTransfer fundTransfer1 = fundTransferDao.query(fundInvestmentDetail.getProduct_id());
            if(fundTransfer1!=null&&fundTransfer1.getPeriods_surplus()!=0){
                    makeOver(fundInvestmentDetail);
            }else {
                FundProduct fundProduct = fundProductDao.query(fundInvestmentDetail.getProduct_id());
                if (fundProduct.getProduct_status().intValue() != 3 || fundInvestmentDetail.getInvest_period() != fundProduct.getLoan_period()) {
                    makeOver(fundInvestmentDetail);
                }
            }
/*            FundTrade fundTrade1 = fundInvestmentDetailDao.queryForFundTrade(fundInvestmentDetail.getInvestment_detail_id());
            FundTrade fundTrade2 = new FundTrade(fundTrade1.getTrade_id());
            fundTrade2.setUpdate_time(calendar.getTime());
            fundTrade2.setTrade_balance(fundTrade1.getTrade_balance().add(fundInvestmentDetail.getTrade_amount()).add(fundInvestmentDetail.getIncome()));
            fundTradeDao.update(fundTrade2);*/
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("noww", calendar.getTime());
        params.put("trade_type", IFundTradeDao.TradeType.INVESTMENT.name());
        List<FundTrade> fundTrades = fundInvestmentDetailDao.queryForFundTrades(params);
        for (FundTrade fundTrade : fundTrades) {
            BigDecimal money = fundTrade.getTrade_balance();
            logger.debug("------------------money[fundTrade.getTrade_balance()]::"+money);
            Short month = Short.valueOf(fundUtil.getMonth(fundTrade.getTrade_time(), fundTrade.getExpect_trade_time()) + "");
            BigDecimal bMoney = new BigDecimal(money.doubleValue());
            Integer product_id = 0;
            boolean need = money.compareTo(BigDecimal.ZERO) > 0;
            logger.debug("------------------need[ money.compareTo(BigDecimal.ZERO) > 0]::"+need);
            boolean flag = false;
            while (need) {
                FundInvestmentDetail fundInvestmentDetail = null;
                if (product_id != 0) {
                    Object[] data = (Object[]) payProduct(money, product_id, fundTrade, flag).getData();
                    if(data==null) continue;
                    fundInvestmentDetail = (FundInvestmentDetail) data[0];
                    money = (BigDecimal) data[1];
                }
                if (bMoney.compareTo(money) != 0) {
//                    FundInvestmentDetail fundInvestmentDetail = new FundInvestmentDetail();
                    Calendar calendar2 = Calendar.getInstance();
                    fundInvestmentDetail.setUpdate_time(calendar2.getTime());
                    fundInvestmentDetail.setProduct_id(product_id);
                    fundInvestmentDetail.setUser_id(fundTrade.getUser_id());
                    fundInvestmentDetail.setUser_account_id(fundTrade.getUser_account_id());
                    fundInvestmentDetail.setInvest_time(calendar2.getTime());
//                    fundInvestmentDetail.setTrade_amount(money.compareTo(BigDecimal.ZERO) >= 0 ? bMoney.subtract(money).setScale(2,RoundingMode.HALF_UP) : bMoney);
//                    fundInvestmentDetail.setTrade_amount(fundInvestmentDetail1.getTrade_amount());
                    fundInvestmentDetail.setFund_trade_id(fundTrade.getTrade_id());
//                    fundInvestmentDetail.setInvest_period(fundInvestmentDetail1.getInvest_period());
//                    fundInvestmentDetail.setIncome(fundInvestmentDetail1.getIncome());
//                    fundInvestmentDetail.setLoan_user_id(fundInvestmentDetail1.getLoan_user_id());
                    fundInvestmentDetailDao.save(fundInvestmentDetail);

                    FundTrade fundTrade3 = new FundTrade(fundTrade.getTrade_id());
                    fundTrade3.setTrade_balance(fundTrade.getTrade_balance().subtract(fundInvestmentDetail.getTrade_amount()));
                    fundTrade3.setUpdate_time(calendar2.getTime());
                    if(fundTrade3.getTrade_balance().compareTo(BigDecimal.ZERO)==0)
                        fundTrade3.setTrade_status(IFundTradeDao.TradeStatus.SUCCESS.name());
                    fundTradeDao.update(fundTrade3);
                    fundTrade.setTrade_balance(fundTrade3.getTrade_balance());
                    bMoney = new BigDecimal(money.doubleValue());
                    logger.info("当前时间"+calendar2.getTime()+"======投资标的:"+product_id+"\t投资明细ID："+fundInvestmentDetail.getInvestment_detail_id()+"\t投资用户ID:"+fundTrade.getUser_id()+"\t投资金额："+fundInvestmentDetail.getTrade_amount());
                }
                if (money.compareTo(BigDecimal.ZERO) > 0) {
                    List<FundTransfer> fundTransfers = fundTransferDao.querys();
                    if (!fundTransfers.isEmpty()) {
                        product_id = fundTransfers.get(0).getTransfer_id();
                        flag = true;
                    } else {
                        flag = false;
                        List<FundProduct> productList = fundProductDao.queryProduct(money, month);
                        if (!productList.isEmpty())
                            product_id = productList.get(0).getProduct_id();
                        else if (!(productList = fundProductDao.queryProduct2(money, month)).isEmpty()) {
                            product_id = productList.get(0).getProduct_id();
                        } else if (!(productList = fundProductDao.queryProduct3(money, month)).isEmpty()) {
                            product_id = productList.get(0).getProduct_id();
                        } else if (!(productList = fundProductDao.queryProduct4(money, month)).isEmpty()) {
                            product_id = productList.get(0).getProduct_id();
                        } else {
                            product_id = 0;
                            need = false;
                        }
                    }
                } else need=false;
            }
        }
        logger.info("当前日期"+new Date()+"========自动投标结束。。。。。。。。。。。");
    }

    /**
     * @param money
     * @param product_id
     * @return
     * @throws InterruptedException
     */
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation = Propagation.NESTED)
    public BaseReturn payProduct(BigDecimal money, Integer product_id, FundTrade trade, boolean flag) throws InterruptedException {
        BaseReturn baseReturn = null;
        CountDownLatch countDownLatch = new CountDownLatch(1);
        if (fundProductMap.putIfAbsent(product_id, countDownLatch) == null) {
            Object[] data = payment(money, product_id, trade, flag);
            fundProductMap.remove(product_id);
            countDownLatch.countDown();
            baseReturn = new BaseReturn(0, data);
        } else {
            CountDownLatch countDownLatch2 = new CountDownLatch(1);
            fundProductMap.replace(product_id, countDownLatch2).await();
            Object[] data = payment(money, product_id, trade, flag);
            fundProductMap.remove(product_id);
            countDownLatch2.countDown();
            baseReturn = new BaseReturn(0, data);
        }
        return baseReturn;
    }

    /**
     * 标的购买
     *
     * @param money
     * @param product_id
     * @param trade
     * @return
     */
    public Object[] payment(BigDecimal money, Integer product_id, FundTrade trade, boolean flag) {
        Calendar calendar = Calendar.getInstance();
        if (flag) {
            FundTransfer fundTransfer = fundTransferDao.queryUnique(product_id);
            FundProduct fundProduct1=fundProductDao.query(fundTransfer.getFundInvestmentDetail().getProduct_id());
            FundTransfer fundTransfer1 = new FundTransfer(fundTransfer.getTransfer_id());
            fundTransfer1.setBuy_user_id(trade.getUser_id());
            BigDecimal surplus = money.subtract(fundTransfer.getShare_surplus());
            FundInvestmentDetail fundInvestmentDetail = new FundInvestmentDetail();
            Short month = Short.valueOf(fundUtil.getMonth(trade.getTrade_time(), trade.getExpect_trade_time()) + "");
            Short period = fundTransfer.getPeriods_surplus() >= month ? month : Short.valueOf(fundTransfer.getPeriods_surplus() + "");//默认一期为一个月
            BigDecimal totalRate = productRateDao.query(trade.getRate_id()).getRate().multiply(new BigDecimal(month / 12d));
            if (surplus.compareTo(BigDecimal.ZERO) >= 0) {
//                fundTransfer1.setTransfer_amount(fundTransfer.getTransfer_amount());
                fundTransfer1.setShare_transfer(fundTransfer.getShare_transfer().add(fundTransfer.getShare_surplus()));
                fundTransfer1.setShare_surplus(BigDecimal.ZERO);
                fundTransfer1.setTransfer_status(3);
                fundTransfer1.setUtime(calendar.getTime());
               int size=fundTransferDao.update(fundTransfer1);
                if(size!=1){
                    logger.info("债权转让已满标，投资失败。债权转让ID:"+product_id+"\t投资用户ID:"+trade.getUser_id()+"\t投资金额："+money+"\t转让用户ID:"+fundTransfer.getTransfer_user_id());
                    return null;
                }
//                loan(fundTransfer1, loanDetail1).getData();
                fundInvestmentDetail.setStatus(fundTransfer1.getTransfer_status().intValue());
                fundInvestmentDetail.setRemark("transfer");
                fundInvestmentDetail.setTrade_amount(fundTransfer.getTransfer_amount());
                fundInvestmentDetail.setIncome(fundTransfer.getShare_surplus().multiply(totalRate).setScale(2,RoundingMode.HALF_UP));
                fundInvestmentDetail.setLoan_user_id(fundProduct1.getUser_id());
            } else {
                fundTransfer1.setShare_transfer(fundTransfer.getShare_transfer().add(money));
                fundTransfer1.setUtime(calendar.getTime());
               int size=fundTransferDao.update(fundTransfer1);
                if(size!=1){
                    logger.info("债权转让已满标，投资失败。债权转让ID:"+product_id+"\t投资用户ID:"+trade.getUser_id()+"\t投资金额："+money+"\t转让用户ID:"+fundTransfer.getTransfer_user_id());
                    return null;
                }
                fundInvestmentDetail.setTrade_amount(money);
                fundInvestmentDetail.setStatus(fundTransfer.getTransfer_status().intValue());
                fundInvestmentDetail.setIncome(money.multiply(totalRate).setScale(2,RoundingMode.HALF_UP));
                fundInvestmentDetail.setLoan_user_id(fundProduct1.getUser_id());
            }
            fundInvestmentDetail.setInvest_period(period);
            return new Object[]{fundInvestmentDetail,surplus};
        } else {
            final FundProduct fundProduct = fundProductDao.query(product_id);
            LoanDetail loanDetail1 = loanDetailDao.query(fundProduct.getLoan_id());
//            FundProduct fundProduct1 = new FundProduct(fundProduct.getProduct_id());
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("product_id", fundProduct.getProduct_id());
            BigDecimal surplus = money.subtract(fundProduct.getInvest_amount().subtract(fundProduct.getInvested_amount()));
            logger.info("++++surplus::"+surplus+"+++++++++++money::"+money+"++++++++++++fundProduct.getInvest_amount()::"+fundProduct.getInvest_amount()+"++fundProduct.getInvested_amount()::"+fundProduct.getInvested_amount());
            FundInvestmentDetail fundInvestmentDetail = new FundInvestmentDetail();
            Short month = Short.valueOf(fundUtil.getMonth(trade.getTrade_time(), trade.getExpect_trade_time()) + "");
            Short period = fundProduct.getLoan_period() >= month ? month : fundProduct.getLoan_period();///////////////////////////////
            BigDecimal totalRate = productRateDao.query(trade.getRate_id()).getRate().multiply(new BigDecimal(month / 12d));

            if (surplus.compareTo(BigDecimal.ZERO) >= 0) {
//                fundProduct1.setInvest_amount(fundProduct.getInvest_amount());
                params.put("money", fundProduct.getInvest_amount().subtract(fundProduct.getInvested_amount()));
                params.put("invested_amount", fundProduct.getInvest_amount());
                BaseReturn aReturn= loan(params, loanDetail1);
                if(aReturn==null){
                    logger.info("标的已满标，投资失败。标的ID:"+product_id+"\t投资用户ID:"+trade.getUser_id()+"\t投资金额："+money+"\t标的借款ID:"+fundProduct.getLoan_id()+"\t借款用户ID:"+fundProduct.getUser_id());
                    return null;
                }
                fundInvestmentDetail.setStatus(Integer.parseInt(params.get("product_status") + ""));
                fundInvestmentDetail.setTrade_amount(fundProduct.getInvest_amount().subtract(fundProduct.getInvested_amount()));
                fundInvestmentDetail.setIncome(fundProduct.getInvest_amount().subtract(fundProduct.getInvested_amount()).multiply(totalRate).setScale(2,RoundingMode.HALF_UP));
                fundInvestmentDetail.setLoan_user_id(fundProduct.getUser_id());
            } else {
//                fundProduct1.setInvested_amount(fundProduct.getInvested_amount().add(money));
//                fundProduct1.setUpdate_time(calendar.getTime());
                params.put("invested_amount", fundProduct.getInvested_amount().add(money));
                params.put("update_time", calendar.getTime());
                logger.info("+++++++++++++++++++fundProduct.getInvested_amount():::" + fundProduct.getInvested_amount());
                logger.info("+++++++++++++++++++money:::"+money);
               int size=fundProductDao.updateProduct(params,product_id,money);
                if(size!=1){
                    logger.info("标的已满标，投资失败。标的ID:"+product_id+"\t投资用户ID:"+trade.getUser_id()+"\t投资金额："+money+"\t标的借款ID:"+fundProduct.getLoan_id()+"\t借款用户ID:"+fundProduct.getUser_id());
                    return null;
                }
                fundInvestmentDetail.setTrade_amount(money);
                fundInvestmentDetail.setStatus(fundProduct.getProduct_status().intValue());
                fundInvestmentDetail.setIncome(money.multiply(totalRate).setScale(2,RoundingMode.HALF_UP));
                fundInvestmentDetail.setLoan_user_id(fundProduct.getUser_id());
            }
            fundInvestmentDetail.setInvest_period(period);
//            int size = Integer.parseInt(String.valueOf(Math.round(Math.ceil(loanDetail1.getLoan_period() / loanDetail1.getRepay_period()))));
            return new Object[]{fundInvestmentDetail,surplus};
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public BaseReturn loan(Map<String,Object> fundProduct, LoanDetail loanDetail1) {
        Calendar calendar = Calendar.getInstance();
        fundProduct.put("product_status", 3);
        fundProduct.put("update_time", calendar.getTime());
        int size = fundProductDao.updateProduct(fundProduct, Integer.parseInt(fundProduct.get("product_id") + ""), new BigDecimal(fundProduct.remove("money") + ""));
        if(size!=1) return null;
        fundProductAuditDao.save(new FundProductAudit(null, Integer.parseInt(fundProduct.get("product_id")+""), 3, "0000", new Date()));
        LoanDetail loanDetail = new LoanDetail(loanDetail1.getLoan_id());
        loanDetail.setLoan_status(Byte.valueOf("3")); //待放款
        loanDetail.setUpdate_time(calendar.getTime());
        loanDetailDao.update(loanDetail);
/*        FundUserAccount fundUserAccount = fundUserAccountDao.queryUserAccount(loanDetail1.getUser_id());
        Invest invest = new Invest(loanDetail1.getUser_id(), fundUserAccount.getUser_account_id(), loanDetail1.getLoan_id(), Integer.parseInt(loanDetail1.getLoan_period() + ""), loanDetail1.getLoan_amount());
        invest.setTradeStatus(IFundTradeDao.TradeStatus.TRANSFEREDMONEY);
        invest.setTradeType(IFundTradeDao.TradeType.LOAN);
        fundUtil.Tradelog(invest);
        fundUtil.preRepayment(loanDetail1);*/
        return new BaseReturn(0, loanDetail, processInfo.OPERATE_SUCCESS);
    }

    public void makeOver(FundInvestmentDetail fundInvestmentDetail) {
        Calendar calendar=Calendar.getInstance();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", fundInvestmentDetail.getUser_id());
        params.put("invest_detail_id", fundInvestmentDetail.getInvestment_detail_id());
        params.put("payment_status", 0);
        List<FundPayment> fundPayments = fundPaymentDao.queryByPros(params);
        if (fundPayments == null || fundPayments.isEmpty()) return;
        BigDecimal capital_balance = BigDecimal.ZERO;
        for (FundPayment fundPayment : fundPayments)
            capital_balance = capital_balance.add(fundPayment.getPre_payment_money());
        FundTransfer fundTransfer = new FundTransfer();
        fundTransfer.setTransfer_user_id(fundInvestmentDetail.getUser_id());
        fundTransfer.setInvest_detail_Id(fundInvestmentDetail.getInvestment_detail_id());
        fundTransfer.setCapital_balance(capital_balance);
        fundTransfer.setPeriods_surplus(fundPayments.size());//剩余期数
        fundTransfer.setTransfer_amount(capital_balance);
        fundTransfer.setShare_surplus(capital_balance);
        fundTransfer.setTransfer_time(calendar.getTime());
        fundTransfer.setTransfer_status(2); //2-转让中。。。。3-已转让
        fundTransfer.setCtime(calendar.getTime());
        fundTransfer.setUtime(calendar.getTime());
        fundTransferDao.save(fundTransfer);
        logger.info("购买债权转让ID:"+fundTransfer.getTransfer_id()+"\t购买用户ID:"+fundTransfer.getBuy_user_id()+"\t转让用户ID:"+fundInvestmentDetail.getUser_id());

    }
}
