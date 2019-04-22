package com.eteng.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import javax.sql.DataSource;

/**
 * JPA(Java Persistence Api) java持久化接口的规范,其中Hibernate是JPA中的
 * 一个实现(还有其他的框架实现)。Spring给JPA提供了封装,包括模板类、由Spring的
 * 上下文管理实体管理器等。Spring Data JPA 是对Spring Data 对 JPA规范的简化。
 * @FileName SpringJpaConfig.java
 * @Author eTeng
 * @Date 2019/4/16 16:19
 * @Description
 */
public class SpringJpaConfig{

    /**
     * 配置应用程序类型实体管理器工厂(保证在应用程序下有持久化单元的配置persistence.xml)
     * @return
     */
//    @Bean
    public LocalEntityManagerFactoryBean localEntityManagerFactoryBean(){
        LocalEntityManagerFactoryBean entityManagerFactory = new LocalEntityManagerFactoryBean();
        // 配置实体管理器的持久化单元,每个持久化单元持有数据源和实体映射
        entityManagerFactory.setPersistenceUnitName("test_unit");
        return entityManagerFactory;
    }

    /**
     * 配置容器类型实体管理器工厂
     * @return
     */
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(DataSource dataSource,JpaVendorAdapter jpaVendorAdapter){
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactory.setPackagesToScan("com.eteng.pojo");
        return entityManagerFactory;
    }

    /**
     * 配置JPA与Hibernate的适配器
     * @return
     */
    @Bean
    public JpaVendorAdapter jpaVendorAdapter(){
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.MYSQL);
        adapter.setGenerateDdl(true);
        adapter.setShowSql(true);
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
        return adapter;
    }
}
