package com.hhn.service.pay;

import com.aipg.common.AipgReq;
import com.aipg.common.InfoReq;
import com.allinpay.XmlTools;
import com.hhn.dao.IFundTransferDao;
import com.hhn.dao.ITransInfoDao;
import com.hhn.pojo.TransInfo;
import com.hhn.util.Base;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lenovo on 2014/12/17.
 */
@Component
public class AllinPaySupport implements ServletContextAware{
    @Resource
    private ITransInfoDao transInfoDao;
    public ServletContext servletContext;
    private String webPath;
    private @Value("#{payConfig['allinPayUrl']}") String allinPayUrl;
    private @Value("#{payConfig['pfxPath']}") String pfxPath;
    private @Value("#{payConfig['pfxPassword']}") String pfxPassword;
    private @Value("#{payConfig['cerPath']}") String cerPath;
    private @Value("#{payConfig['userName']}") String userName;
    private @Value("#{payConfig['password']}") String password;
    private @Value("#{payConfig['merchantId']}") String merchantId;

    public AllinPaySupport() {
    }

    public InfoReq getInfo(String trxcod, String sn) {
        InfoReq infoReq = new InfoReq();
        infoReq.setUSER_NAME(userName);
        infoReq.setUSER_PASS(password);
        infoReq.setMERCHANT_ID(merchantId);
        infoReq.setLEVEL("5");
        infoReq.setDATA_TYPE("2");
        infoReq.setVERSION("03");
        infoReq.setTRX_CODE(trxcod);
        infoReq.setREQ_SN(sn);
        return infoReq;
    }

    public TransInfo execute(AipgReq aipgReq, boolean isTLTFront, TransInfo[] transInfos) {
        String xml = XmlTools.buildXml(aipgReq, true);
        try {
            if (!isTLTFront) {
                xml = XmlTools.signMsg(xml, pfxPath, pfxPassword, false);
            } else {
                xml = xml.replaceAll("<SIGNED_MSG></SIGNED_MSG>", "");
            }
            transInfos[0].setCreate_time(new Date());
            transInfos[0].setContent(xml);
            transInfoDao.save(transInfos[0]);
            return sendXml(xml, allinPayUrl, isTLTFront, transInfos[1]);
        } catch (Exception e) {
            e.printStackTrace();
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

    public static String getFormatDate() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext=servletContext;
        this.webPath = servletContext.getRealPath("/");
        this.pfxPath = webPath + "WEB-INF/classes/" + pfxPath;
        this.cerPath = webPath + "WEB-INF/classes/" + cerPath;
    }
}
