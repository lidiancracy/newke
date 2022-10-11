package com.example.ld;

import com.example.ld.Event.producer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @ClassName kafkatest
 * @Description TODO
 * @Date 2022/10/10 20:42
 */
@SpringBootTest
public class kafkatest {
    @Autowired
    producerss producerss;
    @Test
    void kafka(){
        producerss.senmsg("test","nihao");
        producerss.senmsg("test2","nihao2");

    }
}

/**
 * 生产者
 * 方法1 发送消息 需要kafka工具
 */
@Component
class producerss{
    @Autowired
    KafkaTemplate kafkaTemplate;
    public void senmsg(String tp,String ct){
        kafkaTemplate.send(tp,ct);
    }
}

/**
 * 消费者
 * 被动接受消息
 */
@Component
class consumerss{

    @KafkaListener(topics = {"test1","test2","test3"})
    public  void hand(ConsumerRecord record){
        System.out.println(record.value());
    }

}