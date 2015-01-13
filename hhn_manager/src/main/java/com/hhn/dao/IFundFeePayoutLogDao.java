package com.hhn.dao;

import com.hhn.pojo.FundFeePayoutLog;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundFeePayoutLogDao {
    public int save(FundFeePayoutLog fundFeePayoutLog);

    public FundFeePayoutLog query(int id);

    public int delete(int id);

//    public int update(FundFeePayoutLog fundFeePayoutLog);

}
