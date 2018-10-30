package com.eTeng.autowired.ambiguity.bean.impl;

import com.eTeng.autowired.ambiguity.bean.interfaces.Animal;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @FileName Cat.java
 * @Author 梁怡腾
 * @Date 2018/10/30 16:20
 * @Description
 */
@Component
//@Primary //Primary也可以和@Compoent组合使用
public class Cat implements Animal{

    public int getLegCount(){
        return 4;
    }
}
