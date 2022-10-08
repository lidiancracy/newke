package com.example.ld.service.impl;

import com.example.ld.Util.RedisKeyUtil;
import com.example.ld.entity.User;
import com.example.ld.mapper.UserMapper;
import com.example.ld.service.followService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName followServiceimpl
 * @Description TODO
 * @Date 2022/10/8 15:46
 */
@Service
public class followServiceimpl implements followService {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void followsomeone(Integer userid, int entityType, int entityId) {
        /**
         * 获取 关注者和被关注者的redis key，因为每一次关注和取关都涉及两次操作
         *
         * ok 现在我点击了关注，我关注别人 ，需要在我的关注列表里面加上别人的id(作为value)，key是follow:myid
         * 别人的粉丝列表里面加上我的id(作为value),key是followee:别人id
         */
        String getfollow = RedisKeyUtil.getfollow(entityType, userid);
        String getfollowee = RedisKeyUtil.getfollowee(entityType, entityId);
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                operations.multi();
                redisTemplate.opsForZSet().add(getfollow, entityId, System.currentTimeMillis());
                redisTemplate.opsForZSet().add(getfollowee, userid, System.currentTimeMillis());
                return operations.exec();
            }
        });
    }

    /**
     * 取关某人
     *
     * @param userid
     * @param entityType
     * @param entityId
     */
    @Override
    public void unfollowsomeone(Integer userid, int entityType, int entityId) {
        String getfollow = RedisKeyUtil.getfollow(entityType, userid);
        String getfollowee = RedisKeyUtil.getfollowee(entityType, entityId);
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {

                operations.multi();
                redisTemplate.opsForZSet().remove(getfollow, entityId);
                redisTemplate.opsForZSet().remove(getfollowee, userid);
                return operations.exec();
            }
        });
    }

    /**
     * 现在登录用户有没有关注某用户
     *
     * @param userid
     * @param i
     * @param loginid
     * @return
     */
    @Override
    public boolean followornot(int userid, int i, Integer loginid) {
        String getfollow = RedisKeyUtil.getfollow(i, loginid);
        Double score = redisTemplate.opsForZSet().score(getfollow, userid);
        if (score != null) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 某用户关注总人数
     * @param userid
     * @param i
     * @return
     */
    @Override
    public long followcount(int userid, int i) {
        String getfollow = RedisKeyUtil.getfollow(i,userid );
        return redisTemplate.opsForZSet().zCard(getfollow);
    }

    /**
     * 某用户的fans数
     * @param userid
     * @param i
     * @return
     */
    @Override
    public long fans(int userid, int i) {
        String getfollowee = RedisKeyUtil.getfollowee(i,userid);
        return redisTemplate.opsForZSet().zCard(getfollowee);
    }

    /**
     * 查出所有的关注数
     * @param userid
     * @param offset
     * @param limit
     * @return
     */
    @Autowired
    UserMapper userMapper;

    @Override
    public List<HashMap<String, Object>> findall(int userid, int offset, int limit) {
        String key = RedisKeyUtil.getfollow(3, userid);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(key, offset, offset + limit - 1);
        List<HashMap<String, Object>> users=new ArrayList<HashMap<String, Object>>();
        for (Integer targetId : targetIds) {
            User user = userMapper.selectById(targetId);
            if(user==null){
                continue;
            }
            HashMap<String, Object> map = new HashMap<>();
            Double score = redisTemplate.opsForZSet().score(key, targetId);
            map.put("user",user);
            map.put("hasFollowed",followornot(targetId,3,userid));
            map.put("followTime",new Date(score.longValue()));
            users.add(map);
        }
        return users;
    }

    /**
     * 查询所有fans 并封装
     * @param userid
     * @param offset
     * @param limit
     * @return
     */
    @Override
    public List<HashMap<String, Object>> findallfans(int userid, int offset, int limit) {
        String key = RedisKeyUtil.getfollowee(3, userid);
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(key, offset, offset + limit - 1);
        List<HashMap<String, Object>> users=new ArrayList<HashMap<String, Object>>();
        for (Integer targetId : targetIds) {
            User user = userMapper.selectById(targetId);
            if(user==null){
                continue;
            }
            HashMap<String, Object> map = new HashMap<>();
            Double score = redisTemplate.opsForZSet().score(key, targetId);
            map.put("user",user);
            map.put("hasFollowed",followornot(targetId,3,userid));
            map.put("followTime",new Date(score.longValue()));
            users.add(map);
        }
        return users;
    }
}
