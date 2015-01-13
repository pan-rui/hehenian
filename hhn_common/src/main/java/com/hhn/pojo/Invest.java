package com.hhn.pojo;

import com.hhn.dao.IFundAccountLogDao;
import com.hhn.dao.IFundTradeDao;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by lenovo on 2014/12/12.
 */
public class Invest implements Serializable {
    @NotNull
    private Integer user_id;
    private Integer user_account_id;
     private Integer product_id;
    @NotNull
    private Integer month;
    @Min(1000)
    private BigDecimal money;
    private IFundTradeDao.TradeType tradeType;
    private IFundTradeDao.TradeStatus tradeStatus;
    private IFundTradeDao.TargetType targetType;
    private BigDecimal balance;
    private IFundAccountLogDao.LogType logType;
    public Invest(){}

    public Invest(Integer user_id, Integer user_account_id, Integer product_id, Integer month, BigDecimal money) {
        this.user_id = user_id;
        this.user_account_id = user_account_id;
        this.product_id = product_id;
        this.month = month;
        this.money = money;
    }

    public IFundTradeDao.TradeType getTradeType() {
        return tradeType;
    }

    public IFundTradeDao.TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(IFundTradeDao.TargetType targetType) {
        this.targetType = targetType;
    }

    public void setTradeType(IFundTradeDao.TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public IFundTradeDao.TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(IFundTradeDao.TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getUser_account_id() {
        return user_account_id;
    }

    public void setUser_account_id(Integer user_account_id) {
        this.user_account_id = user_account_id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public IFundAccountLogDao.LogType getLogType() {
        return logType;
    }

    public void setLogType(IFundAccountLogDao.LogType logType) {
        this.logType = logType;
    }
}
