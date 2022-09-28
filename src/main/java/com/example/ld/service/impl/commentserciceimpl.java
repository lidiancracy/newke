package com.example.ld.service.impl;

import com.example.ld.entity.Comment;
import com.example.ld.mapper.commentMapper;
import com.example.ld.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName commentserciceimpl
 * @Description TODO
 * @Date 2022/9/28 14:28
 */
@Service
public class commentserciceimpl implements CommentService {
    @Autowired
    commentMapper commentmapper;
    @Override
    public List<Comment> findcommentlist_page(int yijiTitle, String postid, int offset, int limit) {

        return commentmapper.findpagelist(yijiTitle,postid,offset,limit);
    }
}
