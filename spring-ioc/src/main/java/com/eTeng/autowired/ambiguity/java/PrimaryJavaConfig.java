package com.eTeng.autowired.ambiguity.java;

import com.eTeng.autowired.ambiguity.bean.impl.Bird;
import com.eTeng.autowired.ambiguity.bean.impl.Cat;
import com.eTeng.autowired.ambiguity.bean.impl.ConponentMark;
import com.eTeng.autowired.ambiguity.bean.impl.Dog;
import com.eTeng.autowired.ambiguity.bean.interfaces.Animal;
import org.springframework.context.annotation.*;

/**
 * @FileName AmbiguityJavaConfig.java
 * @Author 梁怡腾
 * @Date 2018/10/30 16:23
 * @Description 自动转配歧义配置(有多个相同类型的bean)
 */
@Configuration
@ImportResource(locations = "classpath:autowirdAmbiguityPrimary.xml")
//@ComponentScan(basePackageClasses = ConponentMark.class)
public class PrimaryJavaConfig{

   /* @Bean
//    @Primary //设置为自动装配首先bean标识。
    public Animal dog(){
        return new Dog();
    }

    @Bean
    @Primary //设置为自动装配首先bean。
    public Animal bird(){
        return new Bird();
    }*/
}
