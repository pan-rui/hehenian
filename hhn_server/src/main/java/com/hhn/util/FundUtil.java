package com.hhn.util;

import com.hehenian.biz.common.system.dataobject.SettDetailDo;
import com.hehenian.biz.common.trade.ISettleCalculatorService;
import com.hhn.dao.*;
import com.hhn.pojo.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2014/12/20.
 */
@Component
public class FundUtil extends BaseService {

    @Resource
    private IProductRateDao productRateDao;
    @Resource
    private IFundAccountLogDao fundAccountLogDao;
    @Resource
    private IFundTradeDao fundTradeDao;
    @Resource
    private IFundPreRepaymentDao fundPreRepaymentDao;
    @Resource
    private ISettleCalculatorService remoteRepaymentService;
    @Resource
    private IFundPaymentDao fundPaymentDao;

    /**
     * 获取相差月份
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public int getMonth(Date startDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int year = 0;
        int result = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        if ((year = end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) > 0)
            result += 12 * year;
        return result;
    }

    /**
     * 获取相差天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public long getDays(Date startDate, Date endDate) {
        long size = endDate.getTime() - startDate.getTime();
        return size / (24 * 3600 * 1000);
    }

    /**
     * 账户交易记录......
     *
     * @param invest
     */
    public FundTrade Tradelog(Invest invest) {
        //交易记录
        FundTrade fundTrade = new FundTrade();
        Calendar calendar = Calendar.getInstance();
        fundTrade.setProduct_id(invest.getProduct_id() != null ? invest.getProduct_id() : 0);
        fundTrade.setUser_id(invest.getUser_id());
        fundTrade.setUser_account_id(invest.getUser_account_id());
        fundTrade.setTrade_amount(invest.getMoney());
        fundTrade.setTrade_type(invest.getTradeType().name());
        fundTrade.setTrade_status(invest.getTradeStatus().name());
        fundTrade.setTrade_time(calendar.getTime());
        fundTrade.setUpdate_time(calendar.getTime());
        calendar.add(Calendar.MONTH, invest.getMonth());
        fundTrade.setExpect_trade_time(calendar.getTime());
        fundTrade.setTarget_type(String.valueOf(invest.getTargetType()));
        if (invest.getBalance() != null) fundTrade.setTrade_balance(invest.getBalance());
        if (invest.getMonth() != 0)
            fundTrade.setRate_id(productRateDao.queryByProperties("period", invest.getMonth()).get(0).getProduct_rate_id());
        fundTradeDao.save(fundTrade);
        logger.info("当前时间：" + calendar.getTime() + "增加一笔交易记录，交易ID:" + fundTrade.getTrade_id() + "\t交易类型:" + invest.getTradeType() + "\t交易状态：" + invest.getTradeStatus());
//流水记录
        FundAccountLog fundAccountLog = new FundAccountLog();
        fundAccountLog.setTrade_id(fundTrade.getTrade_id());
        fundAccountLog.setLog_type(invest.getLogType());
        fundAccountLog.setTrade_amount(fundTrade.getTrade_amount());
        fundAccountLog.setUser_id(fundTrade.getUser_id());
        fundAccountLog.setUser_account_id(fundTrade.getUser_account_id());
        fundAccountLog.setTrade_time(fundTrade.getTrade_time());
        fundAccountLogDao.save(fundAccountLog);
        logger.info("当前时间：" + calendar.getTime() + "增加一笔账户流水记录，流水ID:" + fundAccountLog.getAccount_log_id() + "\t流水类型:" + invest.getLogType());
        return fundTrade;
    }

    /**
     * 预还款记录
     *
     * @param loanDetail
     * @return
     */
    public BaseReturn preRepayment(LoanDetail loanDetail, FundProduct fundProduct, List<FundInvestmentDetail> fundInvestmentDetails) {
        List<SettDetailDo> settDetailDos = remoteRepaymentService.calSettDetail(loanDetail.getLoan_amount().doubleValue(), loanDetail.getLoan_rate().doubleValue(), loanDetail.getLoan_period().intValue(), fundProduct.getRepay_type().longValue());
        Calendar calendar = Calendar.getInstance();
        if (settDetailDos == null || settDetailDos.isEmpty()) return new BaseReturn(1, "还款费用远程接口返回数据为空");
        for (int i = 0; i < settDetailDos.size(); i++) {
            SettDetailDo settDetailDo = settDetailDos.get(i);
            FundPreRepayment fundPreRepayment = new FundPreRepayment();
            fundPreRepayment.setLoan_id(loanDetail.getLoan_id());
            fundPreRepayment.setUser_id(loanDetail.getUser_id());
            fundPreRepayment.setCapital(BigDecimal.valueOf(settDetailDo.getPrincipal()));
            fundPreRepayment.setInterest(BigDecimal.valueOf(settDetailDo.getInterest()));
            fundPreRepayment.setFee_amount(BigDecimal.valueOf(settDetailDo.getConsultFee() + settDetailDo.getServFee()));
            fundPreRepayment.setPre_repay_amount(BigDecimal.valueOf(settDetailDo.getPrincipal() + settDetailDo.getInterest() + settDetailDo.getConsultFee() + settDetailDo.getServFee()));
            fundPreRepayment.setRepay_status(IFundPreRepaymentDao.RepayStatus.NO_REPAYMENT.name());
            fundPreRepayment.setRepay_type(IFundPreRepaymentDao.RepayType.STAGES.name());
            fundPreRepayment.setCreate_time(calendar.getTime());
            fundPreRepayment.setRepay_times(i + 1);
            fundPreRepayment.setUpdate_time(calendar.getTime());
            fundPreRepayment.setPre_repay_date(settDetailDo.getRepayDate());
            fundPreRepaymentDao.save(fundPreRepayment);
            for (int j = 0; j < fundInvestmentDetails.size(); j++) {
                FundInvestmentDetail fundInvestmentDetail = fundInvestmentDetails.get(0);
                BigDecimal proportion = BigDecimal.ZERO;
                BigDecimal capital = BigDecimal.valueOf(settDetailDo.getPrincipal()).multiply(proportion).setScale(2, RoundingMode.HALF_DOWN);
                BigDecimal interest = BigDecimal.valueOf(settDetailDo.getInterest()).multiply(proportion).setScale(2, RoundingMode.HALF_DOWN);
                BigDecimal proportions = proportion;
                if (j < fundInvestmentDetails.size() - 1) {
                    proportion = fundInvestmentDetail.getTrade_amount().divide(loanDetail.getLoan_amount(), new MathContext(12, RoundingMode.HALF_UP));
                    proportions = proportions.add(proportion);
                } else proportion = BigDecimal.ONE.subtract(proportions);
                FundPayment fundPayment = new FundPayment();
                fundPayment.setUser_id(fundInvestmentDetail.getUser_id());
                fundPayment.setInvest_detail_id(fundInvestmentDetail.getInvestment_detail_id());
                fundPayment.setCapital(capital);
                fundPayment.setInterest(interest);
                fundPayment.setPre_payment_money(fundPayment.getCapital().add(fundPayment.getInterest()));
                fundPayment.setPre_payment_time(calendar.getTime());
                fundPayment.setCtime(calendar.getTime());
                fundPayment.setUtime(calendar.getTime());
                fundPayment.setPayment_status(0);
                fundPayment.setPeriods(i + 1);
                fundPaymentDao.save(fundPayment);
//                }
            }
        }
        logger.info("当前时间：" + calendar.getTime() + "新增预还款记录，借款ID:" + loanDetail.getLoan_id());
        return new BaseReturn(0, loanDetail.getLoan_id());
    }


}
