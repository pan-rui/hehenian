package com.hhn.service.impl;

import com.hhn.dao.IFundBankCardDao;
import com.hhn.dao.IFundProductDao;
import com.hhn.dao.IFundTradeDao;
import com.hhn.hessian.withdraw.IWithdrawService;
import com.hhn.pojo.FundTrade;
import com.hhn.service.IRepalyFinancialService;
import com.hhn.util.BaseReturn;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/17.
 */
@Service
public class RepalyFinancialServiceImpl implements IRepalyFinancialService {
    @Autowired
    private IFundProductDao fundProductDao;
    @Autowired
    private IFundBankCardDao fundBankCardDao;
    @Autowired
    private IWithdrawService withdrawService;
    private Logger logger = Logger.getLogger(this.getClass());
    /**
     * 查询提现申请列表
     * @param map
     * @return
     */
    public List<HashMap> getRepalyList(Map<String, Object> map){
        return fundProductDao.getWithdrawList(map);
    }

    /**
     * 提现查询卡信息
     * @param product_id
     * @return
     */
    public HashMap getUserBankDetail(Integer product_id){
        return fundBankCardDao.getUserBankDetail(product_id);
    }

    /**
     * 提交处理
     * 调服务接口提现
     * @param map
     * @return
     */
    public BaseReturn widthDrawTrade(Map<String, Object> map){
        try {
            return withdrawService.withdraw(map);
        }catch (Exception e){
            logger.error("error", e);
            return new BaseReturn(1, "提现调接口异常:"+e.getMessage());
        }
    }

}
