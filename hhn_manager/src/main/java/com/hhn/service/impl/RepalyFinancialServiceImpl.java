package com.hhn.service.impl;

import com.hhn.dao.IFundTradeDao;
import com.hhn.pojo.FundTrade;
import com.hhn.service.IRepalyFinancialService;
import com.hhn.util.BaseReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/17.
 */
@Service
public class RepalyFinancialServiceImpl implements IRepalyFinancialService {
    @Autowired
    private IFundTradeDao fundTradeDao;

    /**
     * 查询提现申请列表
     * @param map
     * @return
     */
    public List<FundTrade> getRepalyList(Map<String, Object> map){
        return fundTradeDao.findByPage(map);
    }

    /**
     * 提现审核处理
     * @param map
     * @return
     */
    public BaseReturn repalyStatus(Map<String, Object> map){

        return new BaseReturn(0,true, "成功放款!");
    }
}
