package com.hhn.pojo;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lenovo on 2014/12/15.
 */
@Alias("product_rate")
public class ProductRate implements Serializable {
    private Integer product_rate_id;
    private Integer period;
    private BigDecimal rate;
    private Integer coefficient;
    private String money_scope;
    private String comment;
    private Date ctime;
    private Date utime;

    public ProductRate(){}

    public ProductRate(Integer product_rate_id, Integer period, BigDecimal rate, Integer coefficient, String money_scope, String comment, Date ctime, Date utime) {
        this.product_rate_id = product_rate_id;
        this.period = period;
        this.rate = rate;
        this.coefficient = coefficient;
        this.money_scope = money_scope;
        this.comment = comment;
        this.ctime = ctime;
        this.utime = utime;
    }

    public Integer getProduct_rate_id() {
        return product_rate_id;
    }

    public String getMoney_scope() {
        return money_scope;
    }

    public void setMoney_scope(String money_scope) {
        this.money_scope = money_scope;
    }

    public ProductRate(Integer product_rate_id){
        this.product_rate_id=product_rate_id;
    }

    public void setProduct_rate_id(Integer product_rate_id) {
        this.product_rate_id = product_rate_id;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Integer coefficient) {
        this.coefficient = coefficient;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }
}
