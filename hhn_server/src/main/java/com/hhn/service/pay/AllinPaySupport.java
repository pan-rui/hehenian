package com.hhn.service.pay;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import com.aipg.rtreq.Trans;
import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.aipg.common.AipgReq;
import com.aipg.common.InfoReq;
import com.allinpay.XmlTools;
import com.hhn.dao.ITransInfoDao;
import com.hhn.pojo.TransInfo;

/**
 * Created by lenovo on 2014/12/17.
 */
@Component
public class AllinPaySupport {
    private Logger logger = Logger.getLogger(getClass());
    @Resource
    private ITransInfoDao transInfoDao;

    private
    @Value("${allinPayUrl}")
    String allinPayUrl;
    private
    @Value("${pfxPath}")
    String pfxPath;
    private
    @Value("${pfxPassword}")
    String pfxPassword;
    private
    @Value("${cerPath}")
    String cerPath;
    private
    @Value("${userName}")
    String userName;
    private
    @Value("${password}")
    String password;
    private
    @Value("${merchantId}")
    String merchantId;
    public
    @Value("${daifuTRX}")
    String daifuTRX;
    public
    @Value("${daishouTRX}")
    String daishouTRX;
    public
    @Value("${daifuBUSINESS}")
    String daifuBUSINESS;
    public
    @Value("${daishouBUSINESS}")
    String daishouBUSINESS;

    public AllinPaySupport() {
    }

    public InfoReq getInfo(String trxcod) {
        InfoReq infoReq = new InfoReq();
        infoReq.setUSER_NAME(userName);
        infoReq.setUSER_PASS(password);
        infoReq.setMERCHANT_ID(merchantId);
        infoReq.setREQ_SN(merchantId + formatUUID());
        infoReq.setLEVEL("5");
        infoReq.setDATA_TYPE("2");
        infoReq.setVERSION("03");
        infoReq.setTRX_CODE(trxcod);
        return infoReq;
    }

    public TransInfo execute(Trans trans, boolean isTLTFront, TransInfo[] transInfos) {
        AipgReq aipgReq = getAipgReq(trans);
        String xml = XmlTools.buildXml(aipgReq, true);
        logger.info("报文参数..........." + JSON.toJSONString(aipgReq));
        logger.info("daifuBUSINESS::::" + daifuBUSINESS + ":::daishouBUSINESS:::" + daishouBUSINESS + ":::daifuTRX:::" + daifuTRX + ":::daishouTRX:::" + daishouTRX);
        try {
            if (!isTLTFront) {
                xml = XmlTools.signMsg(xml, pfxPath, pfxPassword, false);
            } else {
                xml = xml.replaceAll("<SIGNED_MSG></SIGNED_MSG>", "");
            }
            transInfos[0].setCreate_time(new Date());
            transInfos[0].setContent(xml);
            transInfos[0].setThird_sn(aipgReq.getINFO().getREQ_SN());
            transInfos[1].setThird_sn(aipgReq.getINFO().getREQ_SN());
            transInfoDao.save(transInfos[0]);
            return sendXml(xml, allinPayUrl, isTLTFront, transInfos[1]);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    public TransInfo sendXml(String xml, String url, boolean isFront, TransInfo transInfo) throws UnsupportedEncodingException, Exception {
        System.out.println("======================发送报文======================：\n" + xml);
//        String resp=XmlTools.send(url,new String(xml.getBytes(),"GBK"));
        String resp = XmlTools.send(url, xml);
        System.out.println("======================响应内容======================");
//		System.out.println(new String(resp.getBytes(),"GBK")) ;
        boolean flag = XmlTools.verifySign(resp, cerPath, false, isFront);
        if (flag) {
            System.out.println("响应内容验证通过");
        } else {
            System.out.println("响应内容验证不通过");
        }
        transInfo.setCreate_time(new Date());
        transInfo.setContent(resp);
        return transInfo;
    }

    public String formatUUID() {
        StringBuffer sb = new StringBuffer();
        char[] chars = UUID.randomUUID().toString().replace("-","").toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i % 4 == 3) continue;
            else sb.append(chars[i]);
        }
        return sb.toString();
    }

    public String getFormatDate() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public AipgReq getAipgReq(Trans trans) {
        InfoReq infoReq = null;
        if (trans.getREMARK().equals("100011")) {
            infoReq = getInfo(daishouTRX);
            trans.setBUSINESS_CODE(daishouBUSINESS);
        } else if (trans.getREMARK().equals("100014")) {
            infoReq = getInfo(daifuTRX);
            trans.setBUSINESS_CODE(daifuBUSINESS);
        }
        AipgReq aipgReq = new AipgReq();
        aipgReq.setINFO(infoReq);
        trans.setMERCHANT_ID(infoReq.getMERCHANT_ID());
        trans.setSUBMIT_TIME(getFormatDate());
        trans.setACCOUNT_PROP("0");
        trans.setAMOUNT(new BigDecimal(trans.getAMOUNT()).multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).toString());
        aipgReq.addTrx(trans);
        return aipgReq;
    }
/*    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext=servletContext;
        this.webPath = servletContext.getRealPath("/");
        this.pfxPath = webPath + "WEB-INF/classes/" + pfxPath;
        this.cerPath = webPath + "WEB-INF/classes/" + cerPath;
    }*/
}
