package com.hhn.dao;

import com.hhn.pojo.AccountUserLog;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IAccountUserLogDao {
    public int save(AccountUserLog accountUserLog);

    public AccountUserLog query(int id);

    public int delete(int id);

//    public int update(AccountUserLog accountUserLog);

}
