package com.hhn.hessian.recharge.impl;

import com.hhn.hessian.recharge.IRechargeService;
import com.hhn.pojo.FundActualAccountLog;
import com.hhn.service.ProcessInfo;
import com.hhn.service.impl.RechargeServiceImpl;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/17.
 */
@Controller
public class RechargeService extends BaseService<FundActualAccountLog> implements IRechargeService {
    @Resource
    private RechargeServiceImpl rechargeServiceImpl;
    @Resource
    private ProcessInfo processInfo;
    @Override
    public BaseReturn recharge(Map<String, Object> params) {
        try {
            return rechargeServiceImpl.recharge(params);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_data_inValid,processInfo.DATA_INVALID);
        }
    }
}
