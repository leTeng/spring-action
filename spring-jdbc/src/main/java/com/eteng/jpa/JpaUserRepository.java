package com.eteng.jpa;

import com.eteng.pojo.OmOrder;
import com.eteng.pojo.Users;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.*;

/**
 * @FileName JpaUserRepository.java
 * @Author eTeng
 * @Date 2019/4/16 16:52
 * @Description
 */
@Repository
public class JpaUserRepository{

    @PersistenceUnit
    private EntityManagerFactory entityFactory;

    @PersistenceContext
    private EntityManager entity;

    @Transactional
    public Users getUsersById(String uid){
        return entity.find(Users.class,uid);
    }
    
    @Transactional
    public Users getUserByName(String userName){
        Query query = entity.createQuery("select u from Users u WHERE u.username = :userName");
        query.setParameter("userName",userName);
        query.setFirstResult(0);
        query.setMaxResults(1);
        Users users = (Users)query.getSingleResult();
        return users;
    }

    /**
     * 查询已定义列返回数组结构,使用JPQL
     * @return
     */
    @Transactional
    public OmOrder getOrderByOrderNum(String orderNum){
        Query query = entity.createQuery("SELECT new com.eteng.pojo.OmOrder(omo.id,omo.orderNum) FROM OmOrder omo WHERE omo.orderNum = :orderNum");
        query.setParameter("orderNum",orderNum);
        query.setFirstResult(0);
        query.setMaxResults(1);
        OmOrder order = (OmOrder)query.getSingleResult();
        return order;
    }

    /**
     * 查询已定义列返回数组结构,使用SQL(这种方式只适用于Hibernate)
     * @return
     */
    @Transactional
    public OmOrder getOrderByOrderNumAndUsers(String orderNum,String uid){
        Query query = entity.createNativeQuery("SELECT omo.id,omo.order_num FROM om_order omo WHERE omo.order_num = :orderNum AND users = :users");
        query.setParameter("orderNum",orderNum);
        query.setParameter("users",uid);
        query.setFirstResult(0);
        query.setMaxResults(1);
        query.unwrap(SQLQuery.class)
                .addScalar("id",StandardBasicTypes.STRING)
                .addScalar("order_num",StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(Order.class));
        OmOrder order = (OmOrder)query.getSingleResult();
        return order;
    }
}
