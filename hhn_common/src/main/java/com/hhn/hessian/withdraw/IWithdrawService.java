package com.hhn.hessian.withdraw;

import com.hhn.util.BaseReturn;

import java.util.Map;

/**
 * 提现接口
 * Created by lenovo on 2014/12/17.
 */
public interface IWithdrawService {
    /**
     * 提现
     * @param params
     * @return
     */
    public BaseReturn withdraw(Map<String, Object> params);

    /**
     * 提现申请
     * @param params
     * @return
     */
    public BaseReturn applyWithdraw(Map<String, Object> params);

    public BaseReturn preWithdraw(Integer tradeId);
}
