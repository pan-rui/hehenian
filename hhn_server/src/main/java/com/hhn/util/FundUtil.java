package com.hhn.util;

import com.alibaba.fastjson.JSON;
import com.hehenian.biz.common.system.dataobject.SettDetailDo;
import com.hehenian.biz.common.trade.ISettleCalculatorService;
import com.hhn.dao.*;
import com.hhn.pojo.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private ITransInfoDao transInfoDao;
    @Resource
    private IFundActualAccountLogDao fundActualAccountLogDao;
    @Resource
    private IFundUserAccountDao fundUserAccountDao;

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
        logger.info("调用还款费用查询接口(ISettleCalculatorService)参数：借款金额-" + loanDetail.getLoan_amount().doubleValue() + "\t年利率-" + loanDetail.getLoan_rate().doubleValue() + "\t借款期数-" + loanDetail.getLoan_period().intValue() + "\t结算方式-" + fundProduct.getRepay_type().longValue());
        List<SettDetailDo> settDetailDos = remoteRepaymentService.calSettDetail(loanDetail.getLoan_amount().doubleValue(), loanDetail.getLoan_rate().doubleValue(), loanDetail.getLoan_period().intValue(), fundProduct.getRepay_type().longValue());
        logger.info("调用还款费用查询接口返回值。。。。。。。" + JSON.toJSONString(settDetailDos));
        Calendar calendar = Calendar.getInstance();
        if (settDetailDos == null || settDetailDos.isEmpty()) return new BaseReturn(1, "还款费用远程接口返回数据为空");
        BigDecimal[] bigDecimals = new BigDecimal[fundInvestmentDetails.size()];
        for (int i = 0; i < settDetailDos.size(); i++) {
            SettDetailDo settDetailDo = settDetailDos.get(i);
            FundPreRepayment fundPreRepayment = new FundPreRepayment();
            fundPreRepayment.setLoan_id(loanDetail.getLoan_id());
            fundPreRepayment.setUser_id(loanDetail.getUser_id());
            fundPreRepayment.setCapital(BigDecimal.valueOf(settDetailDo.getPrincipal()));
            fundPreRepayment.setInterest(BigDecimal.valueOf(settDetailDo.getInterest()));
            fundPreRepayment.setFee_amount(BigDecimal.valueOf(settDetailDo.getConsultFee() + settDetailDo.getServFee()));
            fundPreRepayment.setPre_repay_amount(BigDecimal.valueOf(settDetailDo.getPrincipal() + settDetailDo.getInterest() + settDetailDo.getConsultFee() + settDetailDo.getServFee()).setScale(2, RoundingMode.HALF_UP));
            fundPreRepayment.setRepay_status(IFundPreRepaymentDao.RepayStatus.NO_REPAYMENT.name());
            fundPreRepayment.setRepay_type(IFundPreRepaymentDao.RepayType.STAGES.name());
            fundPreRepayment.setCreate_time(calendar.getTime());
            fundPreRepayment.setRepay_times(i + 1);
            fundPreRepayment.setUpdate_time(calendar.getTime());
            fundPreRepayment.setPre_repay_date(settDetailDo.getRepayDate());
            fundPreRepaymentDao.save(fundPreRepayment);
            BigDecimal capitals = BigDecimal.ZERO;
            BigDecimal interests = BigDecimal.ZERO;
            for (int j = 0; j < fundInvestmentDetails.size(); j++) {
                FundInvestmentDetail fundInvestmentDetail = fundInvestmentDetails.get(j);
                FundPayment fundPayment = new FundPayment();
                if (j < fundInvestmentDetails.size() - 1) {
                    BigDecimal proportion = fundInvestmentDetail.getTrade_amount().divide(loanDetail.getLoan_amount(), new MathContext(12, RoundingMode.HALF_UP));
                    BigDecimal capital = BigDecimal.valueOf(settDetailDo.getPrincipal()).multiply(proportion).setScale(2, RoundingMode.HALF_DOWN);
                    BigDecimal interest = BigDecimal.valueOf(settDetailDo.getInterest()).multiply(proportion).setScale(2, RoundingMode.HALF_DOWN);
                    capitals = capitals.add(capital);
                    interests = interests.add(interest);
                    if(i<settDetailDos.size()-1) {
                        fundPayment.setCapital(capital);
                    }else
                        fundPayment.setCapital(fundInvestmentDetail.getTrade_amount().subtract(bigDecimals[j]).setScale(2,RoundingMode.HALF_UP));
                        fundPayment.setInterest(interest);
                } else {
                    if (i < settDetailDos.size() - 1) {
                    fundPayment.setCapital(BigDecimal.valueOf(settDetailDo.getPrincipal()).subtract(capitals).setScale(2, RoundingMode.HALF_DOWN));
                    }else fundPayment.setCapital(fundInvestmentDetail.getTrade_amount().subtract(bigDecimals[j]).setScale(2,RoundingMode.HALF_UP));
                    fundPayment.setInterest(BigDecimal.valueOf(settDetailDo.getInterest()).subtract(interests).setScale(2, RoundingMode.HALF_DOWN));
                }
                bigDecimals[j] = fundPayment.getCapital().add(bigDecimals[j] == null ? BigDecimal.ZERO : bigDecimals[j]);
                fundPayment.setUser_id(fundInvestmentDetail.getUser_id());
                fundPayment.setInvest_detail_id(fundInvestmentDetail.getInvestment_detail_id());
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
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.NESTED)
    public void saveFundInfo(BaseReturn baseReturn,FundUserAccount fundUserAccount,FundActualAccountLog fundActualAccountLog,String trxCode,IFundAccountLogDao.LogType logType){
        if (baseReturn.getReturnCode() == 0) {
            TransInfo transInfo = (TransInfo) baseReturn.getData();
            transInfoDao.save(transInfo);
            Calendar calendar = Calendar.getInstance();
            FundUserAccount fundUserAccount1 = new FundUserAccount();
            fundUserAccount1.setUser_account_id(fundUserAccount.getUser_account_id());
            if(trxCode.equals("100011")){
            fundUserAccount1.setBalance_amount(fundUserAccount.getBalance_amount().add(transInfo.getTrans_money()));
            fundUserAccount1.setTotal_recharge_amount(fundUserAccount.getTotal_withdraw_amount().add(transInfo.getTrans_money()));
            }else if (trxCode.equals("100014")) {
                fundUserAccount1.setBalance_amount(fundUserAccount.getBalance_amount().subtract(transInfo.getTrans_money()));
                fundUserAccount1.setTotal_withdraw_amount(fundUserAccount.getTotal_withdraw_amount().add(transInfo.getTrans_money()));
            }
            FundActualAccountLog fundActualAccountLog1 = new FundActualAccountLog(fundActualAccountLog.getActual_account_log_id());
            fundUserAccount1.setUpdate_time(calendar.getTime());
            if(!trxCode.equals("100019"))
            fundUserAccountDao.update(fundUserAccount1); //更新资金账户
            fundActualAccountLog1.setUpdate_time(calendar.getTime());
            fundActualAccountLog1.setTransfer_status(IFundActualAccountLogDao.TransferStatus.TRANSFERRED.name());
            fundActualAccountLog1.setThird_pay_id(transInfo.getThird_sn());
            fundActualAccountLogDao.update(fundActualAccountLog1);
            FundAccountLog fundAccountLog = new FundAccountLog();
            fundAccountLog.setTrade_id(fundActualAccountLog.getActual_account_log_id());
            fundAccountLog.setUser_id(transInfo.getUser_id());
            fundAccountLog.setUser_account_id(fundUserAccount.getUser_account_id());
            fundAccountLog.setRemark(transInfo.getThird_sn());
            fundAccountLog.setTrade_amount(transInfo.getTrans_money());
            fundAccountLog.setTrade_time(calendar.getTime());
            fundAccountLog.setLog_type(logType);
            fundAccountLogDao.save(fundAccountLog);
            logger.info("新增用户实际流水,实际流水ID;"+fundAccountLog.getAccount_log_id()+"\t用户ID:"+fundAccountLog.getUser_id()+"交易金额："+fundAccountLog.getTrade_amount()+"交易类型：充值");
        }
    }

}
