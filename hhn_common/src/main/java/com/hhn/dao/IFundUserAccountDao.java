package com.hhn.dao;

import com.hhn.pojo.FundUserAccount;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundUserAccountDao extends BaseDao<FundUserAccount>{

    public int existUserAccount(int userId);

    public int save(FundUserAccount fundUserAccount);

    public FundUserAccount query(int id);

    public int delete(int id);

    public int update(FundUserAccount fundUserAccount);

    public FundUserAccount queryUserAccount(int userId);

    public void updateAccountMoney(Map<String, Object> map);

    public Map<String, Object> queryPhone(Integer userId);
}
