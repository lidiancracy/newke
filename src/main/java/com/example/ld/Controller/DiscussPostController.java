package com.example.ld.Controller;


import com.example.ld.Util.communityutil;
import com.example.ld.Util.hostholder;
import com.example.ld.entity.DiscussPost;
import com.example.ld.entity.User;
import com.example.ld.service.postservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private postservice postservice;

    @Autowired
    private hostholder hostHolder;

    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return communityutil.getJSONString(403, "你还没有登录！");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId().toString());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        postservice.addDiscussPost(post);

        // 报错的情况统一处理
        return communityutil.getJSONString(0, "发布成功！");
    }


}
