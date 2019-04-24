package com.eteng.redis;

import com.eteng.pojo.Product;
import com.eteng.pojo.ShoppingCart;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 通过声明式缓存
 * @FileName ShoppingCartAutoCacheRepository.java
 * @Author eTeng
 * @Date 2019/4/19 17:43
 * @Description
 */
@Repository
public class ShoppingCartAutoCacheRepository{

    /**
     * Cacheable的使用
     * 使用redis的缓存管理器作为缓存的实现,使用参数的值作为缓存key,并且通过条件判断是否禁用缓存。
     * @param id
     * @return
     */
    @Cacheable(value = "redisCacheManager",condition = "!(#root.args[0].empty)")
    public ShoppingCart cacheable(String id){
        ShoppingCart cart = new ShoppingCart();
        cart.setId(id);
        cart.setProducts(new ArrayList<Product>());
        cart.setTotalPrice(15);
        return cart;
    }

    /**
     * cacheaPut的使用
     * 使用redis的缓存管理器作为缓存的实现,自定义key,并且通过条件判断是否禁用缓存。
     * @param cart
     * @return
     */
    @CachePut(value = "redisCacheManager",key = "#result.id",condition = "#root.args[0] != null ")
    public ShoppingCart cachePut(ShoppingCart cart){
        cart.setId(UUID.randomUUID().toString());
        return cart;
    }

    /**
     * cacheaEvict的使用
     * 使用redis的缓存管理器作为缓存的实现,自定义key,并且通过条件判断是否禁用缓存。
     * @param cart
     * @return
     */
    @CacheEvict(value = "redisCacheManager",key = "#root.args[0].id",condition = "#root.args[0] != null ")
    public boolean cacheEvict(ShoppingCart cart){
        return true;
    }
}
