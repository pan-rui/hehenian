package com.hhn.dao;

import com.hhn.pojo.LoanCorporation;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanCorporationDao {
    public int save(LoanCorporation loanCorporation);

    public LoanCorporation query(int id);

    public int delete(int id);

    public int update(LoanCorporation loanCorporation);

}
