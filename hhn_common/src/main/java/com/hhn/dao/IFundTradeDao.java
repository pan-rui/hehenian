package com.hhn.dao;

import com.hhn.pojo.FundTrade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundTradeDao extends BaseDao<FundTrade> {
public enum TradeType implements Serializable{
    INVESTMENT,RECHARGE,WITHDRAW,PAYMENT,PRE_PAYMENT,INTEREST_FEE,LOAN,TURNOVER_FEE,REPAYMENT,PRE_REPAYMENT,LATE_REPAYMENT,PLATFORM_PAYMENT,VIP_FEE,CLAIM_FEE,RISK_REPAYMENT,RETURN_RISK_REPAYMENT,INTERMEDIARY_FEE,LATE_FEE,OTHER
//    交易类型（INVESTMENT-投资RECHARGE-充值WITHDRAW-提现PAYMENT-按期回款PRE_PAYMENT-提前回款INTEREST_FEE-利息服务费LOAN-借款TURNOVER_FEE-成交服务费REPAYMENT-按期还款PRE_REPAYMENT-提前还款LATE_REPAYMENT-逾期还款PLATFORM_PAYMENT-平台垫付VIP_FEE-VIP费用CLAIM_FEE-债权转让费RISK_REPAYMENT-风险金垫付,RETURN_RISK_REPAYMENT-风'
}
    public enum TradeStatus implements Serializable {
        SUCCESS,PROCESSING,FAILURE,TRANSFEREDMONEY,VERIFY,VERIFYED,VERIFYfAIL,PRELOAN
//        '交易状态（FAILURE-交易失败SUCCESS-交易成功PROCESSING-筹标中TRANSFEREDMONEY 平台已划款;VERIFY待审核;VERIFYED已审核待发布;VERIFYFAIL审核未通过,PRElOAN待放款）'
    }

    public enum TargetType implements Serializable{
        ANDROID,IOS,WP,PC,OTHER
    }

    public int findAllCount(Map map);

    public List<FundTrade> findByPage(Map map);

    public Integer getWebCount(Map map);

    public List<Map> getWebPageList(Map map);

/*    public java.math.BigDecimal querySurplusMoney();

    public java.math.BigDecimal queryPayMoney(@Param("noww")Date noww,@Param("trade_type")String trade_type);*/

    public java.math.BigDecimal queryPay(@Param("noww")Date noww,@Param("trade_type")String trade_type);

}
