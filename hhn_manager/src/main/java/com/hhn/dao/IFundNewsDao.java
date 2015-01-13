package com.hhn.dao;

import com.hhn.pojo.FundNews;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundNewsDao {
    public int save(FundNews fundNews);

    public FundNews query(int id);

    public int delete(int id);

    public int update(FundNews fundNews);

}
