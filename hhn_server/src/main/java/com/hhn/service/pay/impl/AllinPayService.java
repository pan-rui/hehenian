package com.hhn.service.pay.impl;

import com.aipg.common.AipgReq;
import com.aipg.common.InfoReq;
import com.aipg.rtreq.Trans;
import com.hhn.dao.ITransInfoDao;
import com.hhn.pojo.TransInfo;
import com.hhn.service.ProcessInfo;
import com.hhn.service.pay.AllinPaySupport;
import com.hhn.service.pay.IAllinPay;
import com.hhn.util.BaseReturn;
import com.hhn.util.BaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/17.
 */
@Component
public class AllinPayService extends BaseService<TransInfo> implements IAllinPay {
    @Resource
    private ProcessInfo processInfo;
@Resource
AllinPaySupport allinPaySupport;
    @Resource
    private ITransInfoDao transInfoDao;
    @Override
    public BaseReturn allinPay100014(Trans trans,TransInfo[] transInfos) {
        try {
            allinPaySupport.execute(getAipgReq(trans, "100014"), false,transInfos);
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error("充值失败。错误信息=>\r\n"+ex.getMessage());
            return new BaseReturn(1, processInfo.ALLINPAY_CONNECTION_ERROR);
        }
//        transInfoDao.save(transInfos[1]);
        String resultStr=transInfos[1].getContent();
        if(resultStr.contains("<RET_CODE>0000</RET_CODE>"))
        return new BaseReturn(0,transInfos[1], processInfo.OPERATE_SUCCESS);
        else return new BaseReturn(1,resultStr.substring(resultStr.indexOf("<ERR_MSG>") + 9, resultStr.indexOf("</ERR_MSG>")));
    }

    @Override
    public BaseReturn allinPay100011(Trans trans,TransInfo[] transInfos) {
        try{
        allinPaySupport.execute(getAipgReq(trans,"100011"), false,transInfos);
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error("充值失败。错误信息=>\r\n"+ex.getMessage());
            return new BaseReturn(1, processInfo.ALLINPAY_CONNECTION_ERROR);
        }
        String resultStr=transInfos[1].getContent();
        if(resultStr.contains("<RET_CODE>0000</RET_CODE>"))
            return new BaseReturn(0,transInfos[1], processInfo.OPERATE_SUCCESS);
        else return new BaseReturn(1,resultStr.substring(resultStr.indexOf("<ERR_MSG>") + 9, resultStr.indexOf("</ERR_MSG>")));
    }

    public void copyProperties(Map<String, Object> params, Trans trans) {
        for (Iterator<Map.Entry<String,Object>> it = params.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String,Object> entry= it.next();
            if(StringUtils.isNotEmpty(entry.getKey())) {
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


    public AipgReq getAipgReq(Trans trans,String code) {
        InfoReq infoReq = allinPaySupport.getInfo(code,trans.getREMARK());
        AipgReq aipgReq = new AipgReq();
        aipgReq.setINFO(infoReq);
        trans.setMERCHANT_ID(infoReq.getMERCHANT_ID());
        trans.setBUSINESS_CODE("00600");
        trans.setSUBMIT_TIME(AllinPaySupport.getFormatDate());

//        trans.setACCOUNT_NAME("测试试");
//        trans.setACCOUNT_NO("622588121251757643");
//        trans.setACCOUNT_PROP("0");
//        trans.setAMOUNT("100000");
        trans.setBANK_CODE("0105");
//        trans.setCURRENCY("CNY");
//        trans.setCUST_USERID("252523524253xx");
//        trans.setTEL("13434245846");

        aipgReq.addTrx(trans);
        return aipgReq;
    }

    public static void main(String[] args) {
        AllinPayService allinPayService=new AllinPayService();
//        allinPayService.allinPay100014(new Trans());
    }
}
