package com.hhn.hessian.invest.impl;

import com.hhn.hessian.invest.IPaymentService;
import com.hhn.pojo.FundProduct;
import com.hhn.service.ProcessInfo;
import com.hhn.service.impl.PaymentServiceimpl;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Created by hynpublic on 2015/1/7.
 */
@Controller
public class PaymentService extends BaseService<FundProduct> implements IPaymentService {
    @Resource
    private PaymentServiceimpl paymentServiceimpl;
    @Resource
    private ProcessInfo processInfo;
    @Override
    public BaseReturn payment(Integer fundTradeId,String operator) {
        try {
            return paymentServiceimpl.payment(fundTradeId,operator);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        }
    }
}
