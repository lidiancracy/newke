package com.example.ld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class LdApplication {
    @PostConstruct
    public void init() {
        // 解决netty冲突问题
        // redis -> netty; es -> netty 当redis启动后es会检查netty，发现已设置则会不启动，因此报错
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(LdApplication.class, args);
//        String[] beanDefinitionNames = run.getBeanDefinitionNames();
//        for (String beanDefinitionName : beanDefinitionNames) {
//            System.out.println(beanDefinitionName);
//        }
    }

}
