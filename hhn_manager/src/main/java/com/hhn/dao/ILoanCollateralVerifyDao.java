package com.hhn.dao;

import com.hhn.pojo.LoanCollateralVerify;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanCollateralVerifyDao {
    public int save(LoanCollateralVerify loanCollateralVerify);

    public LoanCollateralVerify query(int id);

    public int delete(int id);

    public int update(LoanCollateralVerify loanCollateralVerify);

}
