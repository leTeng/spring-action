package com.eteng.jpa;

import com.eteng.Application;
import com.eteng.pojo.Users;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class SpringDataJpaUserRepositoryTest{

    @Autowired
    private SpringDataJpaUserRepository repository;

    @Test
    public void testGetByUsernameIsLike(){
        List<Users> users = repository.getByUsernameIsLike("%zhang%");
    }

    @Test
    public void testGetByEnableAndUsername(){
        Users zhangsan = repository.getByEnableAndUsername(true,"zhangsan1");
    }

    @Test
    public void testGetUsers(){
        Users zhangsan = repository.getUser(true,"zhangsan1");
    }

    @Test
    public void testGetUser(){
        repository.getUsers(true,"zhangsan1");
    }

    @Test
    public void testGetALlUsers(){
        List<Users> aLlUsers = repository.al();
    }
}