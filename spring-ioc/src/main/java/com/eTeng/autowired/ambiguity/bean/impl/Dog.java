package com.eTeng.autowired.ambiguity.bean.impl;

import com.eTeng.autowired.ambiguity.bean.interfaces.Animal;

/**
 * @FileName Dog.java
 * @Author 梁怡腾
 * @Date 2018/10/30 16:22
 * @Description
 */
public class Dog implements Animal{
    public int getLegCount(){
        return 4;
    }
}
