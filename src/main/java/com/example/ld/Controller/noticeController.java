package com.example.ld.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.ld.Util.ActivateState;
import com.example.ld.Util.hostholder;
import com.example.ld.entity.Event;
import com.example.ld.entity.Message;
import com.example.ld.entity.User;
import com.example.ld.mapper.UserMapper;
import com.example.ld.service.MSGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;

/**
 * @ClassName noticeController
 * @Description TODO
 * @Date 2022/10/12 12:57
 */
@Controller
public class noticeController implements ActivateState {
    @Autowired
    MSGService msgService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    hostholder hostholder;
    /**
     * 返回系统通知
     * - noticeUnreadCount 三类通知一共未读数量
     * - commentNotice 这是一个Map
     *      - unread 评论未读
     *      - message 封装message对象
     *      - user  封装user对象
     *      - entityType 封装类型
     *      - count 封装一共几条会话
     *  -  其余两类通知同理
     *
     * @return
     */
    @GetMapping("/notice/list")
    public String getnoticelist(Model model) {
        User user1 = hostholder.getUser();
        if(user1==null){
            return null;
        }
        Integer id = user1.getId();
        Integer noticeUnreadCount= msgService.unreadall_3(id);
        model.addAttribute("noticeUnreadCount",noticeUnreadCount);
        //封装comment
        HashMap<String, Object> commentNotice = new HashMap<>();
        Message message =msgService.findlatest_comment();
        if(message!=null){
            commentNotice.put("message",message);
        }else {
            commentNotice.put("message",null);
        }

        String content = message.getContent();
        if(content==null){
            throw new IllegalArgumentException("消息内容为空");
        }
        Event event = JSONObject.parseObject(content, Event.class);
        int userId = event.getUserId();
        User user = userMapper.selectById(userId);
        if(user!=null){
            commentNotice.put("user",user);
        }
         Integer unread= msgService.count_commentunread(id);
        Integer count= msgService.count_commentall(id);
        commentNotice.put("unread",unread);
        commentNotice.put("count",count);
        commentNotice.put("entityType",event.getEntityType());
        model.addAttribute("commentNotice",commentNotice);

        //封装like
//        HashMap<String, Object> likeNotice = new HashMap<>();
//        Message messagelike =msgService.findlatest_like();
//        if(messagelike!=null){
//            likeNotice.put("message",messagelike);
//        }
//
//        String likecontent = messagelike.getContent();
//        if(likecontent==null){
//            throw new IllegalArgumentException("消息内容为空");
//        }
//        Event eventlike = JSONObject.parseObject(likecontent, Event.class);
//        int userIdlike = eventlike.getUserId();
//        User userlike = userMapper.selectById(userIdlike);
//        if(userlike!=null){
//            likeNotice.put("user",userlike);
//        }
//        Integer like_unread= msgService.count_likeunread();
//        Integer like_count= msgService.count_likeall();
//        likeNotice.put("unread",like_unread);
//        likeNotice.put("count",like_count);
//        likeNotice.put("entityType",event.getEntityType());
//        model.addAttribute("likeNotice",likeNotice);
//
//        //封装follow
//        HashMap<String, Object> followNotice = new HashMap<>();
//        Message message_floow =msgService.findlatest_fl();
//        if(message_floow!=null){
//            followNotice.put("message",message_floow);
//        }
//
//        String content_fl = message_floow.getContent();
//        if(content_fl==null){
//            throw new IllegalArgumentException("消息内容为空");
//        }
//        Event event_fl = JSONObject.parseObject(content_fl, Event.class);
//        int userId_fl = event_fl.getUserId();
//        User user_fl = userMapper.selectById(userId_fl);
//        if(user_fl!=null){
//            followNotice.put("user",user_fl);
//        }
//        Integer unread_fl= msgService.count_fl_unread();
//        Integer count_fl= msgService.count_fl_all();
//        followNotice.put("unread",unread_fl);
//        followNotice.put("count",count_fl);
//        followNotice.put("entityType",event_fl.getEntityType());
//        model.addAttribute("followNotice",followNotice);
        return "/site/notice";
    }
}
