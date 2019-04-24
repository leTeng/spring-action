package com.eteng.ehcache;

import net.sf.ehcache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.core.io.ClassPathResource;

/**
 * 基于声明式的Ehcache自动缓存
 * @FileName EhcacheConfig.java
 * @Author eTeng
 * @Date 2019/4/18 10:27
 * @Description
 */
public class EhcacheConfig{

    /**
     * 配置Spring提供的Ehcache缓存管理器(依赖于Ehcache的CacheManager)
     * @param cm
     * @return
     */
//    @Bean
    public org.springframework.cache.CacheManager ehCacheCacheManager(CacheManager cm){
        return new EhCacheCacheManager(cm);
    }

    /**
     * 配置Spring提供的Ehcache缓存管理器工厂
     * @return
     */
//    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
        EhCacheManagerFactoryBean cacheManagerFactory = new EhCacheManagerFactoryBean();
        // 配置ehcache的配置文件
        cacheManagerFactory.setConfigLocation(new ClassPathResource("classpath:/ehcache.xml"));
        cacheManagerFactory.afterPropertiesSet();
        return cacheManagerFactory;
    }


}
