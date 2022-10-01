package com.example.ld;


import com.example.ld.entity.Comment;
import com.example.ld.entity.LoginTicket;
import com.example.ld.mapper.LoginTicketMapper;
import com.example.ld.mapper.MessageMapper;
import com.example.ld.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

/**
 * @ClassName testvalidatecode
 * @Description TODO
 * @Date 2022/9/19 17:29
 */
@SpringBootTest(classes = LdApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class redis_test {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * key相当于redis表名，如果存的是String，表里面就只有一个数据
     * 如果是hash，表里面存的是一个hash表
     */
    @Test
    public void redis_String(){
        String key="test:count";
        redisTemplate.opsForValue().set(key,1);
        System.out.println(redisTemplate.opsForValue().get(key));
        System.out.println(redisTemplate.opsForValue().increment(key));
        System.out.println(redisTemplate.opsForValue().decrement(key));
    }

    @Test
    public void redis_hash(){
        String key="test:hash";
        redisTemplate.opsForHash().put(key,"name","lidian");
        redisTemplate.opsForHash().put(key,"age",22);
        Object name = redisTemplate.opsForHash().get(key, "name");
        System.out.println(name);
    }

    @Test
    public void redis_list(){
        String key="test:list";
//        redisTemplate.opsForList().leftPush(key,1);
//        redisTemplate.opsForList().leftPush(key,2);
//        redisTemplate.opsForList().leftPush(key,3);

        Long key1 = redisTemplate.opsForList().size(key);
        System.out.println(key1);
        List range = redisTemplate.opsForList().range(key, 0, 3);
        System.out.println(range);
    }

    /**
     * 和list相比无序
     */
    @Test
    public void redis_set(){
        String key="test:set";
        redisTemplate.opsForSet().add(key,"fuck1");
        redisTemplate.opsForSet().add(key,"fuck2");

        System.out.println(redisTemplate.opsForSet().size(key));
        Object pop = redisTemplate.opsForSet().pop(key);
        System.out.println(pop);
    }
    /**
     * 有序set,有一个score属性，更像一个排行榜
     * rank是排名
     * range可以理解为排名名单
     */
    @Test
    public void redis_sort_set(){
        String key="test:sortedset";
        redisTemplate.opsForZSet().add(key,"fuck1",10);
        redisTemplate.opsForZSet().add(key,"fuck2",20);
        redisTemplate.opsForZSet().add(key,"fuck3",30);
//        System.out.println(redisTemplate.opsForZSet().zCard(key));
//        System.out.println(redisTemplate.opsForZSet().range(key, 0, 1));
        System.out.println(redisTemplate.opsForZSet().reverseRank(key, "fuck1"));
    }
}
