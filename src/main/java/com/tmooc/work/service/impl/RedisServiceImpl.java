package com.tmooc.work.service.impl;


import com.tmooc.work.common.KeyPrefix;
import com.tmooc.work.service.RedisService;
import com.tmooc.work.util.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private JedisPool jedisPool;
    @Override
    public <T> T get(KeyPrefix prefix, String key, TypeReference<T> clazz) {
        Jedis jedis=null;
        try {
            jedis = jedisPool.getResource();
            String value=jedis.get(prefix.getPrefix()+key);
            return JsonUtil.string2Obj(value,clazz) ;
        }finally {
            returnToPool(jedis);
        }
    }

    @Override
    public <T> boolean set(KeyPrefix prefix,String key, T t) {
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            String value = JsonUtil.obj2String(t);
            if (prefix.expireSeconds()<=0){
                jedis.set(prefix.getPrefix()+key,value);
            }else{
                jedis.setex(prefix.getPrefix()+key,prefix.expireSeconds(),value);
            }
            return  true;
        }finally {
            returnToPool(jedis);
        }
    }
    /**
     * 增加值
     * */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     * */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }
    /**
     * 判断key是否存在
     * */
    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix.getPrefix() + key;
            return  jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }
    private void returnToPool(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }
}
