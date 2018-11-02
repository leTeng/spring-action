package com.eTeng.java;


import com.eTeng.point.interfaces.Perform;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AspectJavaConfig.class)
public class AspectJavaConfigTest{

    @Autowired
    Perform perform;

    @Test
    public void testPerformAop(){
        perform.processShow();
    }
}