package com.eTeng.condition.java;

import com.eTeng.condition.bean.impl.PropertiesResourceBean;
import com.eTeng.condition.bean.impl.ResourceExistCondition;
import com.eTeng.condition.bean.interfaces.ResourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @FileName ResourceBeanConfig.java
 * @Author 梁怡腾
 * @Date 2018/10/30 14:46
 * @Description
 */
@Configuration
public class ResourceBeanConfig{

    /*
     * 注意：条件化创建bean,只支持JavaConfig
     */

    @Bean
    //根据条件创建bean
    @Conditional(ResourceExistCondition.class)
    public ResourceBean propertiesResourceBean(){
        return new PropertiesResourceBean();
    }
}
