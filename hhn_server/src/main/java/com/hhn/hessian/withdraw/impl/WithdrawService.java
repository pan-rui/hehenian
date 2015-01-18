package com.hhn.hessian.withdraw.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.hhn.util.DqlcConfig;
import org.springframework.stereotype.Controller;

import com.hhn.hessian.withdraw.IWithdrawService;
import com.hhn.pojo.FundActualAccountLog;
import com.hhn.service.impl.WithdrawServiceImpl;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;

/**
 * 提现
 * Created by lenovo on 2014/12/17.
 */
@Controller
public class WithdrawService extends BaseService<FundActualAccountLog> implements IWithdrawService {

    @Resource
    private WithdrawServiceImpl withdrawServiceImpl;
@Resource
private DqlcConfig processInfo;
    @Override
    public BaseReturn withdraw(Map<String, Object> params) {
        try {
            return withdrawServiceImpl.withdraw(params);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }

    @Override
    public BaseReturn applyWithdraw(Map<String, Object> params) {
        try {
            return withdrawServiceImpl.applyWithdraw(params);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }

    @Override
    public BaseReturn preWithdraw(Integer tradeId) {
        try {
            return withdrawServiceImpl.preWithdraw(tradeId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }
}
