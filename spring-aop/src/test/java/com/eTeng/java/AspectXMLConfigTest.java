package com.eTeng.java;

import com.eTeng.advice.WatchPerformCounterAdvice;
import com.eTeng.point.interfaces.Perform;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @FileName AspectXMLConfigTest.java
 * @Author 梁怡腾
 * @Date 2018/12/12 9:47
 * @Description xml方式配置切面
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext.xml")
public class AspectXMLConfigTest{

    @Autowired
    Perform perform;

    @Autowired
    WatchPerformCounterAdvice wcct;

    @Test
    public void testRandomPerform(){
        perform.processShow();
    }

    @Test
    public void testSpecifyItem(){
        perform.processShow(0);
        perform.processShow(1);
        perform.processShow(1);
        perform.processShow(2);
        perform.processShow(2);
        perform.processShow(3);

        Assert.assertEquals(1,(int)wcct.getCounter(0));
        Assert.assertEquals(2,(int)wcct.getCounter(1));
        Assert.assertEquals(2,(int)wcct.getCounter(2));
        Assert.assertEquals(1,(int)wcct.getCounter(3));
    }

    @Test
    public void testPlayAd(){
//        perform.playAd();
    }
}
