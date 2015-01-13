package com.hhn.dao;

import com.hhn.pojo.LoanCollateral;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanCollateralDao {
    public int save(LoanCollateral loanCollateral);

    public LoanCollateral query(int id);

    public int delete(int id);

    public int update(LoanCollateral loanCollateral);

}
