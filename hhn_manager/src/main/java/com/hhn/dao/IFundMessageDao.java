package com.hhn.dao;

import com.hhn.pojo.FundProduct;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundMessageDao {
    public int save(FundProduct fundProduct);

    public FundProduct query(int id);

    public int delete(int id);

    public int update(FundProduct fundProduct);

}
