package com.hhn.hessian.repay.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import com.hhn.util.DqlcConfig;
import org.springframework.stereotype.Controller;

import com.hhn.hessian.repay.IRepaymentService;
import com.hhn.pojo.FundPreRepayment;
import com.hhn.service.impl.RepaymentServiceImpl;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;

/**
 * 还款
 * Created by lenovo on 2014/12/13.
 */
@Controller
public class RepaymentService extends BaseService<FundPreRepayment> implements IRepaymentService {
    @Resource
    private RepaymentServiceImpl repaymentServiceImpl;
    @Resource
    private DqlcConfig processInfo;

    @Override
    public BaseReturn repay(Integer loan_id,Integer count, String operator, BigDecimal capital, BigDecimal interest,BigDecimal fee,String comment) {
        try {
            return repaymentServiceImpl.repay(loan_id,count, operator, capital, interest,fee,comment);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }
}
