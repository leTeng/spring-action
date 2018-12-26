package com.eTeng.storage.memory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @FileName MemorySecurityConfig.java
 * @Author eTeng
 * @Date 2018/12/24 14:45
 * @Description 扩展该类(WebSecurityConfigurerAdapter)为DelegatingFilterProxy提供委托过滤器
 */
@Configuration //声明为Spring 上下文
@EnableWebSecurity //开启安全
public class MemorySecurityConfig extends WebSecurityConfigurerAdapter{

    /**
     * 重写configure(AuthenticationManagerBuilder auth)方法构建用户的存储
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        //内存的方式存储用户
        auth.inMemoryAuthentication()
                .withUser("mary").password("123").roles("user")
                .and()
                .withUser("admin").password("admin").roles("user","admin");
    }
}
