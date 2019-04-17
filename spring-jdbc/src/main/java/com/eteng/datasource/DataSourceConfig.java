package com.eteng.datasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 数据源(数据库连接池)配置
 *
 * spring 支持两种类型的数据源。
 * 1.基于jdbc驱动的数据源。这种数据源是没有连接池化,要么一直使用一个连接或者每次访问创建一个连接的。
 *   数据源驱动类：
 *      -- DriverManagerDataSource
 *      -- SimpleDriverDataSource
 *      -- SingleConnectionDataSource
 *
 * 2.外部提供的池化数据源(DBCP,c3p0,druid等)，这种数据源是提供连接池化。
 *   数据源驱动类
 *      -- DruidDataSource
 *
 * @FileName DataSourceConfig.java
 * @Author eTeng
 * @Date 2019/4/5 0:04
 * @Description
 */
public class DataSourceConfig{

    /**
     * 基于JDBC的连接池,每次访问都会数据库重新创建连接
     * @param driverClass 驱动类
     * @param url 数据库访问url
     * @param username 用户名
     * @param password 密码
     * @return
     */
//    @Bean
    public DataSource driverManagerDataSource(@Value("${jdbc.driver.class}") String driverClass,
                                              @Value("${jdbc.url}") String url,
                                              @Value("${jdbc.username}") String username,
                                              @Value("${jdbc.password.key}") String password){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        return dataSource;
    }

    /**
     * 基于JDBC的连接池,与DriverManagerDataSource相似,不同是SimpleDriverDataSource
     * 用于加载特定环境的数据库驱动类。
     * @param driverClass 驱动类
     * @param url  数据库url
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws Exception
     */
//    @Bean
    public DataSource simpleDriverDataSource(@Value("${jdbc.driver.class}") String driverClass,
                                             @Value("${jdbc.url}") String url,
                                             @Value("${jdbc.username}") String username,
                                             @Value("${jdbc.password.key}") String password) throws Exception{
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass((Class<Driver>)Class.forName(driverClass));
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        return dataSource;
    }

    /**
     * 基于JDBC的连接池,每次访问数据使用同一个连接
     * @param driverClass 驱动类
     * @param url 数据库url
     * @param username 用户名
     * @param password 密码
     * @return
     */
//    @Bean
    public DataSource singleConnectionDataSource(@Value("${jdbc.driver.class}") String driverClass,
                                                 @Value("${jdbc.url}") String url,
                                                 @Value("${jdbc.username}") String username,
                                                 @Value("${jdbc.password.key}") String password){
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        return dataSource;
    }

    /**
     * Druid连接池
     * @param driverClass 驱动类
     * @param url url
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Bean
    public DataSource druidDataSource(@Value("${jdbc.driver.class}") String driverClass,
                                      @Value("${jdbc.url}") String url,
                                      @Value("${jdbc.username}") String username,
                                      @Value("${jdbc.password}") String password,
                                      @Value("${jdbc.password.key}") String passwordKey,
                                      @Value("${jdbc.public.key}") String publicKey) throws Exception{
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverClass);

        dataSource.setMaxActive(10);
        dataSource.setInitialSize(5);
        dataSource.setMaxWait(60000);
        dataSource.setMinIdle(3);

        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(true);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxOpenPreparedStatements(20);
        // 配置监控过滤器
//        dataSource.setFilters("stat");
        // 配置合并SQL监控过滤器
        dataSource.setFilters("mergeStat");
        // 配置代理监控过滤器
//        dataSource.setProxyFilters(proxyFilters());
//        Properties properties = new Properties();
//
//        // 配置密码解密秘钥
//        properties.setProperty("config.decrypt","true");
//        properties.setProperty("config.decrypt.key",publicKey);
//        dataSource.setConnectProperties(properties);
        return dataSource;
    }

    @Bean
    public List<Filter> proxyFilters(){
        List<Filter> filters = new ArrayList<Filter>();
        // SQL监控拦截器
        StatFilter statFilter = new StatFilter();
        statFilter.setMergeSql(true);
        // 记录SQL
        statFilter.setLogSlowSql(true);
        // 记录慢查询SQL记录
        statFilter.setSlowSqlMillis(10000);
        filters.add(statFilter);

        // 日志拦截器

        return filters;
    }
}
