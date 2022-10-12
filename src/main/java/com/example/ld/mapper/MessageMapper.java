package com.example.ld.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ld.entity.Comment;
import com.example.ld.entity.Message;
import com.example.ld.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * (Message)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-28 15:38:06
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {



    int msgcount(Integer id);

    List<Message> findlist(Integer id, int offset, int limit);

    int unread(String conversationId);

    int eachcount(String conversationId);

    List<Message> getallbyconvid(String conversationId);


    List<Message> getallbyconvid_fenye(String conversationId, int limit, int offset);

    void savemsg(Message message);


    List<Message> commentnoticeall(Integer id,String type, int offset, int limit);

    void status1(Integer id);


    List<Message> likenoticeall(Integer id, String type, int offset, int limit);

    List<Message> flnoticeall(Integer id, String type, int offset, int limit);
}
