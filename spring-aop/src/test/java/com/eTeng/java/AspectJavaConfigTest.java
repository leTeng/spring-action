package com.eTeng.java;

import com.eTeng.advice.WatchPerformCounterAdvice;
import com.eTeng.point.interfaces.Perform;
import org.junit.Assert;
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

    @Autowired
    WatchPerformCounterAdvice wcct;

    /*
     * 测试基本通知
     */
    @Test
    public void testRandomPerform(){
        perform.processShow();
    }

    /*
     * 测试环绕通知
     */
    @Test
    public void testRandomPerform2(){
        perform.processShow();
    }

    /*
     * 测试通知访被通知方法的参数
     */
    @Test
    public void testSpecifyItem(){
        perform.processShow(0);
        perform.processShow(1);
        perform.processShow(1);
        perform.processShow(2);
        perform.processShow(2);
        perform.processShow(2);

        Assert.assertEquals(1,(int)wcct.getCounter(0));
        Assert.assertEquals(2,(int)wcct.getCounter(1));
        Assert.assertEquals(3,(int)wcct.getCounter(2));
        Assert.assertEquals(0,(int)wcct.getCounter(3));
    }

    @Test
    public void testPlayAd(){
//        perform.palyAd();
    }
}