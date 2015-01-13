package com.hhn.hessian.invest.impl;

import com.hhn.dao.IFundProductDao;
import com.hhn.dao.IFundTradeDao;
import com.hhn.dao.IProductRateDao;
import com.hhn.hessian.invest.IInvestProductService;
import com.hhn.util.BaseReturn;
import com.hhn.util.annotaion.ServiceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

/**
 * Created by lenovo on 2014/12/16.
 */
@Controller
@Transactional(rollbackFor = Exception.class)
public class InvestProductService implements IInvestProductService {

    @Autowired
    private IProductRateDao productRateDao;
    @Autowired
    private IFundTradeDao fundTradeDao;
    @Autowired
    private IFundProductDao fundProductDao;

    /**
     * 查询定投列表
     *
     * @return
     */
    @Override
//    @ServiceLog(description = "查询定投利率列表,表名:<product_rate>")
    public BaseReturn getProductRateList() {
        try {
            return new BaseReturn(0, productRateDao.getProductRateList());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_system_error, null);
        }
    }

    /**
     * 查询利率
     *
     * @param map
     * @return
     */
    @Override
//    @ServiceLog(description = "查询指定产品的利率,表名:<product_rate>")
    public BaseReturn getProductRate(HashMap<String, Object> map) {
        try {
            return new BaseReturn(0, productRateDao.getProductRate(map));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_system_error, null);
        }
    }

    /**
     * 查询交易明细总数
     *
     * @param map
     * @return
     */
    @Override
//    @ServiceLog(description = "查询交易明细总数,表名:<fund_trade>")
    public BaseReturn getWebTradeCount(HashMap<String, Object> map) {
        try {
            return new BaseReturn(0, fundTradeDao.getWebCount(map));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_system_error, null);
        }
    }

    /**
     * 查询交易明细
     *
     * @param map
     * @return
     */
    @Override
    @ServiceLog(description = "查询交易明细,表名:<fund_trade>")
    public BaseReturn getWebTradeList(HashMap<String, Object> map) {
        try {
            return new BaseReturn(0, fundTradeDao.getWebPageList(map));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_system_error, null);
        }
    }

    /**
     * 查询标的总数
     *
     * @param map
     * @return
     */
    @Override
    public BaseReturn getWebProductCount(HashMap<String, Object> map) {
        try {
            return new BaseReturn(0, fundProductDao.getWebProductCount(map));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_system_error, null);
        }
    }

    /**
     * 查询标的明细
     *
     * @param map
     * @return
     */
    @Override
    @ServiceLog(description = "查询标的明细,表名:<fund_investment_detail>")
    public BaseReturn getWebProductList(HashMap<String, Object> map) {
        try {
            return new BaseReturn(0, fundProductDao.getWebProductList(map));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_system_error, null);
        }
    }

    /**
     * 查询投资记录数
     * @param map
     * @return
     */
    public BaseReturn getWebInvestmentCount(HashMap<String, Object> map){
        try {
            return new BaseReturn(0, fundProductDao.getWebInvestmentCount(map));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_system_error, null);
        }
    }
    /**
     * 查询投资记录列表
     * @param map
     * @return
     */
    public BaseReturn getWebInvestmentList(HashMap<String, Object> map){
        try {
            return new BaseReturn(0, fundProductDao.getWebInvestmentList(map));
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_system_error, null);
        }
    }

}
