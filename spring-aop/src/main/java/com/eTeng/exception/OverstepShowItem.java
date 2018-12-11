package com.eTeng.exception;

/**
 * @FileName OverstepShowItem.java
 * @Author eTeng
 * @Date 2018/12/10 17:18
 * @Description 超出表演菜单项异常
 */
public class OverstepShowItem extends RuntimeException{
    public OverstepShowItem(){
    }

    public OverstepShowItem(String message){
        super(message);
    }
}
