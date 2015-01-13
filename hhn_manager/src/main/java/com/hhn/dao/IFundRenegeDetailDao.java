package com.hhn.dao;

import com.hhn.pojo.FundRenegeDetail;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundRenegeDetailDao {
    public int save(FundRenegeDetail fundRenegeDetail);

    public FundRenegeDetail query(int id);

    public int delete(int id);

    public int update(FundRenegeDetail fundRenegeDetail);

}
