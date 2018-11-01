package com.eTeng.autowired.ambiguity.java;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QualifierJavaConfig.class)
public class QualifierJavaConfigTest{


    @Test
    public void testQuanlifier(){

    }
}