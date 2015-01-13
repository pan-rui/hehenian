package com.hhn.dao;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by lenovo on 2014/12/6.
 */
@Component
public class T1 {
    private String abd = "abd";
    @Cacheable(value = "commonData",key = "#h",condition ="#h != 'a'",unless = "#h=='bb'")
    public String t2(String h) {
        System.out.println("in.....method...");
        return "World!";
    }
    @CacheEvict(value = "commonData",key = "'bb'")
    public String t3(String b) {
        return "del.....";
    }
}
