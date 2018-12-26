package com.eTeng.storage.jdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @FileName ContextJavaConfig.java
 * @Author eTeng
 * @Date 2018/12/24 15:53
 * @Description 应用上下文配置
 */

@Configuration
@PropertySource("classpath:/jdbc.properties") //注入外部资源
public class ContextJavaConfig{

    //注入上下文环境属性对象
    @Autowired
    Environment environment;

    @Bean
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
