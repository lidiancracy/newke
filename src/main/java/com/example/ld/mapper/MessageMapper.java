package com.example.ld.mapper;


import com.example.ld.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (Message)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-28 15:38:06
 */
@Mapper
public interface MessageMapper  {



    int msgcount(Integer id);

    List<Message> findlist(Integer id, int offset, int limit);

    int unread(String conversationId);

    int eachcount(String conversationId);
}
