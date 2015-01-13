package com.hhn.dao;

import com.hhn.pojo.SysModule;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ISysModuleDao {
    public int save(SysModule sysModule);

    public SysModule query(int id);

    public int delete(int id);

    public int update(SysModule sysModule);

}
