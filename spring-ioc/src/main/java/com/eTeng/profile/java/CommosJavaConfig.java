package com.eTeng.profile.java;

import com.eTeng.profile.bean.ProfileBean;
import com.eTeng.profile.bean.impl.CommosBean;
import org.springframework.context.annotation.Bean;

/**
 * 没有@Profile声明的Bean讲自动创建
 */
public class CommosJavaConfig {

    @Bean
    public CommosBean commosBean(){
        return new CommosBean();
    }
}
