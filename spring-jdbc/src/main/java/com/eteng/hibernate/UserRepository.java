package com.eteng.hibernate;

import com.eteng.pojo.OmOrder;
import com.eteng.pojo.Users;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 使用Hibernate操作数据库,使用@Repository声明为一个组件还有一个其他的用处。
 * 如果不使用HibernateTemplate来访问数据库,直接使用Hibernate的API来访问数
 * 据库时。Spring为持久层操作统一的异常无法进行转换。解决方法是定义一个Persistence-
 * ExceptionTranslationPostProcessor的Bean,它是一个给声明了@Repository注解的
 * 类添加一个后置通知,用于转换SQL的异常为Spring统一的异常。
 * @FileName UserRepository.java
 * @Author eTeng
 * @Date 2019/4/16 14:22
 * @Description
 */
@Repository
public class UserRepository{

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Transactional()
    public String add(){
        Users users  = new Users("zhangsan","123",true);
        hibernateTemplate.save(users);
        return users.getId();
    }

    @Transactional
    public String cascadeAdd(){
        Users users  = new Users("zhangsan","123",true);
        List<OmOrder> orders = new ArrayList<OmOrder>();
        OmOrder order = new OmOrder();
        order.setCreateDate(new Date());
        order.setOrderNum("123");
        order.setTotalPrice(12.1F);
        orders.add(order);
        users.setOrders(orders);
        hibernateTemplate.save(users);
        hibernateTemplate.flush();
        return users.getId();
    }

    @Transactional
    public Users fetch(){
        Session currentSession = sessionFactory.getCurrentSession();
//        Users users  = (Users)currentSession.get(Users.class,"4028801b6a24de12016a24de7cb90000");
        Criteria criteria = currentSession.createCriteria(Users.class);
//        criteria.add()
        criteria.setFirstResult(0);
        criteria.setMaxResults(1);
        Users users = (Users)criteria.uniqueResult();
        if(Objects.nonNull(users)){
            List<OmOrder> orders = users.getOrders();
            for(OmOrder order : orders){
                System.out.println(order.getOrderNum());
            }
        }
        return users;
    }

    @Transactional
    public OmOrder reverseFetch(){
        OmOrder order = hibernateTemplate.get(OmOrder.class,"4028801b6a24de12016a24de7e820001");
        if(Objects.nonNull(order)){
            Users users = order.getUsers();
            System.out.println(users.getId());
        }
        return order;
    }
}
