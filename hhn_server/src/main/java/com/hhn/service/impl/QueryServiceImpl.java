package com.hhn.service.impl;

import com.hhn.dao.IFundBankCardDao;
import com.hhn.dao.IFundTradeDao;
import com.hhn.dao.IFundUserAccountDao;
import com.hhn.pojo.FundBankCard;
import com.hhn.pojo.FundUserAccount;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import com.hhn.util.DqlcConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;

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
    private IFundBankCardDao fundBankCardDao;
    @Resource
    private DqlcConfig dqlcConfig;

    public BaseReturn queryUserBalance(Integer userId) {
        FundUserAccount userAccount = fundUserAccountDao.queryUserAccount(userId);
        return new BaseReturn(0, userAccount == null ? new FundUserAccount() : userAccount);
    }

    public BaseReturn queryTotalInvest(Integer userId) {
        FundUserAccount userAccount = fundUserAccountDao.queryUserAccount(userId);
        return new BaseReturn(0,userAccount == null ? 0 : userAccount.getTotal_invest_amount());
    }

    public BaseReturn queryRound(Integer userId) {
        return new BaseReturn(0, fundTradeDao.queryRound(userId));
    }

    public BaseReturn queryInterested(Integer userId) {
        return new BaseReturn(0, fundTradeDao.queryInterested(userId));
    }

    public BaseReturn queryInterest(Integer userId) {
        return new BaseReturn(0, fundTradeDao.queryInterest(userId));
    }

    public BaseReturn queryOtherInterest(Integer userId) {
        return new BaseReturn(0, 0);
    }

    public BaseReturn queryPhone(Integer userId) {
        return new BaseReturn(0, fundUserAccountDao.queryPhone(userId));
    }

    public BaseReturn queryPay() {
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(dqlcConfig.LOAN_AHEAD));
        BigDecimal bigDecimal = fundTradeDao.queryPay(calendar.getTime());
        return new BaseReturn(bigDecimal == null ? 1 : 0, bigDecimal);
    }

    public BaseReturn queryBankCard(Integer userId) {
        FundBankCard fundBankCard = fundBankCardDao.getBankCard(userId);
        return new BaseReturn(0, fundBankCard == null ? new FundBankCard() : fundBankCard, "");
    }

}
