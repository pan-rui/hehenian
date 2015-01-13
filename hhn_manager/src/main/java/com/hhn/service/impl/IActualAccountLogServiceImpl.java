package com.hhn.service.impl;

import com.hhn.dao.IFundActualAccountLogDao;
import com.hhn.service.IActualAccountLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/8.
 */
@Service
public class IActualAccountLogServiceImpl implements IActualAccountLogService {

    @Autowired
    private IFundActualAccountLogDao fundActualAccountLogDao;


    public int getRecordCount(Map<String, Object> map) {
        return fundActualAccountLogDao.findAllCount(map);
    }

    @Override
    public Map<String, Object> actualAccountLogRecordQuery(Map<String, Object> map) {
        List tradeList = fundActualAccountLogDao.findByPage(map);
        map.put("actualAccountLogList", tradeList);
        return map;
    }


}
