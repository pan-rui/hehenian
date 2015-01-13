package com.hhn.test;

import com.hhn.dao.IAccountUserDao;
import com.hhn.service.impl.InvestRecordServiceImpl;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/8.
 */
public class SavePojo {
    @Resource
    private IAccountUserDao accountUserDao;
    public static String getMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5=MessageDigest.getInstance("MD5");
        byte[] bStr=str.getBytes("UTF-8");
        byte[] mStr = md5.digest(bStr);
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<mStr.length;i++) {
            String bs = Integer.toHexString(0xFF & mStr[i]);
            if(bs.length()==1)
                sb.append(0);
            sb.append(bs);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Byte.parseByte("127");
//        AccountUser user = new AccountUser(222,Byte.parseByte("1"),"test01",getMd5("panrui"));
        System.out.println(Byte.valueOf("2"));
       Method method= InvestRecordServiceImpl.class.getMethod("investRecordList", Map.class);
        System.out.println(Arrays.asList(method.getParameterTypes()).toString());
    }
    
}
