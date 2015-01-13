package com.hhn.service.impl;

import com.hhn.dao.IFundInvestmentDetailDao;
import com.hhn.pojo.FundInvestmentDetail;
import com.hhn.pojo.FundProduct;
import com.hhn.service.IInvestRecordService;
import com.hhn.util.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/9.
 */
@Service
public class InvestRecordServiceImpl extends BaseService<FundProduct> implements IInvestRecordService {

    @Autowired
    private IFundInvestmentDetailDao fundInvestmentDetailDao;
    /**
     * 分页查询用户投资记录
     * @param map
     * @return
     */
    public Map<String, Object> investRecordList(Map<String, Object> map) {
        List<FundInvestmentDetail> investList = fundInvestmentDetailDao.getInvestDetailList(map);
        map.put("investDetailList", investList);
        return map;
    }

}
