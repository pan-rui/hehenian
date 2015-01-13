package com.hhn.dao;

import com.hhn.pojo.ProductRate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IProductRateDao extends BaseDao<ProductRate> {

    public List<ProductRate> getProductRateList();

    public BigDecimal getProductRate(Map<String,Object> map);
}
