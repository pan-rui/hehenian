package com.hhn.service.impl;

import com.hhn.dao.IFundUserAccountDao;
import com.hhn.service.IWithdrawMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/10.
 */
@Service
public class WithdrawMoneyServiceImpl implements IWithdrawMoneyService {

    private final static String httpsUrl = "https://113.108.182.3/aipg/ProcessServlet";
    private final static String passwd = "111111";
    private final static String keyPath = "";
    private final static String trustPath = "";
    @Autowired
    private IFundUserAccountDao fundUserAccountDao;
    /**
     * 用户提现实现
     * @param map
     * @return
     */
    public Map<String, Object> doWithdraw(Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        //先调通联支付扣款
        //返回成功，则扣除用户账户中的金额

        fundUserAccountDao.updateAccountMoney(map);
        resultMap.put("resultCode","Y");
        resultMap.put("resultMsg","申请提现成功！");
        return resultMap;
    }

}
