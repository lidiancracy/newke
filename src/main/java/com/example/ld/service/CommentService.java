package com.example.ld.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ld.entity.Comment;

import java.util.List;


/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2022-09-17 19:57:22
 */
public interface CommentService extends IService<Comment> {

    List<Comment> findcommentlist_page(int yijiTitle, String postid, int offset, int limit);

    long Countcomment(Integer id);
}
