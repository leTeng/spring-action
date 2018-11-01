package com.eTeng.mode.java.config;

import com.eTeng.mode.bean.impl.BarveKnight;
import com.eTeng.mode.bean.impl.RescueDemseQuest;
import com.eTeng.mode.bean.interfaces.Quest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * @FileName BraveKnightConfig.java
 * @Author 梁怡腾
 * @Date 2018/9/21
 * @Description 作为组件扫描基础包的标记。如果没有属性值。
 *              默认以当前类路径作为基础包扫描。配置扫描
 *              除了java配置之外还可以使用xml文件配置。
 *              <context:compent-sacn base-package=""/>
 */

//@ComponentScan
//@ComponentScan("com.eTeng")
@Configuration
//@Import(BarveKnightConfig.class) //当一个配置类过于膨大,
   // 使用多个JavaConfig配置,然后使用@import注解整合成一个JavaConfig
//@ImportResource("classpath:/applicationContext.xml") //改注解将xml配置文件bean导入到JavaConfig中。达成混合配置
public class BarveKnightConfig{

    /**
     * 使用javaConfig方式创建,使用@Bean声明。方法名为bean的id。
     * 方法实现是创建实例的细节。如果有依赖可以调用创建依赖实例的方法，
     * 如果依赖是由其他方式创建(非JavaConfig)，可在方法参数上声明。
     * 然后依赖。
     *
     * @return
     */
  /*  @Bean
    public BarveKnight barveKnight(Quest quest){
//      Quest quest = rescueDemseQuest();
//        return new BarveKnight(quest);
        return new BarveKnight(quest);
    }*/

    @Bean
    public Quest rescueDemseQuest(){
        return new RescueDemseQuest();
    }
}




