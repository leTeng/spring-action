package com.eTeng.storage.jdbc.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;

//
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ContextJavaConfig.class)
public class ContextJavaConfigTest{

    @Autowired
    DataSource dataSource;

    @Test
    public void dataSource() throws Exception{
        Connection connection = dataSource.getConnection();
        Assert.assertNotNull(connection);
    }
}