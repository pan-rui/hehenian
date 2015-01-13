package com.hhn.dao;

import com.hhn.pojo.LoanPerson;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanPersonDao {
    public int save(LoanPerson loanPerson);

    public LoanPerson query(int id);

    public int delete(int id);

    public int update(LoanPerson loanPerson);

}
