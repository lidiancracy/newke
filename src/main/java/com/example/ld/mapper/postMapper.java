package com.example.ld.mapper;


import com.example.ld.entity.Comment;
import com.example.ld.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface postMapper {

    int insertDiscussPost(DiscussPost discussPost);

    List<DiscussPost> getallpost();

    DiscussPost getbyid(int postid);

    List<Comment> selectall_1(int postid);

    int count(int parseInt);
}
