package com.hhn.dao;

import com.hhn.pojo.AccountRecommendLog;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IAccountRecommendLogDao extends BaseDao<AccountRecommendLog> {
    public int save(AccountRecommendLog accountRecommendLog);

    public AccountRecommendLog query(int id);

    public int delete(int id);

//    public int update(FundProduct fundProduct);

}
