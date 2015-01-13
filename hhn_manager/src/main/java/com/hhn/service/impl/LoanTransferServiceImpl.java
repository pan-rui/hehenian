package com.hhn.service.impl;

import com.hhn.dao.IFundTradeDao;
import com.hhn.dao.IFundTradeDetailDao;
import com.hhn.pojo.FundTrade;
import com.hhn.pojo.FundTradeDetail;
import com.hhn.service.ILoanTransferService;
import com.hhn.util.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/10.
 */
@Service
public class LoanTransferServiceImpl extends BaseService<FundTrade> implements ILoanTransferService {
    @Autowired
    private IFundTradeDao fundTradeDao;
    @Autowired
    private IFundTradeDetailDao fundTradeDetailDao;
    /**
     * 债权转让分页查询
     * @param paraMap
     * @return
     */
    public Map<String, Object> getLoanTransferList(Map<String, Object> paraMap){
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//       List<FundTrade> tradeList = null;
//        int count = fundTradeDao.findAllCount(paraMap);
//        if (count>0){
        List<FundTrade> tradeList = fundTradeDao.findByPage(paraMap);
//            for(FundTrade trade:tradeList){
//                int trade_id = trade.getTrade_id();
//                List<FundTradeDetail> tradeDetailList = fundTradeDetailDao.getTradeDetailList(trade_id);
//                trade.setFundTradeDetailList(tradeDetailList);
//            }
//        }
//        resultMap.put("tradeCount",count);
        paraMap.put("tradeList", tradeList);
        return paraMap;
    }

    /**
     * 根据交易ID查询债权转让明细
     * @param trade_id
     * @return
     */
    public List<FundTradeDetail> getTradeDetailList(int trade_id){
       //List<FundTradeDetail> detailList = fundTradeDetailDao.getTradeDetailList(trade_id);
        return null;
    }

}
