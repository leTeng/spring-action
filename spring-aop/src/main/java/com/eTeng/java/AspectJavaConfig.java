package com.eTeng.java;

import com.eTeng.advice.PerformAdvice;
import com.eTeng.advice.PerformAdvice2;
import com.eTeng.advice.WatchPerformCounterAdvice;
import com.eTeng.advice.interfaces.Advice;
import com.eTeng.introduce.PlayAdIntroduce;
import com.eTeng.point.impl.Television;
import com.eTeng.point.interfaces.Perform;
import com.eTeng.pojo.DeFaultPlayAd;
import org.springframework.context.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @FileName AspectJavaConfig.java
 * @Author 梁怡腾
 * @Date 2018/11/2 9:44
 * @Description aop配置类
 */

@Configuration
@EnableAspectJAutoProxy //设置aop自动代理
@ComponentScan(basePackageClasses = Television.class)
public class AspectJavaConfig{

    @Bean
    @Primary
    public Advice performAdvice(){
        return new PerformAdvice();
    }

    @Bean
    public PerformAdvice2 performAdvice2(){
        return new PerformAdvice2();
    }

    @Bean
    public WatchPerformCounterAdvice watchPerformCounterAdvice(){
        return new WatchPerformCounterAdvice();
    }

    @Bean
    public PlayAdIntroduce playAdIntroduce(){
        return new PlayAdIntroduce();
    }

    @Bean
    public DeFaultPlayAd deFaultPlayAd(){
        return new DeFaultPlayAd();
    }

    @Bean
    public Perform perform(){
        List<String> items = new ArrayList<String>();
        items.add("Magic");
        items.add("Sing");
        items.add("Dance");
        items.add("Piano");
        Television tv = new Television();
        tv.setItems(items);
        return tv;
    }

}
