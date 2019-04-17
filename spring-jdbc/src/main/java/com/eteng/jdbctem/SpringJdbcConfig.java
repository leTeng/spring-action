package com.eteng.jdbctem;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * Spring 对JDBC的支持配置
 * @FileName SpringJdbcConfig.java
 * @Author eTeng
 * @Date 2019/4/8 14:12
 * @Description 
 */
public class SpringJdbcConfig{

    /**
     * 配置JDBC模板,基于参数索引进行参数的绑定
     * @param druidDataSource 数据源
     * @return
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource druidDataSource){
        return new JdbcTemplate(druidDataSource);
    }

    /**
     * 配置JDBC模板,基于参数名进行参数的绑定
     * @param druidDataSource
     * @return
     */
//    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource druidDataSource){
        return new NamedParameterJdbcTemplate(druidDataSource);
    }
}
