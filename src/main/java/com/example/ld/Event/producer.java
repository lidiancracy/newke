package com.example.ld.Event;

import com.alibaba.fastjson.JSONObject;
import com.example.ld.Util.ActivateState;
import com.example.ld.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @ClassName producer
 * @Description TODO
 * @Date 2022/10/10 21:16
 */
@Component
public class producer  {
    @Autowired
    KafkaTemplate kafkaTemplate;

    /**
     * 发送事件
     */
    public void senmsg(Event event){
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
