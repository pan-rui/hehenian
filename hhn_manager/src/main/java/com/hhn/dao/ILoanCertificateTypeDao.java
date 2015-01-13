package com.hhn.dao;

import com.hhn.pojo.LoanCertificateType;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanCertificateTypeDao {
    public int save(LoanCertificateType loanCertificateType);

    public LoanCertificateType query(int id);

    public int delete(int id);

    public int update(LoanCertificateType loanCertificateType);

}
