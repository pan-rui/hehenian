package com.hhn.pojo;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hynpublic on 2015/1/8.
 */
@Alias("fund_transfer")
public class FundTransfer implements Serializable {
    private Integer transfer_id;
    private Integer transfer_user_id;
    private Integer buy_user_id;
    private Integer invest_detail_Id;
    private BigDecimal capital_balance;
    private Integer periods_surplus;
    private BigDecimal transfer_amount;
    private BigDecimal share_surplus;
    private BigDecimal share_transfer;
    private Date transfer_time;
    private Integer transfer_status;
    private Date ctime;
    private Date utime;
    private FundInvestmentDetail fundInvestmentDetail;
    public FundTransfer(){}
    public FundTransfer(Integer transfer_id) {
        this.transfer_id = transfer_id;
    }

    public Integer getTransfer_id() {
        return transfer_id;
    }

    public FundInvestmentDetail getFundInvestmentDetail() {
        return fundInvestmentDetail;
    }

    public void setFundInvestmentDetail(FundInvestmentDetail fundInvestmentDetail) {
        this.fundInvestmentDetail = fundInvestmentDetail;
    }

    public void setTransfer_id(Integer transfer_id) {
        this.transfer_id = transfer_id;
    }

    public Integer getTransfer_user_id() {
        return transfer_user_id;
    }

    public void setTransfer_user_id(Integer transfer_user_id) {
        this.transfer_user_id = transfer_user_id;
    }

    public Integer getBuy_user_id() {
        return buy_user_id;
    }

    public void setBuy_user_id(Integer buy_user_id) {
        this.buy_user_id = buy_user_id;
    }

    public Integer getInvest_detail_Id() {
        return invest_detail_Id;
    }

    public void setInvest_detail_Id(Integer invest_detail_Id) {
        this.invest_detail_Id = invest_detail_Id;
    }

    public BigDecimal getCapital_balance() {
        return capital_balance;
    }

    public void setCapital_balance(BigDecimal capital_balance) {
        this.capital_balance = capital_balance;
    }

    public Integer getPeriods_surplus() {
        return periods_surplus;
    }

    public void setPeriods_surplus(Integer periods_surplus) {
        this.periods_surplus = periods_surplus;
    }

    public BigDecimal getTransfer_amount() {
        return transfer_amount;
    }

    public void setTransfer_amount(BigDecimal transfer_amount) {
        this.transfer_amount = transfer_amount;
    }

    public BigDecimal getShare_surplus() {
        return share_surplus;
    }

    public void setShare_surplus(BigDecimal share_surplus) {
        this.share_surplus = share_surplus;
    }

    public BigDecimal getShare_transfer() {
        return share_transfer;
    }

    public void setShare_transfer(BigDecimal share_transfer) {
        this.share_transfer = share_transfer;
    }

    public Date getTransfer_time() {
        return transfer_time;
    }

    public void setTransfer_time(Date transfer_time) {
        this.transfer_time = transfer_time;
    }

    public Integer getTransfer_status() {
        return transfer_status;
    }

    public void setTransfer_status(Integer transfer_status) {
        this.transfer_status = transfer_status;
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
