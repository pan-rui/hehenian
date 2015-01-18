package com.hhn.service.impl;

import com.hhn.dao.*;
import com.hhn.pojo.*;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import com.hhn.util.DqlcConfig;
import com.hhn.util.FundUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2014/12/10.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LoanDetailServiceImpl extends BaseService<LoanDetail> {
    @Resource
    private ILoanDetailDao loanDetailDao;
    @Resource
    private IFundUserAccountDao fundUserAccountDao;
    @Resource
    private DqlcConfig processInfo;
    @Resource
    private FundUtil fundUtil;
    @Resource
    private IFundProductDao fundProductDao;
    @Resource
    private IFundProductAuditDao fundProductAuditDao;

    public BaseReturn apply(LoanDetail loanDetail) {
        BaseReturn baseReturn = null;
        if ((baseReturn = validAndReturn(loanDetail)).getReturnCode() == 1)
            return baseReturn;
//        loanDetail.setUser_id(fundUserAccount.getUser_account_id()); //TODO:用户ID转换为资金账户ID
        loanDetail.setUser_id(loanDetail.getUser_id());
        if (StringUtils.isEmpty(loanDetail.getLoan_status())) loanDetail.setLoan_status(Byte.valueOf("0")); //未审核
        loanDetailDao.save(loanDetail);
        return new BaseReturn(0, loanDetail.getLoan_id());
    }

    //    @VerifyAfter(value = "借款审核")
    public BaseReturn verify(Integer loanDetailId) {
        String errorInfo = processInfo.DATA_INVALID;
        if (loanDetailId == null || loanDetailId <= 0) return new BaseReturn(1, errorInfo);
        LoanDetail loanDetail = getById(loanDetailDao, loanDetailId);
        if (loanDetail == null) return new BaseReturn(1, errorInfo);
        LoanDetail nLoanDetail = new LoanDetail(loanDetailId);
        nLoanDetail.setLoan_status(Byte.valueOf("1")); //已审核
        nLoanDetail.setUpdate_time(Calendar.getInstance().getTime());
        FundUserAccount fundUserAccount1 = fundUserAccountDao.queryUserAccount(loanDetail.getUser_id());
        if(fundUserAccount1==null) {
            //创建用户资金账户
            Date date = new Date();
            FundUserAccount fundUserAccount = new FundUserAccount();
            fundUserAccount.setUser_id(loanDetail.getUser_id());
            fundUserAccount.setBalance_amount(BigDecimal.ZERO);
            fundUserAccount.setTotal_recharge_amount(BigDecimal.ZERO);
            fundUserAccount.setCreate_time(date);
            fundUserAccount.setUpdate_time(date);
            fundUserAccountDao.save(fundUserAccount);
        }
        int size = loanDetailDao.update(nLoanDetail);
        if (size > 0) {
/*            LoanDetail loanDetail1 = new LoanDetail(loanDetailId);
            loanDetail1.setLoan_status(Byte.valueOf("1"));
            loanDetail1.setUpdate_time(calendar.getTime());
            loanDetailDao.update(loanDetail1);*/
            return new BaseReturn(0, loanDetail, processInfo.OPERATE_SUCCESS);
        } else return new BaseReturn(1, errorInfo);
    }

    /**
     * 放款审核
     * @param loanDetailId
     * @param operator
     * @return
     */
    public BaseReturn loanVerify(Integer loanDetailId,String operator) {
        LoanDetail loanDetail1 = loanDetailDao.query(loanDetailId);
        if(loanDetail1.getLoan_status().intValue()==4) {
            logger.info("标的已放款，请勿重复提交请求，标的ID:"+loanDetailId);
            return new BaseReturn(1, "该标的已放款，请勿重复提交。。。");
        }
        LoanDetail loanDetail = new LoanDetail(loanDetailId);
        Calendar calendar = Calendar.getInstance();
        loanDetail.setLoan_status(Byte.valueOf("4"));//已放款
        loanDetail.setUpdate_time(calendar.getTime());
        FundUserAccount fundUserAccount = fundUserAccountDao.queryUserAccount(loanDetail1.getUser_id());
        FundUserAccount fundUserAccount1 = new FundUserAccount(fundUserAccount.getUser_account_id());
        fundUserAccount1.setBalance_amount(fundUserAccount.getBalance_amount().add(loanDetail1.getLoan_amount()));
        fundUserAccount1.setLoan_amount(loanDetail1.getLoan_amount());
        fundUserAccount1.setTotal_loan_amount(fundUserAccount.getTotal_loan_amount().add(loanDetail1.getLoan_amount()));
        fundUserAccount1.setUpdate_time(calendar.getTime());
        fundUserAccountDao.update(fundUserAccount1);
        Invest invest = new Invest(loanDetail1.getUser_id(), fundUserAccount.getUser_account_id(), loanDetailId, Integer.parseInt(loanDetail1.getLoan_period() + ""), loanDetail1.getLoan_amount());
        invest.setTradeStatus(IFundTradeDao.TradeStatus.TRANSFEREDMONEY);
        invest.setTradeType(IFundTradeDao.TradeType.LOAN);
        invest.setLogType(IFundAccountLogDao.LogType.LOAN);
        FundTrade fundTrade = fundUtil.Tradelog(invest);
        loanDetailDao.update(loanDetail);
        FundProduct fundProduct = fundProductDao.queryByloanId(loanDetailId);
        FundProduct fundProduct1 = new FundProduct(fundProduct.getProduct_id());
        fundProduct1.setProduct_status(Byte.valueOf("5")); //还款中
        fundProduct1.setUpdate_time(calendar.getTime());
        fundProductDao.update(fundProduct1);
        fundProductAuditDao.save(new FundProductAudit(null, fundProduct1.getProduct_id(), 5, operator, new Date()));
       List<FundInvestmentDetail> fundInvestmentDetails= loanDetailDao.queryForFID(loanDetailId);
       BaseReturn baseReturn=fundUtil.preRepayment(loanDetail1, fundProduct, fundInvestmentDetails);
        if(baseReturn.getReturnCode()==1) {
            logger.info("当前时间："+calendar.getTime()+"新增预还款记录失败。");
            return baseReturn;
        }
        logger.info("当前时间："+calendar.getTime()+"对借款人放款审核成功，借款ID:"+loanDetail1.getLoan_id()+"\t借款用户ID:"+loanDetail1.getUser_id());
        return new BaseReturn(0, loanDetail1, processInfo.OPERATE_SUCCESS);
    }
}
