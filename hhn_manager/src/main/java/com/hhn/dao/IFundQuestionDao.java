package com.hhn.dao;

import com.hhn.pojo.FundQuestion;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundQuestionDao {
    public int save(FundQuestion fundQuestion);

    public FundQuestion query(int id);

    public int delete(int id);

    public int update(FundQuestion fundQuestion);

}
