package com.hhn.service.impl;

import com.hhn.dao.IFundProductAuditDao;
import com.hhn.dao.IFundProductDao;
import com.hhn.dao.IFundUserAccountDao;
import com.hhn.hessian.loan.ILoanDetailService;
import com.hhn.pojo.FundProduct;
import com.hhn.pojo.FundProductAudit;
import com.hhn.service.ISignAuditService;
import com.hhn.util.BaseReturn;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by lenovo on 2014/12/8.
 */
@Service
@Transactional
public class SignAuditServiceImpl implements ISignAuditService {
    Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private IFundProductDao fundProductDao;
    @Autowired
    private IFundProductAuditDao fundProductAuditDao;
    @Autowired
    private ILoanDetailService loanDetailService;
    /**
     * 标的审核(修改状态)
     * @param fundProduct
     */
    @Transactional(rollbackFor=Exception.class)
    public void updateSignStatus(FundProduct fundProduct, String userName){
        fundProductDao.updateSignStatus(fundProduct);
        //记录标的发布时间
        FundProductAudit productAudit = new FundProductAudit();
        productAudit.setProduct_id(fundProduct.getProduct_id());
        productAudit.setStatus(fundProduct.getProduct_status().intValue());
        productAudit.setOperator(userName);
        productAudit.setModify_time(new Date());
        fundProductAuditDao.save(productAudit);
    }

    /**
     * 放款审核
     * @param fundProduct
     */
    @Transactional(rollbackFor=Exception.class)
    public boolean putMoneyAudit(FundProduct fundProduct, String userName) {
        FundProduct product = fundProductDao.query(fundProduct.getProduct_id());
        BaseReturn baseReturn = loanDetailService.loanVerify(product.getLoan_id(), userName);
        if(baseReturn.getReturnCode()==0){
            fundProductDao.updateSignStatusByLoanId(fundProduct);
            //记录标的放款时间
            FundProductAudit productAudit = new FundProductAudit();
            productAudit.setProduct_id(fundProduct.getProduct_id());
            productAudit.setStatus(fundProduct.getProduct_status().intValue());
            productAudit.setOperator(userName);
            productAudit.setModify_time(new Date());
            fundProductAuditDao.save(productAudit);
            return true;
        }else{
            logger.error(baseReturn.getMessageInfo());
            logger.error(baseReturn.getData());
            return false;
        }
    }

}
