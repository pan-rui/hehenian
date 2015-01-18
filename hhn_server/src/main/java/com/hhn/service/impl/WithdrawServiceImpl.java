package com.hhn.service.impl;

import com.aipg.rtreq.Trans;
import com.hhn.dao.*;
import com.hhn.pojo.*;
import com.hhn.service.pay.impl.AllinPayService;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import com.hhn.util.DqlcConfig;
import com.hhn.util.FundUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/17.
 */
@Service
public class WithdrawServiceImpl extends BaseService<FundActualAccountLog>{
    @Resource
    private AllinPayService allinPayService;
    @Resource
    private IFundUserAccountDao fundUserAccountDao;
    @Resource
    private IFundActualAccountLogDao fundActualAccountLogDao;
    @Resource
    private IFundBankCardDao fundBankCardDao;
    @Resource
    private ITransInfoDao transInfoDao;
    @Resource
    private IFundTradeDao fundTradeDao;
    @Resource
    private DqlcConfig processInfo;
    @Resource
    private FundUtil fundUtil;
    public BaseReturn withdraw(Map<String, Object> params) throws CloneNotSupportedException {
        Integer userId = null;

        //Modify by Tang Rufeng 20140118 begin
        if (params.containsKey("user_id"))
            userId = Integer.valueOf(String.valueOf(params.remove("user_id")));
        else{
            return new BaseReturn(1, "未指定提现用户");
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("user_id",userId);
        //param.put("is_default",1);
        param.put("bank_status", 3);  //查询已绑定的卡
        //Modify by Tang Rufeng 20140118 end

        List<FundBankCard> fundBankCards=fundBankCardDao.queryByPros(param);
        if(fundBankCards==null||fundBankCards.isEmpty())
            return new BaseReturn(1, "用户尚未绑定银行卡");
        Trans trans = new Trans();
        allinPayService.copyProperties(params, trans);
        logger.info("进入提现接口。。。。。。。。。。。。。。。。。。。。。。。提现用户ID：" + userId + "提现金额：" + trans.getAMOUNT() + "提现卡号：" + trans.getACCOUNT_NO());
        trans.setACCOUNT_NO(fundBankCards.get(0).getCard_no());
        trans.setREMARK(String.valueOf(System.currentTimeMillis()));
        FundUserAccount fundUserAccount = fundUserAccountDao.queryUserAccount(userId);
        Calendar calendar = Calendar.getInstance();
        if (fundUserAccount == null)    //是否存在资金账户
            return new BaseReturn(1, "您的账户余额为0.00");
        if(new BigDecimal(trans.getAMOUNT()).compareTo(fundUserAccount.getBalance_amount())>0)
            return new BaseReturn(1, "您的账户余额为:" + fundUserAccount.getBalance_amount() + ",请重新输入.");
        FundActualAccountLog fundActualAccountLog = new FundActualAccountLog();
        fundActualAccountLog.setUser_id(userId);
        fundActualAccountLog.setTrade_amount(new BigDecimal(trans.getAMOUNT()));
        fundActualAccountLog.setTo_account(trans.getACCOUNT_NO());
        fundActualAccountLog.setFrom_account(fundUserAccount.getUser_account_id().toString());
        fundActualAccountLog.setThird_pay_type(5); //4充值，5提现
        fundActualAccountLog.setThird_pay_account_no(trans.getACCOUNT_NAME());
        fundActualAccountLog.setThird_pay_id(trans.getREMARK());
        fundActualAccountLog.setThird_trade_time(calendar.getTime());
        fundActualAccountLog.setTransfer_status(IFundActualAccountLogDao.TransferStatus.NOTRANSFER.name());
        fundActualAccountLog.setUpdate_time(calendar.getTime());
        fundActualAccountLogDao.save(fundActualAccountLog); //实际流水记录
        TransInfo transInfo = new TransInfo();
        transInfo.setUser_id(userId);
        transInfo.setBank_user(trans.getACCOUNT_NAME());
        transInfo.setCard_id(trans.getACCOUNT_NO());
        transInfo.setTrans_money(new BigDecimal(trans.getAMOUNT()));
        transInfo.setThird_sn(trans.getREMARK());
        transInfo.setOperation_type(5);
        transInfo.setActual_id(fundActualAccountLog.getActual_account_log_id());
        transInfo.setSocket_type(1);
        TransInfo transInfo1 = (TransInfo) transInfo.clone();
        transInfo1.setSocket_type(2);
        logger.info("调用通联支付接口开始。。。。。。。。。。。。。。。。。。。。。。。。");
        BaseReturn baseReturn = allinPayService.allinPay100014(trans, new TransInfo[]{transInfo, transInfo1});
        if (baseReturn.getReturnCode() == 0) {
            fundUtil.saveFundInfo(baseReturn, fundUserAccount, fundActualAccountLog,"100011", IFundAccountLogDao.LogType.WITHDRAW);
            logger.info("调用通联支付结束，完成支付,提现用户ID："+userId+"提现金额："+trans.getAMOUNT()+"提现卡号："+trans.getACCOUNT_NO());
            return new BaseReturn(0, baseReturn.getData(), processInfo.OPERATE_SUCCESS);
        } else{
            logger.info("提现失败,提现用户ID："+userId+"提现金额："+trans.getAMOUNT()+"提现卡号："+trans.getACCOUNT_NO()+"errorMessage:"+baseReturn.getMessageInfo());
            transInfoDao.save((TransInfo) baseReturn.getData());
            return baseReturn;
        }
    }



    public BaseReturn applyWithdraw(Map<String, Object> params) {
        Integer userId = Integer.valueOf(String.valueOf(params.get("user_id")));
        BigDecimal amount=null;
        Object obj = params.get("amount");
        Calendar calendar=Calendar.getInstance();
        if(obj instanceof BigDecimal) {
            amount = (BigDecimal) obj;
            FundTrade fundTrade = new FundTrade();
            fundTrade.setUser_id(userId);
            FundUserAccount fundUserAccount = fundUserAccountDao.queryUserAccount(userId);
            if(fundUserAccount==null)
                return new BaseReturn(1, BaseReturn.Err_data_inValid, "账户异常....");
            else
                fundTrade.setUser_account_id(fundUserAccount.getUser_account_id());
            if(amount.compareTo(fundUserAccount.getBalance_amount())>0)
                return new BaseReturn(1, "您的账户余额为:" + fundUserAccount.getBalance_amount() + ",请重新输入.");
            fundTrade.setTrade_amount(amount);
            fundTrade.setTrade_type(IFundTradeDao.TradeType.WITHDRAW.name());
            fundTrade.setTrade_status(IFundTradeDao.TradeStatus.VERIFY.name());
            fundTrade.setExpect_trade_time(calendar.getTime());
            fundTrade.setTrade_time(calendar.getTime());
            fundTrade.setUpdate_time(calendar.getTime());
            fundTradeDao.save(fundTrade);
            logger.info("提现申请成功，提现用户ID:"+fundTrade.getUser_id()+"\t提现金额："+fundTrade.getTrade_amount());
            return new BaseReturn(0,fundTrade,processInfo.OPERATE_SUCCESS);
        }else return new BaseReturn(1, BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
    }

    public BaseReturn preWithdraw(Integer tradeId) {
        FundTrade fundTrade = fundTradeDao.query(tradeId);
        FundTrade fundTrade1 = new FundTrade();
        fundTrade1.setTrade_id(fundTrade.getTrade_id());
        fundTrade1.setTrade_status(IFundTradeDao.TradeStatus.VERIFYED.name());
        fundTrade1.setUpdate_time(Calendar.getInstance().getTime());
        fundTradeDao.update(fundTrade1);
        logger.info("提现审核成功，提现用户ID:"+fundTrade.getUser_id()+"\t提现金额："+fundTrade.getTrade_amount());
        return new BaseReturn(0,fundTrade1,processInfo.OPERATE_SUCCESS);
    }

}
