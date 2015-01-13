package com.hhn.dao;

import com.hhn.pojo.FundInvestmentDetail;
import com.hhn.pojo.FundTrade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundInvestmentDetailDao extends BaseDao<FundInvestmentDetail> {

    public List<FundInvestmentDetail> getInvestDetailList(Map<String, Object> map);

    public List<FundInvestmentDetail> queryByNow(@Param("noww")Date noww);

    public FundTrade queryForFundTrade(Integer investId);

    public List<FundTrade> queryForFundTrades(Map<String, Object> params);

    public FundInvestmentDetail queryForProduct(Integer invest_id);
}
