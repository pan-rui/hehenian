package com.hhn.service;

import java.util.Map;

/**
 * 用户提现接口
 * Created by lenovo on 2014/12/10.
 */
public interface IWithdrawMoneyService {
    /**
     * 用户提现实现
     * @param map
     * @return
     */
    public Map<String, Object> doWithdraw(Map<String, Object> map);

}
