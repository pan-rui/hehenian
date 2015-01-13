package com.hhn.pojo;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hynpublic on 2015/1/9.
 */
@Alias("fund_payment")
public class FundPayment implements Serializable {
    private Integer fund_payment_id;
    private Integer user_id;
    private Integer invest_detail_id;
    private BigDecimal pre_payment_money;
    private BigDecimal payment_money;
    private BigDecimal capital;
    private BigDecimal interest;
    private Date pre_payment_time;
    private Date payment_time;
    private Date ctime;
    private Date utime;
    private Integer payment_status;
    private String comment;
    private BigDecimal fee_amount;
    private Integer periods;

    public FundPayment() {
    }

    public String getComment() {
        return comment;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public FundPayment(Integer fund_payment_id) {
        this.fund_payment_id = fund_payment_id;
    }

    public Integer getFund_payment_id() {
        return fund_payment_id;
    }

    public void setFund_payment_id(Integer fund_payment_id) {
        this.fund_payment_id = fund_payment_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getInvest_detail_id() {
        return invest_detail_id;
    }

    public void setInvest_detail_id(Integer invest_detail_id) {
        this.invest_detail_id = invest_detail_id;
    }

    public BigDecimal getPre_payment_money() {
        return pre_payment_money;
    }

    public void setPre_payment_money(BigDecimal pre_payment_money) {
        this.pre_payment_money = pre_payment_money;
    }

    public BigDecimal getPayment_money() {
        return payment_money;
    }

    public void setPayment_money(BigDecimal payment_money) {
        this.payment_money = payment_money;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public void setCapital(BigDecimal capital) {
        this.capital = capital;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public Date getPre_payment_time() {
        return pre_payment_time;
    }

    public void setPre_payment_time(Date pre_payment_time) {
        this.pre_payment_time = pre_payment_time;
    }

    public Date getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(Date payment_time) {
        this.payment_time = payment_time;
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

    public Integer getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(Integer payment_status) {
        this.payment_status = payment_status;
    }

    public BigDecimal getFee_amount() {
        return fee_amount;
    }

    public void setFee_amount(BigDecimal fee_amount) {
        this.fee_amount = fee_amount;
    }
}
