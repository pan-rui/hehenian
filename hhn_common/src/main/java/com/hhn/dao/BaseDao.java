package com.hhn.dao;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2014/12/8.
 */
public interface BaseDao<T> {
    public List<T> queryAll();

    public List<T> queryForPage(int size);
    public int save(T t);

    public T query(int id);

    public int delete(int id);

    public int update(T t);
    public List<T> queryByProperties(@Param("keyy") String propertyName,@Param("valuee") Serializable propertyValue);
    public List<T> queryByPros(@Param("params")Map<String,Object> params);
}
