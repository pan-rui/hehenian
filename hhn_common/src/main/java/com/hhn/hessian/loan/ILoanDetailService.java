package com.hhn.hessian.loan;

import com.hhn.pojo.LoanDetail;
import com.hhn.util.Base;
import com.hhn.util.BaseReturn;

/**
 * 借款申请接口
 * Created by lenovo on 2014/12/9.
 */
public interface ILoanDetailService {
    /**
     * 借款申请处理
     * @param loanPerson 借款信息
     * @return
     */
    public BaseReturn apply(LoanDetail loanPerson);

    /**
     * 借款申请审核
     * @param loanDetailId 借款 ID
     * @return
     */
    public BaseReturn verify(Integer loanDetailId);

    /**
     * 放款审核
     * @param loanDetailId
     * @return
     */
    public BaseReturn loanVerify(Integer loanDetailId,String operator);
}
