package com.eTeng.context.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.eTeng.storage.memory.config.SecurityPackage;
import com.eTeng.storage.memory.controller.ControllerPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @FileName Application.java
 * @Author eTeng
 * @Date 2018/12/29 16:14
 * @Description Spring Boot 的启动类
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = {ControllerPackage.class,SecurityPackage.class})
@PropertySource("classpath:/application.properties")
public class Application extends SpringBootServletInitializer{

    //注入上下文环境属性对象
    @Autowired
    Environment environment;

    //spring boot 入口
    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return builder.sources(Application.class);
    }

    @Bean
    @Primary
    public DataSource dataSource() throws Exception{

        DruidDataSource dataSource = new DruidDataSource();
        Properties connectProperties = new Properties();
        /*
         * 使用Druid工具生成密文。并生成公钥和秘钥
         * java -cp druid-1.0.16.jar com.alibaba.druid.filter.config.ConfigTools you_password
         */

        //设置密码解码,并提供公钥
        connectProperties.setProperty("config.decrypt","true");
        connectProperties.setProperty("config.decrypt.key",environment.getProperty("jdbc.public.key"));

        dataSource.setDriverClassName(environment.getProperty("jdbc.driver.class"));
        dataSource.setUrl(environment.getProperty("jdbc.url"));
        dataSource.setUsername(environment.getProperty("jdbc.username"));
        dataSource.setPassword(environment.getProperty("jdbc.password"));
        dataSource.setFilters("config");
        dataSource.setConnectProperties(connectProperties);
        return dataSource;
    }
}
