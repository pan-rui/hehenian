package com.hhn.dao;

import com.hhn.pojo.SysLoanCertificateType;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ISysLoanCertificateTypeDao {
    public int save(SysLoanCertificateType sysLoanCertificateType);

    public SysLoanCertificateType query(int id);

    public int delete(int id);

    public int update(SysLoanCertificateType sysLoanCertificateType);

}
