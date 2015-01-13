package com.hhn.dao;

import com.hhn.pojo.SysRole;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ISysRoleDao {
    public int save(SysRole sysRole);

    public SysRole query(int id);

    public int delete(int id);

    public int update(SysRole sysRole);

}
