package com.hhn.service;

import com.hhn.pojo.FundTrade;
import com.hhn.util.BaseReturn;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/17.
 */
public interface IRepalyFinancialService {

    public List<FundTrade> getRepalyList(Map<String, Object> map);

    public BaseReturn repalyStatus(Map<String, Object> map);

}
