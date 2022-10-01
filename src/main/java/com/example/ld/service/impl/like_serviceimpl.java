package com.example.ld.service.impl;

import com.example.ld.Util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @ClassName like_service
 * @Description 接口 实现接口麻烦
 * 我就直接写实现类了
 * @Date 2022/9/30 18:01
 */
@Service
public class like_serviceimpl {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 点赞
     * 首先看type 是给评论点赞还是帖子点赞
     * 拼接key
     * 查询redis 有没有这个userid，也就是这个用户以前有没有给我点过赞
     * 不管是帖子还是评论里面存的都是userid集合，因为只有用户才能点赞
     *
     * @param id
     * @param entityType
     * @param entityId
     */
    public void like(Integer id, int entityType, int entityId,int fromuserid) {

        redisTemplate.execute(new SessionCallback() {  //redis事务系统
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
//                获取key
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(fromuserid);
                Boolean member = redisTemplate.opsForSet().isMember(entityLikeKey, id);

                operations.multi();

                if (member) {
//                    如果在，表示已经点过赞了 再点一次取消赞
                    redisTemplate.opsForSet().remove(entityLikeKey, id);
                    redisTemplate.opsForValue().decrement(userLikeKey);
                } else {
                    redisTemplate.opsForSet().add(entityLikeKey, id);
                    redisTemplate.opsForValue().increment(userLikeKey);
                }

                return operations.exec();

            }
        });

    }

    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);

    }

    public int findEntityLikeStatus(Integer id, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        Boolean member = redisTemplate.opsForSet().isMember(entityLikeKey, id);
//        状态直接输出就行了，点赞动作那一步已经刷新了redis数据了
        if(member){
            return 1;
        }else {
            return 0;
        }
    }
}
