package com.hhn.dao;

import com.hhn.pojo.FundReceipt;
import com.hhn.pojo.FundTrade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundReceiptDao extends BaseDao<FundReceipt>{

public List<FundTrade> queryByDate(@Param("noww")Date noww,@Param("trade_type")String trade_type);

}
