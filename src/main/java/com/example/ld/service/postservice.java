package com.example.ld.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ld.entity.Comment;
import com.example.ld.entity.DiscussPost;
import com.example.ld.entity.User;

import java.util.List;

/**
 * @ClassName postservice
 * @Description TODO
 * @Date 2022/9/24 17:05
 */
public interface postservice extends IService<DiscussPost> {
    void addDiscussPost(DiscussPost post);

    List<DiscussPost> getallpost();

    List<DiscussPost> selectall();

    DiscussPost getpostbyid(String postid);

    List<Comment> selectall_1(String postid);

    int count(String postid);

    void addcomment(Integer id,String postid,String content);

    void updatecount(String postid, int count);

    void totop(String id);

    void towd(String id);

    void tosc(String id);
}
