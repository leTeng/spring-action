package com.eteng.jdbctem;

import com.eteng.Application;
import com.eteng.pojo.Users;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * JDBC 模板测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class SpringJdbcConfigTest{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void queryTest(){
        Object[] vals = {"243"};
        Users users = jdbcTemplate.queryForObject("SEELCT * FROM users WHERE users.id = ?",vals,Users.class);
        Assert.assertNotEquals(users,null);
    }
}