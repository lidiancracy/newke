package com.example.ld.service.impl;

import com.example.ld.entity.DiscussPost;
import com.example.ld.mapper.postMapper;
import com.example.ld.service.postservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

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
}
