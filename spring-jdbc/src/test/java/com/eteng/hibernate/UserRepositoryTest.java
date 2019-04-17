package com.eteng.hibernate;

import com.eteng.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class UserRepositoryTest{

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAdd(){
        userRepository.add();
    }

    @Test
    public void testCascadeAdd(){
        userRepository.cascadeAdd();
    }

    @Test
    public void testFetch(){
        userRepository.fetch();
    }

    @Test
    public void testReverseFetch(){
        userRepository.reverseFetch();
    }
}