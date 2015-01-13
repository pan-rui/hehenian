package com.hhn.dao;

import com.hhn.pojo.FundMessageRecipient;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundMessageRecipientDao {
    public int save(FundMessageRecipient fundMessageRecipient);

    public FundMessageRecipient query(int id);

    public int delete(int id);

    public int update(FundMessageRecipient fundMessageRecipient);

}
