package com.hhn.dao;

import com.hhn.pojo.LoanCertificate;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanCertificateDao {
    public int save(LoanCertificate loanCertificate);

    public LoanCertificate query(int id);

    public int delete(int id);

    public int update(LoanCertificate loanCertificate);

}
