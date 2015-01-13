package com.hhn.dao;

import com.hhn.pojo.LoanAddress;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanAddressDao {
    public int save(LoanAddress loanAddress);

    public LoanAddress query(int id);

    public int delete(int id);

    public int update(LoanAddress loanAddress);

}
