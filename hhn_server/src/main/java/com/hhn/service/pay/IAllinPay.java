package com.hhn.service.pay;

import java.util.Map;

import com.aipg.rtreq.Trans;
import com.hhn.pojo.TransInfo;
import com.hhn.util.BaseReturn;

/**
 * 支付相关
 * Created by lenovo on 2014/12/17.
 */
public interface IAllinPay {
    /**
     * 单笔代付
     * @param trans
     * @return
     */
    public BaseReturn allinPay100014(Trans trans,TransInfo[] transInfos);

    /**
     * 单笔代收
     * @param trans
     * @return
     */
    public BaseReturn allinPay100011(Trans trans,TransInfo[] transInfos);


	
}
