package com.hhn.service;

import java.util.Map;

/**
 * Created by lenovo on 2014/12/9.
 */
public interface IInvestRecordService {
    /**
     * 分页查询用户投资记录
     * @param map
     * @return
     */
    public Map<String, Object> investRecordList(Map<String, Object> map);

}
