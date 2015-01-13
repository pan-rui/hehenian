package com.hhn.dao;

import com.hhn.pojo.FundInvestmentDetail;
import com.hhn.pojo.FundTransfer;
import com.hhn.pojo.LoanDetail;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundTransferDao extends BaseDao<FundTransfer> {
    public List<FundTransfer> querys();

    public FundTransfer queryUnique(Integer transfer_id);

}
