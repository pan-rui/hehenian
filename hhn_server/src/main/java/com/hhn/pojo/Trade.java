package com.hhn.pojo;

import com.hhn.dao.BaseDao;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lenovo on 2014/12/11.
 */
public class Trade<T extends BaseDao> implements Serializable {
    private T dao;
    private Integer id;
    private BigDecimal amount;
    public Trade(){}

    public Trade(Integer id, BigDecimal amount) {
        this.id=id;
        this.amount=amount;
    }

    public Trade(T dao, Integer id, BigDecimal amount) {
        this.dao = dao;
        this.id = id;
        this.amount = amount;
    }

    public BaseDao getDao() {
        return dao;
    }

    public void setDao(T dao) {
        this.dao = dao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
