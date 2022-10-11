package com.example.ld.Event;

import com.alibaba.fastjson.JSONObject;
import com.example.ld.Util.ActivateState;
import com.example.ld.entity.Event;
import com.example.ld.entity.Message;
import com.example.ld.mapper.MessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName consumer
 * @Description TODO
 * @Date 2022/10/10 21:16
 */
@Component
@Slf4j
public class consumer implements ActivateState {
    @Autowired
    MessageMapper messageMapper;


    @KafkaListener(topics = {TOPIC_COMMENT,TOPIC_FOLLOW,TOPIC_LIKE})
    public void savemsgtodb(ConsumerRecord record){
        if (record == null || record.value() == null) {
            log.error("消息的内容为空！");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            log.error("消息格式错误！");
            return;
        }
        /**
         * 收到事件后用megmapper将信息保存在数据库中
         *  content保存在事件data属性里面
         */
        Message message = new Message();
        message.setFromId(SYSTEM_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        /**
         * 消息从系统发给某某用户
         * content可以包含 某某人 做了 某某事  事的链接id
         */
        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        String s = JSONObject.toJSONString(content);
        message.setContent(s);

        /**
         * 保存至数据库
         */
        messageMapper.savemsg(message);
    }
}
