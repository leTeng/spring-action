package com.eTeng.advice;

import com.eTeng.point.impl.Television;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @FileName WatchPerformAdvice.java
 * @Author eTeng
 * @Date 2018/12/10 17:09
 * @Description 记录观看各个频道观众人数
 */
//@Aspect
public class WatchPerformCounterAdvice{

    private static final Logger LOGGER = LoggerFactory
            .getLogger(Television.class);

    private Map<Integer,Integer> counter = new HashMap<Integer,Integer>();

    //选择参数为int类型的连接点,并且将参数传递给通知方法
    @Pointcut("execution(* com.eTeng.point.interfaces.Perform.processShow(int)) " +
            "&& args(itemNum)")
    public void showWithItem(int itemNum){}


    @Before("showWithItem(itemNum)")
    public void recordCounter(int itemNum){
        LOGGER.info("record itemNum:" + itemNum);
        Integer count = getCounter(itemNum);
        counter.put(itemNum,count+1);
    }

    public Integer getCounter(int itemNum){
        return counter.containsKey(itemNum) ? counter.get(itemNum) : 0;
    }
}
