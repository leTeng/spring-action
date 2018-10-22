package com.eTeng.profile.java;

import com.eTeng.profile.bean.ProfileBean;
import com.eTeng.profile.bean.impl.DevelopmentProfileBean;
import com.eTeng.profile.bean.impl.ProduceProfileBean;
import com.eTeng.profile.bean.impl.TestProfileBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

@Configuration
@Import(CommosJavaConfig.class)
//@Profile("dev") //当激活profile为dev则创建该bean
public class ProfileJavaConfig {

    /**
     * 激活为dev时创建
     * @return
     */
    @Bean
    @Profile("dev")  //类级别上声明@Profile,则当激活为该状态时，
//    才会创建下面方法的bean。还可以在方法上声明@Profile
    public ProfileBean developmentProfileBean(){
        return new DevelopmentProfileBean();
    }

    /**
     * 激活为test 时创建
     * @return
     */
    @Bean
    @Profile("test")
    public ProfileBean testProfileBean(){
        return new TestProfileBean();
    }

    /**
     * Profile 激活为produce时创建
     * @return
     */
    @Bean
    @Profile("produce")
    public ProfileBean produceProfileBean(){
        return new ProduceProfileBean();
    }
}
