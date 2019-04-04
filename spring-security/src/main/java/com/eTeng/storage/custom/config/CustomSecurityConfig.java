package com.eTeng.storage.custom.config;

import com.eTeng.storage.custom.pojo.Users;
import com.eTeng.storage.custom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @FileName CustomSecurityConfig.java
 * @Author eTeng
 * @Date 2018/12/24 18:31
 * @Description 使用自定义数据构建认证数据
 */
//@Configuration
//@EnableWebSecurity
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter{

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * 自定义构建用户的存储。
     * 1.这种方式不需要考虑数据的来源,只关注UserDetailsService提供一个UserDetails。使得
     *   用户的信息无论是持久化数据(关系型数据库)还是内存数据(NoSql数据库)
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(new UserDetailsService(){
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
                //获取用户数据
                Users users = userRepository.userByUsernameQuery(username);
                //获取用户已授权的权限
                List<String> authorities = userRepository.authoritiesByUsernameQuery(username);
                //构建User(security规范的User)
                List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

                for(String authority : authorities){
                    grantedAuthorities.add(new SimpleGrantedAuthority(authority));
                }
                User user = new User(users.getUsername(),users.getPassword(),grantedAuthorities);
                return user;
            }
        });
    }
}
