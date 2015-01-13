package com.hhn.dao;

import com.hhn.pojo.LoanPersonWork;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanPersonWorkDao {
    public int save(LoanPersonWork loanPersonWork);

    public LoanPersonWork query(int id);

    public int delete(int id);

    public int update(LoanPersonWork loanPersonWork);

}
