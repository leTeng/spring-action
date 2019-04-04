package com.eTeng.storage.jdbc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.sql.DataSource;

/**
 * @FileName JdbcSecurityConfig.java
 * @Author eTeng
 * @Date 2018/12/24 17:42
 * @Description 扩展该类(WebSecurityConfigurerAdapter)为DelegatingFilterProxy提供委托过滤器
 */
//@Configuration
//@EnableWebSecurity //开启安全
public class JdbcSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    DataSource dataSource;

    /**
     * 基于Jdbc方式构建用户存储
     *
     * 1.提供数据源,并且已存在必须(由userByUserNameQuery()、groupByUserNameQuey()、
     * authoritiesByUserNameQuery()实现的sql定义)的表。
     *
     * 2.PasswordEncoder 在验证时,密码已经加密情况下。可以配置对明文进行加密与正确密码匹配。
     *   Security 的 PasswordEncoder三个实现，分别提供三种不同的加密算法。如果需要自定义
     *   加密算法,实现PasswordEncoder接口。其中matches()是密码对比的实现。
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        //使用默认的查询sql,并使用密码编码
        auth.jdbcAuthentication().dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder());
        //使用自定义的sql查询
//        buildCustomSql(auth);
    }

    /**
     * 使用自定义的sql查询时都要遵循一个原则,所有的查询都是通过username查询。
     * @param auth
     * @throws Exception
     * @throws Exception
     */
    private void buildCustomSql(AuthenticationManagerBuilder auth) throws Exception{
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enable" +
                        "from users where username = ?;")
                .groupAuthoritiesByUsername("select id,gm.group_name,ga.authorities" +
                        "from groups g,group_members gm,group_authorities ga " +
                        "where gm.username = ? and g.id = gm.group_id and g.id = ga.group.id")
                .authoritiesByUsernameQuery("select username,authorities " +
                        "from authorities where username = ?");
    }
}
