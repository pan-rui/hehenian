package com.hhn.hessian.query.impl;

import com.hhn.dao.IFundTradeDao;
import com.hhn.hessian.query.IQueryService;
import com.hhn.pojo.FundUserAccount;
import com.hhn.service.ProcessInfo;
import com.hhn.service.impl.QueryServiceImpl;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hynpublic on 2014/12/26.
 */
@Controller
public class QueryService extends BaseService<FundUserAccount> implements IQueryService {
    @Resource
    private QueryServiceImpl queryServiceImpl;
@Resource
private ProcessInfo processInfo;

    @Override
    public BaseReturn queryUserBalance(Integer userId) {
        try {
            return queryServiceImpl.queryUserBalance(userId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        }
    }

    @Override
    public BaseReturn queryPay() {
        try {
            return queryServiceImpl.queryPay();
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        }
    }

    @Override
    public BaseReturn queryPrincipal(Integer userId) {
        try {
            return queryServiceImpl.queryPrincipal(userId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        }
    }

    @Override
    public BaseReturn queryFreeze(Integer usesId) {
        try {
            return queryServiceImpl.queryFreeze(usesId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        }
    }

    @Override
    public BaseReturn queryInterested(Integer userId) {
        try {
            return queryServiceImpl.queryInterested(userId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        }
    }

    @Override
    public BaseReturn queryInterest(Integer userId) {
        try {
            return queryServiceImpl.queryInterest(userId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        }
    }

    @Override
    public BaseReturn queryOtherInterest(Integer userId) {
        try {
            return queryServiceImpl.queryOtherInterest(userId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        }
    }

    @Override
    public BaseReturn queryPhone(Integer userId) {
        try {
            return queryServiceImpl.queryPhone(userId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, processInfo.DATA_INVALID);
        }
    }
}
