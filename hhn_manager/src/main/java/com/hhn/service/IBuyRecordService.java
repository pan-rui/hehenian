package com.hhn.service;

import java.util.Map;

/**
 * Created by lenovo on 2014/12/8.
 */
public interface IBuyRecordService {
    /**
     * 查询记录总数
     * @param map
     * @return
     */
    public int getRecordCount(Map<String, Object> map);

    /**
     * 查询记录集合
     * @param map
     * @return
     */
    public Map<String, Object> buyRecordQuery(Map<String, Object> map);

}
