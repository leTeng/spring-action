package com.eteng.jpa;

import com.eteng.pojo.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 基于Spring Data项目自动生成JpaRepository接口的实现。JpaRepository提供了18基本的
 * 操作数据库方法。用户也可以根据Spring Data定义的特定语言(DSL)自定义一个查询。该语言用
 * 于接口的方法签名上,Spring Data为该接口产生代理实现.如果方法签名的方式无法满足查询，可
 * 以使用@Query注解自定义JPQL查询。当使用方法签名和自定义SQL无法满足时,使用混合方式。即
 * JpaRepository代理类和手动的实现类混合使用。
 * @FileName SpringDataJpaUserRepository.java
 * @Author eTeng
 * @Date 2019/4/17 10:07
 * @Description
 */
public interface SpringDataJpaUserRepository extends JpaRepository<Users,Long>,JpaUsersExtRepository{

    /**
     * 使用方法签名定义查询,根据继承接口的参数化类型推断返回值。所以方法名可以省略返回值,
     * 查询动词包括get、find、rend、count,get、find、rend是同义词,功能都是相似的。
     * count动词0是统计结果的个数。在By后面是断言(查询条件),多个条件使用AND或者OR相连，
     * 在字段后面可以使用操作符,默认操作符是“=”,例如username like操作的方法名断言为
     * getByUsernameLike(String username)。
     * @param username
     * @return
     */
    @Transactional
    List<Users> getByUsernameIsLike(String username);

    @Transactional
    Users getByEnableAndUsername(boolean enable,String username);

    /**
     * 使用@Query注解自定义的SQL代替方法签名方式。该方式解决了使用方法签名无法满足查询和
     * 使用方法签名定义时方法名过长的情况。自定义的SQL可以是JPQL或者SQL，通过设置nativeQuery
     * 属性控制。如果使用参数名的方式填充参数，需要在方法签名的形参上通过@Param注解的value指定
     * 对应的参数。
     * @param enable
     * @param username
     * @return
     */
    @Transactional
    @Query("SELECT u from Users u where u.enable =:enable and u.username =:username")
    Users getUsers(@Param("enable")boolean enable,@Param("username") String username);

    @Transactional
    @Query(nativeQuery = true,value = "SELECT * from users u where u.enable =:enable and u.user_name =:username")
    Users getUser(@Param("enable")boolean enable,@Param("username") String username);
}
