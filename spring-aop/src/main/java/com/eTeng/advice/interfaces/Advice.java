package com.eTeng.advice.interfaces;

/**
 * @FileName Advice.java
 * @Author eTeng
 * @Date 2018/12/10 16:02
 * @Description
 */
public interface Advice{

    /**
     * 接通电源(调用之前)
     */
    void noElectricity();

    /**
     *打开电视(返回之后)
     */
    void openTv();

    /**
     * 不能显示(抛出异常之后)
     */
    void fix();

    /**
     * 关闭电视(返回之后)
     */
    void closeTv();

    /**
     * 断开电源
     */
    void offElectricity();
}
