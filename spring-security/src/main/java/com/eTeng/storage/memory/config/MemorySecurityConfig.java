package com.eTeng.storage.memory.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * @FileName MemorySecurityConfig.java
 * @Author eTeng
 * @Date 2018/12/24 14:45
 * @Description 扩展该类(WebSecurityConfigurerAdapter)为DelegatingFilterProxy提供委托过滤器
 */
@EnableWebSecurity //开启安全
public class MemorySecurityConfig extends WebSecurityConfigurerAdapter{


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
                .antMatchers("/user/**") //定义第一个拦截表达式
                .authenticated() // 配置权限校验(认证过后允许访问)
                .antMatchers("/vip/**") //定义第二个拦截表达式
                .hasAuthority("USER") //配置权限校验(拥有admin权限,允许访问)
                .anyRequest().permitAll() //配置其他请求都可以访问
                .and() //连接其他配置
                .formLogin().loginPage("/login").defaultSuccessUrl("/user")//配置认证页面和认证成功跳转
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login"); //配置退出页面和退出成功页面
//        http
//            .authorizeRequests()
//            .antMatchers("/").permitAll()
//            .antMatchers("/user/**").hasRole("USER")
//            .and()
//            .formLogin().loginPage("/login").defaultSuccessUrl("/user")
//            .and()
//            .logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }

    /**
     * 重写configure(AuthenticationManagerBuilder auth)方法构建用户的存储
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        //内存的方式存储用户
        auth.inMemoryAuthentication()
                //设置无密码编码
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .withUser("mary").password("123").roles("USER")
                .and()
                .withUser("admin").password("admin").roles("VIP");
    }
}
