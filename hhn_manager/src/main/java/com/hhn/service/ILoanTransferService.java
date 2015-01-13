package com.hhn.service;

import com.hhn.pojo.FundTradeDetail;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/10.
 */
public interface ILoanTransferService {
    /**
     * 债权转让分页查询
     * @param paraMap
     * @return
     */
    public Map<String, Object> getLoanTransferList(Map<String, Object> paraMap);

    /**
     * 根据交易ID查询债权转让明细
     * @param trade_id
     * @return
     */
    public List<FundTradeDetail> getTradeDetailList(int trade_id);

}
