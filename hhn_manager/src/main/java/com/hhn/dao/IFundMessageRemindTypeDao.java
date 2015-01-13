package com.hhn.dao;

import com.hhn.pojo.FundMessageRemindType;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundMessageRemindTypeDao {
    public int save(FundMessageRemindType fundMessageRemindType);

    public FundMessageRemindType query(int id);

    public int delete(int id);

    public int update(FundMessageRemindType fundMessageRemindType);

}
