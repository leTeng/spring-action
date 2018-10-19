package com.eTeng.mode.bean.impl;

import com.eTeng.mode.bean.interfaces.Knight;
import com.eTeng.mode.bean.interfaces.Quest;
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

//    @Autowired
    private Quest quest;
    private String knightName;

    public BarveKnight(){
    }

    public BarveKnight(Quest quest,String knightName){
        this.quest = quest;
        this.knightName = knightName;
    }

    public Quest getQuest(){
        return quest;
    }

    public void setQuest(Quest quest){
        this.quest = quest;
    }

    public String getKnightName(){
        return knightName;
    }

    public void setKnightName(String knightName){
        this.knightName = knightName;
    }


    public void embarkOnQuest(){
        System.out.println(knightName);
        quest.embark();
    }
}
