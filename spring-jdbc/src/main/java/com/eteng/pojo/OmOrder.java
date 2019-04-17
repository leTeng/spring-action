package com.eteng.pojo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @FileName Order.java
 * @Author eTeng
 * @Date 2019/4/16 10:10
 * @Description
 */
@Entity
@Table(name = "om_order")
public class OmOrder{

    @Id
    @GeneratedValue(generator = "order_id")
    @GenericGenerator(name = "order_id",strategy = "uuid")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "users")
    private Users users;

    @Column(name = "order_num",length = 20)
    private String orderNum;

    @Column(name = "total_price")
    private float totalPrice;

    @Column(name = "create_date")
    private Date createDate;

    public OmOrder(){

    }

    public OmOrder(String id,String orderNum){
        this.id = id;
        this.orderNum = orderNum;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public Users getUsers(){
        return users;
    }

    public void setUsers(Users users){
        this.users = users;
    }

    public String getOrderNum(){
        return orderNum;
    }

    public void setOrderNum(String orderNum){
        this.orderNum = orderNum;
    }

    public float getTotalPrice(){
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice){
        this.totalPrice = totalPrice;
    }

    public Date getCreateDate(){
        return createDate;
    }

    public void setCreateDate(Date createDate){
        this.createDate = createDate;
    }

    @Override
    public String toString(){
        return "OmOrder{" +
                "id='" + id + '\'' +
                ", users=" + users +
                ", orderNum='" + orderNum + '\'' +
                ", totalPrice=" + totalPrice +
                ", createDate=" + createDate +
                '}';
    }
}
