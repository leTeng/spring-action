package com.eTeng.mode.java.config.impl;

import com.eTeng.mode.java.config.interfaces.Quest;
import org.springframework.stereotype.Component;

/**
 * @FileName RescueDemseQuest.java
 * @Author 梁怡腾
 * @Date 2018/9/21
 * @Description
 */
@Component("RescueDemse1")
public class RescueDemseQuest implements Quest{

    public void embark(){
        System.out.print("embark success");
    }
}
