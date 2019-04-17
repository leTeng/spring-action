package com.eteng;

import com.eteng.datasource.DataSourceConfig;
import com.eteng.hibernate.HibernateSpringConfig;
import com.eteng.jdbctem.SpringJdbcConfig;
import com.eteng.jpa.SpringJpaConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @FileName Application.java
 * @Author eTeng
 * @Date 2019/4/8 11:58
 * @Description
 */
@Configuration
// 开启事务管理
@EnableTransactionManagement
// 开启JpaRepository自动代理
@EnableJpaRepositories(transactionManagerRef = "hibernateTransactionManager",basePackages = "com.eteng")
@Import({DataSourceConfig.class,HibernateSpringConfig.class,SpringJdbcConfig.class,SpringJpaConfig.class})
@PropertySource("classpath:application.properties")
@ComponentScan
public class Application{


}
