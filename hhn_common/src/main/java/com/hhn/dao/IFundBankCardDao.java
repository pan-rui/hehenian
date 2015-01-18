package com.hhn.dao;

import com.hhn.pojo.FundBankCard;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/3.
 */
@Repository
public interface IFundBankCardDao extends BaseDao<FundBankCard> {
    public List<FundBankCard> queryByUserId(Integer userId);

    public FundBankCard getBankCard(Integer userId);

    public String getUserNameById(Integer userId);

    public List<FundBankCard> getBankCardListByUserId(Integer userId);

    public List<FundBankCard> getBankCardListByCardNo(String cardNo);

    public void updateBankStatus(Map<String,String> params);

    public FundBankCard getUserBindingCard(Integer userId);

    int getBankNoCount(String bankNo);
    //查询借款人卡信息
    public HashMap getUserBankDetail(Integer product_id);

}
