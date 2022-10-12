package com.example.ld.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ld.entity.Comment;
import com.example.ld.entity.Message;

import java.util.List;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2022-09-17 19:57:22
 */
public interface MSGService  extends IService<Message> {

    int msgcount(Integer id);


    List<Message> findmsglist_page(Integer id, int offset, int limit);

    int getunreadcount(String conversationId);

    int eachcount(String conversationId);

    List<Message> selectallby_convid(String conversationId);

    List<Message> selectallby_convid_fenye(String conversationId, int limit, int offset);

    Integer unreadall_3(Integer id);

    Message findlatest_comment();

    Message findlatest_like();

    Integer count_commentunread(Integer id);

    Integer count_commentall(Integer id);

    Integer count_likeunread(Integer id);

    Integer count_likeall(Integer id);

    Message findlatest_fl();

    Integer count_fl_unread(Integer id);

    Integer count_fl_all(Integer id);

    List<Message> find_all_commentnotice(Integer id, int offset, int limit);

    List<Message> find_all_likenotice(Integer id, int offset, int limit);

    List<Message> find_all_flnotice(Integer id, int offset, int limit);

}
