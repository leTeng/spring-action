package com.eTeng.storage.custom.repository;

import com.eTeng.storage.custom.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @FileName UserRepository.java
 * @Author eTeng
 * @Date 2018/12/24 18:44
 * @Description
 */
@Component
public class UserRepository{

    public Users userByUsernameQuery(String username){
        return new Users("mary","123",true);
    }

    public List<String> authoritiesByUsernameQuery(String username){
        List<String> authorities = new ArrayList<String>();
        authorities.add("user");
        authorities.add("admin");
        authorities.add("student");
        return authorities;
    }
}
