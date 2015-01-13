package com.hhn.dao;

import com.hhn.pojo.AccountUserThird;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IAccountUserThirdDao {
    public int save(AccountUserThird accountUserThird);

    public AccountUserThird query(int id);

    public int delete(int id);

    public int update(AccountUserThird accountUserThird);

}
