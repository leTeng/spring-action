package com.eTeng.java;

import com.eTeng.advice.PerformAdvice;
import com.eTeng.point.impl.MagicPerform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @FileName AspectJavaConfig.java
 * @Author 梁怡腾
 * @Date 2018/11/2 9:44
 * @Description aop配置类
 */

@Configuration
@EnableAspectJAutoProxy //设置aop自动代理
@ComponentScan(basePackageClasses = MagicPerform.class)
public class AspectJavaConfig{

    @Bean
    public PerformAdvice performAdvice(){
        return new PerformAdvice();
    }
}
