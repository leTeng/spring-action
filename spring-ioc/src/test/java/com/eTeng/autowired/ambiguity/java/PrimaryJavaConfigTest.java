package com.eTeng.autowired.ambiguity.java;

import com.eTeng.autowired.ambiguity.bean.interfaces.Animal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PrimaryJavaConfig.class)
public class PrimaryJavaConfigTest{

    @Autowired
    Animal animal;

    @Test
    public void tsetPrimary(){
        System.out.println(animal.getLegCount());
    }
}