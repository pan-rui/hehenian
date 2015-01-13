package com.hhn.dao;

import com.hhn.pojo.SysSelectListData;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ISysSelectListDataDao {
    public int save(SysSelectListData sysSelectListData);

    public SysSelectListData query(int id);

    public int delete(int id);

    public int update(SysSelectListData sysSelectListData);

}
