package com.example.ld;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class LdApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(1);
    }
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    String from;
    @Test
    void senderemail() {
        //创建简单邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        //谁发的
        message.setFrom(from);
        //谁要接收
        message.setTo("1960870058@qq.com");
        //邮件标题
        message.setSubject("9/19");
        //邮件内容
        message.setText("9/19");
        try {
            mailSender.send(message);
            System.out.println("ok");
        } catch (MailException e) {
            e.printStackTrace();

        }

    }

}
