package com.hhn.dao;

import com.hhn.pojo.FundTradeDetail;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundTradeDetailDao {
    public int save(FundTradeDetail fundTradeDetail);

    public FundTradeDetail query(int id);

    public int delete(int id);

    public int update(FundTradeDetail fundTradeDetail);

}
