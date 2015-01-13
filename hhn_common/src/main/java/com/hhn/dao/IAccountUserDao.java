package com.hhn.dao;

import com.hhn.pojo.AccountUser;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IAccountUserDao extends BaseDao<AccountUser>{

    public Integer existUser(AccountUser accountUser);

    public Integer getLastUserId();

}
