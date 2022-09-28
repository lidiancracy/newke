package com.example.ld.service.impl;

import com.example.ld.entity.Message;
import com.example.ld.mapper.MessageMapper;
import com.example.ld.service.MSGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName Msgservimpl
 * @Description TODO
 * @Date 2022/9/28 15:35
 */
@Service
public class Msgservimpl implements MSGService {
    @Autowired
    MessageMapper messageMapper;

    /**
     * 用户会话个数
     * @param id
     * @return
     */
    @Override
    public int msgcount(Integer id) {
        return messageMapper.msgcount(id);
    }

    /**
     * 分页查询messagelist
     * @param id
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public List<Message> findmsglist_page(Integer id, int offset, int limit) {
        return messageMapper.findlist(id,offset,limit);
    }

    /**
     * 每条会话未读数
     * @param conversationId
     * @return
     */
    @Override
    public int getunreadcount(String conversationId) {

        return messageMapper.unread(conversationId);
    }

    /**
     * 每个会话多少条对话
     * @param conversationId
     * @return
     */
    @Override
    public int eachcount(String conversationId) {

        return messageMapper.eachcount(conversationId);
    }


}
