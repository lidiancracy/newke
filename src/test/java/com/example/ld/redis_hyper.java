package com.example.ld;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @ClassName redis_hyper
 * @Description TODO
 * @Date 2022/10/15 21:25
 */
@SpringBootTest
public class redis_hyper {
    @Autowired
    RedisTemplate redisTemplate;
    @Test
    public void test1(){
        String key="hyperloglog:test1";
        for (int i = 0; i < 50000; i++) {
            int  num = (int)(Math.random()*50000+1);
            redisTemplate.opsForHyperLogLog()
                    .add(key,num);
        }
        for (int i = 0; i < 50000; i++) {
            int  num = (int)(Math.random()*50000+1);
            redisTemplate.opsForHyperLogLog()
                    .add(key,num);
        }
        System.out.println(redisTemplate.opsForHyperLogLog().size(key));
    }

    @Test
    public void bit(){
        String key="hyperloglog:test2";
        redisTemplate.opsForValue().setBit(key,2,true);
        redisTemplate.opsForValue().setBit(key,3,true);
        redisTemplate.opsForValue().setBit(key,5,true);
        Boolean bit = redisTemplate.opsForValue().getBit(key, 2);
        System.out.println(bit);
        /**
         * 统计
         */
        Object execute = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.bitCount(key.getBytes());
            }
        });
        System.out.println(execute);
    }
}
