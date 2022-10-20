package com.example.ld.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.ld.Util.ActivateState;
import com.example.ld.Util.hostholder;
import com.example.ld.entity.*;
import com.example.ld.mapper.UserMapper;
import com.example.ld.service.MSGService;
import com.example.ld.service.followService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName noticeController
 * @Description TODO
 * @Date 2022/10/12 12:57
 */
@Controller
@Slf4j
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
        HashMap<String, Object> likeNotice = new HashMap<>();
        Message messagelike =msgService.findlatest_like();
        if(messagelike!=null){
            likeNotice.put("message",messagelike);
        }

        String likecontent = messagelike.getContent();
        if(likecontent==null){
            throw new IllegalArgumentException("消息内容为空");
        }
        Event eventlike = JSONObject.parseObject(likecontent, Event.class);
        int userIdlike = eventlike.getUserId();
        User userlike = userMapper.selectById(userIdlike);
        if(userlike!=null){
            likeNotice.put("user",userlike);
        }
        Integer like_unread= msgService.count_likeunread(id);
        Integer like_count= msgService.count_likeall(id);
        likeNotice.put("unread",like_unread);
        likeNotice.put("count",like_count);
        likeNotice.put("entityType",event.getEntityType());
        model.addAttribute("likeNotice",likeNotice);
//
//        //封装follow
        HashMap<String, Object> followNotice = new HashMap<>();
        Message message_floow =msgService.findlatest_fl();
        if(message_floow!=null){
            followNotice.put("message",message_floow);
        }

        String content_fl = message_floow.getContent();
        if(content_fl==null){
            throw new IllegalArgumentException("消息内容为空");
        }
        Event event_fl = JSONObject.parseObject(content_fl, Event.class);
        int userId_fl = event_fl.getUserId();
        User user_fl = userMapper.selectById(userId_fl);
        if(user_fl!=null){
            followNotice.put("user",user_fl);
        }
        Integer unread_fl= msgService.count_fl_unread(id);
        Integer count_fl= msgService.count_fl_all(id);
        followNotice.put("unread",unread_fl);
        followNotice.put("count",count_fl);
        followNotice.put("entityType",event_fl.getEntityType());
        model.addAttribute("followNotice",followNotice);
        return "site/notice";
    }

    /**
     * comment notice 详情页面
     * map需要封装
     * - fromuser对象
     * - notice 对象
     * - topic变量
     * - user trigger对象
     * - entityType 类型
     * - postId 帖子id
     *
     */
    @GetMapping("/notice/detail/comment")
    public String getcommetdetail(Model model, Page page){
        User user = hostholder.getUser();
        if(user==null){
            return  null;
        }
        Integer id = user.getId();
        page.setLimit(2);  // 每页大小
        page.setPath("/notice/detail/comment");  // 上一页下一个跳转，一般我们上下页的请求path是不带页数的
        page.setRows(msgService.count_commentall(id)); // 总数
// 这种自定义不好的一点是每次都需要自己输入总数，虽然redis总数好求
        List<Message> commentList = msgService.find_all_commentnotice(
                id, page.getOffset(), page.getLimit());
        ArrayList<HashMap<String, Object>> notices = new ArrayList<>();

        for (Message comment : commentList) {
            HashMap<String, Object> map = new HashMap<>();
            String content = comment.getContent();
            Event message=null;
            if(StringUtils.hasText(content)){
                 message = JSONObject.parseObject(content, Event.class);
            }
            if(message==null){
                return null;
            }
            int userId = message.getUserId();
            User user1 = userMapper.selectById(userId);
            if(user1!=null){
                map.put("user",user1);
            }


            map.put("fromUser",user);
            map.put("entityType",message.getEntityType());
            map.put("notice",comment);
            map.put("postId",message.getEntityId());
            map.put("topic",TOPIC_COMMENT.equals(comment.getConversationId()));

            notices.add(map);
        }
        model.addAttribute("notices",notices);
        return "site/notice-detail";
    }

    @GetMapping("/notice/detail/like")
    public String getclikedetail(Model model, Page page){
        User user = hostholder.getUser();
        if(user==null){
            return  null;
        }
        Integer id = user.getId();
        page.setLimit(2);  // 每页大小
        page.setPath("/notice/detail/like");  // 上一页下一个跳转，一般我们上下页的请求path是不带页数的
        page.setRows(msgService.count_likeall(id)); // 总数
// 这种自定义不好的一点是每次都需要自己输入总数，虽然redis总数好求
        List<Message> commentList = msgService.find_all_likenotice(
                id, page.getOffset(), page.getLimit());
        ArrayList<HashMap<String, Object>> notices = new ArrayList<>();

        for (Message comment : commentList) {
            HashMap<String, Object> map = new HashMap<>();
            String content = comment.getContent();
            Event message=null;
            if(StringUtils.hasText(content)){
                message = JSONObject.parseObject(content, Event.class);
            }
            if(message==null){
                return null;
            }
            int userId = message.getUserId();
            User user1 = userMapper.selectById(userId);
            if(user1!=null){
                map.put("user",user1);
            }


            map.put("fromUser",user);
            map.put("entityType",message.getEntityType());
            map.put("notice",comment);
            map.put("postId",message.getEntityId());
            map.put("topic",TOPIC_LIKE.equals(comment.getConversationId()));

            notices.add(map);
        }
        model.addAttribute("notices",notices);
        return "site/notice-detail";
    }

    @GetMapping("/notice/detail/follow")
    public String getfollowdetail(Model model, Page page){
        User user = hostholder.getUser();
        if(user==null){
            return  null;
        }
        Integer id = user.getId();
        page.setLimit(2);  // 每页大小
        page.setPath("/notice/detail/follow");  // 上一页下一个跳转，一般我们上下页的请求path是不带页数的
        page.setRows(msgService.count_fl_all(id)); // 总数
// 这种自定义不好的一点是每次都需要自己输入总数，虽然redis总数好求
        List<Message> commentList = msgService.find_all_flnotice(
                id, page.getOffset(), page.getLimit());
        ArrayList<HashMap<String, Object>> notices = new ArrayList<>();

        for (Message comment : commentList) {
            HashMap<String, Object> map = new HashMap<>();
            String content = comment.getContent();
            Event message=null;
            if(StringUtils.hasText(content)){
                message = JSONObject.parseObject(content, Event.class);
            }
            if(message==null){
                return null;
            }
            int userId = message.getUserId();
            User user1 = userMapper.selectById(userId);
            if(user1!=null){
                map.put("user",user1);
            }
            map.put("fromUser",user);
            map.put("entityType",message.getEntityType());
            map.put("notice",comment);
            map.put("postId",message.getEntityId());
            map.put("topic",TOPIC_FOLLOW.equals(comment.getConversationId()));
            notices.add(map);
        }
        model.addAttribute("notices",notices);
        return "site/notice-detail";
    }
}
