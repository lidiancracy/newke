package com.example.ld.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ld.entity.Comment;
import com.example.ld.entity.DiscussPost;
import com.example.ld.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface postMapper  extends BaseMapper<DiscussPost> {

    int insertDiscussPost(DiscussPost discussPost);

    List<DiscussPost> getallpost();

    DiscussPost getbyid(int postid);

    List<Comment> selectall_1(int postid);

    int count(int parseInt);

    void addcomment(int id,int parseInt,String content,String time);

    void updatecount(int parseInt, int count);
}
