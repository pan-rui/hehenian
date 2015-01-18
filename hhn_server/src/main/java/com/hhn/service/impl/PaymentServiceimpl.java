package com.hhn.service.impl;

import com.hhn.dao.*;
import com.hhn.pojo.*;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import com.hhn.util.DqlcConfig;
import com.hhn.util.FundUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回款(手动或自动赎回）
 * Created by lenovo on 2014/12/13.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PaymentServiceimpl extends BaseService<FundReceipt> {
    //    @Resource
//    private IFundReceiptDao fundReceiptDao;
    @Resource
    private IFundProductDao fundProductDao;
    @Resource
    private IFundUserAccountDao fundUserAccountDao;
    @Resource
    private FundUtil fundUtil;
    @Resource
    private IProductRateDao productRateDao;
    @Resource
    private DqlcConfig processInfo;
    @Resource
    private IFundInvestmentDetailDao fundInvestmentDetailDao;
    @Resource
    private IFundTradeDao fundTradeDao;
    @Resource
    private IFundProductAuditDao fundProductAuditDao;
    @Resource
    private IFundPaymentDao fundPaymentDao;
    @Resource
    private IFundTransferDao fundTransferDao;

    public BaseReturn payment(Integer fundTradeId,String operator) {
/*  自动回款
      Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, Integer.valueOf(processInfo.getLOAN_AHEAD())); //计算回款时间
        List<FundTrade> fundTradeList = fundReceiptDao.queryByDate(now.getTime(), IFundTradeDao.TradeType.INVESTMENT.name()); //查询指定时间内待回歀交易
        for (FundTrade fundTrade : fundTradeList) {
  */
        FundTrade fundTrade = fundTradeDao.query(fundTradeId);
        FundTrade fundTrade1 = new FundTrade(fundTradeId);
        FundUserAccount fundUserAccount = fundUserAccountDao.query(fundTrade.getUser_account_id()); //获取该交易资金账户
//            FundProduct fundProduct=fundProductDao.query(fundTrade.getProduct_id());
        List<FundProduct> fundProducts = fundProductDao.queryByTradeId(fundTrade.getTrade_id()); //获取交易相关标的
        FundUserAccount fundUserAccount1 = new FundUserAccount();
        fundUserAccount1.setUser_account_id(fundUserAccount.getUser_account_id());
        BigDecimal srcMoney = new BigDecimal(fundTrade.getTrade_amount().longValue());
        int month = fundUtil.getMonth(fundTrade.getTrade_time(), fundTrade.getExpect_trade_time());
        BigDecimal rate = BigDecimal.ONE.add(BigDecimal.valueOf(month).divide(BigDecimal.valueOf(12)).multiply(productRateDao.query(fundTrade.getRate_id()).getRate()));//TODO:利率
        BigDecimal totalMoney = srcMoney.multiply(rate, new MathContext(2));
        fundUserAccount1.setBalance_amount(fundUserAccount.getBalance_amount().add(totalMoney));
        fundUserAccount1.setTotal_income(fundUserAccount.getTotal_income().add(totalMoney.subtract(srcMoney)));
        fundUserAccount1.setFreeze_amount(fundUserAccount.getFreeze_amount().subtract(srcMoney));//冻结金额
        fundUserAccountDao.update(fundUserAccount1); //更新用户资金账户
        for (FundProduct fundProduct : fundProducts) {
//            FundProduct fundProduct1 = new FundProduct(fundProduct.getProduct_id());
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("product_id", fundProduct.getProduct_id());
            params.put("fund_trade_id", fundTrade.getTrade_id());
            List<FundInvestmentDetail> fundInvestmentDetails = fundInvestmentDetailDao.queryByPros(params);
            if (fundInvestmentDetails.isEmpty()) return new BaseReturn(0, processInfo.DATA_INVALID);
            FundInvestmentDetail fundInvestmentDetail = fundInvestmentDetails.get(0);
            Calendar calendar = Calendar.getInstance();
            if (fundProduct.getInvest_amount().compareTo(srcMoney) <= 0) { //判断交易金额是否等于标的投资金额
                if (fundInvestmentDetail.getInvest_period() < fundProduct.getLoan_period()) {      //...............
                    short days = Short.valueOf(String.valueOf(fundUtil.getDays(calendar.getTime(), fundTrade.getExpect_trade_time())));
                    Short period = (short) (fundProduct.getLoan_period() - fundInvestmentDetail.getInvest_period());
                    makeOver(fundInvestmentDetail);
                }
  /*              else {
                    fundProduct1.setProduct_status(Byte.valueOf("6")); //TODO:标的状态 ：废弃
                    fundProduct1.setUpdate_time(calendar.getTime());
                    fundProductDao.update(fundProduct1);
                }*/
            } else {
                if (fundInvestmentDetail.getInvest_period() < fundProduct.getLoan_period()) {     //...............
                    short days = Short.valueOf(String.valueOf(fundUtil.getDays(calendar.getTime(), fundTrade.getExpect_trade_time())));
                    Short period = (short) (fundProduct.getLoan_period() - fundInvestmentDetail.getInvest_period());
                    makeOver(fundInvestmentDetail);
                }
            }
        }
        fundTrade.setTrade_balance(BigDecimal.ZERO);
        Invest invest = new Invest(fundTrade.getUser_id(), fundTrade.getUser_account_id(), fundTrade.getProduct_id(), 0, totalMoney);
        invest.setTradeType(IFundTradeDao.TradeType.PAYMENT);
        invest.setTradeStatus(IFundTradeDao.TradeStatus.TRANSFEREDMONEY);
        fundTrade1.setUpdate_time(Calendar.getInstance().getTime());
        fundTradeDao.update(fundTrade1);
        invest.setLogType(IFundAccountLogDao.LogType.RETUNED);
        fundUtil.Tradelog(invest); //交易记录
//        }
        logger.info("==========产品赎回成功，赎回金额："+invest.getMoney()+"\t赎回用户ID："+invest.getUser_id());
        return new BaseReturn(0, processInfo.OPERATE_SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public void makeOver(FundInvestmentDetail fundInvestmentDetail) {
        Calendar calendar = Calendar.getInstance();
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
        fundTransfer.setPeriods_surplus(fundPayments.size());
        fundTransfer.setTransfer_amount(capital_balance);
        fundTransfer.setShare_surplus(capital_balance);
        fundTransfer.setTransfer_time(calendar.getTime());
        fundTransfer.setTransfer_status(2); //2-转让中。。。。3-已转让
        fundTransfer.setCtime(calendar.getTime());
        fundTransfer.setUtime(calendar.getTime());
        fundTransferDao.save(fundTransfer);
        logger.info("==========生成债权转让记录，转让记录ID："+fundTransfer.getTransfer_user_id()+"\t购买用户ID:"+fundTransfer.getBuy_user_id()+"\t投资明细ID:"+fundTransfer.getInvest_detail_Id());
    }

}
