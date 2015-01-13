package com.hhn.service.impl;

import com.aipg.rtreq.Trans;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import java.util.*;

/**
 * Created by lenovo on 2014/12/17.
 */
@Service
public class RechargeServiceImpl extends BaseService<FundActualAccountLog> {
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
    private ProcessInfo processInfo;
    @Resource
    private IFundAccountLogDao fundAccountLogDao;
    @Resource
    private IBankCodeDao bankCodeDao;

    public BaseReturn recharge(Map<String, Object> params) throws CloneNotSupportedException {
        Integer userId = null;
        if (params.containsKey("user_id"))
            userId = Integer.valueOf(String.valueOf(params.remove("user_id")));
        Trans trans = new Trans();
        allinPayService.copyProperties(params, trans);
        logger.info("进入充值接口。。。。。。。。。。。。。。。。。。。。。。。充值用户ID：" + userId + "充值金额：" + trans.getAMOUNT() + "充值卡号：" + trans.getACCOUNT_NO());
        trans.setREMARK(String.valueOf(System.currentTimeMillis()));
        List<BankCode> bankCodes = bankCodeDao.queryByProperties("code", trans.getBANK_CODE());
        if(bankCodes==null || bankCodes.isEmpty()) return new BaseReturn(1, processInfo.CARD_MISMATCHING);
        List<String> codes=JSON.parseArray(bankCodes.get(0).getRegex(), String.class);
        boolean flag=false;
        for(String code:codes)
            if(trans.getACCOUNT_NO().startsWith(code))
                flag=true;
        if(!flag) return new BaseReturn(1, processInfo.CARD_MISMATCHING);
        FundUserAccount fundUserAccount = fundUserAccountDao.queryUserAccount(userId);
        Calendar calendar = Calendar.getInstance();
        if (fundUserAccount == null) {   //是否存在资金账户
            fundUserAccount = new FundUserAccount();
            fundUserAccount.setUser_id(userId);
            fundUserAccount.setBalance_amount(BigDecimal.ZERO);
            fundUserAccount.setTotal_recharge_amount(BigDecimal.ZERO);
            fundUserAccount.setCreate_time(calendar.getTime());
            fundUserAccount.setUpdate_time(calendar.getTime());
            fundUserAccountDao.save(fundUserAccount);  //创建资金账户
        }
        FundActualAccountLog fundActualAccountLog = new FundActualAccountLog();
        fundActualAccountLog.setUser_id(userId);
        fundActualAccountLog.setTrade_amount(new BigDecimal(trans.getAMOUNT()));
        fundActualAccountLog.setTo_account(fundUserAccount.getUser_account_id().toString());
        fundActualAccountLog.setFrom_account(trans.getACCOUNT_NO());
        fundActualAccountLog.setThird_pay_type(4); //4充值，5提现
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
        transInfo.setOperation_type(4);
        transInfo.setActual_id(fundActualAccountLog.getActual_account_log_id());
        transInfo.setSocket_type(1);
        TransInfo transInfo1 = (TransInfo) transInfo.clone();
        transInfo1.setSocket_type(2);
        logger.info("调用通联支付接口开始。。。。。。。。。。。。。。。。。。。。。。。。");
        BaseReturn baseReturn = allinPayService.allinPay100014(trans, new TransInfo[]{transInfo, transInfo1});
        if (baseReturn.getReturnCode() == 0) {
            saveFundInfo(baseReturn, fundUserAccount, fundActualAccountLog);
            logger.info("调用通联支付结束，完成支付,充值用户ID："+userId+"充值金额："+trans.getAMOUNT()+"充值卡号："+trans.getACCOUNT_NO());
            return new BaseReturn(0, baseReturn.getData(), processInfo.OPERATE_SUCCESS);
        } else{
            logger.info("充值失败,充值用户ID："+userId+"充值金额："+trans.getAMOUNT()+"充值卡号："+trans.getACCOUNT_NO());
            return baseReturn;
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
    public void saveFundInfo(BaseReturn baseReturn, FundUserAccount fundUserAccount, FundActualAccountLog fundActualAccountLog) {
        if (baseReturn.getReturnCode() == 0) {
            TransInfo transInfo = (TransInfo) baseReturn.getData();
            transInfoDao.save(transInfo);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("user_id", transInfo.getUser_id());
            params.put("card_no", transInfo.getCard_id());
            List<FundBankCard> fundBankCards1 = fundBankCardDao.queryByPros(params);
            Calendar calendar = Calendar.getInstance();
            if (fundBankCards1 == null || fundBankCards1.isEmpty()) {
                FundBankCard fundBankCard1 = new FundBankCard();
                fundBankCard1.setUser_id(transInfo.getUser_id());
                fundBankCard1.setCard_no(transInfo.getCard_id());
                fundBankCard1.setCreate_time(calendar.getTime());
                fundBankCard1.setUpdate_time(calendar.getTime());
                List<FundBankCard> fundBankCards = fundBankCardDao.queryByUserId(transInfo.getUser_id());
                if (fundBankCards != null && !fundBankCards.isEmpty()) {
                    for (FundBankCard fundBankCard : fundBankCards) {
                        FundBankCard fundBankCard2 = new FundBankCard(fundBankCard.getBank_card_id());
                        fundBankCard2.setIs_default(Byte.valueOf("0"));
                        fundBankCard2.setUpdate_time(calendar.getTime());
                        fundBankCardDao.update(fundBankCard2);
                    }
                }
                fundBankCard1.setIs_default(Byte.valueOf("1"));
                fundBankCardDao.save(fundBankCard1); //保存银行卡信息
                logger.info("用户新增银行卡，用户ID："+transInfo.getUser_id()+"\t银行卡号："+transInfo.getCard_id()+"交易金额："+transInfo.getTrans_money());
            }
            FundUserAccount fundUserAccount1 = new FundUserAccount();
            fundUserAccount1.setUser_account_id(fundUserAccount.getUser_account_id());
            fundUserAccount1.setBalance_amount(fundUserAccount.getBalance_amount().add(transInfo.getTrans_money()));
            fundUserAccount1.setTotal_recharge_amount(fundUserAccount.getTotal_recharge_amount().add(transInfo.getTrans_money()));
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
            fundAccountLog.setLog_type(IFundAccountLogDao.LogType.RECHARGE);
            fundAccountLog.setRemark(transInfo.getThird_sn());
            fundAccountLog.setTrade_amount(transInfo.getTrans_money());
            fundAccountLog.setTrade_time(calendar.getTime());
            fundAccountLogDao.save(fundAccountLog);
            logger.info("新增用户实际流水,实际流水ID;"+fundAccountLog.getAccount_log_id()+"\t用户ID:"+fundAccountLog.getUser_id()+"交易金额："+fundAccountLog.getTrade_amount()+"交易类型：充值");
        }
    }
}
