package com.hhn.dao;

import com.hhn.pojo.FundMessageRemind;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundMessageRemindDao {
    public int save(FundMessageRemind fundMessageRemind);

    public FundMessageRemind query(int id);

    public int delete(int id);

    public int update(FundMessageRemind fundMessageRemind);

}
