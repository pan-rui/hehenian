package com.hhn.dao;

import com.hhn.pojo.FundSubbranchBank;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundSubbranchBankDao {
    public int save(FundSubbranchBank fundSubbranchBank);

    public FundSubbranchBank query(int id);

    public int delete(int id);

    public int update(FundSubbranchBank fundSubbranchBank);

}
