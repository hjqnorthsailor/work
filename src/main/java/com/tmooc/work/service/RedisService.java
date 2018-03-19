package com.tmooc.work.service;


import com.tmooc.work.common.KeyPrefix;
import org.codehaus.jackson.type.TypeReference;

public interface RedisService {
    <T> T get(KeyPrefix prefix, String key, TypeReference<T> clazz);
    <T> boolean set(KeyPrefix prefix, String key, T t);
    <T> Long incr(KeyPrefix prefix, String key);
    <T> Long decr(KeyPrefix prefix, String key);
    <T> boolean exists(KeyPrefix prefix, String key);
}
