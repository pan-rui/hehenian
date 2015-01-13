package com.hhn.dao;

import com.hhn.pojo.FinancialVerify;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFinancialVerifyDao {
    public int save(FinancialVerify financialVerify);

    public FinancialVerify query(int id);

    public int delete(int id);

    public int update(FinancialVerify financialVerify);

}
