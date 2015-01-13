package com.hhn.service.impl;

import com.hhn.dao.IFundPaymentDao;
import com.hhn.dao.IFundTradeDao;
import com.hhn.dao.IFundUserAccountDao;
import com.hhn.pojo.FundUserAccount;
import com.hhn.util.Base;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hynpublic on 2014/12/26.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class QueryServiceImpl extends BaseService<FundUserAccount> {

    @Resource
    private IFundUserAccountDao fundUserAccountDao;
    @Resource
    private IFundTradeDao fundTradeDao;
    @Resource
    private IFundPaymentDao fundPaymentDao;
    public BaseReturn queryUserBalance(Integer userId) {
        FundUserAccount userAccount = fundUserAccountDao.queryUserAccount(userId);
        return new BaseReturn(0, userAccount==null?new FundUserAccount():userAccount);
    }

    public BaseReturn queryPrincipal(Integer userId) {
        return new BaseReturn(0,fundPaymentDao.queryCapital(userId));
    }

    public BaseReturn queryFreeze(Integer userId) {
        FundUserAccount fundUserAccount = fundUserAccountDao.queryUserAccount(userId);
        if(fundUserAccount==null)
            return new BaseReturn(1, 0);
        else
        return new BaseReturn(0,fundUserAccount.getFreeze_amount());
    }

    public BaseReturn queryInterested(Integer userId) {
        return new BaseReturn(0,fundPaymentDao.queryInterested(userId));
    }

    public BaseReturn queryInterest(Integer userId) {
        return new BaseReturn(0,fundPaymentDao.queryInterest(userId));
    }

    public BaseReturn queryOtherInterest(Integer userId) {
        return new BaseReturn(0, 0);
    }

    public BaseReturn queryPhone(Integer userId) {
       return new BaseReturn(0,fundUserAccountDao.queryPhone(userId));
    }

    public BaseReturn queryPay() {
        BigDecimal bigDecimal=fundTradeDao.queryPay(new Date(), IFundTradeDao.TradeType.INVESTMENT.name());
        return new BaseReturn(bigDecimal==null?1:0,bigDecimal);
    }
}
