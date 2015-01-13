package com.hhn.dao;

import com.hhn.pojo.SysRoleModule;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ISysRoleModuleDao {
    public int save(SysRoleModule sysRoleModule);

    public SysRoleModule query(int id);

    public int delete(int id);

    public int update(SysRoleModule sysRoleModule);

}
