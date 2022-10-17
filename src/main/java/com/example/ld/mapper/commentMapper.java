package com.example.ld.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ld.entity.Comment;
import com.example.ld.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface commentMapper extends BaseMapper<Comment> {


    List<Comment> findpagelist(int yijiTitle, String postid, int offset, int limit);


}
