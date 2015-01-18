package com.hhn.hessian.query.impl;

import javax.annotation.Resource;

import com.hhn.util.DqlcConfig;
import org.springframework.stereotype.Controller;

import com.hhn.hessian.query.IQueryService;
import com.hhn.pojo.FundUserAccount;
import com.hhn.service.impl.QueryServiceImpl;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;

/**
 * Created by hynpublic on 2014/12/26.
 */
@Controller
public class QueryService extends BaseService<FundUserAccount> implements IQueryService {
    @Resource
    private QueryServiceImpl queryServiceImpl;
@Resource
private DqlcConfig processInfo;

    @Override
    public BaseReturn queryUserBalance(Integer userId) {
        try {
            return queryServiceImpl.queryUserBalance(userId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }

    @Override
    public BaseReturn queryPay() {
        try {
            return queryServiceImpl.queryPay();
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }

    @Override
    public BaseReturn queryTotalInvest(Integer userId) {
        try {
            return queryServiceImpl.queryTotalInvest(userId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }

    @Override
    public BaseReturn queryRound(Integer usesId) {
        try {
            return queryServiceImpl.queryRound(usesId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }

    @Override
    public BaseReturn queryInterested(Integer userId) {
        try {
            return queryServiceImpl.queryInterested(userId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }

    @Override
    public BaseReturn queryInterest(Integer userId) {
        try {
            return queryServiceImpl.queryInterest(userId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }

    @Override
    public BaseReturn queryOtherInterest(Integer userId) {
        try {
            return queryServiceImpl.queryOtherInterest(userId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }

    @Override
    public BaseReturn queryPhone(Integer userId) {
        try {
            return queryServiceImpl.queryPhone(userId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }

    /**
     * 查询银行卡号
     * @param userId
     * @return
     */
    public BaseReturn queryBankCard(Integer userId){
        try{
            return queryServiceImpl.queryBankCard(userId);
        }catch (Exception ex){
            return new BaseReturn(BaseReturn.Err_data_inValid, this.getMessage(processInfo.DATA_INVALID));
        }
    }

}
