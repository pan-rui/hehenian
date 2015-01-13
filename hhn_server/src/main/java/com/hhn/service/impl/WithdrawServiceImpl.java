package com.hhn.service.impl;

import com.aipg.rtreq.Trans;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhn.dao.*;
import com.hhn.pojo.*;
import com.hhn.service.ProcessInfo;
import com.hhn.service.pay.impl.AllinPayService;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private ProcessInfo processInfo;
    @Resource
    private IFundAccountLogDao fundAccountLogDao;
    public BaseReturn withdraw(Map<String, Object> params) throws CloneNotSupportedException {
        Integer userId = null;
        if (params.containsKey("user_id"))
            userId = Integer.valueOf(String.valueOf(params.remove("user_id")));
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("user_id",userId);
        param.put("is_default", 1);
        List<FundBankCard> fundBankCards=fundBankCardDao.queryByPros(param);
        if(fundBankCards==null||fundBankCards.isEmpty()) return new BaseReturn(1, "用户尚未绑定银行卡");
        Trans trans = new Trans();
        allinPayService.copyProperties(params, trans);
        logger.info("进入提现接口。。。。。。。。。。。。。。。。。。。。。。。提现用户ID：" + userId + "提现金额：" + trans.getAMOUNT() + "提现卡号：" + trans.getACCOUNT_NO());
        trans.setACCOUNT_NO(fundBankCards.get(0).getCard_no());
        trans.setREMARK(String.valueOf(System.currentTimeMillis()));
        FundUserAccount fundUserAccount = fundUserAccountDao.queryUserAccount(userId);
        Calendar calendar = Calendar.getInstance();
        if (fundUserAccount == null) {   //是否存在资金账户
            fundUserAccount = new FundUserAccount();
            fundUserAccount.setUser_id(userId);
            fundUserAccount.setBalance_amount(BigDecimal.ZERO);
            fundUserAccount.setTotal_withdraw_amount(BigDecimal.ZERO);
            fundUserAccount.setCreate_time(calendar.getTime());
            fundUserAccount.setUpdate_time(calendar.getTime());
            fundUserAccountDao.save(fundUserAccount);  //创建资金账户
        }
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
            saveFundInfo(baseReturn, fundUserAccount, fundActualAccountLog);
            logger.info("调用通联支付结束，完成支付,提现用户ID："+userId+"提现金额："+trans.getAMOUNT()+"提现卡号："+trans.getACCOUNT_NO());
            return new BaseReturn(0, baseReturn.getData(), processInfo.OPERATE_SUCCESS);
        } else{
            logger.info("提现失败,提现用户ID："+userId+"提现金额："+trans.getAMOUNT()+"提现卡号："+trans.getACCOUNT_NO());
            return baseReturn;
        }
    }

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.NESTED)
    public void saveFundInfo(BaseReturn baseReturn,FundUserAccount fundUserAccount,FundActualAccountLog fundActualAccountLog){
        if (baseReturn.getReturnCode() == 0) {
            TransInfo transInfo = (TransInfo) baseReturn.getData();
            transInfoDao.save(transInfo);
            Calendar calendar = Calendar.getInstance();
            FundUserAccount fundUserAccount1 = new FundUserAccount();
            fundUserAccount1.setUser_account_id(fundUserAccount.getUser_account_id());
            fundUserAccount1.setBalance_amount(fundUserAccount.getBalance_amount().subtract(transInfo.getTrans_money()));
            fundUserAccount1.setTotal_withdraw_amount(fundUserAccount.getTotal_withdraw_amount().add(transInfo.getTrans_money()));
            fundUserAccount1.setUpdate_time(calendar.getTime());
            fundUserAccountDao.update(fundUserAccount1); //更新资金账户
            FundActualAccountLog fundActualAccountLog1 = new FundActualAccountLog(fundActualAccountLog.getActual_account_log_id());
            fundActualAccountLog1.setUpdate_time(calendar.getTime());
            fundActualAccountLog1.setTransfer_status(IFundActualAccountLogDao.TransferStatus.TRANSFERRED.name());
            fundActualAccountLogDao.update(fundActualAccountLog1);
            FundAccountLog fundAccountLog = new FundAccountLog();
            fundAccountLog.setTrade_id(fundActualAccountLog.getActual_account_log_id());
            fundAccountLog.setUser_id(transInfo.getUser_id());
            fundAccountLog.setUser_account_id(fundUserAccount.getUser_account_id());
            fundAccountLog.setLog_type(IFundAccountLogDao.LogType.WITHDRAW);
            fundAccountLog.setRemark(transInfo.getThird_sn());
            fundAccountLog.setTrade_amount(transInfo.getTrans_money());
            fundAccountLog.setTrade_time(calendar.getTime());
            fundAccountLogDao.save(fundAccountLog);
            logger.info("新增用户实际流水,实际流水ID;"+fundAccountLog.getAccount_log_id()+"\t用户ID:"+fundAccountLog.getUser_id()+"交易金额："+fundAccountLog.getTrade_amount()+"交易类型：充值");
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
