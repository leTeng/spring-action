package com.eTeng.advice;

import com.eTeng.point.impl.MagicPerform;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @FileName PerformAdvice.java
 * @Author 梁怡腾
 * @Date 2018/11/2 8:59
 * @Description 表演通知类(切面)
 */

@Aspect //定义为一个切面
public class PerformAdvice{

    private static final Logger LOGGER = LoggerFactory
            .getLogger(MagicPerform.class);

    //定义一个公共切点
    @Pointcut("execution(* com.eTeng.point.impl.MagicPerform.processShow(..))")
    public void performShow(){

    }

    //观众入场(调用之前)
    @Before("performShow()")
    public void admission(){
        LOGGER.info("audience admission success");
    }

    //鼓掌(返回之后)
    @AfterReturning("performShow()")
    public void applause(){
        LOGGER.info("performance was wonderful");
    }

    //退票(出异常)
    @AfterThrowing("performShow()")
    public void refundTicket(){
        LOGGER.info("performance was terrible");
    }

    //观众退场(返回之后或出异常)
    @After("performShow()")
    public void retreat(){
        LOGGER.info("audience retreat success");
    }
}
