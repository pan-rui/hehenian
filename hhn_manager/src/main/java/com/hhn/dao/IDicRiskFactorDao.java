package com.hhn.dao;

import com.hhn.pojo.DicRiskFactor;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IDicRiskFactorDao {
    public int save(DicRiskFactor dicRiskFactor);

    public DicRiskFactor query(int id);

    public int delete(int id);

    public int update(DicRiskFactor dicRiskFactor);

}
