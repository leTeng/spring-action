package com.eTeng.LogbackTest;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @FileName logback.java
 * @Author 梁怡腾
 * @Date 2018/11/1 17:51
 * @Description logback 测试
 */
public class LogbackTest{

    private static Logger LOGGER;

    @Before
    public void setUp(){
        LOGGER = LoggerFactory.getLogger(LogbackTest.class);
    }

    @Test
    public void testLog(){
        LOGGER.trace("log for TRACE");
        LOGGER.debug("log for DEBUF");
        LOGGER.info("log for INFO");
        LOGGER.warn("log for WARN");
        LOGGER.error("log for ERROR");
    }
}
