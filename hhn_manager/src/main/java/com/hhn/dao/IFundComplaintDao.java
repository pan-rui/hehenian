package com.hhn.dao;

import com.hhn.pojo.FundComplaint;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundComplaintDao {
    public int save(FundComplaint fundComplaint);

    public FundComplaint query(int id);

    public int delete(int id);

    public int update(FundComplaint fundComplaint);

}
