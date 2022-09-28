package com.example.ld.service;


import com.example.ld.entity.Message;

import java.util.List;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2022-09-17 19:57:22
 */
public interface MSGService {

    int msgcount(Integer id);


    List<Message> findmsglist_page(Integer id, int offset, int limit);

    int getunreadcount(String conversationId);

    int eachcount(String conversationId);
}
