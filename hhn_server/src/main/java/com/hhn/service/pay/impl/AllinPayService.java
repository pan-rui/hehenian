package com.hhn.service.pay.impl;

import com.aipg.common.AipgReq;
import com.aipg.common.InfoReq;
import com.aipg.rtreq.Trans;
import com.hhn.pojo.TransInfo;
import com.hhn.service.pay.AllinPaySupport;
import com.hhn.service.pay.IAllinPay;
import com.hhn.util.BaseReturn;
import com.hhn.util.DqlcConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/17.
 */
@Component
public class AllinPayService implements IAllinPay {
    private Logger logger = Logger.getLogger(this.getClass());
    @Resource
    private DqlcConfig processInfo;
    @Resource
    private AllinPaySupport allinPaySupport;

    @Override
    public BaseReturn allinPay100014(Trans trans, TransInfo[] transInfos) {
        try {
//            trans.setBUSINESS_CODE(allinPaySupport.daifuBUSINESS);
            trans.setREMARK("100014");
            allinPaySupport.execute(trans, false, transInfos);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error(ex);
            return new BaseReturn(1, processInfo.ALLINPAY_CONNECTION_ERROR);
        }
        String resultStr = transInfos[1].getContent();
        if (resultStr.contains("<RET_CODE>0000</RET_CODE>"))
            return new BaseReturn(0, transInfos[1], processInfo.OPERATE_SUCCESS);
        else {
            logger.info(resultStr);
            return new BaseReturn(1, transInfos[1],resultStr.substring(resultStr.indexOf("<ERR_MSG>") + 9, resultStr.indexOf("</ERR_MSG>")));
        }
    }

    @Override
    public BaseReturn allinPay100011(Trans trans, TransInfo[] transInfos) {
        try {
//            trans.setBUSINESS_CODE(allinPaySupport.daishouBUSINESS);
            logger.info("daifuBUSINESS::::"+allinPaySupport.daifuBUSINESS+":::daishouBUSINESS:::"+allinPaySupport.daishouBUSINESS+":::daifuTRX:::"+allinPaySupport.daifuTRX+":::daishouTRX:::"+allinPaySupport.daishouTRX);
            trans.setREMARK("100011");
            allinPaySupport.execute(trans,false, transInfos);
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error( ex);
            return new BaseReturn(1, processInfo.ALLINPAY_CONNECTION_ERROR);
        }
        String resultStr = transInfos[1].getContent();
        if (resultStr.contains("<RET_CODE>0000</RET_CODE>"))
            return new BaseReturn(0, transInfos[1], processInfo.OPERATE_SUCCESS);
        else
            return new BaseReturn(1, transInfos[1], resultStr.substring(resultStr.indexOf("<ERR_MSG>") + 9, resultStr.indexOf("</ERR_MSG>")));
    }


    public void copyProperties(Map<String, Object> params, Trans trans) {
        for (Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> entry = it.next();
            if (StringUtils.isNotEmpty(entry.getKey())) {
                try {
                    trans.getClass().getMethod("set" + entry.getKey(), String.class).invoke(trans, entry.getValue());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
