package com.example.ld.Controller;

import com.example.ld.Event.producer;
import com.example.ld.Util.ActivateState;
import com.example.ld.Util.RedisKeyUtil;
import com.example.ld.Util.communityutil;
import com.example.ld.Util.hostholder;
import com.example.ld.annociation.loginrequired;
import com.example.ld.entity.Event;
import com.example.ld.entity.User;
import com.example.ld.service.impl.like_serviceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName likeController
 * @Description TODO
 * @Date 2022/9/30 18:34
 */
@Controller
public class likeController implements ActivateState {
    /**
     * 专门处理点赞相关的，用到redis
     */

    /**
     * 显示帖子的点赞并且可以点赞 是一个ajax请求
     * 参数 "entityType" , "entityId" 我们都写成String类型吧
     */
    @Autowired
    like_serviceimpl like_serviceimpl;
    @Autowired
    producer producer;
    @Autowired
    hostholder hostholder;
    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/like")
    @ResponseBody
    @loginrequired
    public String postlikeshow(int entityType, int entityId, int fromuserid) {
//        只有点击赞或者取消赞才回到这个controller方法中来
//        我们规定redis存储结构 like:entityType:entityId  然后里面是一个
        User user = hostholder.getUser();
        /**
         * 第一个是动作
         * 其他两个都是查看redis
         */
        // 点赞动作
//        谁在点赞 点赞帖子还是评论 帖子或者评论id是多少 userid应该是存在reids里面用来统计 key是 type+entityid
        like_serviceimpl.like(user.getId(), entityType, entityId, fromuserid);
        // 数量
        long likeCount = like_serviceimpl.findEntityLikeCount(entityType, entityId);
        // 状态
        int likeStatus = like_serviceimpl.findEntityLikeStatus(user.getId(), entityType, entityId);
//        如果状态是点赞状态就需要发送信息
        // 触发点赞事件
        if (likeStatus == 1) {
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(user.getId())
                    .setEntityType(ENTITY_TYPE_POST)
                    .setEntityId(entityId)
                    .setEntityUserId(fromuserid);
            producer.senmsg(event);
        }
        // 返回结果
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);
        if(entityType==1){
            redisTemplate.opsForSet().add(RedisKeyUtil.gettzrank(),entityId);
        }

        return communityutil.getJSONString(0, null, map);
    }
}
