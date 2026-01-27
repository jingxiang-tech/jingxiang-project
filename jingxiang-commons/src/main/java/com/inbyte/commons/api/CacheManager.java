package com.inbyte.commons.api;

/**
 * 缓存管理
 *
 * @param <K>
 * @param <V>
 */
public interface CacheManager<K, V> {
    /**
     * 将键值对放入缓存中，并指定过期时间
     *
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间（单位：毫秒）
     */
    void put(K key, V value, long expireTime);
    /**
     * 将键值对放入缓存中，并指定过期时间
     *
     * @param key        键
     * @param value      值
     */
    void put(K key, V value);
    /**
     * 将键值对放入缓存中，并指定过期时间
     *
     * @param key        键
     */
    void put(K key);

    /**
     * 根据键从缓存中获取值
     *
     * @param key 键
     * @return 对应的值，如果缓存中不存在该键，则返回null
     */
    V get(K key);

    /**
     * 根据键验证缓存中是否存在对应的值
     *
     * @param key 键
     * @return 如果缓存中存在该键，则返回true，否则返回false
     */
    boolean containsKey(K key);

    /**
     * 根据键从缓存中删除对应的值
     *
     * @param key 键
     */
    void delete(K key);

    /**
     * 根据键验证缓存中是否存在对应的Key
     *
     * @param key 键
     * @return 如果缓存中存在该键，则返回true，同时删除key，否则返回false
     */
    boolean verify(K key);
}