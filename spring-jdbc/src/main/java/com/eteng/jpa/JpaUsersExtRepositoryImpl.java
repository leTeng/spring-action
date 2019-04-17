package com.eteng.jpa;

import com.eteng.pojo.Users;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import java.util.List;

/**
 * @FileName SpringDataJpaUserReposirotyImpl.java
 * @Author eTeng
 * @Date 2019/4/17 11:29
 * @Description
 */
@Repository
public class JpaUsersExtRepositoryImpl implements JpaUsersExtRepository{
    
    @PersistenceContext
    private EntityManager entityManager;

    public List<Users> al(){
        Query query = entityManager.createQuery("select u from Users u");
        return query.getResultList();
    }
}
