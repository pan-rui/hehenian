package com.hhn.hessian.invest.impl;

import javax.annotation.Resource;

import com.hhn.util.DqlcConfig;
import org.springframework.stereotype.Controller;

import com.hhn.hessian.invest.IPaymentService;
import com.hhn.pojo.FundProduct;
import com.hhn.service.impl.PaymentServiceimpl;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;

/**
 * Created by hynpublic on 2015/1/7.
 */
@Controller
public class PaymentService extends BaseService<FundProduct> implements IPaymentService {
    @Resource
    private PaymentServiceimpl paymentServiceimpl;
    @Resource
    private DqlcConfig processInfo;
    @Override
    public BaseReturn payment(Integer fundTradeId,String operator) {
        try {
            return paymentServiceimpl.payment(fundTradeId,operator);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }
}
