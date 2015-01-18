package com.hhn.service.impl;

import com.hhn.dao.IFundInvestmentDetailDao;
import com.hhn.dao.IFundProductDao;
import com.hhn.dao.IFundTradeDao;
import com.hhn.pojo.FundInvestmentDetail;
import com.hhn.pojo.FundProduct;
import com.hhn.pojo.FundTrade;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import com.hhn.util.DqlcConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**
 * Created by hynpublic on 2015/1/13.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ModifyProductServiceImpl extends BaseService<FundProduct> {
    @Resource
    private IFundProductDao fundProductDao;
    @Resource
    private IFundInvestmentDetailDao fundInvestmentDetailDao;
    @Resource
    private IFundTradeDao fundTradeDao;
@Resource
private DqlcConfig processInfo;
    public BaseReturn modifyForStream(Integer product_id) {
        Calendar calendar=Calendar.getInstance();
        FundProduct fundProduct = fundProductDao.query(product_id);
        FundProduct fundProduct1 = new FundProduct(product_id);
        fundProduct1.setProduct_status(Byte.valueOf("4"));//已流标
        fundProduct1.setUpdate_time(calendar.getTime());
        fundProductDao.update(fundProduct1);
        List<FundInvestmentDetail> fundInvestmentDetails = fundInvestmentDetailDao.queryByProperties("product_id", product_id);
        for (FundInvestmentDetail fundInvestmentDetail : fundInvestmentDetails) {
            FundInvestmentDetail fundInvestmentDetail1 = new FundInvestmentDetail(fundInvestmentDetail.getInvestment_detail_id());
            fundInvestmentDetail1.setStatus(4);//已流标
            fundInvestmentDetail1.setUpdate_time(calendar.getTime());
            fundInvestmentDetailDao.update(fundInvestmentDetail1);
            FundTrade fundTrade = fundInvestmentDetailDao.queryForFundTrade(fundInvestmentDetail1.getInvestment_detail_id());
            FundTrade fundTrade1 = new FundTrade(fundTrade.getTrade_id());
            fundTrade1.setTrade_balance(fundTrade.getTrade_balance().add(fundInvestmentDetail.getTrade_amount()));
            fundTrade1.setUpdate_time(calendar.getTime());
            fundTradeDao.update(fundTrade1);
        }
        logger.info("标的状态被更改为4（流标），标的ID:" + product_id);
        return new BaseReturn(0, fundProduct,processInfo.OPERATE_SUCCESS);
    }
}
