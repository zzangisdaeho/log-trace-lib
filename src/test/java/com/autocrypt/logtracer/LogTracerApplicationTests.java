package com.autocrypt.logtracer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootTest
class LogTracerApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {

    }

    @Test
    void checkBeanRead(){
        String[] beanDefinitionNames = context.getBeanDefinitionNames();


        Arrays.stream(beanDefinitionNames).forEach(System.out::println);

        // 'logTraceConfig'라는 이름의 빈이 등록되어 있는지 확인
        boolean isLogTraceConfigPresent = Arrays.asList(beanDefinitionNames).contains("logTraceConfig");

        Assertions.assertTrue(isLogTraceConfigPresent);

        // 'logTraceAutoConfig'라는 이름의 빈이 등록되어 있는지 확인
        boolean isLogTraceAutoConfigPresent = Arrays.asList(beanDefinitionNames).contains("com.autocrypt.logtracer.trace.config.LogTraceAutoConfig");
        Assertions.assertTrue(isLogTraceAutoConfigPresent);
    }

}
