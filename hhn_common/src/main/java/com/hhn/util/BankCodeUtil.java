package com.hhn.util;

/**
 * Created by Administrator on 2015/1/10.
 */
public class BankCodeUtil {

    private final static String[] bankNameArrs = new String[]{"中国银行","农业银行","建设银行","交通银行","招商银行","邮储银行","兴业银行","光大银行"};
    private final static String[] bankCodeArrs = new String[]{"0104","0103","0105","0301","0308","0403","0309","0303"};

    //获取银行代码
    public static String getBankCode(int code){
        if (code>=0 && code<bankCodeArrs.length) {
            return bankCodeArrs[code];
        }
        return "";
    }
    //获取银行名称
    public static String getBankName(int code){
        if (code>=0 && code<bankNameArrs.length) {
            return bankNameArrs[code];
        }
        return "";
    }

}
