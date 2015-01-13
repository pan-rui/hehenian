package com.hhn.service.impl;

import com.hhn.dao.IAccountUserDao;
import com.hhn.dao.IFundProductDao;
import com.hhn.dao.IFundTradeDao;
import com.hhn.dao.IFundUserAccountDao;
import com.hhn.pojo.FundTrade;
import com.hhn.service.IBuyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/8.
 */
@Service
public class BuyRecordServiceImpl implements IBuyRecordService {

    @Autowired
    private IAccountUserDao accountUserDao;
    @Autowired
    private IFundUserAccountDao fundUserAccountDao;
    @Autowired
    private IFundProductDao fundProductDao;
    @Autowired
    private IFundTradeDao fundTradeDao;

    public int getRecordCount(Map<String, Object> map) {
        return fundTradeDao.findAllCount(map);
    }

    /**
     * 用户购买记录查询
     * @param map
     * @return
     */
    public Map<String, Object> buyRecordQuery(Map<String, Object> map){
//        int id = ((Integer)map.get("user_id")).intValue();
//        AccountUser user = accountUserDao.query(id);
//        FundUserAccount userAccount = fundUserAccountDao.queryUserAccount(user.getUser_id());
//        map.put("accountUser",user);
//        map.put("fundUserAccount",userAccount);
//        List<FundTrade> tradeList = fundTradeDao.findByPage(map);
//            for (FundTrade trade:tradeList){
//                id = trade.getProduct_id();
//                FundProduct product = fundProductDao.query(id);
//                trade.setFundProduct(product);
//            }
//        map.put("fundTradeList",tradeList);
        List<FundTrade> tradeList = fundTradeDao.findByPage(map);
        map.put("fundTradeList", tradeList);
        return map;
    }


}
