package com.hhn.hessian.invest;

import com.hhn.util.BaseReturn;

import java.util.HashMap;

/**
 * Created by lenovo on 2014/12/16.
 */
public interface IInvestProductService {

    public BaseReturn getProductRateList();

    public BaseReturn getProductRate(HashMap<String,Object> map);

    public BaseReturn getWebTradeCount(HashMap<String,Object> map);

    public BaseReturn getWebTradeList(HashMap<String,Object> map);

    /**
     *
     * @param map
     * @return
     */
    public BaseReturn getWebProductCount(HashMap<String,Object> map);

    /**
     * 查询购买记录及明细
     * @param map
     * @return
     */
    public BaseReturn getWebProductList(HashMap<String,Object> map);

    /**
     * 查询投资记录数
     * @param map
     * @return
     */
    public BaseReturn getWebInvestmentCount(HashMap<String, Object> map);
    /**
     * 查询投资记录列表
     * @param map
     * @return
     */
    public BaseReturn getWebInvestmentList(HashMap<String, Object> map);

}
