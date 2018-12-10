package com.eTeng.mode.bean.impl;

import com.eTeng.mode.bean.interfaces.Quest;
import org.springframework.stereotype.Component;

/**
 * @FileName RescueDemseQuest.java
 * @Author eTeng
 * @Date 2018/9/21
 * @Description
 */
@Component("RescueDemse1")
public class RescueDemseQuest implements Quest{

    public void embark(){
        System.out.print("embark success");
    }
}
