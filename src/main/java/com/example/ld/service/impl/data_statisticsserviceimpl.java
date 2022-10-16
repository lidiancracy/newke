package com.example.ld.service.impl;

import com.example.ld.Util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName data_statisticsserviceimpl
 * @Description TODO
 * @Date 2022/10/16 15:05
 */
@Service
public class data_statisticsserviceimpl {
    /**
     * 统计用户活跃度相关信息
     */
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
    @Autowired
    RedisTemplate redisTemplate;
    /**
     * 记录某个uv
     * 存的是ip,到时候需要去重,不管用户有没有登录 只要访问就有ip 我们作为访客量依据
     */
    public void uv_writer(String ip){
        String getuv = RedisKeyUtil.getuv(simpleDateFormat.format(new Date()));
        // 粗略记下 使用loglog ，精确记下使用bit
        redisTemplate.opsForHyperLogLog().add(getuv,ip);
    }

    /**
     * 统计uv
     */
    public long uv_mutil(Date start,Date end){
        if(start==null || end == null){
            throw new RuntimeException("参数不能为空");
        }
        // 这个arrlist用来存每天的rediskey
        ArrayList<String> strings = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)){
            // 获取之前记录的key值
            String format = simpleDateFormat.format(calendar.getTime());
            strings.add(RedisKeyUtil.getuv(format));
            calendar.add(Calendar.DATE,1);
        }
        // strings 里面装的是所有天的key，我需要将他们union，统计不重复值(ip)的size
        String finalkey = RedisKeyUtil.getuv_timeline(simpleDateFormat.format(start) , simpleDateFormat.format(end));
        redisTemplate.opsForHyperLogLog().union(finalkey,strings.toArray());
        return redisTemplate.opsForHyperLogLog().size(finalkey);
    }

    /**
     * 记录活跃度用户
     */
    public void dau_writer(int userid){
        String getdau = RedisKeyUtil.getdau(simpleDateFormat.format(new Date()));
        // 粗略记下 使用loglog ，精确记下使用bit
        redisTemplate.opsForValue().setBit(getdau,userid,true);
    }
    /**
     * 统计时间段活跃度用户
     */
    public long dau_mutil(Date start,Date end){
        if (start == null || end == null){
            throw new IllegalArgumentException("统计DAU日期参数不能为空");
        }
        // 整理日期范围内的key
        List<byte[]> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while(!calendar.getTime().after(end)){
            String dauKey = RedisKeyUtil.getdau(simpleDateFormat.format(calendar.getTime()));
            keyList.add(dauKey.getBytes());
            calendar.add(Calendar.DATE,1);
        }

        // 进行OR运算
        return (long) redisTemplate.execute(new RedisCallback() {
            @Nullable
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                String dauKey = RedisKeyUtil.getdau_timeline(simpleDateFormat.format(start),simpleDateFormat.format(end));
                redisConnection.bitOp(RedisStringCommands.BitOperation.OR,
                        dauKey.getBytes(),keyList.toArray(new byte[0][0]));
                return redisConnection.bitCount(dauKey.getBytes());
            }
        });
    }





}
