package com.eTeng.point.impl;

import com.eTeng.point.interfaces.Perform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @FileName MagicPerform.java
 * @Author 梁怡腾
 * @Date 2018/11/1 16:07
 * @Description 魔术表演
 */
@Component
public class MagicPerform implements Perform{

    private static final Logger LOGGER = LoggerFactory
            .getLogger(MagicPerform.class);

    
    public void processShow(){
        LOGGER.info("process magic show");
//        throw new RuntimeException();
    }
}
