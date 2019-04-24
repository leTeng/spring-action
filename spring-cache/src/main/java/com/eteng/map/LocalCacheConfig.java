package com.eteng.map;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

/**
 * 基于注解声明式的本地缓存
 * @FileName LocalCacheConfig.java
 * @Author eTeng
 * @Date 2019/4/18 11:04
 * @Description
 */
public class LocalCacheConfig{

    /**
     * 配置Spring提供本地缓存管理器
     * @return
     */
//    @Bean
    public CacheManager concurrentMapCacheManager(){
        return  new ConcurrentMapCacheManager();
    }

}
