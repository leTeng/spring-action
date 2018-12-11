package com.eTeng.advice;

import com.eTeng.advice.interfaces.Advice;
import com.eTeng.point.impl.Television;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @FileName PerformAdvice.java
 * @Author eTeng
 * @Date 2018/11/2 8:59
 * @Description 表演通知类(切面)
 */

//@Aspect //定义为一个切面
public class PerformAdvice implements Advice{

    private static final Logger LOGGER = LoggerFactory
            .getLogger(Television.class);

    //定义一个公共切点
    @Pointcut("execution(* com.eTeng.point.interfaces.Perform.processShow())")
    public void performShow(){

    }

    //连接电源(调用之前)
    @Before("performShow()")
    public void noElectricity(){
        LOGGER.info("turn on electricity success");
    }

    //连接电源(调用之前)
    @Before("performShow()")
    public void openTv(){
        LOGGER.info("openTv success");
    }

    //关闭(返回之后)
    @AfterReturning("performShow()")
    public void closeTv(){
        LOGGER.info("close success");
    }

    //修复(出异常)
    @AfterThrowing("performShow()")
    public void fix(){
        LOGGER.info("proceed fix");
    }

    //断开电源(返回之后或出异常)
    @After("performShow()")
    public void offElectricity(){
        LOGGER.info("deenergization success");
    }
}
