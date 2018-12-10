package com.eTeng.autowired.ambiguity.bean.impl;

import com.eTeng.autowired.ambiguity.bean.interfaces.Animal;

/**
 * @FileName Bird.java
 * @Author eTeng
 * @Date 2018/10/30 16:22
 * @Description
 */
public class Bird implements Animal{
    public int getLegCount(){
        return 2;
    }
}
