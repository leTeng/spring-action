package com.eteng.pojo;

import java.io.Serializable;

/**
 * @FileName Product.java
 * @Author eTeng
 * @Date 2019/4/18 16:05
 * @Description
 */
public class Product implements Serializable{

    private static final long serialVersionUID = 4948303023978559624L;

    private String id;
    private String name;
    private String code;
    private String price;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getPrice(){
        return price;
    }

    public void setPrice(String price){
        this.price = price;
    }
}
