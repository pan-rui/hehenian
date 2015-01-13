package com.hhn.pojo;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * Created by hynpublic on 2015/1/10.
 */
@Alias("bank_code")
public class BankCode implements Serializable {
    private Integer bank_code_id;
    private String code;
    private String bank_name;
    private String regex;
    private Integer bank_index;
    public BankCode(){}
    public BankCode(Integer bank_code_id) {
        this.bank_code_id = bank_code_id;
    }

    public Integer getBank_code_id() {
        return bank_code_id;
    }

    public void setBank_code_id(Integer bank_code_id) {
        this.bank_code_id = bank_code_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Integer getBank_index() {
        return bank_index;
    }

    public void setBank_index(Integer bank_index) {
        this.bank_index = bank_index;
    }
}
