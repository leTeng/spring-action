package com.eTeng.storage.custom.pojo;

/**
 * @FileName Users.java
 * @Author eTeng
 * @Date 2018/12/24 18:54
 * @Description 用户实体结构
 */
public class Users{

    private String username;
    private String password;
    private boolean enable;

    public Users(String username,String password,boolean enable){
        this.username = username;
        this.password = password;
        this.enable = enable;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public boolean isEnable(){
        return enable;
    }

    public void setEnable(boolean enable){
        this.enable = enable;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }
}
