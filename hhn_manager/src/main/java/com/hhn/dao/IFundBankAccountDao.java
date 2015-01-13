package com.hhn.dao;

import com.hhn.pojo.FundBankAccount;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundBankAccountDao {
    public int save(FundBankAccount fundBankAccount);

    public FundBankAccount query(int id);

    public int delete(int id);

    public int update(FundBankAccount fundBankAccount);

}
