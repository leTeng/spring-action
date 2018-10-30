package com.eTeng.condition.java;

import com.eTeng.condition.bean.interfaces.ResourceBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ResourceBeanConfig.class)
public class ResourceBeanConfigTest{

    @Autowired(required = false)
    ResourceBean resourceBean;

    @Test
    public void testGetResourceName(){
        if(resourceBean != null){
            System.out.println(resourceBean.getResourceName());
        }else{
            System.out.println("not found flag.properties ,fail instance of ResourceBean");
        }
    }
}