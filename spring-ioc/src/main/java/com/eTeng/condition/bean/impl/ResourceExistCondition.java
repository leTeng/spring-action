package com.eTeng.condition.bean.impl;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.io.InputStream;

/**
 * @FileName ResouceExistCondition.java
 * @Author eTeng
 * @Date 2018/10/30 14:13
 * @Description 条件化创建bean的实现
 */
public class ResourceExistCondition implements Condition{

    /*
     * 通过matches的返回值来判断是否创建bean
     */
    public boolean matches(ConditionContext conditionContext,
                           AnnotatedTypeMetadata annotatedTypeMetadata){
        /*
         * 两大参数的应用：
         *
         *      1.ConditionContext:
         *
         *          getRegistry(): 可以检查Bean的定义
         *          getResourceLoader():获取资源加载器
         *          getBeanFactory():可以检查Bean的存在和检查Bean属性
         *          getClassLoader():可以获取类加载器,判断类是否存在
         *          getEnvironment(): 可以获取环境变量
         *
         *
         *      2.AnnotatedTypeMetadata:
         *
         *          getAllAnnotationAttributes():
         *          getAnnotationAttributes():
         */

        //根据判断类路路径指定资源(flag.properties)的属性,是否存在创建bean
        Resource resource = conditionContext.getResourceLoader().getResource("flag.properties");
        if(resource.exists()){
            return true;
        }
        return false;
    }
}
