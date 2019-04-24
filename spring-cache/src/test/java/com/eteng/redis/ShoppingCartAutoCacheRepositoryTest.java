package com.eteng.redis;

import com.eteng.ApplicationConfig;
import com.eteng.pojo.Product;
import com.eteng.pojo.ShoppingCart;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class) //SpringJUnit4ClassRunner是spring对Junit运行环境的自定义扩展，用来标准化在 Spring 环境中 Junit4.5 的测试用例
@ContextConfiguration(classes = ApplicationConfig.class)
public class ShoppingCartAutoCacheRepositoryTest{

    @Autowired
    private ShoppingCartAutoCacheRepository repository;

    @Test
    public void cacheable(){
        ShoppingCart cacheable = repository.cacheable("d99874bf-1b04-428f-9e4c-30f0b979409f");
    }

    @Test
    public void cachePut(){
        ShoppingCart cart = new ShoppingCart();
        cart.setProducts(new ArrayList<Product>());
        cart.setTotalPrice(10);
        ShoppingCart persistence = repository.cachePut(cart);
        System.out.println(persistence.getId());
    }

    @Test
    public void cacheEvict(){
        ShoppingCart cart = new ShoppingCart();
        cart.setId("c61bcf4d-ea62-4476-a337-c76699ff98c2");
        cart.setProducts(new ArrayList<Product>());
        cart.setTotalPrice(10);
        repository.cacheEvict(cart);
    }
}