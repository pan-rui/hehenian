package com.hhn.dao;

import com.hhn.pojo.SysIpAddress;
import org.springframework.stereotype.Repository;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface ISysLoanCertificateDao {
    public int save(SysIpAddress sysIpAddress);

    public SysIpAddress query(int id);

    public int delete(int id);

    public int update(SysIpAddress sysIpAddress);

}
