package com.hhn.service;

import com.hhn.pojo.FundProduct;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lenovo on 2014/12/8.
 */
@Transactional
public interface ISignAuditService {

    public void updateSignStatus(FundProduct fundProduct, String userName);

    public boolean putMoneyAudit(FundProduct fundProduct,String userName);

}
