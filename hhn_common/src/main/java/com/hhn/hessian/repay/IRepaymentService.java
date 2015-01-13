package com.hhn.hessian.repay;

import com.hhn.util.BaseReturn;

import java.math.BigDecimal;

/**
 * 还款
 * Created by lenovo on 2014/12/13.
 */
public interface IRepaymentService {

    public BaseReturn repay(Integer loan_id,Integer count,String operator,BigDecimal capital,BigDecimal interest,BigDecimal fee,String comment);

}
