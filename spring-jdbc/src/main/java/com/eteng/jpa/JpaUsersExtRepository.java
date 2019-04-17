package com.eteng.jpa;

import com.eteng.pojo.Users;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用于混合JpaRepository查询。当方法签名和自定义SQL无法满足数据库访问时，使用实现类
 * 和代理类的方式混合使用。
 * @FileName JpaUsersExtRepository.java
 * @Author eTeng
 * @Date 2019/4/17 11:19
 * @Description
 */
public interface JpaUsersExtRepository{
    @Transactional
    List<Users> al();
}
