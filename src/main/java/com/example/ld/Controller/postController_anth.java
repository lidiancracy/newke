package com.example.ld.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.ld.Util.RedisKeyUtil;
import com.example.ld.Util.communityutil;
import com.example.ld.entity.DiscussPost;
import com.example.ld.service.postservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName postController_anth
 * @Description TODO
 * @Date 2022/10/15 19:55
 */
@Controller
@RequestMapping("/discuss")
public class postController_anth {
    @Autowired
    postservice postservice;
    /**
     * 帖子置顶
     */
    @PostMapping("/top")
    @ResponseBody
    public String totop(String id){
        postservice.totop(id);
        return communityutil.getJSONString(0,"置顶成功");
    }
    /**
     * 帖子精华
     */
    @Autowired
    RedisTemplate redisTemplate;
    @PostMapping("/wonderful")
    @ResponseBody
    public String towd(String id){
        postservice.towd(id);
        redisTemplate.opsForSet().add(RedisKeyUtil.gettzrank(),Integer.parseInt(id) );
        return communityutil.getJSONString(0,"置为精华成功");
    }
    /**
     * 帖子删除
     */
    @PostMapping("/delete")
    @ResponseBody
    public String tosc(String id){
        postservice.tosc(id);
        return communityutil.getJSONString(0,"删除成功");
    }

}
