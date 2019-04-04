package com.eTeng.intercept.plan;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @FileName PlanSecurityConfig.java
 * @Author eTeng
 * @Date 2018/12/27 17:48
 * @Description 使用普通方式自定义配置权限认证
 */
//@Configuration
//@EnableWebSecurity
public class PlanSecurityConfig extends WebSecurityConfigurerAdapter{

    /**
     * 通过重载configure(HttpSecurity http)方法,自定义配置请求拦截规则和权限认证。
     *
     * 使用普通的方式配置权限认证只能是一维的。例如antMatchers("/aut/**").authenticated(),
     * 这一个拦截。如果配置多个权限限定(antMatchers("/aut/**").authenticated().hasAuthority("admin"))
     * 是不允许的。
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/aut/**") //定义第一个拦截表达式
                .authenticated() // 配置权限校验(认证过后允许访问)
                .antMatchers("/aut/getRole") //定义第二个拦截表达式
                .hasAuthority("admin") //配置权限校验(拥有admin权限,允许访问)
                .anyRequest().permitAll() //配置其他请求都可以访问
                .and() //连接其他配置
                .formLogin(); //配置重定向登录页
    }
}
