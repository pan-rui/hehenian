package com.hhn.dao;

import com.hhn.pojo.FundAccountLog;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundAccountLogDao extends BaseDao<FundAccountLog> {
    public enum LogType{
        RECHARGE,WITHDRAW,INVEST,RETUNED,LOAN,REPAYMENT,BUY,VERIFYCARD
//        交易类型（RECHARGE-充值,WITHDRAW-提现，INVEST-投资，RETUNED-回款，LOAN-借款，REPAYMENT-还款，BUY-购买,VERIFYCARD-验卡）
    }

}
