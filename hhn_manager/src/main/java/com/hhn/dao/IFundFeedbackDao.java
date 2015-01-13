package com.hhn.dao;

import com.hhn.pojo.FundFeedback;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundFeedbackDao {
    public int save(FundFeedback fundFeedback);

    public FundFeedback query(int id);

    public int delete(int id);

    public int update(FundFeedback fundFeedbackt);

}
