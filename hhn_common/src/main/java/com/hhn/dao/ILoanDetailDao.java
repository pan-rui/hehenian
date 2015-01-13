package com.hhn.dao;

import com.hhn.pojo.FundInvestmentDetail;
import com.hhn.pojo.LoanDetail;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanDetailDao extends BaseDao<LoanDetail> {

    public int existLoan(LoanDetail loanDetail);
    public int getAllCount(Map<String, Object> paraMap);

    public List<LoanDetail> findByPage(Map<String, Object> paraMap);

    public List<FundInvestmentDetail> queryForFID(Integer loanDetailId);

}
