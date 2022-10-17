package com.example.ld;

import com.example.ld.service.impl.testtheadservice;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @ClassName pooltest
 * @Description TODO
 * @Date 2022/10/17 12:26
 */
@SpringBootTest
@Slf4j
public class pooltest {
    // 普通线程
    @Autowired
    ThreadPoolTaskExecutor executor;
    // 定时线程
    @Autowired
    ThreadPoolTaskScheduler scheduler;

    void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                log.debug("hello word");
            }
        };
        for (int i = 0; i < 10; i++) {
            executor.submit(task);
        }
        sleep(5000);
    }

    @Autowired
    testtheadservice testtheadservice;
    @Test
    public void test2() {
        for (int i = 0; i < 10; i++) {
            testtheadservice.say();
        }
        sleep(5500);
    }
}
