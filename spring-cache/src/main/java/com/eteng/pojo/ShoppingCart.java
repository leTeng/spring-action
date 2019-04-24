package com.eteng.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @FileName ShoppingCart.java
 * @Author eTeng
 * @Date 2019/4/18 16:04
 * @Description
 */
public class ShoppingCart implements Serializable{

    private static final long serialVersionUID = -3427603567286636197L;

    private String id;
    private float totalPrice;
    private List<Product> products;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public float getTotalPrice(){
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice){
        this.totalPrice = totalPrice;
    }

    public List<Product> getProducts(){
        return products;
    }

    public void setProducts(List<Product> products){
        this.products = products;
    }
}
