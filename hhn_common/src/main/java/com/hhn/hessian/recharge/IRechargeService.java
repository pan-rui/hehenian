package com.hhn.hessian.recharge;

import com.hhn.util.BaseReturn;

import java.util.Map;

/**
 * 充值接口
 * Created by lenovo on 2014/12/17.
 */
public interface IRechargeService {
    /**
     * 充值
     * @param params 用户ID,账户 等相关信息
     * @return
     */
    public BaseReturn recharge(Map<String,Object> params);
}
