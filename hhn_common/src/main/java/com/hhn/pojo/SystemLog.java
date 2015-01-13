package com.hhn.pojo;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lenovo on 2014/12/18.
 */
@Alias("system_log")
public class SystemLog implements Serializable {
    private Integer log_id;
    private String description;
    private String method;
    private Byte log_type;
    private String request_ip;
    private String exception_code;
    private String exception_detail;
    private String params;
    private Integer user_id;
    private Date ctime;

    public SystemLog(){}
    public SystemLog(Integer log_id) {
        this.log_id = log_id;
    }

    public Integer getLog_id() {
        return log_id;
    }

    public void setLog_id(Integer log_id) {
        this.log_id = log_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Byte getLog_type() {
        return log_type;
    }

    public void setLog_type(Byte log_type) {
        this.log_type = log_type;
    }

    public String getRequest_ip() {
        return request_ip;
    }

    public void setRequest_ip(String request_ip) {
        this.request_ip = request_ip;
    }

    public String getException_code() {
        return exception_code;
    }

    public void setException_code(String exception_code) {
        this.exception_code = exception_code;
    }

    public String getException_detail() {
        return exception_detail;
    }

    public void setException_detail(String exception_detail) {
        this.exception_detail = exception_detail;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }
}
