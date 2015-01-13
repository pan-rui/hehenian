package com.hhn.dao;

import com.hhn.pojo.LoanCertificateAudi;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ILoanCertificateAudiDao {
    public int save(LoanCertificateAudi loanCertificateAudi);

    public LoanCertificateAudi query(int id);

    public int delete(int id);

    public int update(LoanCertificateAudi loanCertificateAudi);

}
