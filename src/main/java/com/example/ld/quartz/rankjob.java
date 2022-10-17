package com.example.ld.quartz;

import com.example.ld.Util.RedisKeyUtil;
import com.example.ld.entity.DiscussPost;
import com.example.ld.service.postservice;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @ClassName rankjob
 * @Description TODO
 * @Date 2022/10/17 17:06
 */
@Slf4j
public class rankjob implements Job {

    // 牛客纪元
    private static final Date epoch;

    /**
     * 这样赋值，这个常量只会初始化一次
     */
    static {
        try {
            epoch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-08-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化牛客纪元失败!", e);
        }
    }
    /**
     * 实现定时任务
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String rankkey= RedisKeyUtil.gettzrank();
        BoundSetOperations boundSetOperations = redisTemplate.boundSetOps(rankkey);
        if(boundSetOperations.size()==0){
            log.info("redis 排名帖子暂时为空");
            return;
        }
        while(boundSetOperations.size()>0){
            Integer postid = (Integer) boundSetOperations.pop();
            refresh(postid);
        }
    }

    /**
     * 计算分数，记录到数据库
     * @param postid
     */
    @Autowired
    postservice postservice;
    private void refresh(Integer postid) {
        DiscussPost post = postservice.getpostbyid(String.valueOf(postid));
        if(post==null){
            return;
        }
        Integer status = post.getStatus();
        boolean wonderful=status.equals(1);
        Integer commentCount = post.getCommentCount();
        if(commentCount==null){
            commentCount=0;
        }
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(1, postid);
        Set members = redisTemplate.opsForSet().members(entityLikeKey);

        int likeCount=members.size();
        log.info("wonder"+wonderful);
        log.info("likecount"+likeCount);
        log.info("commencount"+commentCount);
        // 计算权重
        double w = (wonderful ? 75 : 0) + commentCount * 10 + likeCount * 2;
        // 分数 = 帖子权重 + 距离天数
        double score = Math.log10(Math.max(w, 1))
                + (post.getCreateTime().getTime() - epoch.getTime()) / (1000 * 3600 * 24);
        postservice.updateScore(postid, score);
    }
}
