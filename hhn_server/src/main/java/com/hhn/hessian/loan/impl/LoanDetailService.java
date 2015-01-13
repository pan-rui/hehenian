package com.hhn.hessian.loan.impl;

import com.hhn.dao.ILoanDetailDao;
import com.hhn.hessian.loan.ILoanDetailService;
import com.hhn.pojo.LoanDetail;
import com.hhn.service.ProcessInfo;
import com.hhn.service.impl.LoanDetailServiceImpl;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * 借款申请与审核
 * Created by lenovo on 2014/12/9.
 */
@Controller
public class LoanDetailService extends BaseService<LoanDetail> implements ILoanDetailService {
    @Resource
    private LoanDetailServiceImpl loanDetailServiceImpl;
    @Resource
    private ILoanDetailDao loanDetailDao;
@Resource
private ProcessInfo processInfo;
    @Override
    public BaseReturn apply(LoanDetail loanDetail) {
        try {
           return loanDetailServiceImpl.apply(loanDetail);
        } catch (Exception rex) {
            rex.printStackTrace();
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        }
    }

    @Override
    public BaseReturn verify(Integer loanDetailId) {
        try {
            return loanDetailServiceImpl.verify(loanDetailId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_data_inValid,processInfo.DATA_INVALID);
        }
    }

    @Override
    public BaseReturn loanVerify(Integer loanDetailId,String operator) {
        try {
            return loanDetailServiceImpl.loanVerify(loanDetailId,operator);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_data_inValid,processInfo.DATA_INVALID);
        }
    }
}
