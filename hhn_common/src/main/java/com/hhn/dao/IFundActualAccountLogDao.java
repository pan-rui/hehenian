package com.hhn.dao;

import com.hhn.pojo.FundActualAccountLog;
import com.hhn.pojo.FundTrade;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundActualAccountLogDao extends BaseDao<FundActualAccountLog> {
    public enum TransferStatus {
        NOTRANSFER, TRANSFERRED
//        '转账状态，NOTRANSFER-未转账；TRANSFERRED-已转账'
    }

//    public int update(FundActualAccountLog fundActualAccountLog);

    /**
     * 充值记录总数
     *
     * @param map
     * @return
     */
    public int findAllCount(Map map);

    /**
     * 充值记录列表
     *
     * @param map
     * @return
     */
    public List<FundTrade> findByPage(Map map);

}
