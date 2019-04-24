package com.eteng;

import com.eteng.ehcache.EhcacheConfig;
import com.eteng.map.LocalCacheConfig;
import com.eteng.redis.RedisCacheConfig;
import com.eteng.redis.RedisConfig;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import java.util.ArrayList;

/**
 * Spring 应用上下文配置

 * Spring为缓存组件提供的集成，集成多种缓存实现(如Ehcache、Redis)。并提供基于应用程序内存的
 * 的实现(如ConCurrentMap)。Spring为缓存组件集成抽象出一个CacheManager。Spring或者
 * Spring Data依赖于缓存实现来实现CacheManager。根据用户所配置的CacheManager，直接使用声
 * 明式进行缓存操作。
 * 缓存管理器是Spring对众多流行缓存组件的一个抽象，这个抽象就是键值对存取。因为每一种缓存组件
 * 都提供键值对存取这简单的功能,所有缓存管理器只能操作键值对。
 *
 * Spring 和 Spring Data 所提供的CacheManager
 *  Spring :
 *    - SimpleCacheManager
 *    - NoOpCacheManager
 *    - ConcurrentMapManager
 *    - CompositeCacheManager
 *    - EhcacheCacheManager
 *  Spring Data:
 *    - RedisCacheManager
 *    - GemfireCahceManager
 * @FileName ApplicationConfig.java
 * @Author eTeng
 * @Date 2019/4/18 10:22
 * @Description
 */
@Configuration
// 开启缓存驱动
@EnableCaching
@ComponentScan
@PropertySource("classpath:redis.properties")
@Import({EhcacheConfig.class,RedisConfig.class,LocalCacheConfig.class,RedisCacheConfig.class})
public class ApplicationConfig{

    /**
     * 多缓存管理器
     * 配置的多个缓存管理器,可以迭代缓存管理器来存取数据
     * @return
     */
//    @Bean
    public CacheManager compositeCacheManager(){
        CompositeCacheManager compositeCacheManager = new CompositeCacheManager();
        ArrayList managers = new ArrayList();
        managers.add(new NoOpCacheManager());
        managers.add(new SimpleCacheManager());
        compositeCacheManager.setCacheManagers(managers);
        compositeCacheManager.setFallbackToNoOpCache(true);
        return compositeCacheManager;
    }
}
