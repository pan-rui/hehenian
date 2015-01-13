package com.hhn.dao;

import com.hhn.pojo.LoanVerify;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanVerifyDao {
    public int save(LoanVerify loanVerify);

    public LoanVerify query(int id);

    public int delete(int id);

    public int update(LoanVerify loanVerify);

}
