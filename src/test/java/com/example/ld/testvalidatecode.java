package com.example.ld;


import com.example.ld.entity.Comment;
import com.example.ld.entity.LoginTicket;
import com.example.ld.entity.Message;
import com.example.ld.mapper.LoginTicketMapper;
import com.example.ld.mapper.MessageMapper;
import com.example.ld.service.CommentService;
import com.example.ld.service.MSGService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.zip.CheckedOutputStream;

/**
 * @ClassName testvalidatecode
 * @Description TODO
 * @Date 2022/9/19 17:29
 */
@SpringBootTest(classes = LdApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class testvalidatecode {
    @Autowired
    LoginTicketMapper loginTicketMapper;


    @Test
    public void validation(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(3);
        loginTicket.setTicket("123");
        loginTicketMapper.insertticket(loginTicket);
    }
    @Test
    public void validation2(){
        LoginTicket selectbyticket = loginTicketMapper.selectbyticket("123");
        System.out.println(selectbyticket);
    }
    @Test
    public void validation3(){
        LoginTicket selectbyticket = loginTicketMapper.selectbyticket("123");
        loginTicketMapper.updateticket(selectbyticket,1);

    }
    /**
     * test 分页查询 commentlist
     */
    @Autowired
    CommentService commentService;
    @Test
    public void test1(){
        List<Comment> comments = commentService.findcommentlist_page(0, "3", 0, 5);
        for (Comment comment : comments) {
            System.out.println(comment);
        }
    }
    /**
     * test 私信count
     */
    @Autowired
    MessageMapper messageMapper;
    @Test
    public void test2(){
        int msgcount = messageMapper.msgcount(8);
        System.out.println(msgcount);
    }

    /**
     * test notice
     * unreadall 全部未读消息
     */
    @Autowired
    MSGService msgService;

    /**
     * findlatest like
     */
    @Test
    public void test4(){
        Message message = msgService.findlatest_like();
        System.out.println(message);
    }


    @Test
    public void test5(){

    }
}
