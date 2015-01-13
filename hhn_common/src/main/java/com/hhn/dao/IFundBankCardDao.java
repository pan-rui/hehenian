package com.hhn.dao;

import com.hhn.pojo.FundBankCard;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundBankCardDao extends BaseDao<FundBankCard> {
    public List<FundBankCard> queryByUserId(Integer userId);



}
