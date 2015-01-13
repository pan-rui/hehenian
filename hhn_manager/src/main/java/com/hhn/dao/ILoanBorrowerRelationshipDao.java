package com.hhn.dao;

import com.hhn.pojo.LoanBorrowerRelationship;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanBorrowerRelationshipDao {
    public int save(LoanBorrowerRelationship loanBorrowerRelationship);

    public LoanBorrowerRelationship query(int id);

    public int delete(int id);

    public int update(LoanBorrowerRelationship loanBorrowerRelationship);

}
