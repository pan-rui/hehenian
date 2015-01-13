package com.hhn.dao;

import com.hhn.pojo.FundPayment;
import com.hhn.pojo.FundTransfer;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundPaymentDao extends BaseDao<FundPayment> {

    public FundPayment queryPayment(Integer repayment_id);

    public BigDecimal queryCapital(Integer user_id);

    public BigDecimal queryInterest(Integer user_id);

    public BigDecimal queryInterested(Integer user_id);

}
