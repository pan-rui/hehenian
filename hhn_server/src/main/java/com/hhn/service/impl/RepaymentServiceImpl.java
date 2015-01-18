package com.hhn.service.impl;

import com.hhn.dao.*;
import com.hhn.pojo.*;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import com.hhn.util.DqlcConfig;
import com.hhn.util.FundUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * 还款
 * Created by lenovo on 2014/12/13.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RepaymentServiceImpl extends BaseService<FundPreRepayment> {
    @Resource
    private IFundPreRepaymentDao fundPreRepaymentDao;
    @Resource
    private IFundUserAccountDao fundUserAccountDao;
    @Resource
    private ILoanDetailDao loanDetailDao;
    @Resource
    private IFundProductDao fundProductDao;
    @Resource
    private FundUtil fundUtil;
    @Resource
    private DqlcConfig processInfo;
    @Resource
    private IFundProductAuditDao fundProductAuditDao;
    @Resource
    private IFundPaymentDao fundPaymentDao;
    @Resource
    private IFundInvestmentDetailDao fundInvestmentDetailDao;
    @Resource
    private IFundTradeDao fundTradeDao;

    public BaseReturn repay(Integer loan_id,Integer count, String operator,BigDecimal capital,BigDecimal interest,BigDecimal fee,String comment) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loan_id", loan_id);
        params.put("repay_times", count);
        LoanDetail loanDetail = loanDetailDao.query(loan_id);
        Calendar calendar = Calendar.getInstance();
        List<FundPreRepayment> fundPreRepayments = fundPreRepaymentDao.queryByPros(params);
        if (fundPreRepayments == null ||fundPreRepayments.isEmpty()) return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        for (FundPreRepayment fundPreRepayment : fundPreRepayments) { //TODO;实际还款金额与预还款金额不一致时。。。。。待确定
            BigDecimal proportion = fundPreRepayment.getPre_capital().divide(capital, new MathContext(10, RoundingMode.HALF_UP));
            FundPreRepayment fundPreRepayment1 = new FundPreRepayment(fundPreRepayment.getRepayment_id());
            fundPreRepayment1.setCapital(capital.multiply(proportion,new MathContext(12,RoundingMode.HALF_UP)));
            fundPreRepayment1.setInterest(interest.multiply(proportion,new MathContext(12,RoundingMode.HALF_UP)));
            if (!StringUtils.isEmpty(comment))
//        if(capital.compareTo(fundPreRepayment.getCapital())!=0 || interest.compareTo(fundPreRepayment.getInterest())!=0)
                fundPreRepayment1.setComment(comment);
            fundPreRepayment1.setRepay_date(calendar.getTime());
            fundPreRepayment1.setRepay_amount(fundPreRepayment1.getCapital().add(fundPreRepayment1.getInterest()));
            fundPreRepayment1.setRepay_status(IFundPreRepaymentDao.RepayStatus.REPAYMENTED.name());
            fundPreRepayment1.setUpdate_time(calendar.getTime());
            fundPreRepaymentDao.update(fundPreRepayment1); //预还款表状态
        }
        FundUserAccount fundUserAccount = fundUserAccountDao.query(loanDetail.getUser_id());
        FundUserAccount fundUserAccount1 = new FundUserAccount(fundUserAccount.getUser_account_id());
        fundUserAccount1.setBalance_amount(fundUserAccount.getBalance_amount().subtract(capital.add(interest).add(fee)));
        fundUserAccount1.setUpdate_time(calendar.getTime());
        fundUserAccountDao.update(fundUserAccount1); //账户余额
        List<FundInvestmentDetail> fundInvestmentDetails=loanDetailDao.queryForFID(loan_id);
        for (FundInvestmentDetail fundInvestmentDetail : fundInvestmentDetails) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("invest_detail_id", fundInvestmentDetail.getInvestment_detail_id());
            param.put("periods", count);
            List<FundPayment> fundPayments = fundPaymentDao.queryByPros(param);
            if(fundPayments==null||fundPayments.isEmpty()) return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
                FundPayment fundPayment1 = new FundPayment(fundPayments.get(0).getFund_payment_id());
                fundPayment1.setPayment_money(fundPayments.get(0).getPre_payment_money());
                fundPayment1.setPayment_time(calendar.getTime());
                fundPayment1.setUtime(calendar.getTime());
                fundPayment1.setPayment_status(1);//回款表状态
                fundPaymentDao.update(fundPayment1);
            FundTrade fundTrade = fundInvestmentDetailDao.queryForFundTrade(fundInvestmentDetail.getInvestment_detail_id());
            FundTrade fundTrade1 = new FundTrade(fundTrade.getTrade_id());
            fundTrade1.setTrade_balance(fundTrade.getTrade_balance().add(fundPayment1.getPayment_money()).setScale(2,RoundingMode.HALF_UP));
            fundTrade1.setUpdate_time(calendar.getTime());
            fundTradeDao.update(fundTrade1);
            logger.info("回款成功，投资人ID:"+fundPayment1.getUser_id()+"回款金额"+fundPayment1.getPayment_money()+"\t回款期数："+fundPayment1.getPeriods()+"\t借款用户ID:"+loanDetail.getUser_id());
        }
        Invest invest = new Invest(fundUserAccount.getUser_id(), fundUserAccount.getUser_account_id(), loan_id, 0,capital.add(interest).add(fee));
/*        if (calendar.getTime().compareTo(fundPreRepayment.getRepay_date()) < 0)
            invest.setTradeType(IFundTradeDao.TradeType.PRE_REPAYMENT);
        else*/
        invest.setTradeType(IFundTradeDao.TradeType.REPAYMENT);
        invest.setTradeStatus(IFundTradeDao.TradeStatus.SUCCESS);
        invest.setLogType(IFundAccountLogDao.LogType.REPAYMENT);
        fundUtil.Tradelog(invest); //交易记录
        FundProduct fundProduct = fundProductDao.queryByloanId(loanDetail.getLoan_id());
        FundProduct fundProduct1 = new FundProduct(fundProduct.getProduct_id());
        int periods=loanDetail.getLoan_period()/loanDetail.getRepay_period();
        if (count==(loanDetail.getLoan_period() % loanDetail.getRepay_period()>0?++periods:periods)) {
            fundProduct1.setProduct_status(Byte.valueOf("6"));//已还款
            fundProductDao.update(fundProduct1); //产品状态
            fundProductAuditDao.save(new FundProductAudit(null, fundProduct1.getProduct_id(), 6, operator, new Date()));
        }
        logger.info("还款成功，还款用户ID:"+loanDetail.getUser_id()+"还款金额："+fundPreRepayments.get(0).getCapital()+"还款利息："+fundPreRepayments.get(0).getInterest()+"还款总费用："+fundPreRepayments.get(0).getFee_amount());
        return new BaseReturn(0, loan_id, processInfo.OPERATE_SUCCESS);
    }
}
