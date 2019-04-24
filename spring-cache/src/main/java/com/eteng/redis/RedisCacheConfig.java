package com.eteng.redis;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import java.time.Duration;

/**
 * @FileName RedisCacheConfig.java
 * @Author eTeng
 * @Date 2019/4/19 15:35
 * @Description
 */
public class RedisCacheConfig{

    /**
     * Spring Data Redis 1.0 版本的配置
     * @param redisTemplate
     * @return
     */
   /* @Bean
    public CacheManager redisCacheManagerByOne(RedisTemplate redisTemplate){
        return new RedisCacheManager(redisTemplate);
    }*/

    /**
     * Spring Data Redis 2.0 版本的配置
     * 相比1.0版本Spring提供一个RedisCacheWriter来进行Redis的访问,并非RedisTemplate。
     * RedisCacheWriter需要RedisConnectionFactory来支撑。CacheManager只是定义了几个
     * 简单的键值对存储或者删除，所以声明式缓存之不能使用Redis更高级的数据结构。
     * @return
     */
    @Bean
    public CacheManager redisCacheManager(JedisConnectionFactory jedisConnectionFactory){
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        // 设置前缀
        configuration.prefixKeysWith("shopping_cart");
        // 设置过期时间
        configuration.entryTtl(Duration.ofHours(1));
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(jedisConnectionFactory)
                // 缓存配置(使用默认配置)
                .cacheDefaults(configuration)
                // 支持开启事务

                .transactionAware()
                .build();
    }
}
