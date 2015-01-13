package com.hhn.dao;

import com.hhn.pojo.SysSmrzImage;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ISysSmrzImageDao {
    public int save(SysSmrzImage sysSmrzImage);

    public SysSmrzImage query(int id);

    public int delete(int id);

    public int update(SysSmrzImage sysSmrzImage);

}
