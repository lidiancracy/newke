package com.example.ld.Controller;

import com.example.ld.Event.producer;
import com.example.ld.Util.ActivateState;
import com.example.ld.Util.communityutil;
import com.example.ld.Util.hostholder;
import com.example.ld.entity.Event;
import com.example.ld.entity.Page;
import com.example.ld.entity.User;
import com.example.ld.mapper.UserMapper;
import com.example.ld.service.followService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName followcontroller
 * @Description TODO
 * @Date 2022/10/8 15:27
 */
@Controller
@Slf4j
public class followcontroller implements ActivateState {

    @Autowired
    hostholder hostholder;
    @Autowired
    followService followservice;
    /**
     * 关注某人
     * 如何储存在redis里面
     * 我们需要添加这个人的id到
     * @return
     */
    @Autowired
    producer producer;
    @PostMapping("/follow")
    @ResponseBody
    public String followpeople(int entityType,int entityId){
        User user = hostholder.getUser();
        if(user!=null){
            Integer userid = user.getId();
            followservice.followsomeone(userid,entityType,entityId);
        }



        Event event = new Event()
                .setTopic(TOPIC_FOLLOW)
                .setUserId(user.getId())
                .setEntityType(ENTITY_TYPE_USER)
                .setEntityId(entityId)
                .setEntityUserId(entityId)
                ;
        producer.senmsg(event);

        return communityutil.getJSONString(0, "已关注！");
    }

    /**
     * 取关某人
     * @return
     */
    @PostMapping("/unfollow")
    @ResponseBody
    public String unfollowpeople(int entityType,int entityId){
        User user = hostholder.getUser();
        if(user!=null){
            Integer userid = user.getId();
            followservice.unfollowsomeone(userid,entityType,entityId);
        }
        return communityutil.getJSONString(0, "已取关！");
    }
    /**
     * 获取关注者列表，按时间排序
     *
     */
    @Autowired
    UserMapper userMapper;
    @GetMapping("/followees/{userid}")
    public String showfollow(@PathVariable("userid") int userid, Page page, Model model){
        User user = userMapper.selectById(userid);
        if(user!=null){
            model.addAttribute("user",user);
        }

//        设置page
        page.setLimit(5);
        page.setPath("/followees/" + userid);
        page.setRows((int) followservice.followcount( userid,3));
//        查出该用户所有的关注

        List<HashMap<String,Object>> users=followservice.findall(userid, page.getOffset(), page.getLimit());
        model.addAttribute("users",users);
        return "/site/followee";
    }

    /**
     * 获取粉丝列表，按被关注者排序
     */
    @GetMapping("/followers/{userid}")
    public String showfans(@PathVariable("userid") int userid,Page page ,Model model){

        User user = userMapper.selectById(userid);
        if(user!=null){
            model.addAttribute("user",user);
        }

//        设置page
        page.setLimit(5);
        page.setPath("/followers/" + userid);
        page.setRows((int) followservice.fans( userid,3));
//        查出该用户所有的关注

        List<HashMap<String,Object>> users=followservice.findallfans(userid, page.getOffset(), page.getLimit());
        model.addAttribute("users",users);
        return "/site/follower";
    }
}
