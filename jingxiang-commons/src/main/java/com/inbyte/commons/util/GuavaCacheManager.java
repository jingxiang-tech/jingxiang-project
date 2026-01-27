package com.inbyte.commons.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.inbyte.commons.api.CacheManager;

import java.util.concurrent.TimeUnit;

public class GuavaCacheManager<K, V> implements CacheManager<K, V> {
    private final Cache<K, V> cache;

    private static final String DEFAULT_VALUE = "";

    public GuavaCacheManager() {
        // 初始化 Guava Cache
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }

    public GuavaCacheManager(long expireMinute) {
        // 初始化 Guava Cache
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expireMinute, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public void put(K key, V value, long expireTime) {
        cache.put(key, value);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void put(K key) {
        cache.put(key, (V)DEFAULT_VALUE);
    }

    @Override
    public V get(K key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void delete(K key) {
        cache.invalidate(key);
    }

    @Override
    public boolean verify(K key) {
        boolean b = containsKey(key);
        if (b) {
            delete(key);
        }
        return b;
    }

    @Override
    public boolean containsKey(K key) {
        return cache.getIfPresent(key) != null;
    }
}