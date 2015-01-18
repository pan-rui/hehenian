package com.hhn.dao;

import com.hhn.pojo.BankCode;
import com.hhn.pojo.FundAccountLog;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IBankCodeDao extends BaseDao<BankCode> {

    String getRegxByCode(String bankCode);


}
