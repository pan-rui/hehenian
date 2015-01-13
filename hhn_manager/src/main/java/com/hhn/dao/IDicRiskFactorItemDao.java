package com.hhn.dao;

import com.hhn.pojo.DicRiskFactorItem;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IDicRiskFactorItemDao {
    public int save(DicRiskFactorItem dicRiskFactorItem);

    public DicRiskFactorItem query(int id);

    public int delete(int id);

    public int update(DicRiskFactorItem dicRiskFactorItem);

}
