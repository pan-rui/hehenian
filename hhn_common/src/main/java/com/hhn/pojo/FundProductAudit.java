package com.hhn.pojo;

import org.apache.ibatis.type.Alias;

import java.util.Date;

/**
 * Created by Administrator on 2015/1/6.
 */
@Alias("fund_product_audit")
public class FundProductAudit implements java.io.Serializable,Cloneable {
    private Integer product_audit_id;
    private Integer product_id;
    private Integer status;
    private String operator;
    private Date modify_time;

    public FundProductAudit() {
    }

    public FundProductAudit(Integer product_audit_id, Integer product_id, Integer status, String operator, Date modify_time) {
        this.product_audit_id = product_audit_id;
        this.product_id = product_id;
        this.status = status;
        this.operator = operator;
        this.modify_time = modify_time;
    }

    public Integer getProduct_audit_id() {
        return product_audit_id;
    }

    public void setProduct_audit_id(Integer product_audit_id) {
        this.product_audit_id = product_audit_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getModify_time() {
        return modify_time;
    }

    public void setModify_time(Date modify_time) {
        this.modify_time = modify_time;
    }

}