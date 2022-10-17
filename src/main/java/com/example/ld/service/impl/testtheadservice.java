package com.example.ld.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @ClassName testtheadservice
 * @Description TODO
 * @Date 2022/10/17 12:52
 */
@Slf4j
@Service
public class testtheadservice {
    @Async
    public void say(){
        log.info("hellow ww");
    }
}
