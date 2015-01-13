package com.hhn.dao;

import com.hhn.pojo.ManageLog;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IManageLogDao {
    public int save(ManageLog manageLog);

    public ManageLog query(int id);

    public int delete(int id);

//    public int update(LoanVerify loanVerify);

}
