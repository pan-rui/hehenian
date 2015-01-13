package com.hhn.dao;

import com.hhn.pojo.LoanCompany;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanCompanyDao {
    public int save(LoanCompany loanCompany);

    public LoanCompany query(int id);

    public int delete(int id);

    public int update(LoanCompany loanCompany);

}
