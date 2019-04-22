package com.eteng.hibernate;

import com.eteng.pojo.Users;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Spring 提供的Hibernate支持
 * Hibernate 是一个ORM框架,提供数据库和pojo的映射。简化了原始的JDBC访问数据库。
 * 并且扩展JDBC的功能,包括缓存、延迟加载、预先抓取、级联等功能。将构建sql抽象化,自动生
 * 成SQL,使得访问数据库更专注业务而非SQL的语法。
 * Spring 对ORM框架提供一下支持：
 *   -- 支持声明式事务
 *   -- 统一的异常处理
 *   -- 线程安全、轻量的模板类
 *   -- DAO支持类
 * @FileName HibernateSpringConfig.java
 * @Author eTeng
 * @Date 2019/4/8 11:59
 * @Description
 */
public class HibernateSpringConfig{

    /**
     * Spring提供Hibernate的支持。适用于Hibernatte3版本,只支持基于XML配置POJO构建SessionFactory。
     * @return
     */
//    @Bean
   /* public org.springframework.orm.hibernate3.LocalSessionFactoryBean localSessionFactoryBean3(DataSource druidDataSource){
        org.springframework.orm.hibernate3.LocalSessionFactoryBean sessionFactory = new org.springframework.orm.hibernate3.LocalSessionFactoryBean();
        sessionFactory.setDataSource(druidDataSource);
        sessionFactory.setMappingResources("users.hbm.xml");
        Properties prop = new Properties();
        sessionFactory.setHibernateProperties(prop);
        return sessionFactory;
    }*/

    /**
     * Spring提供Hibernate的支持。适用于Hibernatte3版本,只支持基于注解配置POJO构建SessionFactory。
     * @return
     */
 /*   public AnnotationSessionFactoryBean annotationSessionFactoryBean(DataSource druidDataSource){
        AnnotationSessionFactoryBean sessionFactory = new AnnotationSessionFactoryBean();
        sessionFactory.setAnnotatedClasses(Users.class);
        Properties prop = new Properties();
        sessionFactory.setHibernateProperties(prop);
        return sessionFactory;
    }*/

    /**
     * Spring提供Hibernate的支持。适用于Hibernatte4以上版本,支持基于XML配置或者注解配置POJO构建SessionFactory。
     * @param druidDataSource 数据源
     * @return
     */
    @Bean
    public LocalSessionFactoryBean localSessionFactoryBean(DataSource druidDataSource){
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        // 配置数据源
        sessionFactory.setDataSource(druidDataSource);
        // 配置扫描实体
        sessionFactory.setPackagesToScan("com.eteng.pojo");
//        配置实体映射文件
//        sessionFactory.setMappingResources();
        // 配置hibernate
        Properties prop = new Properties();
        prop.setProperty("hibernate.dialect","org.hibernate.dialect.MySQLDialect");
        prop.setProperty("hibernate.show_sql","true");
        prop.setProperty("hibernate.hbm2ddl.auto","update");
        prop.setProperty("hibernate.current_session_context_class","org.springframework.orm.hibernate4.SpringSessionContext");
        prop.setProperty("hibernate.format_sql","true");
        sessionFactory.setHibernateProperties(prop);
        return sessionFactory;
    }

    /**
     * 配置HIbernate模板
     * @param sessionFactory
     * @return
     */
    @Bean
    public HibernateTemplate hibernateTemplate(SessionFactory sessionFactory){
        return new HibernateTemplate(sessionFactory);
    }

    /**
     * 配置事务管理器,使用注解驱动的方式开启事务,HibernateTransactionManager是Spring
     * 对Hibernate独立提供的事务管理器。如果未开启事务对数据进行写操作是不允许的。
     * @param sessionFactory
     * @return
     */
    @Bean
    public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory){
        return new HibernateTransactionManager(sessionFactory);
    }

    /**
     * 定义一个的给声明了@Repository注解的类添加异常转换通知的Bean
     * @return
     */
    @Bean
    public BeanPostProcessor persistenTranscation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
