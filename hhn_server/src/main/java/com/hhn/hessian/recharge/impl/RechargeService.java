package com.hhn.hessian.recharge.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.hhn.util.DqlcConfig;
import org.springframework.stereotype.Controller;

import com.hhn.hessian.recharge.IRechargeService;
import com.hhn.pojo.FundActualAccountLog;
import com.hhn.service.impl.RechargeServiceImpl;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;

/**
 * Created by lenovo on 2014/12/17.
 */
@Controller
public class RechargeService extends BaseService<FundActualAccountLog> implements IRechargeService {
    @Resource
    private RechargeServiceImpl rechargeServiceImpl;
    @Resource
    private DqlcConfig processInfo;
    @Override
    public BaseReturn recharge(Map<String, Object> params) {
        try {
            return rechargeServiceImpl.recharge(params);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }
}
