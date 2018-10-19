package com.eTeng.mode.java.config.impl;

import com.eTeng.mode.java.config.interfaces.Quest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class) //SpringJUnit4ClassRunner是spring对Junit运行环境的自定义扩展，用来标准化在 Spring 环境中 Junit4.5 的测试用例
@ContextConfiguration(classes = BarveKnightConfig.class) //加载spring的上下文。可以是java配置的上下文。也可以是xml配置上下文。
//@Transactional //测试时对数据库是事物的操作,程序结束前数据库事物回滚
public class BraveKnightTest{

    @Autowired
    BarveKnight barveKnight;

    @Autowired
    Quest quest;

    @Test
    public void testEmbark(){
        barveKnight.embarkOnQuest();
    }
}
