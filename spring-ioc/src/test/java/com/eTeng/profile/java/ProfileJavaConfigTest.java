package com.eTeng.profile.java;


import com.eTeng.profile.bean.ProfileBean;
import com.eTeng.profile.bean.impl.CommosBean;
import com.eTeng.profile.bean.impl.DevelopmentProfileBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("produce") //激活 Profile
@ContextConfiguration(classes = ProfileJavaConfig.class)
public class ProfileJavaConfigTest {

    @Autowired
//    ProfileBean developmentProfileBean; //当自动注入出现类型歧义时。
//    ProfileBean testProfileBean;            // 可根据属性名和bean的id匹配
    ProfileBean produceProfileBean;

    @Autowired
    CommosBean commosBean;

    @Test
    public void getBeanNameTest(){
        System.out.println(produceProfileBean.getBeanName());
        System.out.println(commosBean.getBeanName());
    }
}