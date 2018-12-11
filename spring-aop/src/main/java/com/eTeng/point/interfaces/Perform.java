package com.eTeng.point.interfaces;

/**
 * @FileName Perform.java
 * @Author eTeng
 * @Date 2018/11/1 16:07
 * @Description
 */
public interface Perform{

    /**
     * 随机频道表演
     */
    void processShow();

    /**
     * 指定频道的表演
     * @param itemNum
     */
    void processShow(int itemNum);
}
