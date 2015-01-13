package com.hhn.hessian.invest.impl;

import com.hhn.hessian.invest.IFundInvestService;
import com.hhn.pojo.FundInvestmentDetail;
import com.hhn.pojo.Invest;
import com.hhn.service.ProcessInfo;
import com.hhn.service.impl.InvestServiceImpl;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * Created by lenovo on 2014/12/12.
 */
@Controller
public class FundInvestmentService extends BaseService<FundInvestmentDetail> implements IFundInvestService {
    @Resource
    private InvestServiceImpl investServiceImpl;
    @Resource
    private ProcessInfo processInfo;
    @Override
    public BaseReturn investment(Invest invest) {
        try {
            return investServiceImpl.investment(invest);
        } catch (Exception ex) {
            ex.printStackTrace();
        return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        }
    }

}
