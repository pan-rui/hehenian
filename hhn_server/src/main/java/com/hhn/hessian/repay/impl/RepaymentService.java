package com.hhn.hessian.repay.impl;

import com.hhn.hessian.repay.IRepaymentService;
import com.hhn.pojo.FundPreRepayment;
import com.hhn.service.ProcessInfo;
import com.hhn.service.impl.RepaymentServiceImpl;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 还款
 * Created by lenovo on 2014/12/13.
 */
@Controller
public class RepaymentService extends BaseService<FundPreRepayment> implements IRepaymentService {
    @Resource
    private RepaymentServiceImpl repaymentServiceImpl;
    @Resource
    private ProcessInfo processInfo;

    @Override
    public BaseReturn repay(Integer loan_id,Integer count, String operator, BigDecimal capital, BigDecimal interest,BigDecimal fee,String comment) {
        try {
            return repaymentServiceImpl.repay(loan_id,count, operator, capital, interest,fee,comment);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        }
    }
}
