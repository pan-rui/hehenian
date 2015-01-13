package com.hhn.dao;

import com.hhn.pojo.LoanCollateralReal;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanCollateralRealDao {
    public int save(LoanCollateralReal loanCollateralReal);

    public LoanCollateralReal query(int id);

    public int delete(int id);

    public int update(LoanCollateralReal loanCollateralReal);

}
