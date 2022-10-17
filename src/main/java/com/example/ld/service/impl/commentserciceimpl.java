package com.example.ld.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class commentserciceimpl extends ServiceImpl<commentMapper,Comment> implements CommentService {
    @Autowired
    commentMapper commentmapper;
    @Override
    public List<Comment> findcommentlist_page(int yijiTitle, String postid, int offset, int limit) {

        return commentmapper.findpagelist(yijiTitle,postid,offset,limit);
    }

    /**
     * 某帖子评论 多少条
     * type为0 entityid为id
     * @param id
     * @return
     */
    @Override
    public long Countcomment(Integer id) {
        LambdaQueryWrapper<Comment> qw = new LambdaQueryWrapper<>();
        qw.eq(Comment::getEntityType,0)
                .eq(Comment::getEntityId,id);
        return count(qw);


    }
}
