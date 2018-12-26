package com.eTeng.context.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * @FileName WebSecurityConfig.java
 * @Author eTeng
 * @Date 2018/12/25 18:01
 * @Description 该方式能代替web.xml配置方式配置Servlet上下文。
 *  原理：
 *      1.在Servlet3.0规范中,会在类路径下扫描实现了ServletContainerInitializer接口的类作为容器的初始化上下文。
 *      2.SpringServletContainerInitializer是Spring提供的ServletContainerInitializer实现类。SpringServletContainerInitializer
 *        反过来查找类路径的WebApplicationInitializer实现类。Spring为多个模块提供WebApplicationInitializer实现类进行自定义的初始化
 *        其中AbstractSecurityWebApplicationInitializer是Spring提供的WebApplicationInitializer实现。用于Spring Security模块
 *        在Servlet上下文启动时注册DelegatingFilterProxy过滤器。
 *
 */
public class WebSecurityConfig extends AbstractSecurityWebApplicationInitializer{

    /*
     * 1.可以重写insertFilter()和addFilter()来注册其他的Filter。
     * 2.除了在Servlet容器注册DelegatingFilterProxy之外。还需要一个委托过滤器(bean)。由DelegatingFilterProxy
     *   委托过滤器,委托的过滤器进行具体的逻辑。该过滤器由Spring提供。
     */
}
