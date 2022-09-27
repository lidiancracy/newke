package com.example.ld.service;

import com.example.ld.entity.Comment;
import com.example.ld.entity.DiscussPost;

import java.util.List;

/**
 * @ClassName postservice
 * @Description TODO
 * @Date 2022/9/24 17:05
 */
public interface postservice {
    void addDiscussPost(DiscussPost post);

    List<DiscussPost> getallpost();

    List<DiscussPost> selectall();

    DiscussPost getpostbyid(String postid);

    List<Comment> selectall_1(String postid);

    int count(String postid);
}
