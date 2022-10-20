package com.example.ld.Controller;

import com.example.ld.Util.ActivateState;
import com.example.ld.Util.hostholder;
import com.example.ld.entity.Comment;
import com.example.ld.entity.Message;
import com.example.ld.entity.Page;
import com.example.ld.entity.User;
import com.example.ld.mapper.MessageMapper;
import com.example.ld.mapper.UserMapper;
import com.example.ld.service.MSGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName MessageController
 * @Description TODO
 * @Date 2022/9/28 15:19
 */
@Controller
public class MessageController implements ActivateState {
    /**
     * 分析私信页面
     * 朋友私信有一个count
     */
    @Autowired
    hostholder hostholder;
    @Autowired
    MSGService msgService;
    @Autowired
    UserMapper userMapper;
    @GetMapping("/letter/list")
    public String getletters(Model model, Page page){
        User user = hostholder.getUser();
        /**
         * 这个用户有多少条会话，也就是和多少人聊过，重点是人
         * 会话集合lists
         * 每个会话list (map)
         *
         */
        int msgcount = msgService.msgcount(user.getId());
        model.addAttribute("letterUnreadCount",msgcount);
// 设置分页
        page.setLimit(2);
        page.setPath("/letter/list");
        page.setRows(msgcount);
        /**
         * 查询出每页的实体集合
         * message集合 每个message是一个会话的最新消息
         * 渲染前端
         */
        List<Message> commentList = msgService.findmsglist_page(
                user.getId(), page.getOffset(), page.getLimit());
        List<HashMap<String, Object>> conversations = new ArrayList<HashMap<String,Object>>();
        if(commentList!=null && !commentList.isEmpty()){
            for (Message message : commentList) {
                HashMap<String, Object> map = new HashMap<>();
                String conversationId = message.getConversationId();
                int unread=msgService.getunreadcount(conversationId);
                int targetId = (user.getId().equals(message.getFromId())) ? message.getToId() : message.getFromId();
                User user1 = userMapper.selectById(targetId);
                int eachcount=msgService.eachcount(conversationId);
                map.put("unreadCount",unread);
                map.put("target",user1);
                map.put("conversation",message);
                map.put("letterCount",eachcount);
                conversations.add(map);
            }
        }
        model.addAttribute("conversations",conversations);
        return "site/letter";
    }

    /**
     * 私信详情
     * @param conversationId
     * @return
     */
    @GetMapping("/letter/detail/{conversationId}")
    public String getdetail(@PathVariable String conversationId,Model model,Page page){
        /**
         * targeg信息显示
         *   list letters
         *   每个letter
         *
         */
        User user = hostholder.getUser();
        model.addAttribute("target",user);
        page.setLimit(2);
        page.setPath("/letter/detail/"+conversationId);
        List<Message> messageList=msgService.selectallby_convid(conversationId);
        page.setRows(messageList.size());
        List<Message> messageList2=msgService.selectallby_convid_fenye(conversationId,page.getLimit(),page.getOffset());
        ArrayList<HashMap<String, Object>> letters = new ArrayList<>();
        if(messageList2!=null && !messageList2.isEmpty()){
            for (Message message : messageList2) {
                User user1 = userMapper.selectById(message.getFromId());
                HashMap<String, Object> map = new HashMap<>();
                map.put("fromUser",user1);
                map.put("letter",message);
                letters.add(map);
            }
        }
        model.addAttribute("letters",letters);

        return "site/letter-detail";
    }
}
