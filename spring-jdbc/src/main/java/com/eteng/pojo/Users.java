package com.eteng.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * @FileName Users.java
 * @Author eTeng
 * @Date 2018/12/24 18:54
 * @Description 用户实体结构
 */
@Entity
@Table(name = "users")
public class Users{

    @Id
    @GeneratedValue(generator = "users_id")
    @GenericGenerator(name = "users_id",strategy = "uuid.hex")
    private String id;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="users",columnDefinition = "CHAR(36)")
    private List<OmOrder> orders;

    @Column(name = "user_name",length = 15)
    private String username;

    @Column(name = "pwd",length = 20)
    private String password;

    @Column(name = "enable",columnDefinition = "TINYINT")
    private boolean enable;

    public Users(){
    }

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

    public List<OmOrder> getOrders(){
        return orders;
    }

    public void setOrders(List<OmOrder> orders){
        this.orders = orders;
    }

    public String getId(){

        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    @Override
    public int hashCode(){
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException{
        return super.clone();
    }

    @Override
    public String toString(){
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable{
        super.finalize();
    }
}
