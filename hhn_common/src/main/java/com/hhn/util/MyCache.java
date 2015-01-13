package com.hhn.util;

import net.spy.memcached.MemcachedClient;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

/**
 * Created by lenovo on 2014/12/27.
 */
public class MyCache implements Cache {
    private String name;
    private MemcachedClient client;
    private int expire;
    public MyCache(){}

    public MyCache(String name, MemcachedClient client) {
        Assert.notNull(client, "Memcahce client must not be null");
        this.client=client;
        this.name=name;
    }
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return client;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value=this.client.get(objectToString(key));
        return (value != null ? new SimpleValueWrapper(value) : null);
    }
    public static String objectToString(Object object) {
        if (object == null) {
            return null;
        } else if (object instanceof String) {
            return (String) object;
        } else {
            return object.toString();
        }
    }
    @Override
    public void put(Object key, Object value) {
        this.client.set(objectToString(key),expire,value);
    }

    @Override
    public void evict(Object key) {
        this.client.delete(objectToString(key));
    }

    @Override
    public void clear() {
        //TODO: delete all data;
    }

    public MemcachedClient getClient() {
        return client;
    }

    public void setClient(MemcachedClient client) {
        this.client = client;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
