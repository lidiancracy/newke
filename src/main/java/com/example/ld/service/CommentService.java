package com.example.ld.service;


import com.example.ld.entity.Comment;
import com.example.ld.entity.User;

import java.io.InputStream;
import java.util.List;
import java.util.Map;


/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2022-09-17 19:57:22
 */
public interface CommentService {

    List<Comment> findcommentlist_page(int yijiTitle, String postid, int offset, int limit);
}
