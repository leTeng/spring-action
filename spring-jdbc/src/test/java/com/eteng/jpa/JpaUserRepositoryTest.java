package com.eteng.jpa;

import com.eteng.Application;
import com.eteng.pojo.OmOrder;
import com.eteng.pojo.Users;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class JpaUserRepositoryTest{

    @Autowired
    private JpaUserRepository jpaUserRepository;
    
    @Test
    public void getUsersById(){
        Users users = jpaUserRepository.getUsersById("4028801b6a24de12016a24de7e820001");
        System.out.println(users);
    }

    @Test
    public void getUserByName(){
        Users zhangsan = jpaUserRepository.getUserByName("zhangsan");
        System.out.println(zhangsan);
    }

    @Test
    public void getOrderByOrderNum(){
        OmOrder order = jpaUserRepository.getOrderByOrderNum("123");
        System.out.println(order);
    }

    @Test
    public void getOrderByOrderNumAndUsers(){
        OmOrder order = jpaUserRepository.getOrderByOrderNumAndUsers("123","4028801b6a24de12016a24de7e820001");
        System.out.println(order);
    }
}