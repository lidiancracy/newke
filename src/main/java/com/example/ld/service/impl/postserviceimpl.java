package com.example.ld.service.impl;

import com.example.ld.entity.Comment;
import com.example.ld.entity.DiscussPost;
import com.example.ld.mapper.postMapper;
import com.example.ld.service.postservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName postserviceimpl
 * @Description TODO
 * @Date 2022/9/24 17:05
 */
@Service
public class postserviceimpl implements postservice {
    @Autowired
    postMapper postmapper;
    @Override
    public void addDiscussPost(DiscussPost post) {
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()) );
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        postmapper.insertDiscussPost(post);
    }

    /**
     * 获取所有帖子
     * @return
     */
    @Override
    public List<DiscussPost> getallpost() {
        return postmapper.getallpost();
    }

    @Override
    public List<DiscussPost> selectall() {
        return postmapper.getallpost();
    }

    @Override
    public DiscussPost getpostbyid(String postid) {
        return postmapper.getbyid(Integer.parseInt(postid));
    }

    @Override
    public List<Comment> selectall_1(String postid) {

        return postmapper.selectall_1(Integer.parseInt(postid));
    }

    @Override
    public int count(String postid) {

        return postmapper.count(Integer.parseInt(postid) );
    }

    /**
     * 插入评论
     * @param postid
     */
    @Override
    public void addcomment(Integer id,String postid,String content) {
        SimpleDateFormat sdf =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date());
        postmapper.addcomment(id,Integer.parseInt(postid) ,content,format);

    }

    @Override
    public void updatecount(String postid, int count) {
         postmapper.updatecount(Integer.parseInt(postid) ,count);
    }
}
