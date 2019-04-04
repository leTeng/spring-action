package com.eTeng.advice;

import com.eTeng.advice.interfaces.Advice;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @FileName PerformAdvice2.java
 * @Author eTeng
 * @Date 2018/12/10 15:36
 * @Description 环绕通知
 */

//@Aspect //声明切面
public class PerformAdvice2{

    @Autowired
    private Advice performAdvice;

    @Pointcut("execution(* com.eTeng.point.interfaces.Perform.processShow())")
    private void processShow(){}

    /**
     * 声明环绕通知
     * @param jp
     */
    @Around("processShow()")
    public void watchPerform(ProceedingJoinPoint jp){
        try{
            performAdvice.noElectricity();
            performAdvice.openTv();
            jp.proceed(); //调用被通知方法
//            performAdvice.closeTv();
            performAdvice.offElectricity();
        }catch(Throwable ex){
            performAdvice.fix();
        }
    }
}
