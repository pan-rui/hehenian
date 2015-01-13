package com.hhn.dao;

import com.hhn.pojo.SysSelectTreeData;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ISysSelectTreeDataDao {
    public int save(SysSelectTreeData sysSelectTreeData);

    public SysSelectTreeData query(int id);

    public int delete(int id);

    public int update(SysSelectTreeData sysSelectTreeData);

}
