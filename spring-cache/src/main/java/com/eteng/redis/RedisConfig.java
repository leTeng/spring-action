package com.eteng.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

/**
 * 基于声明式的Redis自动缓存
 *
 * 1. Spring Data Redis 提供四个连接工厂分别是
 *    - JedisConnectionFactory 一个社区驱动的连接器，由Spring Data Redis模块支持
 *    - LettuceConnectionFactory 一个基于Netty的开源连接器，由Spring Data Redis支持
 *    - JredisConnectionFactory
 *    - SrpConnectionFactory
 *
 * 2.Spring Data Redis 以模板的方式提供了更高级的数据访问方案，分别为
 *
 *   - RedisTemplate 可以持久化各种类型的数据,不局限于字节数组。并可以提供自定义的序列化实现
 *
 *    RedisTemplate的常用API
 *         方法                       子API接口                     功能
 *      opsForVaule()           ValueOperations<K,V>             操作字符串结构
 *      opsForList()            ListOperations<K,V>              操作List结构
 *      opsForSet()             SetOperations<K,V>               操作Set结构
 *      opsForZSet()            ZSetOperations<K,V>              操作ZSet结构
 *      opsForHash()            HashOperations<K,V>              操作Hash结构
 *      boundValueOps(K)        BoundeValueOperations<K,V>       绑定Key操作字符串结构
 *      boundListOps(K)         BoundeListOperations<K,V>        绑定Key操作List结构
 *      boundSetOps(K)          BoundeSetOperations<K,V>         绑定Key操作Set结构
 *      boundZSetValueOps(K)    BoundeZSetOperations<K,V>        绑定Key操作ZSet结构
 *      boundHashOps(K)         BoundeHashOperations<K,V>        绑定Key操作Hash结构
 *
 *   - StringRedisTemplate 是RedisTemplate的扩展，只针对字符串类型数据的持久化
 *
 *     StringRedisTemplate 的使用方式和RedisTemplate类似。
 *
 * 3. Spring Data Redis 持久化数据时提供四种序列化方案
 *
 *    - GenericToStringSerializer    Spring转换服务序列化
 *    - JdkSerializationRedisSerializer Java序列化
 *    - Jackson2JsonRedisSerializer jackson2序列化
 *    - StringRedisSerializer String类型序列化
 *    - OxmSerializer xml序列化
 *
 * 4.注解驱动缓存
 *  Spring会为缓存抽象创建一个切面,通过通知的时机实现缓存。切点是那些标注了注解的方法或者类上。使用在类上的注解将
 *  所有的方法都作为切点，使用在单个方法该方法就是一个切点。
 *
 * 4.1 注解列表
 *          注解                              描述
 *       @Cacheable            调用方法之前查询缓存是否存在，如果找到返回值缓存的值。如果找不到就执行方法并将结果缓存
 *       @CachePut             将返回结果缓存,但不会查询缓存的值返回。(只存不取)
 *       @CacheEvict           删除一个或者多个缓存数据
 *       @Caching              分组注解，能应用多个其他的缓存注解
 *
 * 4.2 注解属性
 *
 * @Cacheable、@CachePut
 *
 *       属性名                类型                      描述
 *       value              String[]                要使用的缓存名称(缓存管理器)
 *       condition          String                  SpEL表达式,结果是false，将不应用缓存切面
 *       key                String                  SpEL表达式,缓存的key
 *       unless             String                  SpEL表达式,结果是true，返回值将不缓存
 *
 *  @Cachevitct
 *
 *      属性名                 类型                      描述
 *      value               String[]                要使用的缓存名称(缓存管理器)
 *      key                 String                  SpEL表达式,缓存的key
 *      condition           String                  SpEL表达式,结果是false，将不应用缓存切面
 *      allEntries          boolean                 如果为true的话，特定缓存的有数据被移除
 *      beforeInvocation    boolean                 如果为true,将在调用之前移除数据。如果为flae,反之。
 *
 *  4.3 自定义key
 *      在方法上使用声明式注解会使用一个默认的key，@Cacheable、@CachePut注解提供一个自定义key属性。
 *      如果想使用自定义的key，需要使用SpEL表达式来计算得到。
 *
 *      表达式列表：
 *
 *          表达式                                 描述
 *        #root.args                        传递给缓存方法的参数,数组形式
 *        #root.cache                       该方法执行时对应的缓存，数组形式
 *        #root.target                      目标对象
 *        #root.targetClass                 目标对象的类
 *        #root.method                      目标方法
 *        #root.methodName                  目标方法的名称
 *        #result                           方法返回值(不能应用在@Cacheable上)
 *        #Argument                         任意方法的参数名
 *
 *  4.4 条件化缓存
 *
 *     @Cacheable、@CachePut 提供两个属性来控制是否缓存数据。这两个属性很相似，但有一点区别。
 *     condition属性计算结果如果是false,将禁用缓存功能，即不从缓存查数据，也不将数据放入缓存。
 *     unless属性计算结果如果是fale,会从缓存中查询数据,但不会缓存数据。
 *
 * 4.5
 * @Cachevitct
 * @Caching
 *
 * @FileName RedisConfig.java
 * @Author eTeng
 * @Date 2019/4/18 10:27
 * @Description
 */
@Import(RedisCacheConfig.class)
public class RedisConfig{

    /**
     * 创建RedisTemplate,Key-Value支持多种序列化
     * @param jedisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory jedisConnectionFactory,
                                       RedisSerializer redisSerializer){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        // 设置序列化实现
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setKeySerializer(redisSerializer);
        return redisTemplate;
    }

    /**
     * 创建系列化的实现
     * @return
     */
    @Bean
    public RedisSerializer redisSerializer(){
        return new JdkSerializationRedisSerializer();
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory jedisConnectionFactory){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(jedisConnectionFactory);
        return stringRedisTemplate;
    }

    /**
     * 创建JedisConnectionFactory
     * @return
     */
//    @Bean
    public RedisConnectionFactory jedisConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration){
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    /**
     * 创建LettuceConnectionFactory
     * @param redisStandaloneConfiguration
     * @return
     */
//    @Bean
    public RedisConnectionFactory lettuceConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration){
       return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    /**
     * redis的集群配置
     * @param nodes 节点列表
     * @param maxRedirect 最大重定向次数
     * @param pwd 秘钥
     * @return
     */
    @Bean
    public RedisConnectionFactory jedisClusterConnectionFactory(@Value("#{'${rd.cluster.node}'.split(',')}") List<String> nodes,
                                                                @Value("${rd.cluster.max_redirects}") Integer maxRedirect,
                                                                @Value("${rd.cluster.passwrod}") String pwd){
        // 节点配置
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(nodes);
        // 重定向次数配置
        redisClusterConfiguration.setMaxRedirects(maxRedirect);
        // 秘钥配置
        redisClusterConfiguration.setPassword(RedisPassword.of(pwd));
        return new JedisConnectionFactory(redisClusterConfiguration);
    }

    /**
     * redis 的连接配置
     * @param host 主机
     * @param port 端口
     * @param dbIndex 槽
     * @param passwrod 密码
     */
    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration(@Value("${rd.host}") String host,
                                                                     @Value("${rd.port}") Integer port,
                                                                     @Value("${rd.db.index}") Integer dbIndex,
                                                                     @Value("${rd.passwrod}") String passwrod){
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setDatabase(dbIndex);
        config.setPort(port);
        config.setHostName(host);
        config.setPassword(RedisPassword.of(passwrod));
        return config;
    }
}
