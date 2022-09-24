package com.example.ld.mapper;


import com.example.ld.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface postMapper {

    int insertDiscussPost(DiscussPost discussPost);
}
