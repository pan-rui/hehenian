package com.hhn.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by lenovo on 2014/12/9.
 */
@Component
public class ProcessInfo {
/*    private static ResourceBundle bundle;
    static {
        bundle = ResourceBundle.getBundle("processInfo", Locale.getDefault());
    }*/
    public @Value("#{process['operate.success']}") String OPERATE_SUCCESS;
    public @Value("#{process['invest.returned.task']}") String INVEST_RETURNED_TASK;
    public @Value("#{process['data.inValid']}") String DATA_INVALID;
    public @Value("#{process['loan_ahead']}") String LOAN_AHEAD;
    public @Value("#{process['not.product']}") String NOT_PRODUCT;
    public @Value("#{process['allinpay.connection.error']}") String ALLINPAY_CONNECTION_ERROR;
    public @Value("#{process['user.balance.lack']}") String USER_BALANCE_LACK;
    public @Value("#{process['card.mismatching']}") String CARD_MISMATCHING;

    public ProcessInfo() {
    }
}
