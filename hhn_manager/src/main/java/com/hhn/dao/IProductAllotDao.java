package com.hhn.dao;

import com.hhn.pojo.ProductAllot;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IProductAllotDao {
    public int save(ProductAllot productAllot);

    public ProductAllot query(int id);

    public int delete(int id);

    public int update(ProductAllot productAllot);

}
