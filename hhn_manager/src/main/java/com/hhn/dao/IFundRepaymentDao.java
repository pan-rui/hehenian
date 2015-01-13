package com.hhn.dao;

import com.hhn.pojo.FundRepayment;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundRepaymentDao {
    public int save(FundRepayment fundRepayment);

    public FundRepayment query(int id);

    public int delete(int id);

    public int update(FundRepayment fundRepayment);

    public List<HashMap> getReymentDetailList(Integer product_id);

}
