package com.eTeng.mode.java.config.impl;

import com.eTeng.mode.java.config.interfaces.Knight;
import com.eTeng.mode.java.config.interfaces.Quest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @FileName BraveKnight.java
 * @Author 梁怡腾
 * @Date 2018/9/21
 * @Description
 */
@Component("braveKnight1")
public class BarveKnight implements Knight{

    @Autowired
    private Quest quest;

    public void embarkOnQuest(){
        quest.embark();
    }
}
