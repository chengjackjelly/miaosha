package com.miaosha.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.miaosha.service.CacheService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {

    private Cache<String,Object> commonCache=null;

    @PostConstruct
    public void init(){
        commonCache = CacheBuilder.newBuilder()
                .initialCapacity(10)//初始容量
                .maximumSize(100)//缓存中最大可存储100个key，超过100个后会按照LRU更新
                .expireAfterWrite(30, TimeUnit.SECONDS).build();
    }

    @Override
    public void setCommonCache(String key, Object value) {
        commonCache.put(key,value);
    }

    @Override
    public Object getFromCommonCache(String key) {
        return commonCache.getIfPresent(key);
    }
}
