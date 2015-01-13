package com.hhn.dao;

import com.hhn.pojo.SysMailLog;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ISysMailLogDao {
    public int save(SysMailLog sysMailLog);

    public SysMailLog query(int id);

    public int delete(int id);

//    public int update(SysLoanCertificateType sysLoanCertificateType);

}
