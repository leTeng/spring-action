package com.eTeng.point.impl;

import com.eTeng.exception.OverstepShowItem;
import com.eTeng.point.interfaces.Perform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * @FileName MagicPerform.java
 * @Author eTeng
 * @Date 2018/11/1 16:07
 * @Description 电视机
 */
//@Component
public class Television implements Perform{

    private static final Logger LOGGER = LoggerFactory
            .getLogger(Television.class);

    //节目单
    private List<String> items;

    public void processShow(){
        LOGGER.info(items.get(new Random().nextInt(items.size())) + "show");
//        throw new RuntimeException();
    }

    public void processShow(int itemNum){
        if(itemNum > items.size() - 1){
            throw new OverstepShowItem(itemNum+"");
        }
        LOGGER.info(items.get(itemNum) + "show");
//        throw new RuntimeException();
    }

    public List<String> getItems(){
        return items;
    }

    public void setItems(List<String> items){
        this.items = items;
    }
}
