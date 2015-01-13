package com.hhn.dao;

import com.hhn.pojo.FundPreRepayment;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundPreRepaymentDao extends BaseDao<FundPreRepayment> {
    public enum RepayStatus{
        NO_REPAYMENT,REPAYMENTED
    }
    public enum RepayType{
        STAGES,PRINCIPAL,INTEREST,PRE_PENALTY,INTERMEDIARY_FEE,LATE_FEE
//        '还款类型（STAGES-分期,PRINCIPAL-本金,INTEREST-利息,PRE_PENALTY-提前还款违约金,INTERMEDIARY_FEE-居间服务费,LATE_FEE-逾期服务费）',
    }

}
