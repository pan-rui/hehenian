package com.hhn.pojo;

// Generated 2014-12-2 15:40:14 by Hibernate Tools 3.4.0.CR1

import com.hhn.dao.ILoanDetailDao;
import com.hhn.util.annotaion.KeyInTable;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * FundProduct generated by hbm2java
 */
@Alias("fund_product")
public class FundProduct implements java.io.Serializable,Cloneable{
	private Integer product_id;
	@KeyInTable(ILoanDetailDao.class)
	private Integer loan_id;
//	@KeyInTable(IAccountUserDao.class)
	private Integer user_id;
	@NotNull
	private String product_name;
	private BigDecimal invest_amount;
	private volatile BigDecimal invested_amount;
	private String product_usage;
	private Short loan_period;
	private BigDecimal annual_rate;
	private Short repay_period;
	private Byte repay_type=null;
	private Short tender_day;
	private String remark;
	private Byte product_status;
	private Byte loan_type;
	private Byte security_type;
	private String bin_level;
	private Date create_time;
	private Date update_time;
	private Date publish_time;
	private BigDecimal loan_rate;
	private AccountUserDo accountUserDo;

	public FundProduct() {
	}

	public FundProduct(Integer product_id) {
		this.product_id=product_id;
	}
	public FundProduct(Integer loan_id,Integer user_id) {
		this.loan_id=loan_id;
		this.user_id=user_id;
	}

	public Integer getProduct_id() {
		return product_id;
	}

	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}

	public Integer getLoan_id() {
		return loan_id;
	}

	public void setLoan_id(Integer loan_id) {
		this.loan_id = loan_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public BigDecimal getInvest_amount() {
		return invest_amount;
	}

	public void setInvest_amount(BigDecimal invest_amount) {
		this.invest_amount = invest_amount;
	}

	public BigDecimal getInvested_amount() {
		return invested_amount;
	}

	public void setInvested_amount(BigDecimal invested_amount) {
		this.invested_amount = invested_amount;
	}

	public String getProduct_usage() {
		return product_usage;
	}

	public void setProduct_usage(String product_usage) {
		this.product_usage = product_usage;
	}

	public Short getLoan_period() {
		return loan_period;
	}

	public void setLoan_period(Short loan_period) {
		this.loan_period = loan_period;
	}

	public BigDecimal getAnnual_rate() {
		return annual_rate;
	}

	public void setAnnual_rate(BigDecimal annual_rate) {
		this.annual_rate = annual_rate;
	}

	public Short getRepay_period() {
		return repay_period;
	}

	public void setRepay_period(Short repay_period) {
		this.repay_period = repay_period;
	}

	public Byte getRepay_type() {
		return repay_type;
	}

	public void setRepay_type(Byte repay_type) {
		this.repay_type = repay_type;
	}

	public Short getTender_day() {
		return tender_day;
	}

	public void setTender_day(Short tender_day) {
		this.tender_day = tender_day;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Byte getProduct_status() {
		return product_status;
	}

	public void setProduct_status(Byte product_status) {
		this.product_status = product_status;
	}

	public Byte getLoan_type() {
		return loan_type;
	}

	public void setLoan_type(Byte loan_type) {
		this.loan_type = loan_type;
	}

	public Byte getSecurity_type() {
		return security_type;
	}

	public void setSecurity_type(Byte security_type) {
		this.security_type = security_type;
	}

	public String getBin_level() {
		return bin_level;
	}

	public void setBin_level(String bin_level) {
		this.bin_level = bin_level;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public Date getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(Date publish_time) {
		this.publish_time = publish_time;
	}

	public BigDecimal getLoan_rate() {
		return loan_rate;
	}

	public void setLoan_rate(BigDecimal loan_rate) {
		this.loan_rate = loan_rate;
	}

	public AccountUserDo getAccountUserDo() {
		return accountUserDo;
	}

	public void setAccountUserDo(AccountUserDo accountUserDo) {
		this.accountUserDo = accountUserDo;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}

