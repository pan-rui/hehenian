package com.hhn.util;

import java.io.Serializable;

/**
 * Created by lenovo on 2014/12/6.
 */
public final class BaseReturn implements Serializable {
    public static int Err_data_empty=100;  //参数为空
    public static int Err_data_duplicate=101; //
    public static int Err_data_inValid=102;   //参数不合法
    public static int Err_system_error=301;   //系统错误

    private int returnCode;
    private Object data;
    private String messageInfo;

    public BaseReturn() {}

    public BaseReturn(int returnCode, Object data) {
        this.returnCode=returnCode;
        this.data=data;
    }


    public BaseReturn(int returnCode, String messageInfo) {
        this.returnCode=returnCode;
        this.messageInfo=messageInfo;
    }

    public BaseReturn(int returnCode, Object data, String messageInfo) {
        this.returnCode = returnCode;
        this.data = data;
        this.messageInfo = messageInfo;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
