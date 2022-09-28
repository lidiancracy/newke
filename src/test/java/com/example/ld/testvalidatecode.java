package com.example.ld;


import com.example.ld.entity.Comment;
import com.example.ld.entity.LoginTicket;
import com.example.ld.mapper.LoginTicketMapper;
import com.example.ld.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.concurrent.CompletionException;

/**
 * @ClassName testvalidatecode
 * @Description TODO
 * @Date 2022/9/19 17:29
 */
@SpringBootTest
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
}
