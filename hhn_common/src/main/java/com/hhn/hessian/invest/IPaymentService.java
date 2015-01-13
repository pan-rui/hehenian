package com.hhn.hessian.invest;

import com.hhn.util.BaseReturn;

/**
 * 回款
 * Created by lenovo on 2014/12/13.
 */
public interface IPaymentService {
    /**
     * 赎回处理
     * @param fundTradeId
     * @return
     */
    public BaseReturn payment(Integer fundTradeId,String operator);

}
