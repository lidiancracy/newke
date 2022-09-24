package com.example.ld.service.impl;

import com.example.ld.entity.DiscussPost;
import com.example.ld.mapper.postMapper;
import com.example.ld.service.postservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        postmapper.insertDiscussPost(post);
    }
}
