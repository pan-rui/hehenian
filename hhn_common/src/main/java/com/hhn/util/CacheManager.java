package com.hhn.util;

import net.spy.memcached.MemcachedClient;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;

/**
 * Created by lenovo on 2014/12/27.
 */
public class CacheManager extends AbstractCacheManager {
    private Collection<? extends Cache> caches;
    private MemcachedClient client;

    public CacheManager() {
    }

    public CacheManager(MemcachedClient client) {
        setClient(client);
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return this.caches;
    }

    public void setClient(MemcachedClient client) {
        this.client = client;
        updateCaches();
    }

    public MemcachedClient getClient() {
        return client;
    }

    public Cache getCache(String name) {
        if (client == null)
            throw new IllegalStateException("MemcacheClient must not be null.");
        Cache cache = getCache(name);
        if (cache == null) {
            cache = new MyCache(name, client);
            addCache(cache);
        }
        return cache;
    }

    public void setCaches(Collection<? extends Cache> caches) {
        this.caches = caches;
    }

    public void updateCaches() {
        if (caches != null) {
            for (Cache cache : caches) {
                if (cache instanceof MyCache) {
                    ((MyCache) cache).setClient(client);
                }
            }
        }
    }
}
